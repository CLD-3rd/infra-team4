package roomReservation.domain.infra.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomReservation.domain.infra.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByMember_MemberId(Long memberId);
    /**
     * 해당 룸에 대해 요청된 시간대와 겹치는 예약이 존재하는지 여부를 확인하는 쿼리
     * 예약 상태가 PENDING 또는 APPROVED인 예약만 고려 (CANCELLED 등은 제외; cancled도 예약 기록 남음(삭제x))
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.room.roomId = :roomId " +
            "AND r.status IN ('PENDING', 'APPROVED') " +
            "AND r.startTime < :endTime AND r.endTime > :startTime")
    boolean existsByRoomAndTimeOverlap(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

}
