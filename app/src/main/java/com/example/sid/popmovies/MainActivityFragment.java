package com.example.sid.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final String LOG_TAG = "PopMovies|" + MainActivityFragment.class.getSimpleName();

    private MoviePosterAdapter moviePosterAdapter;
    private EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
        @Override
        public boolean onLoadMore(int page, int totalItemCount) {
            updateMovieData(page);
            return true;
        }
    };

    private ArrayList<MovieDetail> movieDetails;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movieDetails")) {
            movieDetails = new ArrayList<>();
        } else {
            movieDetails = savedInstanceState.getParcelableArrayList("movieDetails");
        }
    }

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieDetails", movieDetails);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        moviePosterAdapter.clear();
        if (key.equals(R.string.pref_sort_key)) {
            updateMovieData(1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        moviePosterAdapter = new MoviePosterAdapter(getActivity(), movieDetails);

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_images_grid);
        gridView.setOnScrollListener(endlessScrollListener);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetail movieDetail = moviePosterAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("movieDetailObj", movieDetail);
                startActivity(intent);
            }
        });
        gridView.setAdapter(moviePosterAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume Called!");
    }

    private void updateMovieData(int page) {
        int currentPage = endlessScrollListener.getCurrentPage();
        int count = moviePosterAdapter.getCount();
        final int ITEMS_PER_PAGE = 20;
        Log.d(LOG_TAG, "updateMovieData called, count:" + count + " currPg: " +
            currentPage * ITEMS_PER_PAGE);
        if (count < currentPage * ITEMS_PER_PAGE) {
            FetchMovieDetailTask movieDetailTask = new FetchMovieDetailTask();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String sortBy = prefs.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_values_popularity));
            movieDetailTask.execute(String.valueOf(page), sortBy);
            endlessScrollListener.incrementPreviousPage();
        }
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "onStart Called, loading pg " + endlessScrollListener.getCurrentPage());
        super.onStart();
        updateMovieData(endlessScrollListener.getCurrentPage());
    }



    public class FetchMovieDetailTask extends AsyncTask<String, Void, List<MovieDetail>> {

        private final String LOG_TAG = "PopMovies|" + FetchMovieDetailTask.class.getSimpleName();

        private List<MovieDetail> getMovieDetailFromJson(String tmdbJsonStr) throws JSONException {
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_ID = "id";
            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_BACKDROP_PATH = "backdrop_path";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            JSONObject tmdbJson = new JSONObject(tmdbJsonStr);
            JSONArray tmdbResultsArray = tmdbJson.getJSONArray(TMDB_RESULTS);
            List<MovieDetail> movieDetailList = new ArrayList<>();
//            Log.d(LOG_TAG, String.valueOf(tmdbResultsArray.length()));
            for (int i = 0; i < tmdbResultsArray.length(); i++) {
                JSONObject tmdbResultsObject = tmdbResultsArray.getJSONObject(i);
                movieDetailList.add(new MovieDetail(
                        tmdbResultsObject.getInt(TMDB_ID),
                        tmdbResultsObject.getString(TMDB_POSTER_PATH),
                        tmdbResultsObject.getString(TMDB_ORIGINAL_TITLE),
                        tmdbResultsObject.getString(TMDB_OVERVIEW),
                        tmdbResultsObject.getDouble(TMDB_VOTE_AVERAGE),
                        tmdbResultsObject.getString(TMDB_RELEASE_DATE),
                        tmdbResultsObject.getString(TMDB_BACKDROP_PATH)
                    )
                );

            }
            return movieDetailList;
        }

        @Override
        protected List<MovieDetail> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String tmdbJsonStr = null;
            Log.d(LOG_TAG,params[0] + " " + params[1]);

            try {
                final String TMDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORTBY_PARAM = "sort_by";
                final String PAGE_PARAM = "page";
                final String APIKEY_PARAM = "api_key";
                Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTBY_PARAM, params[1])
                        .appendQueryParameter(PAGE_PARAM, params[0])
                        .appendQueryParameter(APIKEY_PARAM, BuildConfig.THE_MOVIEDB_API_KEY).build();
                URL url = new URL(builtUri.toString());
//                Log.d(LOG_TAG, "TMDB URL: " + url);

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
//                Log.d(LOG_TAG, "TMDB JSON: " + tmdbJsonStr);
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
                return getMovieDetailFromJson(tmdbJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieDetail> movieDetailList) {

            if (movieDetailList != null) {
                moviePosterAdapter.addAll(movieDetailList);
            }
        }
    }
}
