package com.DH.BookProject.main.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookResult {
    String title;
    String link;
    String author;
    String pubDate;
    String isbn13;
    String cover;
}
