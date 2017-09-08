package anthonyfdev.com.popmovies.discovery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.discovery.model.Review;

/**
 * @author Anthony Fermin
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {


    private List<Review> reviews;

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size(): 0;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvBody;
        private final TextView tvAuthor;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvBody = (TextView) itemView.findViewById(R.id.tv_body);
        }

        public void bind(int position) {
            if (position >= 0 && position < reviews.size()) {
                Review review = reviews.get(position);
                tvAuthor.setText(review.getAuthor());
                tvBody.setText(review.getContent());
            } else {
                tvAuthor.setText("");
                tvBody.setText("");
            }
        }
    }

}
