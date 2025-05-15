package com.DH.BookProject.ReadingGoal.DTO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter

@Getter
@Entity
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private BadgeType type;

    private Integer threshold; // 뱃지 획득을 위한 임계값

    // getters, setters, constructors
}

