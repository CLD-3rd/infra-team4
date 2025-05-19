package com.cloudboot.room_reservation.notice.controller;

import com.cloudboot.room_reservation.notice.dto.request.NoticeRequestDto;
import com.cloudboot.room_reservation.notice.dto.response.NoticeResponseDto;
import com.cloudboot.room_reservation.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notices")
public class AdminNoticeController {

    private final NoticeService noticeService;

    // 공지사항 등록
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody NoticeRequestDto requestDto,
                                                          @RequestParam Long adminId) {
        NoticeResponseDto responseDto = noticeService.createNotice(requestDto, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 공지사항 수정
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long noticeId,
                                                          @RequestBody NoticeRequestDto requestDto) {
        NoticeResponseDto responseDto = noticeService.updateNotice(noticeId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 공지사항 삭제
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Map<String, String>> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok(Map.of("message", "공지사항이 삭제되었습니다."));
    }
}

