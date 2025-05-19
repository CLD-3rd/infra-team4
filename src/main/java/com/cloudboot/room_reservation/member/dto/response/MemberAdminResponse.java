package com.cloudboot.room_reservation.member.dto.response;

import com.cloudboot.room_reservation.member.dto.MemberDTO;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.enumerate.Role;
import lombok.Data;

@Data
public class MemberAdminResponse {

    private Long id;

    private String username;

    private String password;

    private Role role;

    protected MemberAdminResponse() {
    }

    private MemberAdminResponse(Long id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public static MemberAdminResponse of(Long id, String username, Role role) {
        return new MemberAdminResponse(id, username, role);
    }

    public static MemberAdminResponse from(Member member) {
        return MemberAdminResponse.of(member.getId(), member.getUsername(), member.getRole());
    }

}
