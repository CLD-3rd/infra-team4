package com.cloudboot.room_reservation.room.repository;

import com.cloudboot.room_reservation.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    /**
     * 룸 번호로 룸 조회
     * @param roomNumber 룸 번호
     * @return Optional 형태의 Room 엔티티
     */
    Optional<Room> findByRoomNumber(String roomNumber);
    
    @Query("""
    	    SELECT DISTINCT r 
    	    FROM Room r
    	    LEFT JOIN FETCH r.reservations res
    	    LEFT JOIN FETCH res.member m
    	    """)
    	List<Room> findAllByFetchJoin();
}
