package anthonyfdev.com.popmovies.discovery;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.common.TMDBNetworkHelper;
import anthonyfdev.com.popmovies.discovery.model.Trailer;

/**
 * @author Anthony Fermin (Fuzz)
 */

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailers;

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trailer,
                        parent,
                        false));
    }

    public void setTrailers(List<Trailer> trailers) {
        if (trailers != null) {
            this.trailers = trailers;
        } else {
            this.trailers = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return trailers != null ? trailers.size() : 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final ImageView ivThumbnail;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
        }

        public void bind(int position) {
            if (position >= 0 && position < trailers.size()) {
                final Trailer trailer = trailers.get(position);
                tvTitle.setText(trailer.getName());
                Picasso.with(itemView.getContext())
                        .load(TMDBNetworkHelper.buildYoutubeImageUrlString(trailer.getKey()))
                        .fit()
                        .into(ivThumbnail);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, TMDBNetworkHelper.buildYoutubeUrl(trailer.getKey()));
                        if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                            v.getContext().startActivity(intent);
                        }
                    }
                });
            }
        }

    }
}
