package com.cloudboot.room_reservation.alarm.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cloudboot.room_reservation.util.exception.ApiException;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {


	private final JavaMailSender mailSender;
	private final EmailTemplateService templateService;
	
	@Value("${spring.mail.username}")
	private String mailFrom;
	
	private static final String HTML_SUFFIX = ".html";
	
	
	public void sendTemplate(String to, String subject, String resourceName, Map<String, String> data) {

		MimeMessageHelper message = new MimeMessageHelper(mailSender.createMimeMessage());
		String template = templateService.emailTemplate(resourceName + HTML_SUFFIX, data);
		
		try {
			message.setTo(to);
			message.setFrom(mailFrom);
			message.setSubject(subject);
			message.setText(template, true);
			
			mailSender.send(message.getMimeMessage());
			
		} catch (MessagingException e) {
			log.error("메일 전송 중 예외 발생", e);
			throw new ApiException(HttpStatus.BAD_REQUEST, "메일 전송에 실패하였습니다.");
		}
	}
}
