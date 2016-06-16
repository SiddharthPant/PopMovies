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
public class MoviePosterAdapter extends ArrayAdapter<MovieDetail> {
    private Context context;
    private LayoutInflater inflater;
    private static final String LOG_TAG = "PopMovies" + MoviePosterAdapter.class.getSimpleName();
    private List<MovieDetail> movieDetails;

    public MoviePosterAdapter(Activity context, List<MovieDetail> movieDetails) {
        super(context, 0, movieDetails);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.movieDetails = movieDetails;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieDetail movieDetail = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.image_item, parent, false);
        }

        ImageView movieImageView = (ImageView) convertView.findViewById(R.id.movie_image);
//        Log.d(LOG_TAG,movieDetail.toString());
        Glide
            .with(context)
            .load(movieDetail.getPosterUrl())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(movieImageView);

        return convertView;
    }
}
