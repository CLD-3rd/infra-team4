// user-dashboard.js
const accessToken = localStorage.getItem('access');

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("btnReserve").addEventListener("click", () => {
    window.location.href = "/html/reservation/reserve.html";
  });

  document.getElementById("btnNotice").addEventListener("click", () => {
    window.location.href = "/html/notice/notice.html";
  });

  document.querySelector('.psr-logo').addEventListener('click', () => {
    window.location.href = '/html/dashboard/user-dashboard.html';
  });
});

