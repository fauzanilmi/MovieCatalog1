package com.example.fauzan.moviecatalog.API;

import com.example.fauzan.moviecatalog.DataModel.Catalog;

import java.util.List;

public interface OnGetCatalogsCallback {
    void onSuccess(int page, List<Catalog> catalogs);

    void onError();
}
