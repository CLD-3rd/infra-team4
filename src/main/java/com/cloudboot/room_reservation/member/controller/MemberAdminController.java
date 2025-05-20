package com.cloudboot.room_reservation.member.controller;

import com.cloudboot.room_reservation.member.dto.CustomMemberDetails;
import com.cloudboot.room_reservation.member.dto.request.UpdateMemberRequest;
import com.cloudboot.room_reservation.member.dto.request.UpdateMemberRoleRequest;
import com.cloudboot.room_reservation.member.dto.response.MemberAdminResponse;
import com.cloudboot.room_reservation.member.dto.response.MemberResponse;
import com.cloudboot.room_reservation.member.service.MemberAdminService;
import com.cloudboot.room_reservation.util.global.PagedApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MemberAdminController {

    private final MemberAdminService memberAdminService;

    public MemberAdminController(MemberAdminService memberAdminService) {
        this.memberAdminService = memberAdminService;
    }

    // 회원 전체 조회
    @GetMapping("/admin/members")
    public ResponseEntity<PagedApiResponse<MemberAdminResponse>> findAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MemberAdminResponse> result = memberAdminService.findAllMembers(pageable);

        return ResponseEntity.status(HttpStatus.OK).body((PagedApiResponse.of(result)));
    }

    // 특정 회원 조회
    @GetMapping("/admin/members/{memberId}")
    public ResponseEntity<MemberAdminResponse> findOneMember(@PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberAdminService.findOne(memberId));
    }


    @PutMapping("/admin/members/{memberId}")
    public ResponseEntity<MemberAdminResponse> editMemberRole(@PathVariable(name = "memberId") Long memberId,
                                                              @RequestBody UpdateMemberRoleRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(memberAdminService.changeMemberRole(memberId, request));
    }

    @GetMapping("/admin/member")
    public ResponseEntity<MemberResponse> findMyDetails(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(memberAdminService.findById(customMemberDetails.getId()));
    }

    @PutMapping("/admin/member")
    public ResponseEntity<Map<String, String>> updateMember(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                            @RequestBody @Validated UpdateMemberRequest updateMemberRequest) {
        memberAdminService.updateMember(customMemberDetails.getId(), updateMemberRequest);
        Map<String, String> body = Map.of("message", "비밀번호 변경이 완료되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }
}
