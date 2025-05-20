package com.cloudboot.room_reservation.member.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(unique = true)
    private String username;

    private String refresh;

    private String expiration;

    public RefreshToken() {
    }

    private RefreshToken(String username, String refresh, String expiration) {
        this.username = username;
        this.refresh = refresh;
        this.expiration = expiration;
    }

    public static RefreshToken of(String username, String refresh, String expiration) {
        return new RefreshToken(username, refresh, expiration);
    }

}
