package com.cloudboot.room_reservation.reservation.controller;

import com.cloudboot.room_reservation.reservation.dto.response.ReservationResponseDto;
import com.cloudboot.room_reservation.reservation.service.ReservationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getByRoom(@RequestParam Long roomId) {
        return ResponseEntity.ok(reservationAdminService.getReservationsByRoom(roomId));
    }
}
