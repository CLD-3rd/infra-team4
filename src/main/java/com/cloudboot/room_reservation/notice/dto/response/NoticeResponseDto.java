package com.cloudboot.room_reservation.notice.dto.response;

import com.cloudboot.room_reservation.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long adminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

