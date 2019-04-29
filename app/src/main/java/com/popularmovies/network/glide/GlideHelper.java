package com.popularmovies.network.glide;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

public abstract class GlideHelper {

    public static void loadImageIntoImageView(View view,
                                              final ImageView imageView,
                                              String imageURL,
                                              Integer errorImage) {
        RequestBuilder builder = Glide.with(view).load(imageURL);
        if (errorImage != null) {
            builder.error(errorImage);
        }
        builder.into(imageView);
    }
}
