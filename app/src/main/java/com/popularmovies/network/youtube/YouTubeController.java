package com.popularmovies.network.youtube;

import android.view.View;
import android.widget.ImageView;

import com.popularmovies.network.glide.GlideHelper;

public abstract class YouTubeController {

    private static final String THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/%s/0.jpg";

    public static final String BASE_URL = "http://www.youtube.com/watch?v=";

    public static final String YOUTUBE_APP_URI = "vnd.youtube:";

    public static void loadVideoThumbnail(View view, final ImageView imageView, String videoKey) {
        GlideHelper.loadImageIntoImageView(view,
                imageView,
                String.format(THUMBNAIL_BASE_URL, videoKey),
                null);
    }
}
