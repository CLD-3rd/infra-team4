package com.cloudboot.room_reservation.alarm.service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cloudboot.room_reservation.alarm.enumerate.ReservationMailStatus;
import com.cloudboot.room_reservation.reservation.entity.Reservation;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationEmailAsyncSender {

	private final EmailService emailService;
	
	final String RESERVATION_SUBJECT = "[스터디룸 예약시스템] 📢 예약 알림 안내";

    @Async
    public void sendReminder(Reservation reservation) {
        emailService.sendTemplate(
                reservation.getUsername(), 
                RESERVATION_SUBJECT,
                resourceBy(ReservationMailStatus.REMINDER),
                dataFrom(reservation)
        );
    }
    
    @Async
    public void send(ReservationMailStatus status, Reservation reservation) {
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
}
