package com.example.fauzan.moviecatalog.API;

import com.example.fauzan.moviecatalog.DataModel.Catalog;

public interface OnGetCatalogCallback {
    void onSuccess(Catalog catalog);

    void onError();
}
