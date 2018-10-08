package com.example.fauzan.moviecatalog.API;

import com.example.fauzan.moviecatalog.DataModel.Catalog;
import com.example.fauzan.moviecatalog.DataModel.CatalogResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CatalogRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static final String KEY_API = "313c351a126a28423fefa61de53a0004";
    private static CatalogRepository repository;
    private BaseAPIService baseAPIService;

    private CatalogRepository(BaseAPIService baseAPIService) {
        this.baseAPIService = baseAPIService;
    }

    public static CatalogRepository getInstance(){
        if (repository == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository =  new CatalogRepository(retrofit.create(BaseAPIService.class));

        }
        return repository;
    }

    public void getCatalogs(int page, final OnGetCatalogsCallback callback){
        baseAPIService.getPopularCatalogs(KEY_API, LANGUAGE, page)
                .enqueue(new Callback<CatalogResponse>() {
                    @Override
                    public void onResponse(Call<CatalogResponse> call, Response<CatalogResponse> response) {
                        if (response.isSuccessful()){
                            CatalogResponse catalogResponse = response.body();

                            if (catalogResponse != null && catalogResponse.getMovies() != null){
                                callback.onSuccess(catalogResponse.getPage(), catalogResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<CatalogResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getCatalog(int catalogId, final OnGetCatalogCallback callback) {
        baseAPIService.getCatalog(catalogId, KEY_API, LANGUAGE)
                .enqueue(new Callback<Catalog>() {
                    @Override
                    public void onResponse(Call<Catalog> call, Response<Catalog> response) {
                        if (response.isSuccessful()) {
                            Catalog catalog = response.body();
                            if (catalog != null) {
                                callback.onSuccess(catalog);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Catalog> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void searchMovie(int page, String query, final OnGetCatalogsCallback callback){
        baseAPIService.searchCatalog(KEY_API, LANGUAGE, page, query)
                .enqueue(new Callback<CatalogResponse>() {
                    @Override
                    public void onResponse(Call<CatalogResponse> call, Response<CatalogResponse> response) {
                        if(response.isSuccessful()){

                            CatalogResponse catalogResponse = response.body();

                            if(catalogResponse != null && catalogResponse.getMovies() != null){
                                callback.onSuccess(catalogResponse.getPage(), catalogResponse.getMovies());
                            } else {
                                callback.onError();
                            }

                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<CatalogResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
    }

