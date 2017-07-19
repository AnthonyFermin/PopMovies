package anthonyfdev.com.popmovies.discovery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.common.TMDBNetworkHelper;
import anthonyfdev.com.popmovies.discovery.model.Movie;

/**
 * @author Anthony Fermin
 */

class MovieDiscoveryAdapter extends RecyclerView.Adapter<MovieDiscoveryAdapter.MovieDiscoveryViewHolder> {

    private final List<Movie> movieList = new ArrayList<>();

    public void setMovieList(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public MovieDiscoveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_discovery, parent, false);
        return new MovieDiscoveryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieDiscoveryViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieDiscoveryViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPoster;

        MovieDiscoveryViewHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    if (context != null) {
                        Movie movie = movieList.get(getAdapterPosition());
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailActivity.ARG_MOVIE, movie);
                        context.startActivity(intent);
                    }
                }
            });
        }

        void bind(int pos) {
            if (pos < movieList.size() && pos >= 0) {
                Movie movie = movieList.get(pos);
                Picasso.with(itemView.getContext())
                        .load(TMDBNetworkHelper.BASE_IMAGE_URL + movie.getPosterThumbnail())
                        .fit()
                        .into(ivPoster);
            }
        }
    }
}
