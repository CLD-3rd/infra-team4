package com.cloudboot.room_reservation.room.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    @Column(nullable=false, unique=true)
    private String roomNumber;

    public Long getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}
