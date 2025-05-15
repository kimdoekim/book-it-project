package com.DH.BookProject.ReadingGoal.Repository;

import com.DH.BookProject.ReadingGoal.DTO.Badge;
import com.DH.BookProject.ReadingGoal.DTO.BadgeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    List<Badge> findByType(BadgeType type);

    @Query("SELECT b FROM Badge b WHERE b.type = :type AND b.threshold <= :value ORDER BY b.threshold DESC")
    List<Badge> findEligibleBadges(BadgeType type, int value);
}