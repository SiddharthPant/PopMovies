package com.example.sid.popmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieImageAdapter imageAdapter;

    private ArrayList<MovieImage> imageList;

    MovieImage[] movieImages = {
            new MovieImage(R.drawable.deadpool),
            new MovieImage(R.drawable.hunger_games),
            new MovieImage(R.drawable.interstellar),
            new MovieImage(R.drawable.mad_max),
            new MovieImage(R.drawable.zootopia),
            new MovieImage(R.drawable.dawn_of_justice),
            new MovieImage(R.drawable.insurgent)
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("images")) {
            imageList = new ArrayList<MovieImage>(Arrays.asList(movieImages));
        } else {
            imageList = savedInstanceState.getParcelableArrayList("images");
        }
    }

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("images", imageList);
        super.onSaveInstanceState(outState);
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
