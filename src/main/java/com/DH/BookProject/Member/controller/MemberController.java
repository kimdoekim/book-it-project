package com.DH.BookProject.Member.controller;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.repository.MemberRepository;
import com.DH.BookProject.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final MemberService service;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("member", new Member()); // 빈 객체를 전달하여 폼 바인딩
        return "register";
    }

    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        try {
            service.saveMember(member);
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("회원가입 처리 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }



}