package com.popularmovies.adapter;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.popularmovies.R;
import com.popularmovies.network.themoviedb.model.Review;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    private RecyclerView recyclerView;

    public ReviewListAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_review, parent, false);
        return new ReviewListAdapter.ReviewViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.authorTextView.setText(review.getAuthor());
        holder.contentTextView.setText(review.getContent());
        holder.itemView.setOnClickListener(v -> {
            holder.itemView.getRootView();
            TransitionManager.beginDelayedTransition(recyclerView);
            if (!holder.expanded) {
                holder.contentTextView.setMaxLines(Integer.MAX_VALUE);
                holder.expanded = true;
            } else {
                holder.contentTextView.setMaxLines(2);
                holder.expanded = false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        boolean expanded = false;

        TextView authorTextView;

        TextView contentTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.review_author);
            contentTextView = itemView.findViewById(R.id.review_content);
        }
    }
}
