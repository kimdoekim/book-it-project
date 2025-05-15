package com.DH.BookProject.ReadingGoal.Repository;


import com.DH.BookProject.ReadingGoal.DTO.GoalPeriod;
import com.DH.BookProject.ReadingGoal.DTO.ReadingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReadingGoalRepository extends JpaRepository<ReadingGoal, Long> {

    List<ReadingGoal> findByMemberMemberIdAndCompletedFalseAndEndDateAfter(String memberId, LocalDate date);

    List<ReadingGoal> findByMemberMemberIdAndPeriodAndStartDateBetween(
            String memberId, GoalPeriod period, LocalDate startDate, LocalDate endDate);

    Optional<ReadingGoal> findByMemberMemberIdAndPeriodAndEndDateAfterAndCompletedFalse(
            String memberId, GoalPeriod period, LocalDate endDate);

    @Query("SELECT COUNT(g) FROM ReadingGoal g WHERE g.member.memberId = :memberId AND g.completed = true")
    int countCompletedGoalsByMemberMemberId(String memberId);

    List<ReadingGoal> findByMemberMemberIdAndCompletedTrue(String userId);
}
