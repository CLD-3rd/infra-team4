const accessToken = localStorage.getItem('access');

window.addEventListener('DOMContentLoaded', async () => {
    try {
        const res = await fetch('/admin/member', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            },
            credentials: 'include',
        });

        if (res.ok) {
            const data = await res.json();
            document.getElementById('username').textContent = data.username;
            document.getElementById('role').textContent = data.role;
        } else {
            alert('사용자 정보를 불러올 수 없습니다.');
        }
    } catch (err) {
        console.error('오류:', err);
        alert('서버와 연결할 수 없습니다.');
    }
});
