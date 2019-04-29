package com.popularmovies.network.youtube;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.popularmovies.R;

import java.security.InvalidParameterException;

public abstract class YouTubeController {

    //Replace the value with your YouTube api key
    private static final String YOUTUBE_API_KEY = "";

    private static final String THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/%s/0.jpg";

    public static void initializeYouTubeVideoPlayer(YouTubePlayerSupportFragment youTubePlayerSupportFragment,
                                                    final String videoId) {
        if (youTubePlayerSupportFragment == null) {
            throw new InvalidParameterException("Fragment should not be null.");
        }
        youTubePlayerSupportFragment.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(videoId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        //Empty
                    }
                });
    }

    public static void loadMoviePoster(View view, final ImageView imageView, String videoKey) {
        Glide.with(view)
                .load(String.format(THUMBNAIL_BASE_URL, videoKey))
                .error(R.drawable.load_poster_error_image)
                .into(imageView);
    }
}
