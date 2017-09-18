package com.codepath.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
       // getVideo(intent.getIntExtra(MovieActivity.MOVIE_ID, 550));
        getVideoUsingOkHTTP(intent.getIntExtra(MovieActivity.MOVIE_ID, 550));
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

    private String getVideoUsingOkHTTP(int movieId) {
        OkHttpClient client = new OkHttpClient();
        String videoUrl = String.format(getResources().getString(R.string.movie_videos_api), movieId);

        Request request = new Request.Builder().url(videoUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                VideoPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("ERROR", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONArray videoJsonArray = null;

                try {
                    String responseString = response.body().string();
                    JSONObject json = new JSONObject(responseString);
                    videoJsonArray = json.getJSONArray("results");
                    JSONObject videoJson = videoJsonArray.getJSONObject(0);
                    Video video = new Video(videoJson);
                    if (video.getSite().equalsIgnoreCase(YOUTUBE)) {
                        youTubeKey = video.getKey();
                    }
                    VideoPlayerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Get the video to be played on YouTubePlayerFragment
                            playOnFragment();
                        }
                    });

                    Log.d("DEBUG", videoJsonArray.toString());
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        });
     return youTubeKey;
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
