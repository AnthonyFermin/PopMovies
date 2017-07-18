package anthonyfdev.com.popmovies.discovery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.discovery.model.Movie;

/**
 * @author Anthony Fermin
 */

class MovieDiscoveryAdapter extends RecyclerView.Adapter<MovieDiscoveryAdapter.MovieDiscoveryViewHolder> {

    List<Movie> movieList = new ArrayList<>();

    public void setMovieList(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
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

    public static class MovieDiscoveryViewHolder extends RecyclerView.ViewHolder {

        public MovieDiscoveryViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(int pos) {
            // TODO: bind view
        }
    }
}
