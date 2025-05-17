package com.cloudboot.room_reservation.member.service;

import com.cloudboot.room_reservation.member.dto.request.UpdateMemberRoleRequest;
import com.cloudboot.room_reservation.member.dto.response.MemberAdminResponse;
import com.cloudboot.room_reservation.member.dto.response.MemberResponse;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.enumerate.Role;
import com.cloudboot.room_reservation.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberAdminService {

    private final MemberRepository memberRepository;

    public MemberAdminService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberAdminResponse> findAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberAdminResponse::from)
                .collect(Collectors.toList());
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


}
