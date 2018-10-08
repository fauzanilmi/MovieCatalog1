package com.example.fauzan.moviecatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fauzan.moviecatalog.API.CatalogRepository;
import com.example.fauzan.moviecatalog.API.OnGetCatalogCallback;
import com.example.fauzan.moviecatalog.DataModel.Catalog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    public static String ID_MOVIE = "movie_id";
    private ImageView imgMovie;
    private RatingBar movieRating;
    private TextView synopsis, movieTitle, movieRelease;

    private CatalogRepository catalogRepository;
    private int movieId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieId = getIntent().getIntExtra(ID_MOVIE, movieId);

        catalogRepository = CatalogRepository.getInstance();
        init();
    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        imgMovie = findViewById(R.id.detailImg);
        movieRating = findViewById(R.id.ratingBar);
        synopsis = findViewById(R.id.synopsisDetaiil);
        movieTitle = findViewById(R.id.titleDetailMovie);
        movieRelease = findViewById(R.id.movieRelease);

        getOneMovie();
    }

    private void getOneMovie(){
        catalogRepository.getCatalog(movieId, new OnGetCatalogCallback() {
            @Override
            public void onSuccess(Catalog catalog) {


                movieRelease.setText(getStringFormatted(catalog.getReleaseDate()));
                movieTitle.setText(catalog.getTitle());
                movieRating.setVisibility(View.VISIBLE);
                movieRating.setRating(catalog.getVoteAverage()/2);
                synopsis.setText(catalog.getOverview());
                Glide.with(DetailActivity.this)
                        .load( "http://image.tmdb.org/t/p/w185" +catalog.getImgMovie())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgMovie);

            }

            @Override
            public void onError() {
                showError();
                finish();
            }
        });
    }

    private void showError() {
        Toast.makeText(DetailActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

    public String getStringFormatted(String datestring) {
        String format = "MMM dd, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(new Date(datestring.replaceAll("-", "/")));
    }

    private void showTrailer(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
