package com.cloudboot.room_reservation.alarm.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudboot.room_reservation.alarm.enumerate.ReservationMailStatus;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.enumerate.Role;
import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.cloudboot.room_reservation.reservation.enumerate.ReservationStatus;
import com.cloudboot.room_reservation.reservation.repository.ReservationRepository;
import com.cloudboot.room_reservation.room.entity.Room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationEmailSender {

	private final EmailService emailService;
	private final ReservationRepository reservationRepository;
	
	final String RESERVATION_SUBJECT = "[스터디룸 예약시스템] 📢 예약 알림 안내";


	public void sendApprovedTest(String to) {
		Reservation reservation = createReservation(to, ReservationStatus.APPROVED);

		send(ReservationMailStatus.valueOf(reservation.getStatus().toString()), 
				reservation);
	}

	

	public void sendCanceledTest(String to) {
		Reservation reservation = createReservation(to, ReservationStatus.CANCELED);

		send(ReservationMailStatus.valueOf(reservation.getStatus().toString()), 
				reservation);
		
	}

	public void sendRejectedTest(String to) {
		Reservation reservation = createReservation(to, ReservationStatus.REJECTED);

		send(ReservationMailStatus.valueOf(reservation.getStatus().toString()), 
				reservation);
		
	}	
	
	/**
	 * 예약 알림 전송
	 * - APPROVED (승인)
	 * - REJECTED (거절)
	 * - CANCELED (취소)
	 * @param reservation
	 */
	@Transactional
	public void send(Reservation reservation) {
		// fetch join 하여 데이터 가져옴
		reservationRepository.findByReservationId(reservation.getReservationId());
		send(ReservationMailStatus.valueOf(reservation.getStatus().toString()), 
				reservation);
	}
	
	/**
	 * 10분 전 예약 알림 전송
	 */
	public void sendReminder(String to) {
		// 1. 시작 10분 전 예약 조회 (전송 딜레이 오차 허용)
		LocalDateTime now = LocalDateTime.now();
		List<Reservation> reservations = reservationRepository.findByStartTimeBetween(now.plusMinutes(9), now.plusMinutes(11));
		
		reservations.forEach(reservation -> {
			
			// 2. 전송
			send(ReservationMailStatus.REMINDER, 
					reservation);
		});
	}
	

    @Async
    public void sendReminderAsync(Reservation reservation) {
        emailService.sendTemplate(
                reservation.getUsername(), 
                RESERVATION_SUBJECT,
                resourceBy(ReservationMailStatus.REMINDER),
                dataFrom(reservation)
        );
    }
    
    private void send(ReservationMailStatus status, Reservation reservation) {
    	emailService.sendTemplate(reservation.getUsername(), 
    			RESERVATION_SUBJECT, 
    			resourceBy(status), 
    			dataFrom(reservation));
    }

	private String resourceBy(ReservationMailStatus status) {
		return switch (status) {
			case APPROVED -> "reservation-success";
			case REJECTED -> "reservation-rejected";
			case CANCELED -> "reservation-canceled";
			case REMINDER -> "reservation-reminder";
					
			default ->
				throw new IllegalArgumentException("Unexpected value: " + status);
			};
	}


	private Map<String, String> dataFrom(Reservation reservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Map<String, String> data = new HashMap<>();
		data.put("name", reservation.getUsername());
		data.put("startTime", reservation.getStartTime().format(formatter));
		data.put("endTime", reservation.getEndTime().format(formatter));
		data.put("room", String.valueOf(reservation.getRoomNumber()));
		
		return data;
	}
	

	private Reservation createReservation(String to, ReservationStatus status) {
		return new Reservation(
				1L,
				new Room(1L, 202), 
				LocalDateTime.now(), 
				LocalDateTime.now().plusHours(2), 
				status,
				Member.of(to, "1234", Role.ROLE_USER));
	}

	
}
