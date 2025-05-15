package com.DH.BookProject.main.service;

import com.DH.BookProject.main.dto.SearchBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final AladinApiService service;

    public SearchBook getSearchBookList(String searchWord, int page) {
        SearchBook searchBook = service.getSearchBookList(searchWord, page);

        searchBook.setCurrentPage(page);
        // 총 페이지 수 계산
        int totalPages = calculateTotalPages(searchBook.getTotalResults(), searchBook.getItemsPerPage());
        if (totalPages > 20) {
            totalPages = 20;  // 최대 20페이지까지만 보여줌
        }
        searchBook.setTotalPages(totalPages);

        // 페이지 범위 계산 (1~10, 11~20 등)
        int startPage = ((page - 1) / 10) * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPages);

        searchBook.setStartPage(startPage);
        searchBook.setEndPage(endPage);

        return searchBook;
    }

    private int calculateTotalPages(Integer totalResults, Integer itemsPerPage) {
        if (totalResults != null && itemsPerPage != null && itemsPerPage > 0) {
            return (int) Math.ceil((double) totalResults / itemsPerPage);
        }
        return 1;
    }

}
