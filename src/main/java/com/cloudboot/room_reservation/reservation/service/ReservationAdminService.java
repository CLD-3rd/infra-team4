package com.cloudboot.room_reservation.reservation.service;

import com.cloudboot.room_reservation.reservation.dto.response.ReservationListResponseDto;
import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.cloudboot.room_reservation.reservation.enumerate.ReservationStatus;
import com.cloudboot.room_reservation.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationAdminService {
    private final ReservationRepository reservationRepository;

    // 1. 관리자) 예약 승인
    public void approveReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            throw new IllegalStateException("이미 승인된 예약입니다.");
        } else if (reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new IllegalStateException("거절된 예약은 승인할 수 없습니다.");
        }
        reservation.setStatus(ReservationStatus.APPROVED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        // 이후 메일 전송 기능 연결
        // emailService.sendApprovalEmail(reservation);
    }

    // 2. 관리자) 예약 거절
    public void rejectReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        if (reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new IllegalStateException("이미 거절된 예약입니다.");
        } else if (reservation.getStatus() == ReservationStatus.APPROVED) {
            throw new IllegalStateException("승인된 예약은 거절할 수 없습니다.");
        }

        reservation.setStatus(ReservationStatus.REJECTED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        // 이후 메일 전송 기능 연결
        // emailService.sendRejectionEmail(reservation);
    }

    // 3. 관리자) 전체 예약 목록 조회
    public List<ReservationListResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
