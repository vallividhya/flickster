package com.codepath.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetail);

        TextView titleText = (TextView) findViewById(R.id.tvMovieTitle);
        TextView synopsisText = (TextView) findViewById(R.id.tvSynopsis);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ImageView imageView = (ImageView) findViewById(R.id.ivBackDrop);
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            titleText.setText(intent.getStringExtra(MovieActivity.TITLE));
            ratingBar.setRating(intent.getExtras().getFloat(MovieActivity.RATING, 5));
            synopsisText.setText(intent.getStringExtra(MovieActivity.SYNOPSIS));
            Picasso.with(getApplicationContext()).load(intent.getStringExtra(MovieActivity.IMAGE))
                    .fit().placeholder(R.drawable.placeholder_landscape)
                    .transform(new RoundedCornersTransformation(20, 20))
                    .into(imageView);

        }

    }
}
