package com.cloudboot.room_reservation.room.controller;

import com.cloudboot.room_reservation.room.dto.request.RoomRequestDto;
import com.cloudboot.room_reservation.room.dto.response.RoomResponseDto;
import com.cloudboot.room_reservation.room.service.RoomAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class RoomAdminController {

    private final RoomAdminService roomAdminService;

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomRequestDto dto) {
        return ResponseEntity.ok(roomAdminService.createRoom(dto));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        return ResponseEntity.ok(roomAdminService.getAllRooms());
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long roomId,
                                                      @RequestBody RoomRequestDto dto) {
        return ResponseEntity.ok(roomAdminService.updateRoom(roomId, dto));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomAdminService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
