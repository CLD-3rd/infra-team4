const adminListContainer = document.getElementById("admin-reservation-list");
const accessToken = localStorage.getItem("access");

function formatDateTime(dateStr) {
  const date = new Date(dateStr);
  if (isNaN(date)) return dateStr;
  return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일 ${date.getHours()}시 ${date.getMinutes()}분`;
}

function translateStatus(status) {
  switch (status) {
    case 'APPROVED': return '승인됨';
    case 'CANCELED': return '취소됨';
    case 'PENDING': return '대기 중';
    case 'REJECTED': return '거절됨';
    default: return status;
  }
}

function fetchAllReservations() {
  fetch("/api/admin/reservations", {
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  })
    .then(res => res.json())
    .then(data => {
      adminListContainer.innerHTML = "";

      if (data.length === 0) {
        adminListContainer.innerHTML = "<p>예약 내역이 없습니다.</p>";
        return;
      }

      data.forEach(reservation => {
        const card = document.createElement("div");
        card.classList.add("reservation-card");

        const status = translateStatus(reservation.status);

        let actionButtons = "";
        if (reservation.status === "PENDING") {
          actionButtons = `
            <button class="approve-btn" onclick="approve(${reservation.reservationId})">승인</button>
            <button class="reject-btn" onclick="reject(${reservation.reservationId})">거절</button>
          `;
        }

        card.innerHTML = `
          <div><strong>예약 ID:</strong> ${reservation.reservationId}</div>
          <div><strong>회원 ID:</strong> ${reservation.memberId}</div>
          <div><strong>방 번호:</strong> ${reservation.roomId}</div>
          <div><strong>시작:</strong> ${formatDateTime(reservation.startTime)}</div>
          <div><strong>종료:</strong> ${formatDateTime(reservation.endTime)}</div>
          <div><strong>상태:</strong> ${status}</div>
          <div><strong>생성:</strong> ${formatDateTime(reservation.createdAt)}</div>
          <div class="actions">${actionButtons}</div>
        `;

        adminListContainer.appendChild(card);
      });
    });
}

function approve(reservationId) {
  fetch(`/api/admin/reservations/${reservationId}/approve`, {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  })
    .then(() => {
      alert("예약이 승인되었습니다.");
      fetchAllReservations();
    });
}

function reject(reservationId) {
  fetch(`/api/admin/reservations/${reservationId}/reject`, {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  })
    .then(() => {
      alert("예약이 거절되었습니다.");
      fetchAllReservations();
    });
}

fetchAllReservations();
