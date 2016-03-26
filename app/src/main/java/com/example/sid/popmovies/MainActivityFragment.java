package com.example.sid.popmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private void updateMovieData() {
        FetchMovieDetailTask movieDetailTask = new FetchMovieDetailTask();
        movieDetailTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieData();
    }

    public class FetchMovieDetailTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMovieDetailTask.class.getSimpleName();

        private String[] getMovieImageFromJson(String tmdbJsonStr) throws JSONException {
            // TODO
            return null;
        }

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String tmdbJsonStr = null;
            String sortMethod = "popularity.desc";

            try {
                final String TMDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORTBY_PARAM = "sort_by";
                final String APIKEY_PARAM = "api_key";
                Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTBY_PARAM, sortMethod)
                        .appendQueryParameter(APIKEY_PARAM, BuildConfig.THE_MOVIEDB_API_KEY).build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "TMDB URL: " + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ( (line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                tmdbJsonStr = buffer.toString();
                Log.v(LOG_TAG, "TMDB JSON: " + tmdbJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieImageFromJson(tmdbJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
    }
}
