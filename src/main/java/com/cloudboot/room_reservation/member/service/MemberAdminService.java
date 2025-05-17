package com.cloudboot.room_reservation.member.service;

import com.cloudboot.room_reservation.member.dto.response.MemberAdminResponse;
import com.cloudboot.room_reservation.member.dto.response.MemberResponse;
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

}
