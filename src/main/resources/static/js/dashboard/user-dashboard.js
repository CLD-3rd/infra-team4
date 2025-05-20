// 토큰 확인
const accessToken = localStorage.getItem('access');
console.log(accessToken);

// 로고 클릭 이벤트
document.querySelector('.psr-logo').addEventListener('click', () => {
    window.location.href = '/html/dashboard/user-dashboard.html';
});

// 프로필 메뉴 관련 요소
const profileWrapper = document.getElementById('profileWrapper');
const profileMenu = document.getElementById('profileMenu');

// 프로필 메뉴 토글
profileWrapper.addEventListener('click', (e) => {
    e.stopPropagation();
    profileMenu.classList.toggle('active');
});

// 메뉴 밖 클릭 시 닫기
window.addEventListener('click', () => {
    profileMenu.classList.remove('active');
});

// 프로필 설정 버튼 클릭
document.getElementById('profileSettingsBtn').addEventListener('click', () => {
    window.location.href = '/html/dashboard/my-profile.html';
    profileMenu.classList.remove('active');
});

// 공지사항 보기
document.getElementById("btnNotice").addEventListener("click", () => {
  window.location.href = "/html/notice/notice.html";
});

// 스터디룸 예약하기
document.getElementById("btnReserve").addEventListener("click", () => {
  window.location.href = "/html/reservation/reserve.html";
});


// 로그아웃 처리
document.getElementById('logoutBtn').addEventListener('click', async () => {
    try {
        const response = await fetch('/api/logout', {
            method: 'POST',
            credentials: 'include'
        });

        if (response.ok) {
            localStorage.removeItem('access');
            window.location.href = '/html/auth/index.html';
        } else {
            alert('로그아웃에 실패했습니다.');
        }
    } catch (err) {
        console.error('로그아웃 요청 중 오류 발생:', err);
        alert('서버와의 연결에 실패했습니다.');
    } finally {
        profileMenu.classList.remove('active');
    }
});