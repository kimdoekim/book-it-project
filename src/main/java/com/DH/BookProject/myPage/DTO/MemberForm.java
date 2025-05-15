package com.DH.BookProject.myPage.DTO;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class MemberForm {

    private String memberId;

    @NotBlank(message = "이름은 필수 항목입니다")
    @Size(max = 10, message = "이름은 10자 이하여야 합니다")
    private String memberName;

    @NotBlank(message = "이메일은 필수 항목입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다")
    private String email;

    private String password;

    // 비밀번호 변경 관련 필드
    private String changePassword; // 비밀번호 변경 여부 (checkbox)
    private String currentPassword; // 현재 비밀번호
    private String newPassword; // 새 비밀번호
    private String confirmPassword; // 새 비밀번호 확인
}