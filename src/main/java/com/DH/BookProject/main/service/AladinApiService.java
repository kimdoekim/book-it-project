package com.DH.BookProject.main.service;

import com.DH.BookProject.main.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AladinApiService {

    private final String aladin_api_key;


    public AladinApiService(@Value("${aladin-api-key}") String aladinApiKey) {
        this.aladin_api_key = aladinApiKey;
    }

    private String fetchData(StringBuilder urlBuilder, Map<String, String> headers){
        try {
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            // 헤더 부분 추가
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            log.info("응답코드: {}", conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            return sb.toString();
        } catch (IOException e) {
            log.error("API 요청 실패: {}", urlBuilder, e);
            return null;
        }
    }

    private String getBestSeller(){
        StringBuilder urlBuilder = new StringBuilder("http://www.aladin.co.kr/ttb/api/ItemList.aspx");
        urlBuilder.append("?ttbkey=").append(aladin_api_key);
        urlBuilder.append("&QueryType=BestSeller");
        urlBuilder.append("&MaxResults=15");
        urlBuilder.append("&start=1");
        urlBuilder.append("&cover=big");
        urlBuilder.append("&output=js");
        urlBuilder.append("&SearchTarget=Book").append("&Version=20131101");

        return fetchData(urlBuilder,null);
    }

    public List<BestBook> getBestSellerList(){
        List<BestBook> bookList = new ArrayList<>();
        Gson gson = new Gson();

        JsonObject jsonObject = JsonParser.parseString(getBestSeller()).getAsJsonObject();
        JsonArray items = jsonObject.getAsJsonArray("item");
        int itemCnt = 0;
        for(JsonElement element : items){
            if(itemCnt>=10){
                break;
            }
            BestBook bb = gson.fromJson(element, BestBook.class);
            String BookTitle = bb.getTitle();
            if(BookTitle.contains("-")){
                BookTitle = BookTitle.split("-")[0].trim();
                bb.setTitle(BookTitle);
            }
            bookList.add(bb);
            itemCnt++;
        }
        bookList.sort(Comparator.comparingInt(BestBook::getBestRank));
//        System.out.println(bookList);
        return bookList;
    }

    public NewCategoryList getNewSpecialList(int categoryId){
        StringBuilder urlBuilder = new StringBuilder("http://www.aladin.co.kr/ttb/api/ItemList.aspx");
        urlBuilder.append("?ttbkey=").append(aladin_api_key);
        urlBuilder.append("&QueryType=ItemNewSpecial");
        urlBuilder.append("&MaxResults=10");
        urlBuilder.append("&start=1");
        urlBuilder.append("&CategoryId=").append(categoryId);
        urlBuilder.append("&cover=big");
        urlBuilder.append("&output=js");
        urlBuilder.append("&SearchTarget=Book").append("&Version=20131101");
        String jsonStr = fetchData(urlBuilder,null);

        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        JsonArray items = jsonObject.getAsJsonArray("item");

        String categoryName = jsonObject.get("searchCategoryName").getAsString();
        List<NewCategoryBook> bookList = new ArrayList<>();

        for(JsonElement element : items){
            NewCategoryBook book = gson.fromJson(element, NewCategoryBook.class);
            JsonObject aa= element.getAsJsonObject();
            String BookTitle = book.getTitle();
            if(BookTitle.contains("-")){
                BookTitle = BookTitle.split("-")[0].trim();
                book.setTitle(BookTitle);
            }
            bookList.add(book);
        }

        NewCategoryList newCategoryList = new NewCategoryList(categoryName,bookList);


        return newCategoryList;
    }

    public BookDetail getBookDetail(String isbn13){
        StringBuilder urlBuilder = new StringBuilder("http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx");
        urlBuilder.append("?ttbkey=").append(aladin_api_key);
        urlBuilder.append("&itemIdType=ISBN13");
        urlBuilder.append("&ItemId=").append(isbn13);
        urlBuilder.append("&output=js");
        urlBuilder.append("&version=20131101");
        urlBuilder.append("&OptResult=ratingInfo");
        urlBuilder.append("&cover=big");
        String jsonStr = fetchData(urlBuilder,null);

        BookDetail bookDetail = new BookDetail();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
            JsonArray item = jsonObject.getAsJsonArray("item");
            JsonElement element;
            if (!item.isJsonNull()) {
                element = item.get(0);
                JsonObject info = element.getAsJsonObject();
                JsonObject subInfo = info.getAsJsonObject("subInfo");
                JsonObject ratingInfo = subInfo.getAsJsonObject("ratingInfo");

                String originalCover =  info.get("cover").getAsString();
                String largeCover = originalCover.replace("cover200","cover500");

                bookDetail.setIsbn13(info.get("isbn13").getAsString());
                bookDetail.setPublisher(info.get("publisher").getAsString());

                bookDetail.setTitle( info.get("title").getAsString());
                bookDetail.setCover(largeCover);
                bookDetail.setAuthor( info.get("author").getAsString());
                bookDetail.setLink(info.get("link").getAsString());
                bookDetail.setPubDate(info.get("pubDate").getAsString());
                bookDetail.setDescription(info.get("description").getAsString());
                bookDetail.setPriceSales(info.get("priceSales").getAsInt());
                bookDetail.setPriceStandard(info.get("priceStandard").getAsInt());
                bookDetail.setCategoryName(info.get("categoryName").getAsString());
                bookDetail.setCategoryId(info.get("categoryId").getAsInt());

                bookDetail.setItemPage(subInfo.get("itemPage").getAsInt());
                bookDetail.setRatingScore(ratingInfo.get("ratingScore").getAsDouble());
                bookDetail.setRatingCount(ratingInfo.get("ratingCount").getAsInt());
            }
        }catch (Exception e){
            System.out.println(e+"error");
            return null;
        }
        return bookDetail;
    }

    public SearchBook getSearchBookList(String query, int page)  {
        StringBuilder urlBuilder = new StringBuilder("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?");
        urlBuilder.append("ttbkey=").append(aladin_api_key);
        try {
            urlBuilder.append("&Query=").append(URLEncoder.encode(query, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        urlBuilder.append("&start=").append(page);
        urlBuilder.append("&MaxResults=10");
        urlBuilder.append("&SearchTarget=Book");
        urlBuilder.append("&output=js");
        urlBuilder.append("&Version=20131101");
        urlBuilder.append("&Cover=big");
        String jsonStr = fetchData(urlBuilder,null);

        SearchBook bookList = new SearchBook();
        try {
            Gson gson = new Gson();
            JsonObject object = JsonParser.parseString(jsonStr).getAsJsonObject();
            JsonArray items = object.get("item").getAsJsonArray();

            List<BookResult> bookResultList = new ArrayList<>();
            for(JsonElement element: items){
                BookResult bookResult= gson.fromJson(element, BookResult.class);
                bookResultList.add(bookResult);
            }

            bookList.setBookList(bookResultList);
            bookList.setTotalResults(object.get("totalResults").getAsInt());
            bookList.setItemsPerPage(object.get("itemsPerPage").getAsInt());
            bookList.setQuery(object.get("query").getAsString());
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return null;
        }
        return bookList;
    }



}
