package com.pinschaneer.bertram.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinschaneer.bertram.popularmovies.data.MovieResultData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieAdapterViewHolder> {

    private final MovieListAdapterOnClickHandler mCLickHandler;
    private ArrayList<MovieResultData> mMovieDataList;

    public MovieListAdapter(MovieListAdapterOnClickHandler clickHandler) {
        mCLickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.moviedata_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        MovieResultData data = mMovieDataList.get(position);
        Picasso.get()
                .load(data.getPosterImageUrl())
                .into(holder.mMovieDataImage);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieDataList) return 0;
        return mMovieDataList.size();
    }

    public void setMovieData(ArrayList<MovieResultData> data) {
        if (mMovieDataList == null) {
            mMovieDataList = data;
        } else {
            mMovieDataList.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieListAdapterOnClickHandler {
        void onClick(MovieResultData movieData);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mMovieDataImage;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieDataImage = itemView.findViewById(R.id.iv_movie_posterImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MovieResultData data = mMovieDataList.get(position);
            mCLickHandler.onClick(data);

        }
    }
}
