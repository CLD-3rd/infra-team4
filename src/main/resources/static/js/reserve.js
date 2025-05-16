const profileWrapper = document.getElementById('profileWrapper');
const profileMenu = document.getElementById('profileMenu');
profileWrapper.addEventListener('click', () => profileMenu.classList.toggle('active'));
window.addEventListener('click', e => {
  if (!profileWrapper.contains(e.target)) profileMenu.classList.remove('active');
});
document.getElementById('logoutBtn').addEventListener('click', () => {
  alert('로그아웃 처리 필요');
});
document.getElementById('profileSettingsBtn').addEventListener('click', () => {
  alert('프로필 설정 페이지로 이동');
});

const timeSlotsContainer = document.getElementById('time-slots');
const reservationDateInput = document.getElementById('reservation-date');
const roomButtons = document.getElementById('room-buttons').querySelectorAll('.room-button');

const mockReservations = {
  '2025-05-15|Room1': ['10:00', '14:00', '16:00']
};

function generateTimeSlots() {
  const slots = [];
  for (let h = 9; h <= 17; h++) {
    slots.push(`${h.toString().padStart(2, '0')}:00`);
  }
  return slots;
}

let selectedRoom = 'Room1';

function renderTimeSlots(date, room) {
  timeSlotsContainer.innerHTML = '';
  const reserved = mockReservations[`${date}|${room}`] || [];
  generateTimeSlots().forEach(time => {
    const div = document.createElement('div');
    div.className = reserved.includes(time) ? 'time-slot reserved' : 'time-slot available';
    div.textContent = reserved.includes(time) ? `${time} (예약됨)` : time;
    if (!reserved.includes(time)) {
      div.addEventListener('click', () => alert(`${date} ${room} ${time} 예약 신청됨`));
    }
    timeSlotsContainer.appendChild(div);
  });
}

const today = new Date().toISOString().slice(0,10);
reservationDateInput.value = today;

roomButtons.forEach(btn => {
  btn.addEventListener('click', () => {
    roomButtons.forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    selectedRoom = btn.dataset.room;
    renderTimeSlots(reservationDateInput.value, selectedRoom);
  });
});
reservationDateInput.addEventListener('change', e => renderTimeSlots(e.target.value, selectedRoom));
renderTimeSlots(today, selectedRoom);
