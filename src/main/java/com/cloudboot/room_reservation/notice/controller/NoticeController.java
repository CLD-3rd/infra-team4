package com.cloudboot.room_reservation.notice.controller;


import com.cloudboot.room_reservation.notice.dto.response.NoticeResponseDto;
import com.cloudboot.room_reservation.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/admin/notices"})
public class NoticeController {
    private final NoticeService noticeService;
    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices() {
        List<NoticeResponseDto> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }
}