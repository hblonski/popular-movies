package com.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.popularmovies.MainActivity;
import com.popularmovies.MovieDetailsFragment;
import com.popularmovies.R;
import com.popularmovies.model.Movie;
import com.popularmovies.network.themoviedb.MoviesController;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends BaseAdapter {

    private List<Movie> movieList;

    private Context context;

    public MovieListAdapter(Context context) {
        this.context = context;
        this.movieList = new ArrayList<>();
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movieList != null ? movieList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.movieList != null ? this.movieList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false);
        }
        ImageView imageViewPoster = convertView.findViewById(R.id.image_view_poster);
        final Movie viewMovie = movieList.get(position);
        MoviesController.loadMoviePoster(convertView, imageViewPoster, viewMovie.getPosterPath());
        imageViewPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).createFragment(MovieDetailsFragment.newInstance(viewMovie));
            }
        });
        return convertView;
    }
}
