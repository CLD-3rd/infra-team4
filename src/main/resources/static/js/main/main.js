function goTo(path) {
  window.location.href = path;
}

// 프로필 드롭다운 토글
const profileWrapper = document.getElementById('profileWrapper');
const profileMenu = document.getElementById('profileMenu');

profileWrapper.addEventListener('click', () => {
  profileMenu.classList.toggle('active');
});

window.addEventListener('click', (e) => {
  if (!profileWrapper.contains(e.target)) {
    profileMenu.classList.remove('active');
  }
});

document.getElementById('logoutBtn').addEventListener('click', () => {
  alert('로그아웃 처리 필요');
  profileMenu.classList.remove('active');
});

document.getElementById('profileSettingsBtn').addEventListener('click', () => {
  alert('프로필 설정 페이지로 이동 (구현 필요)');
  profileMenu.classList.remove('active');
});
