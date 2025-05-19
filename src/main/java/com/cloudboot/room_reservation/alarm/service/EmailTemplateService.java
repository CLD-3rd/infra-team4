package com.cloudboot.room_reservation.alarm.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.io.ClassPathResource;

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
			// TODO 에러핸들러 수정
			throw new RuntimeException("파일을 가져오는 데에 실패하였습니다.", e);
		}
	}

}
