// profile-menu.js
(function initProfileMenu() {
  const profileWrapper = document.getElementById("profileWrapper");
  const profileMenu = document.getElementById("profileMenu");

  if (profileWrapper && profileMenu) {
    profileWrapper.addEventListener("click", (e) => {
      e.stopPropagation();
      profileMenu.classList.toggle("active");
    });

    window.addEventListener("click", (e) => {
      if (!profileWrapper.contains(e.target)) {
        profileMenu.classList.remove("active");
      }
    });
  }

  const profileSettingsBtn = document.getElementById("profileSettingsBtn");
  if (profileSettingsBtn) {
    profileSettingsBtn.addEventListener("click", () => {
      window.location.href = "/html/dashboard/my-profile.html";
      profileMenu.classList.remove("active");
    });
  }

  const myReservationsBtn = document.getElementById("myReservationsBtn");
  if (myReservationsBtn) {
    myReservationsBtn.addEventListener("click", () => {
      window.location.href = "/html/reservation/reserve-list.html";
      profileMenu.classList.remove("active");
    });
  }

  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", async () => {
      try {
        const response = await fetch("/api/logout", {
          method: "POST",
          credentials: "include"
        });

        if (response.ok) {
          localStorage.removeItem("access");
          window.location.href = "/html/auth/index.html";
        } else {
          alert("로그아웃에 실패했습니다.");
        }
      } catch (err) {
        console.error("로그아웃 오류:", err);
        alert("서버와 연결할 수 없습니다.");
      } finally {
        profileMenu.classList.remove("active");
      }
    });
  }
})();
