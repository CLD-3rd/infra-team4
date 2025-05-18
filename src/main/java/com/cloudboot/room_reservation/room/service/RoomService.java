package com.cloudboot.room_reservation.room.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.cloudboot.room_reservation.room.entity.Room;
import com.cloudboot.room_reservation.room.repository.RoomRepository;

@Service
@Transactional
public class RoomService {
    private final RoomRepository repo;
    public RoomService(RoomRepository repo) { this.repo = repo; }
    public List<Room> listAll() { return repo.findAll(); }
    public Room create(Room r) { return repo.save(r); }
    public Room update(Long id, Room r) {
        Room room = repo.findById(id).orElseThrow();
        room.setRoomNumber(r.getRoomNumber());
        return repo.save(room);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
