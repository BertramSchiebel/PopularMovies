package com.pinschaneer.bertram.popularmovies.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinschaneer.bertram.popularmovies.R;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.DetailedReviewListAdapterViewHolder>
{
    private ArrayList<ReviewEntry> reviewEntries;

    public void setReviewEntries(ArrayList<ReviewEntry> reviewEntries) {
        this.reviewEntries = reviewEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewListAdapter.DetailedReviewListAdapterViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new DetailedReviewListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ReviewListAdapter.DetailedReviewListAdapterViewHolder holder, int position) {
        ReviewEntry reviewEntry = reviewEntries.get(position);
        holder.authorTextView.setText(reviewEntry.getAuthor());
        holder.contentTextView.setText(reviewEntry.getContent());
    }

    @Override
    public int getItemCount() {
        if (reviewEntries == null) {
            return 0;
        }
        return reviewEntries.size();
    }

    class DetailedReviewListAdapterViewHolder extends RecyclerView.ViewHolder
    {
        final TextView authorTextView;
        final TextView contentTextView;

        private DetailedReviewListAdapterViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.tv_review_author);
            contentTextView = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
