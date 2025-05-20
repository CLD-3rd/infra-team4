const listContainer = document.getElementById("admin-notice-list");
const titleInput = document.getElementById("title");
const contentInput = document.getElementById("content");
const submitBtn = document.getElementById("submit-btn");

const adminId = 1; // 로그인한 관리자 ID
let editingNoticeId = null; // 수정 중인 공지 ID

submitBtn.addEventListener("click", () => {
  const title = titleInput.value.trim();
  const content = contentInput.value.trim();
  if (!title || !content) return alert("제목과 내용을 모두 입력하세요.");

  // 수정 중이면 PUT, 아니면 POST
  if (editingNoticeId !== null) {
    fetch(`/api/admin/notices/${editingNoticeId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, content })
    })
      .then(res => res.json())
      .then(() => {
        alert("수정 완료");
        resetForm();
        loadNotices();
      });
  } else {
    fetch(`/api/admin/notices?adminId=${adminId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, content })
    })
      .then(res => res.json())
      .then(() => {
        alert("등록 완료");
        resetForm();
        loadNotices();
      });
  }
});

function loadNotices() {
  fetch("/api/notices")
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
  fetch(`/api/admin/notices/${id}`, { method: "DELETE" })
    .then(res => res.json())
    .then(() => {
      alert("삭제 완료");
      loadNotices();
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

loadNotices();

