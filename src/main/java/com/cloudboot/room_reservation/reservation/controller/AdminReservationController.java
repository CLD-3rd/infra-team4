package com.cloudboot.room_reservation.reservation.controller;

import com.cloudboot.room_reservation.reservation.dto.response.ReservationListResponseDto;
import com.cloudboot.room_reservation.reservation.service.ReservationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/admin/reservations"})
public class AdminReservationController {
    private final ReservationAdminService reservationAdminService;

    @PostMapping({"/{reservationId}/approve"})
    public ResponseEntity<Map<String, String>> approveReservation(@PathVariable Long reservationId) {
        this.reservationAdminService.approveReservation(reservationId);
        Map<String, String> body = Map.of("message", "예약이 승인되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping({"/{reservationId}/reject"})
    public ResponseEntity<Map<String, String>> rejectReservation(@PathVariable Long reservationId) {
        this.reservationAdminService.rejectReservation(reservationId);
        Map<String, String> body = Map.of("message", "예약이 취소되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping
    public ResponseEntity<List<ReservationListResponseDto>> getAllReservations() {
        return ResponseEntity.ok(this.reservationAdminService.getAllReservations());
    }

}