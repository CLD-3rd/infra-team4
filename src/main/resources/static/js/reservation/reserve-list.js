const reservationListContainer = document.getElementById("reservation-list");
// const memberId = 1; // TODO: 실제 로그인된 사용자 ID로 교체

// 날짜 변환
function formatDateTime(dateStr) {
  const date = new Date(dateStr);
  if (isNaN(date)) return dateStr;
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes().toString().padStart(2, '0');
  return `${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분`;
}

// 상태를 한글로
function translateStatus(status) {
  switch (status) {
    case 'APPROVED': return '승인됨';
    case 'CANCELED': return '취소됨';
    case 'PENDING': return '대기 중';
    case 'REJECTED': return '거절됨';
    default: return status;
  }
}

// 사용자 예약 목록 불러오기
function fetchReservations() {
  fetch(`/api/reservations/member/${memberId}`)
    .then(res => res.json())
    .then(data => {
      reservationListContainer.innerHTML = ""; // 초기화

      if (data.length === 0) {
        reservationListContainer.innerHTML = "<p> 예약 내역이 없습니다.</p>";
        return;
      }

      data.forEach(reservation => {
        const card = document.createElement("div");
        card.classList.add("reservation-card");

        const translatedStatus = translateStatus(reservation.status);

        const cancelButtonHtml = translatedStatus === "취소됨"
          ? `<button class="cancel-btn" disabled>취소 완료</button>`
          : `<button class="cancel-btn" onclick="cancelReservation(${reservation.reservationId})">예약 취소</button>`;

        card.innerHTML = `
          <div class="row"><span class="label">예약 ID:</span> ${reservation.reservationId}</div>
          <div class="row"><span class="label">방 번호:</span> ${reservation.roomId}</div>
          <div class="row"><span class="label">시작 시간:</span> ${formatDateTime(reservation.startTime)}</div>
          <div class="row"><span class="label">종료 시간:</span> ${formatDateTime(reservation.endTime)}</div>
          <div class="row"><span class="label">상태:</span> ${translatedStatus}</div>
          <div class="row"><span class="label">예약 생성:</span> ${formatDateTime(reservation.createdAt)}</div>
          ${cancelButtonHtml}
        `;

        reservationListContainer.appendChild(card);
      });
    })
    .catch(err => {
      console.error("예약 목록 조회 실패", err);
      reservationListContainer.innerHTML = "<p>예약 목록을 불러오는 데 실패했습니다.</p>";
    });
}

// 예약 취소 요청
function cancelReservation(reservationId) {
  if (!confirm("이 예약을 취소하시겠습니까?")) return;

  fetch(`/api/reservations/${reservationId}/cancel`, {
    method: "DELETE"
  })
    .then(res => {
      if (res.ok) {
        alert("예약이 취소되었습니다.");
        fetchReservations(); // 목록 갱신
      } else {
        alert("예약 취소에 실패했습니다.");
      }
    })
    .catch(err => {
      console.error("예약 취소 중 오류 발생", err);
      alert("오류가 발생했습니다.");
    });
}

// 초기 실행
fetchReservations();
