package com.cloudboot.room_reservation.member.entity;


import com.cloudboot.room_reservation.member.enumerate.Role;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private Member(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    protected Member() { }

    public static Member of(String username, String password, Role role) {
        return new Member(username, password, role);
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

}
