package com.DH.BookProject.Member.controller;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.MemberUser;
import com.DH.BookProject.Member.DTO.Wishlist;
import com.DH.BookProject.Member.DTO.WishlistRequest;
import com.DH.BookProject.Member.service.MemberService;
import com.DH.BookProject.Member.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final MemberService memberService;

    @PostMapping("/wishlist/add")
    public ResponseEntity<Map<String, Object>> addToWishlist(@RequestBody WishlistRequest request,
                                                             Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        // 로그인 확인
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            response.put("success", false);
            response.put("message", "로그인이 필요한 기능입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String username = authentication.getName();

        try {
            boolean success = wishlistService.addToWishlist(username, request);

            if (success) {
                response.put("success", true);
                response.put("message", "내 서재에 추가되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "이미 내 서재에 추가된 도서입니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/wishlist/remove/{id}")
    public Map<String, Object> removeFromWishlist(@PathVariable("id") Long id, Authentication auth) {
        Map<String, Object> response = new HashMap<>();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();

            try {
                boolean removed = wishlistService.removeWishlist(id, username);
                response.put("success", removed);

                if (!removed) {
                    response.put("message", "삭제 권한이 없거나 항목을 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "삭제 중 오류가 발생했습니다: " + e.getMessage());
            }
        } else {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
        }

        return response;
    }


}