package com.cloudboot.room_reservation.member.controller;

import com.cloudboot.room_reservation.member.dto.CustomMemberDetails;
import com.cloudboot.room_reservation.member.dto.request.UpdateMemberRequest;
import com.cloudboot.room_reservation.member.dto.response.MemberResponse;
import com.cloudboot.room_reservation.member.service.MemberApiService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberApiController {

    private final MemberApiService memberApiService;

    public MemberApiController(MemberApiService memberApiService) {
        this.memberApiService = memberApiService;
    }

    @GetMapping("/user")
    public MemberResponse findMyDetails(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        return memberApiService.findById(customMemberDetails.getId());
    }

    @PutMapping("/user/update")
    public void updateMember(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                             @RequestBody @Validated UpdateMemberRequest updateMemberRequest) {
        memberApiService.updateMember(customMemberDetails.getId(), updateMemberRequest);
    }

    @GetMapping("member/list")
    public List<MemberResponse> findAll() {
        return memberApiService.findAllMembers();
    }

}
