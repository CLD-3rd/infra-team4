package com.cloudboot.room_reservation.notice.service;

import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.member.repository.MemberRepository;
import com.cloudboot.room_reservation.notice.dto.request.NoticeRequestDto;
import com.cloudboot.room_reservation.notice.dto.response.NoticeResponseDto;
import com.cloudboot.room_reservation.notice.entity.Notice;
import com.cloudboot.room_reservation.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public NoticeResponseDto createNotice(NoticeRequestDto dto, Long adminId) {
        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .admin(admin)
                .build();
        noticeRepository.save(notice);
        return toDto(notice);
    }

    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto dto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."));
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        return toDto(noticeRepository.save(notice));
    }

    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
    public List<NoticeResponseDto> getAllNotices() {

        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private NoticeResponseDto toDto(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .adminId(notice.getAdmin().getMemberId())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }

}