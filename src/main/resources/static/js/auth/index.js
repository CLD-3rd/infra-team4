const loginTab = document.getElementById("loginTab");
const signupTab = document.getElementById("signupTab");
const loginForm = document.getElementById("loginForm");
const signupForm = document.getElementById("signupForm");

const signupMessage = document.getElementById("signupMessage");
const loginMessage = document.getElementById("loginMessage");

loginTab.addEventListener("click", () => {
    loginTab.classList.add("active");
    signupTab.classList.remove("active");
    loginForm.style.display = "flex";
    signupForm.style.display = "none";
    clearMessages();
});

signupTab.addEventListener("click", () => {
    signupTab.classList.add("active");
    loginTab.classList.remove("active");
    signupForm.style.display = "flex";
    loginForm.style.display = "none";
    clearMessages();
});

function clearMessages() {
    signupMessage.textContent = "";
    loginMessage.textContent = "";
    signupMessage.className = "message";
    loginMessage.className = "message";
}

const usernameInput = document.getElementById("signupUsername");
const emailValidation = document.getElementById("emailValidation");

const passwordInput = document.getElementById("signupPassword");
const hasLetter = document.getElementById("hasLetter");
const hasNumber = document.getElementById("hasNumber");
const hasSpecial = document.getElementById("hasSpecial");
const passwordCriteria = document.getElementById("passwordCriteria");

const confirmInput = document.getElementById("signupConfirmPassword");
const passwordMatch = document.getElementById("passwordMatch");

// 이메일 입력 시 실시간 유효성 검사
usernameInput.addEventListener("input", () => {
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
    const isValid = emailRegex.test(usernameInput.value);

    emailValidation.classList.remove("hidden");
    emailValidation.textContent = isValid ? "이메일 ✓" : "이메일 ✖";
    emailValidation.className = isValid ? "validation-msg valid" : "validation-msg invalid";
});

// 비밀번호 입력 시 조건 검사
passwordInput.addEventListener("focus", () => {
    passwordCriteria.classList.remove("hidden");
});

passwordInput.addEventListener("input", () => {
    const value = passwordInput.value;

    if (/[A-Za-z]/.test(value)) {
        hasLetter.textContent = "영어 ✓";
        hasLetter.className = "valid";
    } else {
        hasLetter.textContent = "영어 ✖";
        hasLetter.className = "invalid";
    }

    if (/\d/.test(value)) {
        hasNumber.textContent = "숫자 ✓";
        hasNumber.className = "valid";
    } else {
        hasNumber.textContent = "숫자 ✖";
        hasNumber.className = "invalid";
    }

    if (/[!@#$%^&*()_+=\[\]{}|;:,.<>?~-]/.test(value)) {
        hasSpecial.textContent = "특수문자 ✓";
        hasSpecial.className = "valid";
    } else {
        hasSpecial.textContent = "특수문자 ✖";
        hasSpecial.className = "invalid";
    }

    // 비밀번호 확인도 실시간으로 갱신
    updatePasswordMatch();
});

confirmInput.addEventListener("input", () => {
    passwordMatch.classList.remove("hidden");
    updatePasswordMatch();
});

function updatePasswordMatch() {
    const match = passwordInput.value === confirmInput.value && confirmInput.value.length > 0;

    passwordMatch.textContent = match ? "비밀번호 일치 ✓" : "비밀번호 일치 ✖";
    passwordMatch.className = match ? "validation-msg valid" : "validation-msg invalid";
}

// 회원가입 전송
signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = usernameInput.value;
    const password = passwordInput.value;
    const passwordConfirm = confirmInput.value;

    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+=\[\]{}|;:,.<>?~-]).{8,}$/;

    if (!emailRegex.test(username)) {
        signupMessage.textContent = "ID가 올바른 이메일 형식이 아닙니다.";
        signupMessage.className = "message error";
        return;
    }

    if (!passwordRegex.test(password)) {
        signupMessage.textContent = "비밀번호는 영어, 숫자, 특수문자를 각각 하나 이상 포함하고 8자 이상이어야 합니다.";
        signupMessage.className = "message error";
        return;
    }

    if (password !== passwordConfirm) {
        signupMessage.textContent = "비밀번호가 일치하지 않습니다.";
        signupMessage.className = "message error";
        return;
    }

    // ✅ 실제 회원가입 요청 추가
    try {
        const response = await fetch("/api/join", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                password,
                passwordConfirm
            }),
        });

        if (response.ok) {
            signupMessage.textContent = "회원가입 성공!";
            signupMessage.className = "message success";
            setTimeout(() => {
                window.location.href = "/index.html";
            }, 500);
        } else {
            const error = await response.json();
            signupMessage.textContent = error.errorMessage || "회원가입 실패: 알 수 없는 오류";
            signupMessage.className = "message error";
        }
    } catch (err) {
        console.error("회원가입 요청 중 오류 발생:", err);
        signupMessage.textContent = "서버와의 연결에 실패했습니다.";
        signupMessage.className = "message error";
    }
});

// 로그인
loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("loginUsername").value;
    const password = document.getElementById("loginPassword").value;

    // ✅ 클라이언트 유효성 검사 추가
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;

    if (!emailRegex.test(username)) {
        loginMessage.textContent = "ID는 이메일 형식이어야 합니다.";
        loginMessage.className = "message error";
        return;
    }

    if (!password || password.trim().length === 0) {
        loginMessage.textContent = "비밀번호를 입력해주세요.";
        loginMessage.className = "message error";
        return;
    }


    // 로그인
    try {
        const response = await fetch("/api/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: 'include',
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const accessToken = response.headers.get("access");
            const redirectUrl = response.headers.get("redirect-url");

            if (accessToken) {
                localStorage.setItem("access", accessToken);
                console.log("Token 저장 완료:", accessToken);
            }

            window.location.href = redirectUrl || "/user-dashboard.html";
        } else {
            const errorData = await response.json();
            loginMessage.textContent = errorData.errorMessage || "로그인 실패: 알 수 없는 오류";
            loginMessage.className = "message error";
        }
    } catch (err) {
        console.error("로그인 요청 중 오류 발생:", err);
        loginMessage.textContent = "서버와의 연결에 실패했습니다.";
        loginMessage.className = "message error";
    }



});