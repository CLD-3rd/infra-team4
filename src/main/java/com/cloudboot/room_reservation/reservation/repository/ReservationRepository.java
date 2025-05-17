package com.cloudboot.room_reservation.reservation.repository;

import com.cloudboot.room_reservation.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRoom_RoomId(Long roomId);
}
