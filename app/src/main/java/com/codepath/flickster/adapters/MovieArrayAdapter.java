package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vidhya on 9/12/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // View lookup Cache

    private static  class ViewHolder {
        ImageView ivMovieImage;
        TextView tvMovieTitle;
        TextView tvMovieOverview;
    }

    public MovieArrayAdapter(@NonNull Context context, @NonNull List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            // No view to reuse. So, inflate a new view
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
            viewHolder.ivMovieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.tvMovieTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvMovieOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvMovieTitle.setText(movie.getOriginalTitle());
        viewHolder.tvMovieOverview.setText(movie.getOverview());
        // Clears the image view
        viewHolder.ivMovieImage.setImageResource(0);
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(getContext()).load(movie.getPosterPath()).fit().placeholder(R.drawable.placeholder_potrait_light).into(viewHolder.ivMovieImage);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(getContext()).load(movie.getBackdropPath()).fit().placeholder(R.drawable.placeholder_land_light).into(viewHolder.ivMovieImage);
        }

        return convertView;
    }
}
