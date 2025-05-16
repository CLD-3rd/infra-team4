package com.cloudboot.room_reservation.member.dto;


import com.cloudboot.room_reservation.member.dto.request.JoinRequest;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.enumerate.Role;
import lombok.Data;

@Data
public class JoinDTO {

    private String username;

    private String password;


    protected JoinDTO() {
    }

    private JoinDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static JoinDTO of(String username, String password) {
        return new JoinDTO(username, password);
    }

    public static JoinDTO from(JoinRequest joinRequest) {
        return of(joinRequest.getUsername(), joinRequest.getPassword());
    }

    public static Member toEntity(JoinDTO joinDTO) {
        return Member.of(joinDTO.getUsername(), joinDTO.getPassword(), Role.ROLE_USER);
    }
}
