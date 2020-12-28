package com.o2o.action.server.util;

import com.o2o.action.server.DTO.CategoryDTO;
import com.o2o.action.server.DTO.CategoryNewsDTO;
import com.o2o.action.server.DTO.NewsDTO;
import com.o2o.action.server.DTO.RealtimeWordDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface RestAPI {

    @GET("category/insertCategoryNews.jsp")
    Call<String> insertCategoryNews();

    @GET("category/selectCategory.jsp")
    Call<ArrayList<CategoryDTO>> selectCategory();

    @GET("category/selectOneCategoryNews.jsp")
    Call<ArrayList<CategoryNewsDTO>> selectOneCategoryNews(@Query(value ="category") String category);

    @GET("category/selectCategoryNews.jsp")
    Call<CategoryNewsDTO> selectCategoryNews();

    @GET("realtimeword/insertRealtimeWord.jsp")
    Call<String> insertRealtimeWord();

    @GET("realtimeword/selectRealtimeWord.jsp")
    Call<ArrayList<RealtimeWordDTO>> selectRealtimeWord();

    @GET("search/insertSearchNews.jsp")
    Call<String> insertSearchNews(@Query(value ="searchword") String searchword);

    @GET("search/selectSearchNews.jsp")
    Call<NewsDTO> selectSearchNews();

    @GET("search/selectDetailNews.jsp")
    Call<NewsDTO> selectDetailNews(@Query(value ="aid") String aid);

    @GET("search/selectOneSearchNews.jsp")
    Call<ArrayList<NewsDTO>> selectOneSearchNews(@Query(value ="searchword") String searchword);

    @GET("search/updateCategoryNews.jsp")
    Call<CategoryNewsDTO> updateCategoryNews(@Query(value ="url") String url);

    @GET("search/updateNews.jsp")
    Call<NewsDTO> updateNews(@Query(value ="url") String url);

}
