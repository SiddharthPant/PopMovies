package com.example.sid.popmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieImageAdapter imageAdapter;

    MovieImage[] movieImages = {
            new MovieImage(R.drawable.deadpool),
            new MovieImage(R.drawable.hunger_games),
            new MovieImage(R.drawable.interstellar),
            new MovieImage(R.drawable.mad_max),
            new MovieImage(R.drawable.zootopia),
            new MovieImage(R.drawable.dawn_of_justice),
            new MovieImage(R.drawable.insurgent)
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        imageAdapter = new MovieImageAdapter(getActivity(), Arrays.asList(movieImages));

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_images_grid);
        gridView.setAdapter(imageAdapter);

        return rootView;
    }
}
