package com.cloudboot.room_reservation.reservation.entity;

import com.cloudboot.room_reservation.member.entity.Member;
import com.cloudboot.room_reservation.reservation.enumerate.ReservationStatus;
import com.cloudboot.room_reservation.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 편의 메서드
    public String getUsername() {
        return this.member.getUsername();
    }

    public String getRoomNumber() {
        return this.room.getRoomNumber();
    }

	public Reservation(long reservationId, Room room, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus status,
			Member member) {
		this.reservationId = reservationId;
		this.room = room;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.member = member;
		
	}
}
