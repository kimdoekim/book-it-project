package com.DH.BookProject.Member.repository;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByMemberAndBookId(Member member, String bookId);

    List<Wishlist> findByMember_MemberId(String username);
}
