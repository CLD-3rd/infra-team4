package com.cloudboot.room_reservation.alarm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudboot.room_reservation.alarm.dto.EmailRequest;
import com.cloudboot.room_reservation.alarm.service.ReservationEmailSender;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class EmailController {
	
	private final ReservationEmailSender emailSender;
	
	
	/**
	 * 예약 승인 이메일 전송 테스트
	 * @param to 받는사람 이메일주소
	 * @return
	 */
	@PostMapping("/approved")
	public ResponseEntity<?> approved(@RequestBody EmailRequest emailRequest) {
		
		emailSender.sendApprovedTest(emailRequest.getTo());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 예약 취소 이메일 전송 테스트
	 * @param to
	 * @return
	 */
	@PostMapping("/canceled")
	public ResponseEntity<?> canceled(@RequestBody EmailRequest emailRequest) {
		
		emailSender.sendCanceledTest(emailRequest.getTo());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 예약 거절 이메일 전송 테스트
	 * @param to
	 * @return
	 */
	@PostMapping("/rejected")
	public ResponseEntity<?> rejected(@RequestBody EmailRequest emailRequest) {
		
		emailSender.sendRejectedTest(emailRequest.getTo());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
