package roomReservation.domain.infra.reservation.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequestDto {
    private Long roomId;
    private Long memberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
