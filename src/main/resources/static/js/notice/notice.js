const noticeList = document.getElementById("notice-list");

fetch("/api/notices")
  .then(res => res.json())
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
  });
