package com.popularmovies;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RecyclerViewClickListener;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.popularmovies.adapter.ReviewListAdapter;
import com.popularmovies.adapter.TrailerListAdapter;
import com.popularmovies.data.entity.FavoriteMovie;
import com.popularmovies.data.viewmodel.FavoriteMovieViewModel;
import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.Review;
import com.popularmovies.network.themoviedb.model.Video;
import com.popularmovies.network.themoviedb.MoviesController;
import com.popularmovies.network.youtube.YouTubeController;
import com.popularmovies.util.LottieHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MovieDetailsFragment extends Fragment implements RecyclerViewClickListener {

    private static final String ARG_MOVIE = "movie";

    //Number of review cards loaded initially and every time the user clicks the "load more" button
    private static final int REVIEWS_PAGE_SIZE = 3;

    private FavoriteMovieViewModel favoriteMovieViewModel;

    private Movie movie;

    private FavoriteMovie favoriteMovie = null;

    private View fragmentView;

    private List<String> youTubeVideoKeys;

    private YouTubeController youTubeController;

    private List<Review> loadedReviews;

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
        youTubeController = new YouTubeController();
        loadedReviews = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        setupTrailersKeyList();
        setupTrailersRecyclerView();
        setupMovieInfoViews();
        setupFavoriteButton();
        setupYouTubeVideoPlayer();
        setupReviewsRecyclerView();

        return fragmentView;
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
        RecyclerView trailersRecyclerView = fragmentView.findViewById(R.id.trailers_recycler_view);
        TrailerListAdapter trailerListAdapter = new TrailerListAdapter(youTubeVideoKeys, this);
        trailersRecyclerView.setAdapter(trailerListAdapter);
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL,false));
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
    }

    //Hides the "load more reviews" button if there are no more reviews to be loaded
    private void checkForMoreReviews() {
        if (movie.getReviews() != null && loadedReviews.size() == movie.getReviews().size()) {
            fragmentView.findViewById(R.id.d_load_more_reviews_button).setVisibility(View.GONE);
        }
    }

    private void setupYouTubeVideoPlayer() {
        String trailerURL = !youTubeVideoKeys.isEmpty() ? youTubeVideoKeys.get(0) : "";
        YouTubePlayerSupportFragment youTubePlayerSupportFragment;
        //Android Studio marks this cast as an error. It is not an error and it does work.
        //This occurs because Google hasn't migrated the YouTube API to AndroidX yet.
        youTubePlayerSupportFragment = (YouTubePlayerSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.youtube_player_fragment);
        youTubeController.initializeYouTubeVideoPlayer(youTubePlayerSupportFragment, trailerURL);
    }

    private void setupFavoriteButton() {
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
            }
            LottieHelper.startAnimation(animationView);
        });
    }

    @Override
    public void onItemClicked(int position) {
        if (youTubeController.getYouTubePlayer() != null) {
            youTubeController.getYouTubePlayer().cueVideo(youTubeVideoKeys.get(position));
        }
    }
}
