package com.DH.BookProject.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewCategoryList {

    String searchCategoryName;
    List<NewCategoryBook> newCategoryBookList;

}
