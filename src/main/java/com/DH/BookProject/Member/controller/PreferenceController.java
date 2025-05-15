package com.DH.BookProject.Member.controller;

import com.DH.BookProject.Member.DTO.MemberUser;
import com.DH.BookProject.main.service.PreferredCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/preference/category")
public class PreferenceController {

    private final PreferredCategoryService preferredCategoryService;

    @PostMapping("/update")
    public String updateCategories(@RequestParam(value = "categoryIds", required = false) List<Integer> categoryIds,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        String username = auth.getName();

        try {
            // null 체크 추가 (아무것도 선택하지 않은 경우)
            if (categoryIds == null) {
                categoryIds = new ArrayList<>();
            }

            preferredCategoryService.updateUserCategories(username, categoryIds);
            redirectAttributes.addFlashAttribute("message", "관심 장르가 업데이트 되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "관심 장르 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/myPage";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addCategory(@RequestBody Map<String, Object> request, Authentication auth) {
        Map<String, Object> response = new HashMap<>();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        String username = auth.getName();
        Integer categoryId = ((Number) request.get("categoryId")).intValue();
        String categoryName = (String) request.get("categoryName");

        try {
            boolean added = preferredCategoryService.addCategory(username, categoryId, categoryName);
            response.put("success", added);

            if (!added) {
                response.put("message", "이미 선호 장르에 추가된 카테고리입니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "카테고리 추가 중 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

    @GetMapping("/remove")
    public String removeCategory(@RequestParam("categoryId") Long categoryId,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        String username = auth.getName();

        try {
            boolean removed = preferredCategoryService.removeCategoryById(categoryId, username);
            if (removed) {
                redirectAttributes.addFlashAttribute("message", "관심 장르가 제거되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("error", "해당 관심 장르를 찾을 수 없거나 제거할 권한이 없습니다.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "관심 장르 제거 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/myPage";
    }
}