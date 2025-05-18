package com.cloudboot.room_reservation.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.cloudboot.room_reservation.reservation.repository.ReservationRepository;

@Service
@Transactional(readOnly=true)
public class ReservationAdminService {
    private final ReservationRepository repo;
    public ReservationAdminService(ReservationRepository repo) { this.repo = repo; }
    public List<Reservation> listByRoom(Long roomId) { return repo.findByRoom_RoomId(roomId); }
}
