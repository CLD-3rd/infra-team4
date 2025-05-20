package com.cloudboot.room_reservation.room.service;

import java.util.List;
import com.cloudboot.room_reservation.room.dto.RoomDto;
import com.cloudboot.room_reservation.room.dto.RoomStatusDto;

public interface RoomAdminService {
    RoomDto create(RoomDto dto);
    RoomDto update(Long id, RoomDto dto);
    void delete(Long id);
    List<RoomStatusDto> listWithStatus();
}
