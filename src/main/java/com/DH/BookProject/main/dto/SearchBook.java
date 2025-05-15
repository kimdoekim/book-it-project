package com.DH.BookProject.main.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchBook {

    String query;
    Integer totalResults;
    Integer itemsPerPage;
    Integer totalPages;
    Integer startPage;
    Integer endPage;
    Integer currentPage;
    List<BookResult> bookList;
}
