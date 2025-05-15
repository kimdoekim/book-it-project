package com.DH.BookProject.main.repository;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.PreferredCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferredCategoryRepository extends JpaRepository<PreferredCategory, Long> {
    List<PreferredCategory> findAllByMemberMemberId(String memberId);

    boolean existsByMemberMemberIdAndCategoryId(String memberId, Integer categoryId);

    void deleteByMember(Member member);

    boolean existsByMemberAndCategoryId(Member member, Integer categoryId);
}
