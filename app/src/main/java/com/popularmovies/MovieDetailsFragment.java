package com.popularmovies;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.popularmovies.adapter.ReviewListAdapter;
import com.popularmovies.adapter.TrailerListAdapter;
import com.popularmovies.data.entity.FavoriteMovie;
import com.popularmovies.data.viewmodel.FavoriteMovieViewModel;
import com.popularmovies.network.themoviedb.controller.MoviesController;
import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.Review;
import com.popularmovies.network.themoviedb.model.Video;
import com.popularmovies.network.themoviedb.viewmodel.MoviesViewModel;
import com.popularmovies.util.LottieHelper;
import com.popularmovies.util.bus.EventBus;
import com.popularmovies.util.bus.MovieEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.disposables.Disposable;


public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    //Number of review cards loaded initially and every time the user clicks the "load more" button
    private static final int REVIEWS_PAGE_SIZE = 3;

    private FavoriteMovieViewModel favoriteMovieViewModel;

    private MoviesViewModel moviesViewModel;

    private Movie movie;

    private FavoriteMovie favoriteMovie = null;

    private View fragmentView;

    private List<String> youTubeVideoKeys = new ArrayList<>();

    private List<Review> loadedReviews;

    private Disposable eventBusSubscription;

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
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        loadedReviews = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        EventBus eventBus = EventBus.getInstance();
        eventBusSubscription = eventBus.getObservable().subscribe(m -> {
            if (m!= null && m.getMovieId() != null && m.getMovieId().equals(movie.getId())) {
                if (!m.isSuccess()) {
                    handleConnectionFailure();
                } else {
                    switch (m.getType()) {
                        case LOADED_VIDEOS: setupTrailersRecyclerView();
                        case LOADED_REVIEWS: setupReviewsRecyclerView();
                    }
                }
            }
        });
        moviesViewModel.fetchVideos(movie);
        moviesViewModel.fetchReviews(movie);
        setupMovieInfoViews();
        setupFavoriteButton(eventBus);

        return fragmentView;
    }

    @Override
    public void onDestroy() {
        if (eventBusSubscription != null) {
            eventBusSubscription.dispose();
        }
        super.onDestroy();
    }

    private void setupMovieInfoViews() {
        MoviesController.loadMoviePoster(fragmentView,
                fragmentView.findViewById(R.id.d_image_view_poster),
                movie.getPosterPath());
        ((TextView) fragmentView.findViewById(R.id.d_movie_title)).setText(movie.getTitle());
        ((TextView) fragmentView.findViewById(R.id.d_movie_overview)).setText(movie.getOverview());
        ((TextView) fragmentView.findViewById(R.id.d_movie_release_date)).setText(movie.getReleaseDate());
        ((TextView) fragmentView.findViewById(R.id.d_user_score)).setText(String.format("%s/10", movie.getVoteAverage().toString()));
    }

    private void setupTrailersKeyList() {
        List<Video> trailers = movie.getVideos();

        if (trailers != null && !trailers.isEmpty()) {
            youTubeVideoKeys = trailers
                    .stream()
                    .filter(v -> v.getSite().equals("YouTube") && v.getType().equals("Trailer"))
                    .map(Video::getKey)
                    .collect(Collectors.toList());
        }
    }

    private void setupTrailersRecyclerView() {
        setupTrailersKeyList();
        RecyclerView trailersRecyclerView = fragmentView.findViewById(R.id.d_trailers_recycler_view);
        TrailerListAdapter trailerListAdapter = new TrailerListAdapter(youTubeVideoKeys);
        trailersRecyclerView.setAdapter(trailerListAdapter);
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL,false));
        handleNoTrailersLabel();
    }

    private void handleNoTrailersLabel() {
        if (youTubeVideoKeys == null || youTubeVideoKeys.isEmpty()) {
            fragmentView.findViewById(R.id.d_label_no_trailers_found).setVisibility(View.VISIBLE);
        } else {
            fragmentView.findViewById(R.id.d_label_no_trailers_found).setVisibility(View.GONE);
        }
    }

    private void setupReviewsRecyclerView() {
        RecyclerView reviewsRecyclerView = fragmentView.findViewById(R.id.d_reviews_recycler_view);
        ReviewListAdapter reviewListAdapter = new ReviewListAdapter();
        reviewsRecyclerView.setAdapter(reviewListAdapter);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,false));
        loadMoreReviews(reviewsRecyclerView);

        CardView loadMoreButton = fragmentView.findViewById(R.id.d_load_more_reviews_button);
        loadMoreButton.setOnClickListener(l -> loadMoreReviews(reviewsRecyclerView));
    }

    private void loadMoreReviews(RecyclerView reviewsRecyclerView) {
        List<Review> movieReviews = movie.getReviews();
        if (movieReviews != null && movieReviews.size() != loadedReviews.size()) {
            int nextToLoad = loadedReviews.size();
            for (int i = 0; i < REVIEWS_PAGE_SIZE && nextToLoad < movieReviews.size(); i++, nextToLoad++) {
                loadedReviews.add(movieReviews.get(nextToLoad));
            }
            TransitionManager.beginDelayedTransition(reviewsRecyclerView);
            ((ReviewListAdapter) reviewsRecyclerView.getAdapter()).setReviewList(loadedReviews);
        }
        checkForMoreReviews();
        handleNoReviewsLabel();
    }

    //Hides the "load more reviews" button if there are no more reviews to be loaded
    private void checkForMoreReviews() {
        if (movie.getReviews() != null && loadedReviews.size() == movie.getReviews().size()) {
            fragmentView.findViewById(R.id.d_load_more_reviews_button).setVisibility(View.GONE);
        }
    }

    private void handleNoReviewsLabel() {
        if (loadedReviews == null || loadedReviews.isEmpty()) {
            fragmentView.findViewById(R.id.d_label_no_reviews_found).setVisibility(View.VISIBLE);
        } else {
            fragmentView.findViewById(R.id.d_label_no_reviews_found).setVisibility(View.GONE);
        }
    }

    private void handleConnectionFailure() {
        Toast.makeText(getContext(),
                R.string.connection_error_the_movie_db,
                Toast.LENGTH_LONG).show();
    }

    private void setupFavoriteButton(EventBus eventBus) {
        final LottieAnimationView animationView = fragmentView.findViewById(R.id.d_favorite_button);
        favoriteMovieViewModel.findByMovieId(movie.getId()).observe(MovieDetailsFragment.this, favoriteMovie -> {
            MovieDetailsFragment.this.favoriteMovie = favoriteMovie;
            if (favoriteMovie != null) {
                //Sets the initial state to "filled star" if the movie is already marked as favorite
                animationView.setProgress(LottieHelper.PROGRESS_END);
            }
        });

        animationView.setOnClickListener(v -> {
            if (favoriteMovie == null) {
                favoriteMovie = new FavoriteMovie(movie.getId(), movie.getTitle());
                favoriteMovieViewModel.insert(favoriteMovie);
            } else {
                favoriteMovieViewModel.delete(favoriteMovie);
                eventBus.publish(new MovieEvent(favoriteMovie.getMovieId(),
                        MovieEvent.Type.DELETED_FAVORITE,
                        true));
            }
            LottieHelper.startAnimation(animationView);
        });
    }
}
