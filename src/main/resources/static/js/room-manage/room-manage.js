const baseUrl = window.location.origin;
const authHeader = 'Basic ' + btoa('admin@naver.com:admin');

async function loadRooms() {
  const res = await fetch(`${baseUrl}/admin/rooms`, {
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

async function createRoom() {
  const num = document.getElementById('roomNumberInput').value.trim();
  if (!num) return alert('룸 번호를 입력하세요.');
  await fetch(`${baseUrl}/admin/rooms`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: authHeader
    },
    body: JSON.stringify({ roomNumber: num })
  });
  document.getElementById('roomNumberInput').value = '';
  loadRooms();
}

function showUpdatePrompt(id, oldNum) {
  const newNum = prompt('새 룸 번호를 입력하세요', oldNum);
  if (newNum) updateRoom(id, newNum);
}

async function updateRoom(id, newNum) {
  await fetch(`${baseUrl}/admin/rooms/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: authHeader
    },
    body: JSON.stringify({ roomNumber: newNum })
  });
  loadRooms();
}

async function deleteRoom(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return;
  await fetch(`${baseUrl}/admin/rooms/${id}`, {
    method: 'DELETE',
    headers: { Authorization: authHeader }
  });
  loadRooms();
}

async function showReservations(roomId, roomNumber) {
  const res = await fetch(`${baseUrl}/admin/rooms/${roomId}/reservations`, {
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
      li.textContent = `[${r.status}] ${r.startTime} ~ ${r.endTime} (Member: ${r.member.memberId})`;
      ul.appendChild(li);
    });
  }
  document.getElementById('reservation-modal').classList.add('active');
}

document.getElementById('btn-create').addEventListener('click', createRoom);
document.getElementById('btn-close-modal').addEventListener('click', () => {
  document.getElementById('reservation-modal').classList.remove('active');
});
window.addEventListener('DOMContentLoaded', loadRooms);
