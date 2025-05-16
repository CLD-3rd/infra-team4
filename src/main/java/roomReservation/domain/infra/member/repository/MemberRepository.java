package roomReservation.domain.infra.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomReservation.domain.infra.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
