package roomReservation.domain.infra.reservation.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomReservation.domain.infra.member.entity.Member;
import roomReservation.domain.infra.member.repository.MemberRepository;
import roomReservation.domain.infra.reservation.dto.ReservationRequestDto;
import roomReservation.domain.infra.reservation.entity.Reservation;
import roomReservation.domain.infra.reservation.repository.ReservationRepository;
import roomReservation.domain.infra.room.entity.Room;
import roomReservation.domain.infra.room.repository.RoomRepository;
import roomReservation.domain.infra.reservation.enumerate.ReservationStatus;
import roomReservation.domain.infra.reservation.dto.ReservationListResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    // 1. 사용자) 예약 생성
    public void createReservation(ReservationRequestDto request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당 룸이 존재하지 않습니다."));

        // 중복 예약 방지 (동일 룸, 겹치는 시간)
        boolean exists = reservationRepository.existsByRoomAndTimeOverlap(
                request.getRoomId(), request.getStartTime(), request.getEndTime());
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
    }
    // 3. 사용자) 예약 목록 조회
    public List<ReservationListResponseDto> getUserReservations(Long memberId) {
        List<Reservation> reservations = reservationRepository.findAllByMember_MemberId(memberId);
        return reservations.stream()
                .map(ReservationListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }



    // 4. 관리자) 예약 승인
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

    // 5. 관리자) 예약 거절
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

    // 6. 관리자) 전체 예약 목록 조회
    public List<ReservationListResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 7. 예약 상세 조회
    public ReservationListResponseDto getReservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));
        return ReservationListResponseDto.fromEntity(reservation);
    }

}
