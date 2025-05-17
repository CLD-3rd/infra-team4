package com.cloudboot.room_reservation.member.controller;

import com.cloudboot.room_reservation.member.dto.response.MemberAdminResponse;
import com.cloudboot.room_reservation.member.service.MemberAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberAdminController {

    private final MemberAdminService memberAdminService;

    public MemberAdminController(MemberAdminService memberAdminService) {
        this.memberAdminService = memberAdminService;
    }

    @GetMapping("/admin/members")
    public List<MemberAdminResponse> findAllMembers() {
        return memberAdminService.findAllMembers();
    }
}
