package com.cloudboot.room_reservation.room.service;

import com.cloudboot.room_reservation.room.entity.Room;
import com.cloudboot.room_reservation.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RoomAdminService {
    private final RoomRepository repo;
    public RoomAdminService(RoomRepository repo) { this.repo = repo; }
    public List<Room> getAll() { return repo.findAllByFetchJoin(); }
    
    public Room create(Room room) { return repo.save(room); }
    public Room update(Long id, String number) {
        Room r = repo.findById(id).orElseThrow();
        r.setRoomNumber(number);
        return repo.save(r);
    }
    public void delete(Long id) { repo.deleteById(id); }
}