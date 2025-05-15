package com.DH.BookProject.main.controller;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.MemberUser;
import com.DH.BookProject.Member.DTO.PreferredCategory;
import com.DH.BookProject.Member.DTO.Review;
import com.DH.BookProject.Member.service.MemberService;
import com.DH.BookProject.main.dto.*;
import com.DH.BookProject.main.service.AladinApiService;
import com.DH.BookProject.main.service.PreferredCategoryService;
import com.DH.BookProject.main.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final AladinApiService service;
    private final PreferredCategoryService preferredCategoryService;
    private final MemberService memberService;
    private final ReviewService reviewService;


    @GetMapping("/")
    public String mainPage(Model model, Authentication auth) {
        List<BestBook> bookList = service.getBestSellerList();

        String categoryName = "";
        int categoryId = 0; // 기본값
        boolean isPersonalized = false;
        String memberName = "";

        // 로그인한 사용자가 있는 경우 선호 카테고리 가져오기
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            MemberUser memberUser = (MemberUser) auth.getPrincipal();
            MemberUser member = (MemberUser) memberService.loadUserByUsername(memberUser.getUsername());
            memberName = member.getMemberName();

            List<PreferredCategory> preferredCategories = preferredCategoryService.getCategoriesByMemberId(memberUser.getUsername());

            if (!preferredCategories.isEmpty()) {
                int randomIndex = (int) (Math.random() * preferredCategories.size());
                PreferredCategory selectedCategory = preferredCategories.get(randomIndex);
                categoryId = selectedCategory.getCategoryId();
                isPersonalized = true;
            }

        }


        NewCategoryList newCategoryBookList = service.getNewSpecialList(categoryId);
        categoryName = newCategoryBookList.getSearchCategoryName();


        // 개인화된 카테고리 제목 생성
        String categoryTitle = "국내도서 신간 추천";
        if (isPersonalized) {
            categoryTitle = memberName + "님이 선호하는 " + categoryName + " 신간 도서";
        } else {
            categoryTitle = categoryName + " - 주목할 만한 신간";
        }


        model.addAttribute("bookList", bookList);
        model.addAttribute("categoryTitle", categoryTitle);
        model.addAttribute("newBookList", newCategoryBookList.getNewCategoryBookList());
        return "index";
    }

    @GetMapping("/detail/{itemId}")
    public String detailPage(@PathVariable("itemId") String itemId,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             Model model) {
        // 책 상세 정보 가져오기
        BookDetail bookDetail = service.getBookDetail(itemId);
        model.addAttribute("book", bookDetail);

        // 리뷰 목록 가져오기 (페이지네이션 적용, 한 페이지에 5개씩)
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createDate").descending());
        Page<Review> reviews = reviewService.getReviewsByBookId(itemId, pageable);
        model.addAttribute("reviews", reviews);

        return "detail";
    }



}
