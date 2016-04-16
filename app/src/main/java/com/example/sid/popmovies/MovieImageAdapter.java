package com.example.sid.popmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by sid on 19/3/16.
 */
public class MovieImageAdapter extends ArrayAdapter<MovieImage> {
    private static final String LOG_TAG = MovieImageAdapter.class.getSimpleName();

    public MovieImageAdapter(Activity context, List<MovieImage> movieImages) {
        super(context, 0, movieImages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieImage movieImage = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.image_item, parent, false);
        }

        ImageView movieImageView = (ImageView) convertView.findViewById(R.id.movie_image);
        Log.v(LOG_TAG,"url: " + movieImage.getImageUrl());
        if (movieImage.getImageUrl() == null)
            movieImageView.setImageResource(movieImage.image);
        else
            Glide
                .with(getContext())
                .load(movieImage.getImageUrl())
                .centerCrop()
                .into(movieImageView);

        return convertView;
    }
}
