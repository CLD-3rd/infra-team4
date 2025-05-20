package com.cloudboot.room_reservation.reservation.service;

import com.cloudboot.room_reservation.alarm.service.ReservationEmailSender;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.repository.MemberRepository;
import com.cloudboot.room_reservation.reservation.dto.request.ReservationRequestDto;
import com.cloudboot.room_reservation.reservation.dto.response.ReservationListResponseDto;
import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.cloudboot.room_reservation.reservation.enumerate.ReservationStatus;
import com.cloudboot.room_reservation.reservation.repository.ReservationRepository;
import com.cloudboot.room_reservation.room.entity.Room;
import com.cloudboot.room_reservation.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    
    private final ReservationEmailSender reservationEmailSender;
    

    // 1. 사용자) 예약 생성
    @Transactional
    public void createReservation(ReservationRequestDto request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Room room = roomRepository.findByRoomNumber(String.valueOf(request.getRoomNumber()))
                .orElseThrow(() -> new IllegalArgumentException("해당 룸이 존재하지 않습니다."));
        LocalDateTime now = LocalDateTime.now();
        if (request.getStartTime().isBefore(now.plusHours(2))) {
            throw new IllegalArgumentException("예약은 현재 시간으로부터 최소 2시간 이후부터 가능합니다.");
        }
        // 중복 예약 방지 (동일 룸, 겹치는 시간)
        boolean exists = reservationRepository.existsByRoomAndTimeOverlap(
                request.getRoomNumber(), request.getStartTime(), request.getEndTime());
        if (exists) {
            throw new IllegalStateException("해당 시간대에는 이미 예약이 존재합니다.");
        }
        Reservation reservation = Reservation.builder()
                .member(member)
                .room(room)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        reservationRepository.save(reservation);
    }
    // 2. 사용자) 예약 취소
    @Transactional
    public void cancelReservation(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        if (reservation.getStatus() == ReservationStatus.CANCELED ||
                reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new IllegalStateException("이미 취소되었거나 거절된 예약입니다.");
        }
        reservation.setStatus(ReservationStatus.CANCELED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        // 메일 전송
        reservationEmailSender.send(reservation);
    }
    // 3. 사용자) 예약 목록 조회
    public List<ReservationListResponseDto> getUserReservations(Long memberId) {
        List<Reservation> reservations = reservationRepository.findAllByMember_MemberId(memberId);
        return reservations.stream()
                .map(ReservationListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 4. 예약 상세 조회
    public ReservationListResponseDto getReservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));
        return ReservationListResponseDto.fromEntity(reservation);
    }

}