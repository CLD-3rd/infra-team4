package com.cloudboot.room_reservation.reservation.repository;

import com.cloudboot.room_reservation.reservation.entity.Reservation;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 룸별 예약 조회
	@Query("""
			SELECT DISTINCT r
			FROM Reservation r
			JOIN FETCH r.member m
			JOIN FETCH r.room rm 
			WHERE rm.roomId = :roomId
			""")
    List<Reservation> findByRoomRoomId(@Param("roomId") Long roomId);

    // 회원별 전체 예약 조회
    List<Reservation> findAllByMember_MemberId(Long memberId, Sort sort);

    /**
     * 해당 룸에 대해 요청된 시간대와 겹치는 예약이 존재하는지 확인
     * 예약 상태가 PENDING 또는 APPROVED인 예약만 고려
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Reservation r " +
           "WHERE r.room.roomId = :roomId " +
           "AND r.status IN ('PENDING', 'APPROVED') " +
           "AND r.startTime < :endTime AND r.endTime > :startTime")
    boolean existsByRoomAndTimeOverlap(
        @Param("roomId") String roomNumber,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    /**
     * 주어진 기간 내 예약 목록 조회 (member, room fetch 조인 포함)
     */
    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.member " +
           "JOIN FETCH r.room " +
           "WHERE r.startTime BETWEEN :start AND :end")
    List<Reservation> findByStartTimeBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    /**
     * 특정 예약 단건 조회 (fetch 조인)
     */
    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.member " +
           "JOIN FETCH r.room " +
           "WHERE r.reservationId = :reservationId")
    Reservation findByReservationId(@Param("reservationId") Long reservationId);
    
    List<Reservation> findAllByOrderByReservationIdDesc();
}
