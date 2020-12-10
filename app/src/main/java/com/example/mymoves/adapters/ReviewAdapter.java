package com.example.mymoves.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoves.R;
import com.example.mymoves.data.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolde> {

    private ArrayList<Review> reviews;

    @NonNull
    @Override
    public ReviewViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item,parent,false);
        return new ReviewViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolde holder, int position) {
        Review review = reviews.get(position);
        holder.content.setText(review.getContent());
        holder.author.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolde extends RecyclerView.ViewHolder{

        private TextView author;
        private TextView content;

        public ReviewViewHolde(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.texxtViewAuthor);
            content = itemView.findViewById(R.id.texxtViewContent);
        }
    }
}
