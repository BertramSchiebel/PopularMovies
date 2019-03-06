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

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.DetailedVideoListAdapterViewHolder>
{
    private ArrayList<TrailerEntry> trailerEntries;

    @NonNull
    @Override
    public DetailedVideoListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new DetailedVideoListAdapterViewHolder(view);

    }

    public void setTrailerEntries(ArrayList<TrailerEntry> trailerEntries) {
        this.trailerEntries = trailerEntries;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedVideoListAdapterViewHolder holder, int position) {
        TrailerEntry trailer = trailerEntries.get(position);
        holder.titelTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if (trailerEntries == null) {
            return 0;
        }
        return trailerEntries.size();
    }

    public class DetailedVideoListAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView titelTextView;

        public DetailedVideoListAdapterViewHolder(View itemView) {
            super(itemView);
            titelTextView = itemView.findViewById(R.id.list_movieTitel);
        }
    }
}
