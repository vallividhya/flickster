package com.codepath.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Video;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final String YOUTUBE = "YouTube";
    private static final String YOUTUBE_API_KEY = "AIzaSyDzM0tYqCSwy2PAWdVgrlhgZ1WPOCBrDjM";
    String youTubeKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        // Receive Intent and get movie Id from the main activity
        Intent intent = getIntent();
        // Get video key from the API
        getVideo(intent.getIntExtra(MovieActivity.MOVIE_ID, 550));
    }

    private void playOnFragment() {
        YouTubePlayerFragment youTubeFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeFragment);
        youTubeFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(youTubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("DEBUG", "Failed to get video");
            }
        });
    }

    private String getVideo(int movieId) {
        String videoUrl = String.format(getResources().getString(R.string.movie_videos_api), movieId);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(videoUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray videoJsonArray = null;

                try {
                    videoJsonArray = response.getJSONArray("results");
                    JSONObject videoJson = videoJsonArray.getJSONObject(0);
                    Video video = new Video(videoJson);
                    if (video.getSite().equalsIgnoreCase(YOUTUBE)) {
                        youTubeKey = video.getKey();
                    }
                    // Get the video to be played on YouTubePlayerFragment
                    playOnFragment();

                    Log.d("DEBUG", videoJsonArray.toString());
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


        return youTubeKey;
    }

}
