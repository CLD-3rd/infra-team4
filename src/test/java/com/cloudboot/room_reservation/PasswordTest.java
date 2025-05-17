package com.cloudboot.room_reservation;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {

    @Test
    void generateAdminPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123!"; // 실제 사용할 비밀번호
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("암호화된 비밀번호: " + encodedPassword);

        // 검증
        System.out.println("검증 결과: " + encoder.matches(rawPassword, encodedPassword));
    }

}
