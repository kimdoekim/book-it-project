package com.DH.BookProject.main.controller;

import com.DH.BookProject.Member.DTO.MemberUser;
import com.DH.BookProject.main.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add")
    public String addReview(
            @RequestParam("bookId") String bookId,
            @RequestParam("rating") int rating,
            @RequestParam("comment") String comment,
            @RequestParam("bookTitle") String title,
            @RequestParam("bookCover") String cover,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            MemberUser memberUser = (MemberUser) auth.getPrincipal();

            try {
                boolean isAdded = reviewService.addReview(memberUser.getUsername(), bookId, rating, comment, title, cover);

                if (isAdded) {
                    redirectAttributes.addFlashAttribute("message", "리뷰가 등록되었습니다.");
                } else {
                    redirectAttributes.addFlashAttribute("warning", "이미 이 책에 리뷰를 작성하셨습니다.");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "리뷰 등록 중 오류가 발생했습니다: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
        }

        return "redirect:/detail/" + bookId;  // 경로 수정
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> deleteReview(@PathVariable("id") Long id, Authentication auth) {
        Map<String, Object> response = new HashMap<>();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            MemberUser memberUser = (MemberUser) auth.getPrincipal();

            try {
                boolean deleted = reviewService.deleteReview(id, memberUser.getUsername());
                response.put("success", deleted);

                if (!deleted) {
                    response.put("message", "삭제 권한이 없거나 리뷰를 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "리뷰 삭제 중 오류가 발생했습니다: " + e.getMessage());
            }
        } else {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
        }

        return response;
    }





}