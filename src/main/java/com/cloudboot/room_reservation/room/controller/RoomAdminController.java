package com.cloudboot.room_reservation.room.controller;

import com.cloudboot.room_reservation.room.entity.Room;
import com.cloudboot.room_reservation.room.service.RoomAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/rooms")
public class RoomAdminController {
    private final RoomAdminService service;
    public RoomAdminController(RoomAdminService service) { this.service = service; }

    @GetMapping public ResponseEntity<List<Room>> list() {
        return ResponseEntity.ok(service.getAll());
    }
    @PostMapping public ResponseEntity<Room> create(@RequestBody Room room) {
        return ResponseEntity.status(201).body(service.create(room));
    }
    @PutMapping("/{id}") public ResponseEntity<Room> update(@PathVariable Long id, @RequestBody Room body) {
        return ResponseEntity.ok(service.update(id, body.getRoomNumber()));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}