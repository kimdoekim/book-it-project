package com.DH.BookProject.main.service;

import com.DH.BookProject.main.dto.SearchBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchServiceTest {

    private final SearchService service;

    @Autowired
    SearchServiceTest(SearchService service) {
        this.service = service;
    }

    @Test
    void test1(){
        SearchBook book= service.getSearchBookList("학생회",1);
        System.out.println(book);
    }

}