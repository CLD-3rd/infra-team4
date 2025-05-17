package com.cloudboot.room_reservation.reservation.service;

import com.cloudboot.room_reservation.reservation.dto.response.ReservationResponseDto;
import com.cloudboot.room_reservation.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationAdminService {

    private final ReservationRepository reservationRepository;

    public List<ReservationResponseDto> getReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoom_RoomId(roomId)
                .stream()
                .map(ReservationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
