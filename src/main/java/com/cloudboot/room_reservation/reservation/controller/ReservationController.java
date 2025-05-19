package com.cloudboot.room_reservation.reservation.controller;

import java.util.List;
import java.util.Map;

import com.cloudboot.room_reservation.reservation.dto.request.ReservationRequestDto;
import com.cloudboot.room_reservation.reservation.dto.response.ReservationListResponseDto;
import com.cloudboot.room_reservation.reservation.service.ReservationService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/reservations"})
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    //생성
    @PostMapping
    public ResponseEntity<Map<String, String>> createReservation(@RequestBody ReservationRequestDto request) {
        this.reservationService.createReservation(request);
        Map<String, String> body = Map.of("message", "예약이 생성되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
    //취소
    @DeleteMapping({"/{reservationId}/cancel"})
    public ResponseEntity<Map<String, String>> cancelReservation(@PathVariable Long reservationId) {
        this.reservationService.cancelReservation(reservationId);
        Map<String, String> body = Map.of("message", "예약이 취소되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    // 모든 예약
    @GetMapping({"/member/{memberId}"})
    public ResponseEntity<List<ReservationListResponseDto>> getMyReservations(@PathVariable Long memberId) {
        return ResponseEntity.ok(this.reservationService.getUserReservations(memberId));
    }
    // 예약 상세 정보
    @GetMapping({"/detail/{reservationId}"})
    public ResponseEntity<ReservationListResponseDto> getReservationDetail(@PathVariable Long reservationId) {
        return ResponseEntity.ok(this.reservationService.getReservationDetail(reservationId));
    }
}