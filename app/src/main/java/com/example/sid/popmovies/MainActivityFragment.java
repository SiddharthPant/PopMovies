package com.example.sid.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
public class MainActivityFragment extends Fragment {

    private MoviePosterAdapter moviePosterAdapter;

    private ArrayList<MovieDetail> movieDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movieDetails")) {
            movieDetails = new ArrayList<MovieDetail>();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        moviePosterAdapter = new MoviePosterAdapter(getActivity(), movieDetails);

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_images_grid);
        gridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                updateMovieData(page);
                return true;
            }
        });
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

    private void updateMovieData(int page) {
        FetchMovieDetailTask movieDetailTask = new FetchMovieDetailTask();
        movieDetailTask.execute(String.valueOf(page));
    }

    @Override
    public void onStart() {
        super.onStart();
        int firstPage = 1;
        updateMovieData(firstPage);
    }

    public class FetchMovieDetailTask extends AsyncTask<String, Void, List<MovieDetail>> {

        private final String LOG_TAG = FetchMovieDetailTask.class.getSimpleName();

        private List<MovieDetail> getMovieDetailFromJson(String tmdbJsonStr) throws JSONException {
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_ID = "id";
            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_TITLE = "title";
            final String TMDB_BACKDROP_PATH = "backdrop_path";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            JSONObject tmdbJson = new JSONObject(tmdbJsonStr);
            JSONArray tmdbResultsArray = tmdbJson.getJSONArray(TMDB_RESULTS);
            List<MovieDetail> movieDetailList = new ArrayList<>();
            String[] tmdbResults = new String[tmdbResultsArray.length()];
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
            String sortMethod = "popularity.desc";

            try {
                final String TMDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORTBY_PARAM = "sort_by";
                final String PAGE_PARAM = "page";
                final String APIKEY_PARAM = "api_key";
                Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTBY_PARAM, sortMethod)
                        .appendQueryParameter(PAGE_PARAM, params[0])
                        .appendQueryParameter(APIKEY_PARAM, BuildConfig.THE_MOVIEDB_API_KEY).build();
                URL url = new URL(builtUri.toString());
//                Log.v(LOG_TAG, "TMDB URL: " + url);

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
//                Log.v(LOG_TAG, "TMDB JSON: " + tmdbJsonStr);
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
