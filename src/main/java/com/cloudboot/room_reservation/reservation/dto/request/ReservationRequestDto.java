package com.cloudboot.room_reservation.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequestDto {
    private Long roomNumber;
    private Long memberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
