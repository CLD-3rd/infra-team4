package com.cloudboot.room_reservation.room.dto;

import java.time.LocalDateTime;
import com.cloudboot.room_reservation.reservation.entity.ReservationStatus;

public class ReservationStatusDto {
    private Long reservationId;
    private Long memberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;

    public ReservationStatusDto() {}
    public ReservationStatusDto(Long reservationId, Long memberId,
                                LocalDateTime startTime, LocalDateTime endTime,
                                ReservationStatus status) {
        this.reservationId = reservationId;
        this.memberId = memberId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getReservationId() { return reservationId; }
    public Long getMemberId() { return memberId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public ReservationStatus getStatus() { return status; }
}
