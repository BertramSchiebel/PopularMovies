package com.pinschaneer.bertram.popularmovies.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinschaneer.bertram.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for the recycler view
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieAdapterViewHolder>
{

    private final MovieListAdapterOnClickHandler mCLickHandler;

    public void setMovieDataList(List<MovieDataEntry> mMovieDataList) {
        this.mMovieDataList = mMovieDataList;
    }

    private List<MovieDataEntry> mMovieDataList;


    /**
     * Constructor
     *
     * @param clickHandler the event handler for a click on item in the recycler view
     */
    public MovieListAdapter(MovieListAdapterOnClickHandler clickHandler) {
        mCLickHandler = clickHandler;
    }

    /**
     * Creates a view holder for the adapter
     *
     * @param parent   the parent of the view holder
     * @param viewType the type of the view
     * @return A viewholder for the adapter
     */
    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.moviedata_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * Binds the data of list element to the items in the view holder
     *
     * @param holder   the view holder
     * @param position the position of the elment in the list of the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        MovieDataEntry data = mMovieDataList.get(position);
        Picasso.get()
                .load(data.getPosterImageUrl())
                .placeholder(R.drawable.default_poster)
                .error(R.drawable.error_poster)
                .into(holder.mMovieDataImage);

    }

    /**
     * Gets the number of elements in the list of the adapter
     * @return the count of the elements
     */
    @Override
    public int getItemCount() {
        if (null == mMovieDataList) return 0;
        return mMovieDataList.size();
    }


//    /**
//     * Sets the data in the list of the adapter
//     * @param data the new data
//     */
//    public void addMovieData(List<MovieDataEntry> data) {
//        if (mMovieDataList == null) {
//            mMovieDataList = data;
//        } else {
//            mMovieDataList.addAll(data);
//        }
//        notifyDataSetChanged();
//    }


//    /**
//     *   clear the list of the adapter
//     */
//    public void clearMovieData() {
//        if (mMovieDataList == null) {
//            return;
//        }
//        mMovieDataList.clear();
//        notifyDataSetChanged();
//    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieListAdapterOnClickHandler {
        void onClick(MovieDataEntry movieData);
    }


    /**
     * the class for the  view holder of the adapter
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mMovieDataImage;

        /**
         * the constructor for the view holder
         * @param itemView the view for teh view holder
         */
        private MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieDataImage = itemView.findViewById(R.id.iv_movie_posterImage);
            itemView.setOnClickListener(this);
        }

        /**
         * the event handler for a click
         * @param view the clicked view
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MovieDataEntry data = mMovieDataList.get(position);
            mCLickHandler.onClick(data);
        }
    }
}
