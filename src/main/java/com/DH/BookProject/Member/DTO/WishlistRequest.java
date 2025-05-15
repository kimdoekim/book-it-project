package com.DH.BookProject.Member.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WishlistRequest {
    private String bookId;
    private String title;
    private String author;
    private String coverUrl;
    private String publisher;
    private Integer price;
}