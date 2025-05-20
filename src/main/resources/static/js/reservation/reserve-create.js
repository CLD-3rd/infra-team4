const timeSlotsContainer = document.getElementById('time-slots');
const reservationDateInput = document.getElementById('reservation-date');
const roomButtons = document.querySelectorAll('.room-button');
const reserveBtn = document.getElementById('reserve-btn');
const accessToken = localStorage.getItem('access');
let selectedRoom = 'Room1';
let selectedTime = null;

const mockReservations = {
  '2025-05-19|Room1': ['11:00', '16:00'],
  '2025-05-19|Room2': ['10:00', '13:00']
};

// 1. 시간 슬롯 생성 (1시간 간격, 09~22시)
function generateTimeSlots() {
  const slots = [];
  for (let h = 9; h <= 22; h++) {
    slots.push(h.toString().padStart(2, '0') + ':00');
  }
  return slots;
}

// 1. 렌더링 함수
function renderTimeSlots(date, room) {
  timeSlotsContainer.innerHTML = '';
  selectedTime = null;
  reserveBtn.style.display = 'none'; //예약 버튼 숨기기

  const reservedTimes = mockReservations[`${date}|${room}`] || [];
  const allSlots = generateTimeSlots();

  const now = new Date();
  const bufferTime = new Date(now.getTime() + 2 * 60 * 60 * 1000); // <- 여기서 2시간 뒤 계산


  allSlots.forEach(time => {
    const slotDiv = document.createElement('div');
    slotDiv.classList.add('time-slot');
    // 예약 가능 여부
    const slotDateTime = new Date(`${date}T${time}`);
    const reserved = reservedTimes.includes(time);
    const tooSoon = slotDateTime < bufferTime;

    if (reserved || tooSoon) {
      slotDiv.classList.add('reserved');
      slotDiv.textContent = `${time}${reserved ? ' (예약됨)' : ' (불가)'}`;
    } else {
      slotDiv.classList.add('available');
      slotDiv.textContent = time;
      slotDiv.addEventListener('click', () => {
        // 선택 UI
        document.querySelectorAll('.time-slot').forEach(el => el.classList.remove('selected'));
        slotDiv.classList.add('selected');
        selectedTime = time;
        reserveBtn.style.display = 'block';
      });
    }

    timeSlotsContainer.appendChild(slotDiv);
  });
}

// 3. 날짜 & 룸 변경 이벤트
reservationDateInput.addEventListener('change', () => {
  renderTimeSlots(reservationDateInput.value, selectedRoom);
});

roomButtons.forEach(button => {
  button.addEventListener('click', () => {
    roomButtons.forEach(btn => btn.classList.remove('active'));
    button.classList.add('active');
    selectedRoom = button.dataset.room;
    renderTimeSlots(reservationDateInput.value, selectedRoom);
  });
});

//4. 예약 버튼 클릭
reserveBtn.addEventListener('click', () => {
  if (selectedTime && reservationDateInput.value && selectedRoom) {
    const roomNumberMap = {
      Room1: 101,
      Room2: 102,
      Room3: 103
    };
    const roomNumber = roomNumberMap[selectedRoom];
    //const memberId = 1; // 실제 로그인 사용자 ID로 교체

    const startTime = `${reservationDateInput.value}T${selectedTime}`;

    const endDate = new Date(`${reservationDateInput.value}T${selectedTime}`);
    endDate.setHours(endDate.getHours() + 1);

    const pad = n => n.toString().padStart(2, '0');
    const endTime = `${endDate.getFullYear()}-${pad(endDate.getMonth() + 1)}-${pad(endDate.getDate())}T${pad(endDate.getHours())}:${pad(endDate.getMinutes())}`;

    const reservationData = {
      roomNumber,
      startTime,
      endTime
    };

    console.log("예약 데이터: ", JSON.stringify(reservationData));

    fetch("/api/member/reservations", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${accessToken}`
      },
      body: JSON.stringify(reservationData)
    })
      .then(res => {
        if (!res.ok) {
          return res.text().then(text => {
            throw new Error(text || "예약 실패");
          });
        }
        return res.json();
      })
      .then(data => {
        alert(data.message || "예약이 완료되었습니다.");

        const key = `${reservationDateInput.value}|${selectedRoom}`;
        if (!mockReservations[key]) {
          mockReservations[key] = [];
        }
        mockReservations[key].push(selectedTime);

        document.querySelectorAll('.time-slot').forEach(el => el.classList.remove('selected'));
        selectedTime = null;
        reserveBtn.style.display = 'none';
        renderTimeSlots(reservationDateInput.value, selectedRoom);
      })
      .catch(err => {
        console.error("예약 요청 중 오류 발생:", err);
        alert("예약 요청 실패: " + err.message);
      });
  }
});

// 5. 초기 렌더링
const today = new Date().toISOString().slice(0, 10);
reservationDateInput.value = today;
renderTimeSlots(today, selectedRoom);