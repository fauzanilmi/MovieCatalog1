package com.example.fauzan.moviecatalog.API;

import com.example.fauzan.moviecatalog.DataModel.Catalog;
import com.example.fauzan.moviecatalog.DataModel.CatalogResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseAPIService {
    @GET("search/movie")
    Call<CatalogResponse> searchCatalog(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page,
            @Query("query") String query
    );

    @GET("movie/popular")
    Call<CatalogResponse> getPopularCatalogs(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<Catalog> getCatalog(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

}
