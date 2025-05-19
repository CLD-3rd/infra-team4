package com.cloudboot.room_reservation.member.service;

import com.cloudboot.room_reservation.member.dto.CustomMemberDetails;
import com.cloudboot.room_reservation.member.dto.MemberDTO;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomerMemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member findMember = memberRepository.findByUsername(username);

        if (findMember == null) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }

        MemberDTO memberDTO = MemberDTO.from(findMember);

        return new CustomMemberDetails(memberDTO);

    }
}
