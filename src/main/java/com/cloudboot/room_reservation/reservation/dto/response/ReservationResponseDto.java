package com.cloudboot.room_reservation.reservation.dto.response;

import com.cloudboot.room_reservation.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {

    private Long reservationId;
    private Long roomId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static ReservationResponseDto fromEntity(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getReservationId(),
                reservation.getRoom().getRoomId(),
                reservation.getStatus().name(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );
    }
}
