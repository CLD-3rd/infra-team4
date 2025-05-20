const accessToken = localStorage.getItem("access");
const noticeList = document.getElementById("notice-list");

fetch("/api/notices", {
  method: "GET",
  headers: {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${accessToken}`  // 토큰 추가
  }
})
  .then(res => {
    if (!res.ok) throw new Error("공지사항을 불러오는 데 실패했습니다.");
    return res.json();
  })
  .then(data => {
    noticeList.innerHTML = "";
    data.forEach(n => {
      const div = document.createElement("div");
      div.className = "notice-card";
      div.innerHTML = `
        <div><strong>${n.title}</strong></div>
        <div>${n.content}</div>
        <small>${new Date(n.createdAt).toLocaleDateString()}</small>
      `;
      noticeList.appendChild(div);
    });
  })
  .catch(err => {
    console.error("공지사항 불러오기 오류:", err);
    noticeList.innerHTML = "<p>공지사항을 불러오지 못했습니다.</p>";
  });
