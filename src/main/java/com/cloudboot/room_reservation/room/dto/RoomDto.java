package com.cloudboot.room_reservation.room.dto;

public class RoomDto {
    private Long id;
    private String roomNumber;

    public RoomDto() {}
    public RoomDto(Long id, String roomNumber) {
        this.id = id;
        this.roomNumber = roomNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}
