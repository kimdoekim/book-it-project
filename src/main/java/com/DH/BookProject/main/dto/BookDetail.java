package com.DH.BookProject.main.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDetail {
    String title;
    String cover;
    String author;
    String link;
    String pubDate;
    String description;
    Integer priceSales;
    Integer priceStandard;
    String categoryName;
    Integer categoryId;
    Integer itemPage;
    Double ratingScore;
    Integer ratingCount;

    String isbn13;
    String publisher;

}
