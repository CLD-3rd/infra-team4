package com.cloudboot.room_reservation.alarm.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import com.cloudboot.room_reservation.util.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailTemplateService {
	
	private static final String MAIL_TEMPLATE_PATH = "/templates/mail/";
	private static final String VARIABLE_PREFIX = "${";
	private static final String VARIABLE_SUFFIX = "}";
	
	
	public String emailTemplate(final String resourceName, Map<String, String> data) {
		
		try {
			final File resource = new ClassPathResource(MAIL_TEMPLATE_PATH + resourceName).getFile();
			String html = Files.readString(resource.toPath(), StandardCharsets.UTF_8);
				
			for (Entry<String, String> entry : data.entrySet()) {
				html = html.replace(VARIABLE_PREFIX + entry.getKey() + VARIABLE_SUFFIX, entry.getValue());
			}
			
			return html;
			
		} catch (IOException e) {
			log.error("이메일 템플릿 파일 로딩 중 예외 발생", e);
			throw new ApiException(HttpStatus.BAD_REQUEST, "파일을 가져오는 데에 실패하였습니다.");
		}
	}

}
