package com.DH.BookProject.main.repository;


import com.DH.BookProject.Member.DTO.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByBookId(String bookId, Pageable pageable);

    boolean existsByMemberMemberIdAndBookId(String username, String bookId);

    List<Review> findByMember_MemberId(String username);
}