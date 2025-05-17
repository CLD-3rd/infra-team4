package com.cloudboot.room_reservation.member.controller;

import com.cloudboot.room_reservation.member.dto.CustomMemberDetails;
import com.cloudboot.room_reservation.member.dto.request.UpdateMemberRequest;
import com.cloudboot.room_reservation.member.dto.response.MemberResponse;
import com.cloudboot.room_reservation.member.service.MemberApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class MemberApiController {

    private final MemberApiService memberApiService;

    public MemberApiController(MemberApiService memberApiService) {
        this.memberApiService = memberApiService;
    }

    @GetMapping("/user")
    public ResponseEntity<MemberResponse> findMyDetails(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(memberApiService.findById(customMemberDetails.getId()));
    }

    @PutMapping("/user/update")
    public ResponseEntity<Map<String, String>> updateMember(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                            @RequestBody @Validated UpdateMemberRequest updateMemberRequest) {
        memberApiService.updateMember(customMemberDetails.getId(), updateMemberRequest);
        Map<String, String> body = Map.of("message", "비밀번호 변경이 완료되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }

}
