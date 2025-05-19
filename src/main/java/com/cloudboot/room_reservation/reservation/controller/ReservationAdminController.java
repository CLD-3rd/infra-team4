package com.cloudboot.room_reservation.reservation.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.cloudboot.room_reservation.reservation.service.ReservationAdminService;

@RestController
@RequestMapping("/api/admin/reservations")
public class ReservationAdminController {
    private final ReservationAdminService service;
    public ReservationAdminController(ReservationAdminService service) { this.service = service; }
    @GetMapping public List<Reservation> listByRoom(@RequestParam Long roomId) { return service.listByRoom(roomId); }
}
