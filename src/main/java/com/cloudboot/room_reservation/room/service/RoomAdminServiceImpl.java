package com.cloudboot.room_reservation.room.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.List;
import com.cloudboot.room_reservation.room.dto.*;
import com.cloudboot.room_reservation.room.entity.Room;
import com.cloudboot.room_reservation.room.repository.RoomRepository;
import com.cloudboot.room_reservation.reservation.repository.ReservationRepository;
import com.cloudboot.room_reservation.reservation.entity.Reservation;

@Service
@Transactional
public class RoomAdminServiceImpl implements RoomAdminService {
    private final RoomRepository roomRepo;
    private final ReservationRepository reservationRepo;

    public RoomAdminServiceImpl(RoomRepository roomRepo, ReservationRepository reservationRepo) {
        this.roomRepo = roomRepo;
        this.reservationRepo = reservationRepo;
    }

    @Override
    public RoomDto create(RoomDto dto) {
        Room room = new Room(dto.getRoomNumber());
        Room saved = roomRepo.save(room);
        return new RoomDto(saved.getId(), saved.getRoomNumber());
    }

    @Override
    public RoomDto update(Long id, RoomDto dto) {
        Room room = roomRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        room.setRoomNumber(dto.getRoomNumber());
        return new RoomDto(room.getId(), room.getRoomNumber());
    }

    @Override
    public void delete(Long id) {
        roomRepo.deleteById(id);
    }

    @Override
    public List<RoomStatusDto> listWithStatus() {
        return roomRepo.findAll().stream()
            .map(room -> {
                List<Reservation> list = reservationRepo.findByRoomId(room.getId());
                List<ReservationStatusDto> stats = list.stream()
                    .map(r -> new ReservationStatusDto(
                        r.getId(),
                        r.getMember().getId(),
                        r.getStartTime(),
                        r.getEndTime(),
                        r.getStatus()))
                    .collect(Collectors.toList());
                return new RoomStatusDto(room.getId(), room.getRoomNumber(), stats);
            })
            .collect(Collectors.toList());
    }
}
