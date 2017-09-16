package com.codepath.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flickster.R;
import com.codepath.flickster.adapters.MovieArrayAdapter;
import com.codepath.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    private static final String LIST_STATE = "listState";
    public static final String POSITION = "POSITION";
    public static final String TITLE = "TITLE";
    public static final String IMAGE = "IMAGE_PATH";
    public static final String RATING = "RATING";
    public static final String SYNOPSIS = "SYNOPSIS";
    public static int REQUEST_CODE = 20;

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    Parcelable listState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.now_playing_text));

        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);
        setUpListener();
        getMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        listState =  lvItems.onSaveInstanceState();
        state.putParcelable(LIST_STATE, listState);
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        listState = state.getParcelable(LIST_STATE);
    }

    private void setUpListener() {
        // Listener to edit item on click
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
                Movie movie = (Movie) adapterView.getItemAtPosition(pos);
                if (movie != null) {
                    intent.putExtra(POSITION, pos);
                    intent.putExtra(TITLE, movie.getOriginalTitle());
                    intent.putExtra(IMAGE, movie.getBackdropPath());
                    intent.putExtra(RATING, movie.getVoteAverage() / 2);
                    intent.putExtra(SYNOPSIS, movie.getOverview());
                    startActivity(intent);
                }
            }
        });
    }

    private void getMovies() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(getResources().getString(R.string.now_playing_api), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonAnrray = null;

                try {
                    movieJsonAnrray = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJsonAnrray));
                    movieAdapter.notifyDataSetChanged();
                    // Set the scroll position of the listview from the saved state
                    // Doing it here and not on onResume() as we do not know when this async call would return
                    // There could be an instance when the list view is already set, but the results from the network call
                    // have not returned yet.
                    if (listState != null) {
                        lvItems.onRestoreInstanceState(listState);
                        listState = null;
                    }
                    Log.d("DEBUG", movieJsonAnrray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

}
