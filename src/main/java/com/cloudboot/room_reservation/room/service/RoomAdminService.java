package com.cloudboot.room_reservation.room.service;

import com.cloudboot.room_reservation.room.dto.request.RoomRequestDto;
import com.cloudboot.room_reservation.room.dto.response.RoomResponseDto;
import com.cloudboot.room_reservation.room.entity.Room;
import com.cloudboot.room_reservation.room.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomAdminService {

    private final RoomRepository roomRepository;

    public RoomResponseDto createRoom(RoomRequestDto dto) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        return RoomResponseDto.fromEntity(roomRepository.save(room));
    }

    public List<RoomResponseDto> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(RoomResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public RoomResponseDto updateRoom(Long roomId, RoomRequestDto dto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        room.setRoomNumber(dto.getRoomNumber());
        return RoomResponseDto.fromEntity(roomRepository.save(room));
    }

    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
