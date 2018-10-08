package com.example.fauzan.moviecatalog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.fauzan.moviecatalog.API.CatalogRepository;
import com.example.fauzan.moviecatalog.API.OnCatalogsClickCallback;
import com.example.fauzan.moviecatalog.API.OnGetCatalogsCallback;
import com.example.fauzan.moviecatalog.Adapter.CatalogAdapter;
import com.example.fauzan.moviecatalog.DataModel.Catalog;

import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private CatalogAdapter catalogAdapter;
    private RecyclerView recyclerView;
    private CatalogRepository catalogRepository;
    private TextView result;
    private boolean isFetchingCatalog;
    private int atPage = 1;
    private String query;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_search);
        catalogRepository = CatalogRepository.getInstance();
        query = getIntent().getStringExtra("query");

        Toolbar actionToolbar = (Toolbar)findViewById(R.id.action_bar);
        if (actionToolbar != null){
            actionToolbar.setTitleTextColor(Color.WHITE);

            recyclerView = findViewById(R.id.searchRecycler_view);
            init();
        }
    }
    private void init(){
        result = findViewById(R.id.querySearch);
        result.setText(query);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                Log.d("total item count: ", String.valueOf(totalItemCount));

                int visibleItemCount = linearLayoutManager.getChildCount();
                Log.d("visible item count: ", String.valueOf(visibleItemCount));

                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                Log.d("fitst visible item: ", String.valueOf(firstVisibleItem));

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingCatalog) {
                        getMovies(atPage + 1);
                    }
                }
            }

        });
        getMovies(atPage);

    }

    private void getMovies(int page){
        isFetchingCatalog = true;
        catalogRepository.searchMovie(page, query, new OnGetCatalogsCallback() {
            @Override
            public void onSuccess(int page, List<Catalog> catalogs) {
                if (catalogAdapter == null){
                    catalogAdapter = new CatalogAdapter(catalogs, callback);
                    recyclerView.setAdapter(catalogAdapter);
                } else {
                    catalogAdapter.appendMovies(catalogs);
                }
                atPage = page;
                isFetchingCatalog = false;
            }

            @Override
            public void onError() {
                Toast.makeText(ResultActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    OnCatalogsClickCallback callback = new OnCatalogsClickCallback() {
        @Override
        public void onClick(Catalog catalog) {
            Intent intent =  new Intent(ResultActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.ID_MOVIE, catalog.getId());
            startActivity(intent);
        }
    };
}

