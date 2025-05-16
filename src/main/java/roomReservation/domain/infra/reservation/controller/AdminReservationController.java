package roomReservation.domain.infra.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomReservation.domain.infra.reservation.dto.ReservationListResponseDto;
import roomReservation.domain.infra.reservation.service.ReservationService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    // 관리자) 예약 승인
    @PostMapping("/{reservationId}/approve")
    public ResponseEntity<Map<String, String>> approveReservation(@PathVariable Long reservationId) {
        reservationService.approveReservation(reservationId);
        Map<String, String> body = Map.of("message", "예약이 승인되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 관리자) 예약 거절
    @PostMapping("/{reservationId}/reject")
    public ResponseEntity<Map<String, String>> rejectReservation(@PathVariable Long reservationId) {
        reservationService.rejectReservation(reservationId);
        Map<String, String> body = Map.of("message", "예약이 취소되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 관리자) 예약 목록 조회
    @GetMapping
    public ResponseEntity<List<ReservationListResponseDto>> getAllReservations(){
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

}
