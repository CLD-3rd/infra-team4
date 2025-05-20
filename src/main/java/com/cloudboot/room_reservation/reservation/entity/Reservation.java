package com.cloudboot.room_reservation.reservation.entity;

import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.room.entity.Room;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @ManyToOne @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne @JoinColumn(name = "room_id")
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private Status status;
    public enum Status { PENDING, APPROVED, CANCELED, REJECTED }
    public Long getReservationId() { return reservationId; }
    public Member getMember() { return member; }
    public Room getRoom() { return room; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Status getStatus() { return status; }
}