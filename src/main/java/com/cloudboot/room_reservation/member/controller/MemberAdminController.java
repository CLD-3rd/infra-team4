package com.cloudboot.room_reservation.member.controller;

import com.cloudboot.room_reservation.member.dto.request.UpdateMemberRoleRequest;
import com.cloudboot.room_reservation.member.dto.response.MemberAdminResponse;
import com.cloudboot.room_reservation.member.service.MemberAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberAdminController {

    private final MemberAdminService memberAdminService;

    public MemberAdminController(MemberAdminService memberAdminService) {
        this.memberAdminService = memberAdminService;
    }

    // 회원 전체 조회
    @GetMapping("/admin/members")
    public ResponseEntity<List<MemberAdminResponse>> findAllMembers() {
        return ResponseEntity.status(HttpStatus.OK).body(memberAdminService.findAllMembers());

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
}
