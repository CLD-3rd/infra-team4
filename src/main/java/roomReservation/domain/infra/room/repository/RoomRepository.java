package roomReservation.domain.infra.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import roomReservation.domain.infra.room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
