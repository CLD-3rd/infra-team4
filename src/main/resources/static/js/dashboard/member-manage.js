const accessToken = localStorage.getItem('access');
const profileWrapper = document.getElementById('profileWrapper');
const profileMenu = document.getElementById('profileMenu');
const modal = document.getElementById('roleModal');
const roleSelect = document.getElementById('roleSelect');
let selectedMemberId = null;
let currentPage = 0;
let totalPages = 0;

profileWrapper.addEventListener('click', () => {
    profileMenu.classList.toggle('active');
});

window.addEventListener('click', (e) => {
    if (!profileWrapper.contains(e.target)) {
        profileMenu.classList.remove('active');
    }
    if (!modal.contains(e.target) && !e.target.closest('tr')) {
        modal.classList.remove('active');
    }
});

document.getElementById('profileSettingsBtn').addEventListener('click', () => {
    window.location.href = '/html/dashboard/profile_detail.html';
    profileMenu.classList.remove('active');
});

document.getElementById('logoutBtn').addEventListener('click', async () => {
    try {
        const response = await fetch('/api/logout', {
            method: 'POST',
            credentials: 'include'
        });

        if (response.ok) {
            window.location.href = '/html/auth/index.html';
        } else {
            showNotification('로그아웃에 실패했습니다.', false);
        }
    } catch (err) {
        console.error('로그아웃 오류:', err);
        showNotification('서버와의 연결 실패', false);
    } finally {
        profileMenu.classList.remove('active');
    }
});

function showNotification(message, isSuccess = true) {
    const bar = document.getElementById('notificationBar');
    bar.textContent = message;
    bar.style.backgroundColor = isSuccess ? '#68f056' : '#e53935';
    bar.style.display = 'block';

    setTimeout(() => {
        bar.style.display = 'none';
    }, 3000);
}

async function fetchMembers(page = 0) {
    try {
        document.getElementById('loadingText').style.display = 'block';
        document.getElementById('memberTable').style.display = 'none';

        const response = await fetch(`/admin/members?page=${page}&size=5`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            }
        });

        if (!response.ok) {
            throw new Error('서버 응답 오류');
        }

        const data = await response.json();
        const members = data.data;
        currentPage = data.currentPage;
        totalPages = data.totalPages;

        const tbody = document.getElementById('memberBody');
        tbody.innerHTML = '';

        members.forEach(member => {
            const roleName = member.role.replace('ROLE_', '');
            const roleClass = roleName === 'ADMIN' ? 'role-admin' : 'role-user';

            const tr = document.createElement('tr');
            tr.innerHTML = `
          <td>${member.id}</td>
          <td>${member.username}</td>
          <td><span class="role-badge ${roleClass}">${roleName}</span></td>
        `;
            tr.addEventListener('click', () => {
                selectedMemberId = member.id;
                roleSelect.value = member.role;
                modal.classList.add('active');
            });
            tbody.appendChild(tr);
        });

        updatePagination();

        document.getElementById('loadingText').style.display = 'none';
        document.getElementById('memberTable').style.display = 'table';
    } catch (error) {
        document.getElementById('loadingText').innerText = '회원 정보를 불러오지 못했습니다.';
        console.error('회원 목록 조회 실패:', error);
        showNotification('회원 목록 조회 중 오류 발생', false);
    }
}

// ✅ 페이지네이션 UI 업데이트 (버튼 최대 10개까지)
function updatePagination() {
    const container = document.getElementById('paginationContainer');
    if (!container) return;

    container.innerHTML = '';

    // 이전 버튼
    if (currentPage > 0) {
        const prevBtn = document.createElement('button');
        prevBtn.className = 'page-btn';
        prevBtn.innerHTML = '&laquo;';
        prevBtn.addEventListener('click', () => fetchMembers(currentPage - 1));
        container.appendChild(prevBtn);
    }

    const maxButtons = 10;
    const half = Math.floor(maxButtons / 2);
    let startPage = Math.max(0, currentPage - half);
    let endPage = startPage + maxButtons - 1;

    if (endPage >= totalPages) {
        endPage = totalPages - 1;
        startPage = Math.max(0, endPage - maxButtons + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        const pageBtn = document.createElement('button');
        pageBtn.className = `page-btn ${i === currentPage ? 'active' : ''}`;
        pageBtn.textContent = i + 1;
        pageBtn.addEventListener('click', () => fetchMembers(i));
        container.appendChild(pageBtn);
    }

    // 다음 버튼
    if (currentPage < totalPages - 1) {
        const nextBtn = document.createElement('button');
        nextBtn.className = 'page-btn';
        nextBtn.innerHTML = '&raquo;';
        nextBtn.addEventListener('click', () => fetchMembers(currentPage + 1));
        container.appendChild(nextBtn);
    }
}

document.getElementById('saveRoleBtn').addEventListener('click', async () => {
    const selectedRole = roleSelect.value;

    try {
        const response = await fetch(`http://localhost:8080/admin/members/${selectedMemberId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify({ role: selectedRole })
        });

        if (response.ok) {
            const rows = document.querySelectorAll('#memberBody tr');
            rows.forEach(row => {
                const idCell = row.cells[0];
                if (idCell.textContent === String(selectedMemberId)) {
                    const roleCell = row.cells[2];
                    const roleName = selectedRole.replace('ROLE_', '');
                    const roleClass = roleName === 'ADMIN' ? 'role-admin' : 'role-user';
                    roleCell.innerHTML = `<span class="role-badge ${roleClass}">${roleName}</span>`;
                }
            });

            modal.classList.remove('active');
            showNotification('권한이 성공적으로 변경되었습니다.');
        } else {
            showNotification('권한 변경에 실패했습니다.', false);
        }
    } catch (err) {
        console.error('권한 변경 오류:', err);
        showNotification('서버 오류가 발생했습니다.', false);
    }
});

fetchMembers(0);