package com.DH.BookProject.main.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class PreferredCategoryServiceTest {

    private final PreferredCategoryService preferredCategoryService;

    @Autowired
    PreferredCategoryServiceTest(PreferredCategoryService preferredCategoryService) {
        this.preferredCategoryService = preferredCategoryService;
    }

    @Test
    void test1(){
//        preferredCategoryService.addCategory("abc123",0);
    }
}