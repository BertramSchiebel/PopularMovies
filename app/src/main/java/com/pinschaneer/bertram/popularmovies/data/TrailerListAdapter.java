package com.pinschaneer.bertram.popularmovies.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinschaneer.bertram.popularmovies.R;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.DetailedVideoListAdapterViewHolder>
{
    private ArrayList<TrailerEntry> trailerEntries;
    private final TrailerListAdapterOnClickHandler clickHandler;

    public TrailerListAdapter(TrailerListAdapterOnClickHandler onClickHandler) {
        clickHandler = onClickHandler;
    }

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


    public interface TrailerListAdapterOnClickHandler
    {
        void onClick(TrailerEntry trailerData);
    }

    public class DetailedVideoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final TextView titelTextView;

        DetailedVideoListAdapterViewHolder(View itemView) {
            super(itemView);
            titelTextView = itemView.findViewById(R.id.list_movieTitel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TrailerEntry tralerData = trailerEntries.get(position);
            clickHandler.onClick(tralerData);
        }
    }
}
