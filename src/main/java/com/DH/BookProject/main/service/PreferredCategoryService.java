package com.DH.BookProject.main.service;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.PreferredCategory;
import com.DH.BookProject.Member.repository.MemberRepository;
import com.DH.BookProject.Member.service.MemberService;
import com.DH.BookProject.main.repository.PreferredCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreferredCategoryService {
    private final PreferredCategoryRepository preferredCategoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void updateUserCategories(String username, List<Integer> categoryIds) {
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 기존 카테고리 삭제
        preferredCategoryRepository.deleteByMember(member);
    }

    @Transactional
    public boolean addCategory(String username, Integer categoryId, String categoryName) {
        Member member =  memberRepository.findByMemberId(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 이미 존재하는 카테고리인지 확인
        boolean exists = preferredCategoryRepository
                .existsByMemberAndCategoryId(member, categoryId);

        if (exists) {
            return false;
        }

        PreferredCategory newCategory = new PreferredCategory();
        newCategory.setMember(member);
        newCategory.setCategoryId(categoryId);
        newCategory.setCategoryName(categoryName);

        preferredCategoryRepository.save(newCategory);
        return true;
    }

    public List<PreferredCategory> getCategoriesByMemberId(String username) {
        return preferredCategoryRepository.findAllByMemberMemberId(username);
    }


    @Transactional
    public boolean removeCategoryById(Long categoryId, String username) {
        try {
            // 카테고리가 실제로 해당 사용자의 것인지 확인
            Optional<PreferredCategory> categoryOpt = preferredCategoryRepository.findById(categoryId);

            if (categoryOpt.isPresent() && categoryOpt.get().getMember().getMemberId().equals(username)) {
                preferredCategoryRepository.deleteById(categoryId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("카테고리 제거 중 오류 발생: ", e);
            return false;
        }
    }
}
