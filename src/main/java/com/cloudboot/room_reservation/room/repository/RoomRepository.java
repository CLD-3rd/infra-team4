package com.cloudboot.room_reservation.room.repository;

import com.cloudboot.room_reservation.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    /**
     * 룸 번호로 룸 조회
     * @param roomNumber 룸 번호
     * @return Optional 형태의 Room 엔티티
     */
    Optional<Room> findByRoomNumber(Long roomNumber);
}
