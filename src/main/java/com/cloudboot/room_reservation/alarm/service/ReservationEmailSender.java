package com.cloudboot.room_reservation.alarm.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudboot.room_reservation.alarm.enumerate.ReservationMailStatus;
import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.enumerate.Role;
import com.cloudboot.room_reservation.reservation.entity.Reservation;
import com.cloudboot.room_reservation.reservation.enumerate.ReservationStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationEmailSender {

	private final EmailService emailService;
	
	final String RESERVATION_SUBJECT = "[스터디룸 예약시스템] 📢 예약 알림 안내";


	public void sendApprovedTest(String to) {
		Reservation reservation = createReservation(to, ReservationStatus.APPROVED);

		send(reservation.getMember().getUsername(), 
				ReservationMailStatus.valueOf(reservation.getStatus().toString()), 
				reservation);
	}

	

	public void sendCanceledTest(String to) {
		Reservation reservation = createReservation(to, ReservationStatus.CANCELED);

		send(reservation.getMember().getUsername(), 
				ReservationMailStatus.valueOf(reservation.getStatus().toString()), 
				reservation);
		
	}

	public void sendRejectedTest(String to) {
		Reservation reservation = createReservation(to, ReservationStatus.REJECTED);

		send(reservation.getMember().getUsername(), 
				ReservationMailStatus.valueOf(reservation.getStatus().toString()), 
				reservation);
		
	}	
	
	/**
	 * 예약 알림 전송
	 * - 승인 알림
	 * - 거절 알림
	 * - 취소 알림
	 * 
	 * @param reservation
	 */
	public void send(Reservation reservation) {
	
		send(reservation.getMember().getUsername(), 
				ReservationMailStatus.valueOf(reservation.getStatus().toString()), 
				reservation);
	}
		

	private void send(String to, ReservationMailStatus status, Reservation reservation) {
		
		emailService.sendTemplate(to, RESERVATION_SUBJECT, resourceBy(status), dataFrom(reservation));
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
				202, 
				LocalDateTime.now(), 
				LocalDateTime.now().plusHours(2), 
				status,
				Member.of(to, "1234", Role.ROLE_USER));
	}

	
}
