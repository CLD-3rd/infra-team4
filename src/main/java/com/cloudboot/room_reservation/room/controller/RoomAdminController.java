package com.cloudboot.room_reservation.room.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.cloudboot.room_reservation.room.entity.Room;
import com.cloudboot.room_reservation.room.service.RoomService;

@RestController
@RequestMapping("/api/admin/rooms")
public class RoomAdminController {
    private final RoomService service;
    public RoomAdminController(RoomService service) { this.service = service; }

    @GetMapping public List<Room> list() { return service.listAll(); }
    @PostMapping public Room create(@RequestBody Room r) { return service.create(r); }
    @PutMapping("/{id}") public Room update(@PathVariable Long id, @RequestBody Room r) { return service.update(id, r); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
