package com.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by vidhya on 9/12/17.
 */

public class Movie {
    static String BACKDROP_URL = "https://image.tmdb.org/t/p/w1280/%s";
    static String POSTER_URL = "https://image.tmdb.org/t/p/w342/%s";

    int movieId;
    String originalTitle;
    String posterPath;
    String overview;
    String backdropPath;
    float voteAverage;
    //String youTubeVideoKey;


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return String.format(POSTER_URL, posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath() {
        return String.format(BACKDROP_URL, backdropPath);
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isPopularMovie() {
        return (this.getVoteAverage() > 5);
    }


    public Movie(JSONObject jsonObject) throws JSONException {
        this.movieId = jsonObject.getInt("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.voteAverage = BigDecimal.valueOf(jsonObject.getDouble("vote_average")).floatValue();
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray jArray) {
        ArrayList<Movie> resultList = new ArrayList<Movie>();
        for (int i = 0; i < jArray.length(); i++) {
            try {
                resultList.add(new Movie(jArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return resultList;

    }
}
