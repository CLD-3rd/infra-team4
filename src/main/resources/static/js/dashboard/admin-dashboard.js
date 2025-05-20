// // 프로필 메뉴 토글
// const profileWrapper = document.getElementById('profileWrapper');
// const profileMenu = document.getElementById('profileMenu');
//
// profileWrapper.addEventListener('click', () => {
//     profileMenu.classList.toggle('active');
// });
//
// // 메뉴 밖 클릭 시 닫기
// window.addEventListener('click', (e) => {
//     if (!profileWrapper.contains(e.target)) {
//         profileMenu.classList.remove('active');
//     }
// });
//
// // 로그아웃 처리
// document.getElementById('logoutBtn').addEventListener('click', async () => {
//     try {
//         const response = await fetch('api/logout', {
//             method: 'POST',
//             credentials: 'include'
//         });
//
//         if (response.ok) {
//             window.location.href = '/html/auth/index.html';
//         } else {
//             alert('로그아웃에 실패했습니다.');
//         }
//     } catch (err) {
//         console.error('로그아웃 요청 중 오류:', err);
//         alert('서버 연결 실패');
//     } finally {
//         profileMenu.classList.remove('active');
//     }
// });
//
// // 프로필 설정 이동
// document.getElementById('profileSettingsBtn').addEventListener('click', () => {
//     window.location.href = '/html/dashboard/my-profile.html';
//     profileMenu.classList.remove('active');
// });
