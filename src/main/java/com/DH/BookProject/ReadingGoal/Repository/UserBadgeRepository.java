package com.DH.BookProject.ReadingGoal.Repository;

import com.DH.BookProject.ReadingGoal.DTO.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findByMemberMemberIdOrderByAwardedAtDesc(String MemberId);

    boolean existsByMemberMemberIdAndBadgeId(String MemberId, Long badgeId);
}