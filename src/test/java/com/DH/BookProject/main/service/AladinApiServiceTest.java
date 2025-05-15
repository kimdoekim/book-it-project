package com.DH.BookProject.main.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AladinApiServiceTest {

    @Autowired
    private AladinApiService service;

    @Test
    public void test1(){
//        service.getBestSellerList();
//        service.getNewSpecialList(2551);
//        service.getBookDetail("9791171257539");
        service.getSearchBookList("소드 아트 온라인",1);
    }
}