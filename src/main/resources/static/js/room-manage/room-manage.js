const baseUrl = window.location.origin;
const authHeader = 'Basic ' + btoa('admin:admin');

// 방 목록 불러와 그리드 렌더링
async function loadRooms() {
  const res = await fetch(`${baseUrl}/api/admin/rooms`, {
    headers: { Authorization: authHeader }
  });
  const rooms = await res.json();
  const grid = document.getElementById('room-grid');
  grid.innerHTML = '';
  if (!rooms.length) {
    grid.innerHTML = '<p>등록된 룸이 없습니다.</p>';
    return;
  }
  rooms.forEach(r => {
    const card = document.createElement('div');
    card.className = 'room-card';
    card.innerHTML = `
      <h3>Room ${r.roomNumber}</h3>
      <div class="actions">
        <button onclick="showUpdatePrompt(${r.roomId}, '${r.roomNumber}')">수정</button>
        <button onclick="deleteRoom(${r.roomId})">삭제</button>
        <button onclick="showReservations(${r.roomId}, '${r.roomNumber}')">예약조회</button>
      </div>`;
    grid.appendChild(card);
  });
}

// 룸 생성
async function createRoom() {
  const num = document.getElementById('new-room-number').value.trim();
  if (!num) return alert('룸 번호를 입력하세요.');
  await fetch(`${baseUrl}/api/admin/rooms`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: authHeader
    },
    body: JSON.stringify({ roomNumber: num })
  });
  document.getElementById('new-room-number').value = '';
  loadRooms();
}

// 룸 수정
async function updateRoom(id, newNum) {
  await fetch(`${baseUrl}/api/admin/rooms/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: authHeader
    },
    body: JSON.stringify({ roomNumber: newNum })
  });
  loadRooms();
}

// 수정 프롬프트
function showUpdatePrompt(id, oldNum) {
  const newNum = prompt('새 룸 번호를 입력하세요', oldNum);
  if (newNum) updateRoom(id, newNum);
}

// 룸 삭제
async function deleteRoom(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return;
  await fetch(`${baseUrl}/api/admin/rooms/${id}`, {
    method: 'DELETE',
    headers: { Authorization: authHeader }
  });
  loadRooms();
}

// 예약 조회 모달
async function showReservations(roomId, roomNumber) {
  const res = await fetch(`${baseUrl}/api/admin/reservations?roomId=${roomId}`, {
    headers: { Authorization: authHeader }
  });
  const list = await res.json();
  document.getElementById('modal-room-number').textContent = roomNumber;
  const ul = document.getElementById('reservation-list');
  ul.innerHTML = '';
  if (!list.length) {
    ul.innerHTML = '<li>예약 내역이 없습니다.</li>';
  } else {
    list.forEach(r => {
      const li = document.createElement('li');
      li.textContent = `[${r.status}] ${r.startTime} ~ ${r.endTime} (Member: ${r.memberId || r.member.memberId})`;
      ul.appendChild(li);
    });
  }
  document.getElementById('reservation-modal').classList.remove('hidden');
}

// 모달 닫기
document.getElementById('btn-close-modal').addEventListener('click', () => {
  document.getElementById('reservation-modal').classList.add('hidden');
});

// 생성 버튼
document.getElementById('btn-create').addEventListener('click', createRoom);

// 초기 로드
window.addEventListener('DOMContentLoaded', loadRooms);
