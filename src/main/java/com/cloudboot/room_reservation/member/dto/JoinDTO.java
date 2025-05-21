package com.cloudboot.room_reservation.member.dto;


import com.cloudboot.room_reservation.member.dto.request.JoinRequest;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.enumerate.Role;
import lombok.Data;

@Data
public class JoinDTO {

    private String username;

    private String password;

    private Role role;


    protected JoinDTO() {
    }

    private JoinDTO(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static JoinDTO of(String username, String password, Role role) {
        return new JoinDTO(username, password, role);
    }

    public static JoinDTO from(JoinRequest joinRequest) {
        return of(joinRequest.getUsername(), joinRequest.getPassword(), joinRequest.getRole());
    }

    public static Member toEntity(JoinDTO joinDTO) {
        return Member.of(joinDTO.getUsername(), joinDTO.getPassword(), joinDTO.getRole());
    }
}
