package com.cloudboot.room_reservation.member.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinRequest {

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "ID가 올바른 이메일 형식이 아닙니다."
    )
    private String username;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[\\]{}|;:,.<>?~-]).{8,}$",
            message = "비밀번호는 영어, 숫자, 특수문자를 각각 하나 이상 포함하고 8자 이상이어야 합니다."
    )
    private String password;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[\\]{}|;:,.<>?~-]).{8,}$",
            message = "비밀번호는 영어, 숫자, 특수문자를 각각 하나 이상 포함하고 8자 이상이어야 합니다."
    )
    private String passwordConfirm;

    public JoinRequest() {
    }

}