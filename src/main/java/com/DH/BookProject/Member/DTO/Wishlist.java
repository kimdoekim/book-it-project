package com.DH.BookProject.Member.DTO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 20)
    private String bookId;

    // 기본 도서 정보 캐싱
    @Column(length = 200)
    private String title;

    @Column(length = 100)
    private String author;

    @Column(length = 200)
    private String coverUrl;

    @Column(length = 100)
    private String publisher;

    // 가격 정보 (선택사항)
    private Integer price;

    // 위시리스트에 추가한 날짜
    private LocalDateTime createDate = LocalDateTime.now();

}