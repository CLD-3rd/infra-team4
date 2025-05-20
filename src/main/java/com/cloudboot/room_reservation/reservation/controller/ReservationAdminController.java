package com.cloudboot.room_reservation.reservation.controller;

import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.cloudboot.room_reservation.reservation.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/rooms/{roomId}/reservations")
public class ReservationAdminController {
    private final ReservationRepository repo;
    public ReservationAdminController(ReservationRepository repo) { this.repo = repo; }

    @GetMapping public ResponseEntity<List<Reservation>> list(@PathVariable Long roomId) {
        return ResponseEntity.ok(repo.findByRoomRoomId(roomId));
    }
}