package com.example.fauzan.moviecatalog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.fauzan.moviecatalog.API.CatalogRepository;
import com.example.fauzan.moviecatalog.API.OnCatalogsClickCallback;
import com.example.fauzan.moviecatalog.API.OnGetCatalogsCallback;
import com.example.fauzan.moviecatalog.Adapter.CatalogAdapter;
import com.example.fauzan.moviecatalog.DataModel.Catalog;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CatalogAdapter catalogAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private CatalogRepository catalogRepository;
    private SearchView searchButton;
    private boolean isFetchingMovies;
    private int atPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catalogRepository = CatalogRepository.getInstance();
        Toolbar actionBarToolbar = (Toolbar)findViewById(R.id.action_bar);
        if(actionBarToolbar != null)
            actionBarToolbar.setTitleTextColor(Color.WHITE);

        init();
    }


    private void init() {

        searchButton = findViewById(R.id.searchView);
        searchButton.setQueryHint("Search Movie");

        searchButton.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(atPage + 1);
                        }
            }}}
        );
        getMovies(atPage);


    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        catalogRepository.getCatalogs(page, new OnGetCatalogsCallback() {
            @Override
            public void onSuccess(int page, List<Catalog> catalogs) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if(catalogAdapter == null){
                    catalogAdapter = new CatalogAdapter(catalogs, callback);
                    recyclerView.setAdapter(catalogAdapter);
                } else {
                    catalogAdapter.appendMovies(catalogs);
                }
                atPage = page;
                isFetchingMovies = false;

            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    OnCatalogsClickCallback callback = new OnCatalogsClickCallback() {
        @Override
        public void onClick(Catalog catalog) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.ID_MOVIE, catalog.getId());
            startActivity(intent);
        }

    };


}



