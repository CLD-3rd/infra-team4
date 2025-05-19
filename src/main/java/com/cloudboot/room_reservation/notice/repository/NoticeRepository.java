package com.cloudboot.room_reservation.notice.repository;

import com.cloudboot.room_reservation.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
