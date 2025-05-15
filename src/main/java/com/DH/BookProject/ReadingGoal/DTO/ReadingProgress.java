package com.DH.BookProject.ReadingGoal.DTO;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.main.dto.BookDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Book;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reading_progress")
public class ReadingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String isbn13;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private ReadingGoal goal; // 연결된 독서 목표 (없을 수도 있음)

    private Integer pagesRead;
    private Boolean completed = false;
    private LocalDateTime completedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // getters, setters, constructors

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

