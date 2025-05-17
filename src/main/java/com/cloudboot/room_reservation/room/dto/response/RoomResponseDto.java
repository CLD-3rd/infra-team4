package com.cloudboot.room_reservation.room.dto.response;

import com.cloudboot.room_reservation.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {
    private Long roomId;
    private String roomNumber;

    public static RoomResponseDto fromEntity(Room room) {
        return new RoomResponseDto(room.getRoomId(), room.getRoomNumber());
    }
}
