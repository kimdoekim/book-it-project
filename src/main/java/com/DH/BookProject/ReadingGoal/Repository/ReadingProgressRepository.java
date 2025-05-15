package com.DH.BookProject.ReadingGoal.Repository;

import com.DH.BookProject.ReadingGoal.DTO.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {

    List<ReadingProgress> findByMemberMemberIdOrderByUpdatedAtDesc(String memberId);

    List<ReadingProgress> findByMemberMemberIdAndCompletedTrue(String memberId);

    @Query("SELECT SUM(p.pagesRead) FROM ReadingProgress p WHERE p.member.memberId = :memberId")
    Integer getTotalPagesReadByMemberMemberId(String memberId);

    @Query("SELECT COUNT(p) FROM ReadingProgress p WHERE p.member.memberId = :memberId AND p.completed = true")
    int countCompletedBooksByMemberMemberId(String memberId);

    @Query("SELECT p FROM ReadingProgress p WHERE p.member.memberId = :memberId AND p.updatedAt >= :startDate ORDER BY p.updatedAt")
    List<ReadingProgress> findRecentProgressByMemberMemberId(String memberId, LocalDateTime startDate);
}