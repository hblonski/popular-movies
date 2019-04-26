package com.popularmovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.popularmovies.data.entity.FavoriteMovie;
import com.popularmovies.data.viewmodel.FavoriteMovieViewModel;
import com.popularmovies.model.Movie;
import com.popularmovies.network.MovieController;
import com.popularmovies.util.LottieHelper;


public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    private FavoriteMovieViewModel favoriteMovieViewModel;

    private Movie movie;

    private FavoriteMovie favoriteMovie = null;

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
        favoriteMovieViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        MovieController.loadMoviePoster(fragmentView,
                (ImageView) fragmentView.findViewById(R.id.d_image_view_poster),
                movie.getPosterPath());
        ((TextView) fragmentView.findViewById(R.id.d_movie_title)).setText(movie.getTitle());
        ((TextView) fragmentView.findViewById(R.id.d_movie_overview)).setText(movie.getOverview());
        ((TextView) fragmentView.findViewById(R.id.d_movie_release_date)).setText(movie.getReleaseDate());
        ((TextView) fragmentView.findViewById(R.id.d_user_score)).setText(String.format("%s/10", movie.getVoteAverage().toString()));

        setupFavoriteButton(fragmentView);

        return fragmentView;
    }

    private void setupFavoriteButton(View fragmentView) {
        final LottieAnimationView animationView = fragmentView.findViewById(R.id.d_favorite_button);
        favoriteMovieViewModel.findByMovieId(movie.getId()).observe(MovieDetailsFragment.this, new Observer<FavoriteMovie>() {
            @Override
            public void onChanged(FavoriteMovie favoriteMovie) {
                MovieDetailsFragment.this.favoriteMovie = favoriteMovie;
                if (favoriteMovie != null) {
                    //Sets the initial state to "filled star" if the movie is already marked as favorite
                    animationView.setProgress(LottieHelper.PROGRESS_END);
                }
            }
        });

        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteMovie == null) {
                    favoriteMovie = new FavoriteMovie(movie.getId(), movie.getTitle());
                    favoriteMovieViewModel.insert(favoriteMovie);
                } else {
                    favoriteMovieViewModel.delete(favoriteMovie);
                }
                LottieHelper.startAnimation(animationView);
            }
        });
    }
}
