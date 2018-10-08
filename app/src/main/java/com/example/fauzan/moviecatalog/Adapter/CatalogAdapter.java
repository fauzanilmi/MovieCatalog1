package com.example.fauzan.moviecatalog.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fauzan.moviecatalog.API.OnCatalogsClickCallback;
import com.example.fauzan.moviecatalog.DataModel.Catalog;
import com.example.fauzan.moviecatalog.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CustomViewHolder>{

    private List<Catalog> catalogList;
    private OnCatalogsClickCallback callback;

    public CatalogAdapter (List<Catalog> catalogList, OnCatalogsClickCallback callback) {
        this.catalogList = catalogList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);

        return new CustomViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        customViewHolder.bind(catalogList.get(i));
    }

    public int getItemCount(){
        return catalogList.size();
    }

    public void appendMovies(List<Catalog> catalogsToAppend){
        catalogList.addAll(catalogsToAppend);
        notifyDataSetChanged();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView moviesTitle, moviesRelease;
        ImageView moviesImg;

        Catalog catalog;

        public CustomViewHolder(View view){
            super(view);

            moviesTitle = (TextView) view.findViewById(R.id.titleMovie);
            moviesRelease = (TextView) view.findViewById(R.id.releaseMovie);
            moviesImg = (ImageView) view.findViewById(R.id.ImgMovie);

            view.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View view) {
                 callback.onClick(catalog);
             }
            });

        }

        public void bind (Catalog catalog){
            moviesTitle.setText(catalog.getTitle());
            moviesRelease.setText(getStringFormatted(catalog.getReleaseDate()));
            Log.d("img","http://image.tmdb.org/t/p/w185" + catalog.getImgMovie());
            Glide.with(itemView)
                    .load("http://image.tmdb.org/t/p/w185" + catalog.getImgMovie())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(moviesImg);

            this.catalog = catalog;
        }

        public String getStringFormatted(String datestring){
            String format = "MM dd, yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            if(datestring.equalsIgnoreCase("")){
                return datestring;
            }
            return simpleDateFormat.format(new Date(datestring.replaceAll("-", "/")));
        }
    }
}
