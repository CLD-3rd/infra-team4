const listContainer = document.getElementById("admin-notice-list");
const titleInput = document.getElementById("title");
const contentInput = document.getElementById("content");
const submitBtn = document.getElementById("submit-btn");
const accessToken = localStorage.getItem("access");

let editingNoticeId = null; // 수정 중인 공지 ID

submitBtn.addEventListener("click", () => {
  const title = titleInput.value.trim();
  const content = contentInput.value.trim();
  if (!title || !content) return alert("제목과 내용을 모두 입력하세요.");

  const headers = {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${accessToken}`
  };

  // 수정이면 PUT
  if (editingNoticeId !== null) {
    fetch(`/api/admin/notices/${editingNoticeId}`, {
      method: "PUT",
      headers,
      body: JSON.stringify({ title, content })
    })
      .then(res => {
        if (!res.ok) throw new Error("수정 실패");
        return res.json();
      })
      .then(() => {
        alert("수정 완료");
        resetForm();
        loadNotices();
      })
      .catch(err => {
        console.error(err);
        alert("수정 중 오류 발생");
      });
  } else {
    // 등록이면 POST
    fetch("/api/admin/notices", {
      method: "POST",
      headers,
      body: JSON.stringify({ title, content })
    })
      .then(res => {
        if (!res.ok) throw new Error("등록 실패");
        return res.json();
      })
      .then(() => {
        alert("등록 완료");
        resetForm();
        loadNotices();
      })
      .catch(err => {
        console.error(err);
        alert("등록 중 오류 발생");
      });
  }
});

function loadNotices() {
  fetch("/api/notices", {
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  })
    .then(res => res.json())
    .then(data => {
      listContainer.innerHTML = "";
      data.forEach(n => {
        const div = document.createElement("div");
        div.className = "notice-card";
        div.innerHTML = `
          <div><strong>${n.title}</strong></div>
          <div>${n.content}</div>
          <small>${new Date(n.createdAt).toLocaleDateString()}</small>
          <div style="margin-top: 10px;">
            <button class="edit-btn" onclick="editNotice(${n.id}, '${n.title}', \`${n.content}\`)">수정</button>
            <button class="delete-btn" onclick="deleteNotice(${n.id})">삭제</button>
          </div>
        `;
        listContainer.appendChild(div);
      });
    });
}

function deleteNotice(id) {
  if (!confirm("삭제하시겠습니까?")) return;
  fetch(`/api/admin/notices/${id}`, {
    method: "DELETE",
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  })
    .then(res => {
      if (!res.ok) throw new Error("삭제 실패");
      return res.json();
    })
    .then(() => {
      alert("삭제 완료");
      loadNotices();
    })
    .catch(err => {
      console.error(err);
      alert("삭제 중 오류 발생");
    });
}

function editNotice(id, title, content) {
  titleInput.value = title;
  contentInput.value = content;
  editingNoticeId = id;
  submitBtn.textContent = "공지 수정";
}

function resetForm() {
  editingNoticeId = null;
  titleInput.value = "";
  contentInput.value = "";
  submitBtn.textContent = "공지 등록";
}

// 초기 로딩
loadNotices();
