package com.cloudboot.room_reservation.room.dto;

import java.util.List;

public class RoomStatusDto {
    private Long roomId;
    private String roomNumber;
    private List<ReservationStatusDto> reservations;

    public RoomStatusDto() {}
    public RoomStatusDto(Long roomId, String roomNumber, List<ReservationStatusDto> reservations) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.reservations = reservations;
    }

    public Long getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public List<ReservationStatusDto> getReservations() { return reservations; }
}
