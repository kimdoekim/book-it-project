package com.DH.BookProject.Member.DTO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 20)
    private String bookId;

    @Column(length = 200)
    private String bookTitle;

    private String cover;

    private int rating;

    @Column(length = 500)
    private String comment;

    private LocalDateTime createDate = LocalDateTime.now();
}
