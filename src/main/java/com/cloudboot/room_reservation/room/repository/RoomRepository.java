package com.cloudboot.room_reservation.room.repository;

import com.cloudboot.room_reservation.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
