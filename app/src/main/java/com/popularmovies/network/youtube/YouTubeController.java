package com.popularmovies.network.youtube;

import android.view.View;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.popularmovies.network.glide.GlideHelper;

import java.security.InvalidParameterException;

public class YouTubeController {

    //Replace the value with your YouTube api key
    private static final String YOUTUBE_API_KEY = "";

    private static final String THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/%s/0.jpg";

    private YouTubePlayer youTubePlayer;

    public void initializeYouTubeVideoPlayer(YouTubePlayerSupportFragment youTubePlayerSupportFragment,
                                             String trailerURL) {

        if (youTubePlayerSupportFragment == null) {
            throw new InvalidParameterException("Fragment should not be null.");
        }
        youTubePlayerSupportFragment.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        YouTubeController.this.youTubePlayer = youTubePlayer;
                        youTubePlayer.cueVideo(trailerURL);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        //Empty
                    }
                });
    }

    public YouTubePlayer getYouTubePlayer() {
        return youTubePlayer;
    }

    public static void loadMoviePoster(View view, final ImageView imageView, String videoKey) {
        GlideHelper.loadImageIntoImageView(view,
                imageView,
                String.format(THUMBNAIL_BASE_URL, videoKey),
                null);
    }
}
