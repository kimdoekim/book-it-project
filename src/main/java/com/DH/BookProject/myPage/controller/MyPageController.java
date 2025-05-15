package com.DH.BookProject.myPage.controller;

import com.DH.BookProject.Member.DTO.*;
import com.DH.BookProject.Member.service.MemberService;
import com.DH.BookProject.Member.service.WishlistService;
import com.DH.BookProject.main.service.PreferredCategoryService;
import com.DH.BookProject.main.service.ReviewService;
import com.DH.BookProject.myPage.DTO.MyPageUser;
import com.DH.BookProject.myPage.service.myPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final WishlistService wishlistService;
    private final ReviewService reviewService;
    private final PreferredCategoryService preferredCategoryService;

    @GetMapping("/myPage")
    public String myPage(Model model, Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String username = auth.getName();
        MemberUser user = (MemberUser) auth.getPrincipal();

        try {
            // 사용자 정보 가져오기
            model.addAttribute("user", user);

            // 추가 사용자 정보 조회 (Member 엔티티에서)
            Member memberDetail = memberService.findByMemberId(username);
            // 또는 MemberService를 통해: Member memberDetail = memberService.findByMemberId(username);
            model.addAttribute("memberDetail", memberDetail);

            // 위시리스트 가져오기
            List<Wishlist> wishlist = wishlistService.findByMemberId(username);
            model.addAttribute("wishlist", wishlist);

            // 사용자 리뷰 가져오기
            List<Review> reviews = reviewService.getReviewsByMemberId(username);
            model.addAttribute("reviews", reviews);

            // 사용자 선호 카테고리 가져오기
            List<PreferredCategory> categories = preferredCategoryService.getCategoriesByMemberId(username);
            model.addAttribute("categories", categories);


        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "myPage";
    }
}
