package roomReservation.domain.infra.reservation.dto;


import lombok.Builder;
import lombok.Getter;
import roomReservation.domain.infra.reservation.entity.Reservation;

@Getter
@Builder
public class ReservationListResponseDto {
    private Long reservationId;
    private Long memberId;
    private Long roomId;
    private String startTime;     // "2025-05-15 09:00"
    private String endTime;       // "2025-05-15 12:00"
    private String status;        // "APPROVED"
    private String createdAt;     // "2025-05-15 03:20"
    private String updatedAt;     // "2025-05-15 03:20"

    public static ReservationListResponseDto fromEntity(Reservation reservation) {
        return ReservationListResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .memberId(reservation.getMember().getMemberId())
                .roomId(reservation.getRoom().getRoomId())
                .startTime(reservation.getStartTime().toString().substring(0,16))  // yyyy-MM-dd HH:mm
                .endTime(reservation.getEndTime().toString().substring(0,16))
                .status(reservation.getStatus().name())
                .createdAt(reservation.getCreatedAt().toString().substring(0,16))
                .updatedAt(reservation.getUpdatedAt().toString().substring(0,16))
                .build();
    }
}
