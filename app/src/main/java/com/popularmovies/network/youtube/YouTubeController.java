package com.popularmovies.network.youtube;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.security.InvalidParameterException;

public abstract class YouTubeController {

    //Replace the value with your YouTube api key
    private static final String YOUTUBE_API_KEY = "";

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
}
