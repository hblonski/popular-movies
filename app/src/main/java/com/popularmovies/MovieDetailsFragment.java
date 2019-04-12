package com.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.model.Movie;
import com.popularmovies.network.MovieController;


public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    private Movie movie;

    /**
     * Use this factory method to createFragment a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie the {@link Movie}.
     * @return A new instance of fragment MovieDetailsFragment.
     */
    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        MovieController.loadMoviePoster((ImageView)
                fragmentView.findViewById(R.id.d_image_view_poster), movie.getPosterPath());
        ((TextView) fragmentView.findViewById(R.id.d_movie_title)).setText(movie.getTitle());
        ((TextView) fragmentView.findViewById(R.id.d_movie_overview)).setText(movie.getOverview());
        ((TextView) fragmentView.findViewById(R.id.d_movie_release_date)).setText(movie.getReleaseDate());
        ((TextView) fragmentView.findViewById(R.id.d_vote_average)).setText(movie.getVoteAverage().toString());

        return fragmentView;
    }
}
