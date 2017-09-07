package anthonyfdev.com.popmovies.discovery;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.discovery.model.Review;

/**
 * @author Anthony Fermin ()
 */

public class ReviewsView extends FrameLayout {

    private RecyclerView rvReviews;
    private TextView tvEmptyText;
    private View progressBar;
    private ReviewAdapter reviewAdapter;

    public ReviewsView(@NonNull Context context) {
        super(context);
        init();
    }

    public ReviewsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReviewsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReviewsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_reviews, this, true);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        rvReviews = (RecyclerView) findViewById(R.id.rv_reviews);
        tvEmptyText = (TextView) findViewById(R.id.tv_emptyText);
        progressBar = findViewById(R.id.progress_bar);

        reviewAdapter = new ReviewAdapter();
        rvReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReviews.setAdapter(reviewAdapter);
    }

    public void setReviews(List<Review> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            reviewAdapter.setReviews(reviews);
            tvEmptyText.setVisibility(View.GONE);
            rvReviews.setVisibility(View.VISIBLE);
        } else {
            tvEmptyText.setVisibility(View.VISIBLE);
            rvReviews.setVisibility(View.GONE);
        }
    }

    public void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

}
