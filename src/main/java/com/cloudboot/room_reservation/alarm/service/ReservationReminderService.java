package com.cloudboot.room_reservation.alarm.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.cloudboot.room_reservation.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationReminderService {


    private final ReservationRepository reservationRepository;
    private final ReservationEmailSender emailService;

    /**
     * 매시 50분마다 10분 전 예약을 조회하고 비동기로 이메일 알림 전송
     */
    @Scheduled(cron = "0 50 * * * *") // 매시 50분 정각에 실행
    public void sendReminder() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservations = reservationRepository.findByStartTimeBetween(
                now.plusMinutes(9), now.plusMinutes(11));

        reservations.forEach(emailService::sendReminderAsync); // 비동기 호출
    }
	
}
