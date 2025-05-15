package com.DH.BookProject.main.controller;

import com.DH.BookProject.main.dto.SearchBook;
import com.DH.BookProject.main.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Controller
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public String searchPage(@RequestParam("searchWord") String searchWord,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             Model model) {

        // 검색 결과 가져오기
        SearchBook bookList = searchService.getSearchBookList(searchWord, page);

        // 모델에 데이터 추가
        model.addAttribute("bookList", bookList);
        model.addAttribute("currentPage", page);  // 현재 페이지도 모델에 추가
        model.addAttribute("searchWord", searchWord);  // 검색어도 모델에 추가

        return "search";
    }
}
