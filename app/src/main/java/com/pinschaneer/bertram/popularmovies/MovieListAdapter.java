package com.pinschaneer.bertram.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinschaneer.bertram.popularmovies.data.MovieResultData;

class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieAdapterViewHolder> {

    private final MovieListAdapterOnClickHandler mCLickHandler;
    private MovieResultData[] mMovieDataList;

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
        MovieResultData data = mMovieDataList[position];
        holder.mMovieDataTextView.setText(data.toString());
    }

    @Override
    public int getItemCount() {
        if (null == mMovieDataList) return 0;
        return mMovieDataList.length;
    }

    public void setMovieData(MovieResultData[] data) {
        mMovieDataList = data;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieListAdapterOnClickHandler {
        void onClick(MovieResultData movieData);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mMovieDataTextView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieDataTextView = itemView.findViewById(R.id.tv_movie_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MovieResultData data = mMovieDataList[position];
            mCLickHandler.onClick(data);

        }
    }
}
