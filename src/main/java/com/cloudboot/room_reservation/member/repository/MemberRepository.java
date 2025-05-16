package com.cloudboot.room_reservation.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudboot.room_reservation.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

}
