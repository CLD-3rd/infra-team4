package com.cloudboot.room_reservation.member.controller;

import com.cloudboot.room_reservation.member.dto.request.JoinRequest;
import com.cloudboot.room_reservation.member.service.JoinApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class JoinApiController {

    private final JoinApiService joinApiService;

    public JoinApiController(JoinApiService joinApiService) {
        this.joinApiService = joinApiService;
    }

    @PostMapping("/join")
    public void join(@RequestBody @Validated JoinRequest joinRequest) {
        log.info("Join request received: {}", joinRequest);  // 로깅 추가
        joinApiService.registerMember(joinRequest);
    }

}