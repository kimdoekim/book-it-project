package com.DH.BookProject.myPage.controller;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.service.MemberService;
import com.DH.BookProject.myPage.DTO.MemberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberEditController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/edit")
    public String editForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Member member = memberService.findByMemberId(username);

        if (member == null) {
            return "redirect:/";
        }

        // DTO에 회원 정보 복사
        MemberForm memberForm = new MemberForm();
        memberForm.setMemberId(member.getMemberId());
        memberForm.setMemberName(member.getMemberName());
        memberForm.setEmail(member.getEmail());

        model.addAttribute("memberForm", memberForm);
        return "edit";
    }

    @PostMapping("/update")
    public String updateMember(
            @Valid @ModelAttribute("memberForm") MemberForm memberForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();

        // 본인 정보만 수정 가능하도록 체크
        if (!username.equals(memberForm.getMemberId())) {
            redirectAttributes.addFlashAttribute("error", "본인의 정보만 수정할 수 있습니다.");
            return "redirect:/myPage";
        }

        // 폼 검증 에러가 있으면 다시 폼으로
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        // 비밀번호 변경 처리
        boolean changePassword = Boolean.valueOf(memberForm.getChangePassword());
        if (changePassword) {
            // 현재 비밀번호 확인
            Member currentMember = memberService.findByMemberId(username);
            if (!passwordEncoder.matches(memberForm.getCurrentPassword(), currentMember.getPassword())) {
                model.addAttribute("currentPasswordError", "현재 비밀번호가 일치하지 않습니다.");
                return "edit";
            }

            // 새 비밀번호 유효성 검사
            if (memberForm.getNewPassword().length() < 8) {
                model.addAttribute("newPasswordError", "비밀번호는 8자 이상이어야 합니다.");
                return "edit";
            }

            // 새 비밀번호 일치 여부 확인
            if (!memberForm.getNewPassword().equals(memberForm.getConfirmPassword())) {
                model.addAttribute("confirmPasswordError", "비밀번호가 일치하지 않습니다.");
                return "edit";
            }

            // 비밀번호 암호화
            memberForm.setPassword(passwordEncoder.encode(memberForm.getNewPassword()));
        }

        try {
            // 회원 정보 업데이트
            memberService.updateMember(memberForm);
            redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/myPage";
    }
}