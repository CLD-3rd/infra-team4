package com.cloudboot.room_reservation.member.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateMemberRequest {

    private String originPassword;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[\\]{}|;:,.<>?~-]).{8,}$",
            message = "비밀번호는 영어, 숫자, 특수문자를 각각 하나 이상 포함하고 8자 이상이어야 합니다."
    )
    private String newPassword;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[\\]{}|;:,.<>?~-]).{8,}$",
            message = "비밀번호는 영어, 숫자, 특수문자를 각각 하나 이상 포함하고 8자 이상이어야 합니다."
    )
    private String newPasswordConfirm;

}
