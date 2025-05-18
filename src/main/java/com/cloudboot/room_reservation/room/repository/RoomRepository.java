package com.cloudboot.room_reservation.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cloudboot.room_reservation.room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {}
