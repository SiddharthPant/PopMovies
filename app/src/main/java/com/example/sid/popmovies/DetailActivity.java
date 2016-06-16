package com.example.sid.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by sid on 18/4/16.
 */
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            MovieDetail movieDetail = null;

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("movieDetailObj")) {
                movieDetail = intent.getParcelableExtra("movieDetailObj");
            }

            if (movieDetail != null) {
                getActivity().setTitle(movieDetail.getOriginalTitle());

                Glide
                        .with(getActivity())
                        .load(movieDetail.getBackdropUrl())
                        .placeholder(R.drawable.placeholder)
                        .into((ImageView) rootView.findViewById(R.id.imageView));
                ((TextView) rootView.findViewById(R.id.releaseDateText))
                        .setText(movieDetail.getReleaseDate().toString());
//                ((TextView) rootView.findViewById(R.id.runtimeText))
//                        .setText(movieDetail.getReleaseDate().toString());
                ((TextView) rootView.findViewById(R.id.userRatingText))
                        .setText(movieDetail.getVoteAverage() + "/10");
                ((TextView) rootView.findViewById(R.id.synopsisText))
                        .setText(movieDetail.getOverview());
            }
            return rootView;
        }
    }
}