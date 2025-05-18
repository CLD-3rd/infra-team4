## 프로젝트 개요

- **관리자 기능**  
  - 룸(Room) 등록(Create) / 조회(Read) / 수정(Update) / 삭제(Delete)  
  - 룸별 예약(Reservation) 상태 조회(Read)  
- **보안**  
  - `/admin/**` 경로는 Basic Auth (`admin` / `admin`) 로 보호  
- **기술 스택**  
  - Spring Boot 3.4.5, Java 17  
  - Spring Data JPA (Hibernate)  
  - Spring Security (Basic Auth)  
  - MySQL 8.0  
  - Maven Wrapper  
  ## DB 스키마 & 샘플 데이터

```sql
-- 1) 룸 테이블
CREATE TABLE room (
  room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  room_number VARCHAR(100) NOT NULL UNIQUE
);

-- 2) 예약 테이블
CREATE TABLE reservation (
  reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  member_id BIGINT NOT NULL,
  room_id BIGINT NOT NULL,
  start_time DATETIME,
  end_time DATETIME,
  status VARCHAR(20),
  FOREIGN KEY (room_id) REFERENCES room(room_id),
  FOREIGN KEY (member_id) REFERENCES member(member_id)
);

-- 3) 테스트용 회원 삽입
INSERT INTO member (username, password, role, created_at, updated_at)
VALUES ('testuser','testpass','ROLE_USER',NOW(),NOW());

-- 4) 테스트용 룸 삽입
INSERT INTO room (room_number) VALUES ('T100');

-- 5) 테스트용 예약 삽입
INSERT INTO reservation (member_id, room_id, start_time, end_time, status)
VALUES (LAST_INSERT_ID(), 1, '2025-05-19 09:00:00', '2025-05-19 10:00:00', 'PENDING');


Environment 설정

baseUrl = http://localhost:8080

user = admin

pw = admin

컬렉션: Room Admin APIs

Authorization (Basic Auth) → {{user}} / {{pw}}

Request 목록

| Name              | Method | URL                                       | Body (raw JSON)              |
| ----------------- | ------ | ----------------------------------------- | ---------------------------- |
| List Rooms        | GET    | `{{baseUrl}}/admin/rooms`                 | –                            |
| Create Room       | POST   | `{{baseUrl}}/admin/rooms`                 | `{ "roomNumber": "B202" }`   |
| Update Room       | PUT    | `{{baseUrl}}/admin/rooms/1`               | `{ "roomNumber": "B202-1" }` |
| Delete Room       | DELETE | `{{baseUrl}}/admin/rooms/1`               | –                            |
| List Reservations | GET    | `{{baseUrl}}/admin/reservations?roomId=1` | –                            |
