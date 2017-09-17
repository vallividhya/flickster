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

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by vidhya on 9/12/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // View lookup Cache
    List<Movie> moviesList;
    private static  class ViewHolder {
        ImageView ivMovieImage;
        TextView tvMovieTitle;
        TextView tvMovieOverview;
    }

    private static class PopularMovieViewHolder {
        ImageView ivPopularMovieImage;
    }

    public MovieArrayAdapter(@NonNull Context context, @NonNull List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
        moviesList = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (movie.isPopularMovie()) {
                // Portrait mode showing popular movie (only backdrop image)
                PopularMovieViewHolder popularMovieViewHolder;
                if (convertView == null) {
                    popularMovieViewHolder = new PopularMovieViewHolder();
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_popular_movie, parent, false);
                    popularMovieViewHolder.ivPopularMovieImage = (ImageView) convertView.findViewById(R.id.ivPopularMovie);
                    convertView.setTag(popularMovieViewHolder);
                } else {
                    popularMovieViewHolder = (PopularMovieViewHolder) convertView.getTag();
                }
                popularMovieViewHolder.ivPopularMovieImage.setImageResource(0);
                Picasso.with(getContext()).load(movie.getBackdropPath())
                        .fit().placeholder(R.drawable.placeholder_land_light)
                        .transform(new RoundedCornersTransformation(20, 20))
                        .into(popularMovieViewHolder.ivPopularMovieImage);
            } else {
                // Portrait mode showing less popular movie (poster + title + summary)
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
                Picasso.with(getContext()).load(movie.getPosterPath())
                        .fit().placeholder(R.drawable.placeholder_potrait_light)
                        .transform(new RoundedCornersTransformation(20, 20))
                        .into(viewHolder.ivMovieImage);
            }
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
            Picasso.with(getContext()).load(movie.getBackdropPath())
                    .fit().placeholder(R.drawable.placeholder_land_light)
                    .transform(new RoundedCornersTransformation(20, 20))
                    .into(viewHolder.ivMovieImage);
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return moviesList.get(position).isPopularMovie() ? 0 : 1;
    }
}
