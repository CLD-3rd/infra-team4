package com.cloudboot.room_reservation.member.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomMemberDetails implements UserDetails {

    private final MemberDTO memberDTO;

    public CustomMemberDetails(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    public Long getId() {
        return memberDTO.getId();
    }

    @Override
    public String getUsername() {
        return memberDTO.getUsername();
    }

    @Override
    public String getPassword() {
        return memberDTO.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(memberDTO.getRole());
            }
        });
        return collection;
    }




}
