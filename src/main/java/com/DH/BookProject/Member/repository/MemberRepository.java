package com.DH.BookProject.Member.repository;

import com.DH.BookProject.Member.DTO.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByMemberId(String username);
}
