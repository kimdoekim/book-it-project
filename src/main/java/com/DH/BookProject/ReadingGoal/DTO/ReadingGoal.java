package com.DH.BookProject.ReadingGoal.DTO;

import com.DH.BookProject.Member.DTO.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reading_goals")
public class ReadingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private GoalPeriod period; // MONTHLY, YEARLY

    @Enumerated(EnumType.STRING)
    private GoalType type; // BOOKS_COUNT, PAGES_COUNT

    private Integer targetValue; // 목표 책 수 또는 페이지 수
    private Integer currentValue; // 현재 읽은 책 수 또는 페이지 수

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private Boolean completed = false;

    // getters, setters, constructors
}


