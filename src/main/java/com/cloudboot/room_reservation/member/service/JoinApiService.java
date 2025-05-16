package com.cloudboot.room_reservation.member.service;

import com.cloudboot.room_reservation.member.dto.JoinDTO;
import com.cloudboot.room_reservation.member.dto.request.JoinRequest;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.exception.PasswordMismatchException;
import com.cloudboot.room_reservation.member.exception.UsernameAlreadyExistsException;
import com.cloudboot.room_reservation.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JoinApiService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinApiService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void registerMember(JoinRequest joinRequest) {

        // 이미 존재하는 회원인지 검증
        validateUsernameAvailability(joinRequest);

        // 비밀번호1과 비밀번호2가 일치한지 검증
        validatePasswordConfirmation(joinRequest);

        String username = joinRequest.getUsername();
        // 암호화 수행 (
        String password = bCryptPasswordEncoder.encode(joinRequest.getPassword());

        // JoinRequest -> JoinDTO
        JoinDTO joinDTO = JoinDTO.of(username, password);

        // JoinDTO -> Member
        Member member = JoinDTO.toEntity(joinDTO);
        memberRepository.save(member);
    }

    private void validateUsernameAvailability(JoinRequest joinRequest) {
        if (memberRepository.existsByUsername(joinRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("이미 사용중인 ID 입니다.");
        }
    }

    private void validatePasswordConfirmation(JoinRequest joinRequest) {
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordConfirm())) {
            throw new PasswordMismatchException("비밀번호가 서로 일치하지 않습니다.");
        }
    }



}
