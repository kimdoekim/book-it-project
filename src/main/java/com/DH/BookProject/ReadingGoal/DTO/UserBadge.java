package com.DH.BookProject.ReadingGoal.DTO;

import com.DH.BookProject.Member.DTO.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_badges")
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @Column(nullable = false)
    private LocalDateTime awardedAt;

    // getters, setters, constructors

    @PrePersist
    protected void onCreate() {
        this.awardedAt = LocalDateTime.now();
    }
}
