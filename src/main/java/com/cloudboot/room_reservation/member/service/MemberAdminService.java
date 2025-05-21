package com.cloudboot.room_reservation.member.service;

import com.cloudboot.room_reservation.member.dto.request.UpdateMemberRequest;
import com.cloudboot.room_reservation.member.dto.request.UpdateMemberRoleRequest;
import com.cloudboot.room_reservation.member.dto.response.MemberAdminResponse;
import com.cloudboot.room_reservation.member.dto.response.MemberResponse;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.enumerate.Role;
import com.cloudboot.room_reservation.member.exception.PasswordMismatchException;
import com.cloudboot.room_reservation.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberAdminService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberAdminService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Page<MemberAdminResponse> findAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberAdminResponse::from);
    }

    public MemberAdminResponse findOne(Long memberId) {
        return MemberAdminResponse.from(memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다.")));
    }

    public MemberAdminResponse changeMemberRole(Long memberId, UpdateMemberRoleRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        member.changeRole(Role.valueOf(request.getRole().toUpperCase()));
        return MemberAdminResponse.from(member);
    }

    // 특정 회원 조회
    public MemberResponse findById(Long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        return MemberResponse.of(findMember.getUsername(), findMember.getRole());
    }

    @Transactional
    // 특정 회원 정보 수정 (비밀번호 변경)
    public void updateMember(Long id, UpdateMemberRequest updateMemberRequest) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

//        log.info("id = {}", id);
//        log.info("origin password = {}", findMember.getPassword());
//        log.info("new password = {}", updateMemberRequest.getNewPassword());
//        log.info("new password confirm = {}", updateMemberRequest.getNewPasswordConfirm());

        // 기존 비밀번호 검증
        validateOriginPassword(updateMemberRequest.getOriginPassword(), findMember.getPassword());
        // 새 비밀번호 확인 일치 검증
        validatePasswordConfirmation(updateMemberRequest.getNewPassword(), updateMemberRequest.getNewPasswordConfirm());


        // 새 비밀번호 암호화 후 저장
        String encodedNewPassword = bCryptPasswordEncoder.encode(updateMemberRequest.getNewPassword());
        findMember.updatePassword(encodedNewPassword);
    }

    private void validateOriginPassword(String rawPassword, String encodedPassword) {
        if (!bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
            throw new PasswordMismatchException("기존 비밀번호가 일치하지 않습니다.");
        }
    }

    private void validatePasswordConfirmation(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordMismatchException("새 비밀번호가 서로 일치하지 않습니다.");
        }
    }

}
