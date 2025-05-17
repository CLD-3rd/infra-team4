package com.cloudboot.room_reservation.member.dto.response;

import com.cloudboot.room_reservation.member.enumerate.Role;
import lombok.Data;

@Data
public class MemberResponse {

    private String username;

    private Role role;

    protected MemberResponse() { }

    private MemberResponse(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public static MemberResponse of(String username, Role role) {
        return new MemberResponse(username, role);
    }
}
