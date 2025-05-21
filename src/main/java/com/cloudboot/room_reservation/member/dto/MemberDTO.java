package com.cloudboot.room_reservation.member.dto;

import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.enumerate.Role;
import lombok.Data;

@Data
public class MemberDTO {

    private Long memberId;

    private String username;

    private String password;

    private Role role;

    protected MemberDTO() {
    }

    private MemberDTO(Long id, String username, String password, Role role) {
        this.memberId = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static MemberDTO of(Long id, String username, String password, Role role) {
        return new MemberDTO(id, username, password, role);
    }

    public static MemberDTO from(Member member) {
        return MemberDTO.of(member.getMemberId(), member.getUsername(), member.getPassword(), member.getRole());
    }
}
