package roomReservation.domain.infra.reservation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomReservation.domain.infra.reservation.dto.ReservationRequestDto;
import roomReservation.domain.infra.reservation.dto.ReservationListResponseDto;
import roomReservation.domain.infra.reservation.service.ReservationService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;


    // 사용자) 예약 생성
    @PostMapping
    public ResponseEntity<Map<String, String>> createReservation(@RequestBody ReservationRequestDto request) {
        reservationService.createReservation(request);
        Map<String, String> body = Map.of("message", "예약이 생성되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // 사용자) 예약 취소
    @DeleteMapping("/{reservationId}/cancel")
    public ResponseEntity<Map<String, String>> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        Map<String, String> body = Map.of("message", "예약이 취소되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 사용자) 예약 목록 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ReservationListResponseDto>> getMyReservations(@PathVariable Long memberId){
        return ResponseEntity.ok(reservationService.getUserReservations(memberId));
    }

    // 사용자) 예약 상세 조회
    @GetMapping("/detail/{reservationId}")
    public ResponseEntity<ReservationListResponseDto> getReservationDetail(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.getReservationDetail(reservationId));
    }
}
