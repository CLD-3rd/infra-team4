package com.cloudboot.room_reservation.room.entity;

import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;
    
    @Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public Room(Long id, String roomNumber) {
        this.roomId = id;
        this.roomNumber = roomNumber;
    }
}
