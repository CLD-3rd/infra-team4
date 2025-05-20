const accessToken = localStorage.getItem('access');
const form = document.getElementById('passwordForm');
const messageBox = document.getElementById('messageBox');
const container = document.querySelector('.container');


form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData(form);
    const data = {
        originPassword: formData.get('originPassword'),
        newPassword: formData.get('newPassword'),
        newPasswordConfirm: formData.get('newPasswordConfirm')
    };

    try {
        const res = await fetch('/api/member', {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'access': accessToken
            },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            messageBox.textContent = '✅ 비밀번호가 성공적으로 변경되었습니다.';
            messageBox.className = 'message success';
            setTimeout(() => {
                window.location.href = '/html/dashboard/user-dashboard.html';
            }, 2000);
        } else {
            const errMsg = await res.text();
            messageBox.textContent = '❌ 비밀번호 변경 실패: ' + errMsg;
            messageBox.className = 'message error';
            container.classList.add('shake');
            setTimeout(() => container.classList.remove('shake'), 500);
        }
    } catch (err) {
        console.error('요청 실패:', err);
        messageBox.textContent = '🚫 서버와 연결할 수 없습니다.';
        messageBox.className = 'message error';
        container.classList.add('shake');
        setTimeout(() => container.classList.remove('shake'), 500);
    }
});
