package anthonyfdev.com.popmovies.discovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.common.BaseModelAsyncTask;
import anthonyfdev.com.popmovies.common.TMDBNetworkHelper;
import anthonyfdev.com.popmovies.discovery.model.Movie;
import anthonyfdev.com.popmovies.discovery.model.ReviewResponse;
import anthonyfdev.com.popmovies.discovery.model.TrailerResponse;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String ARG_MOVIE = TAG + ":argMovie";
    private static final String PATH_TRAILERS = "/movie/$s1/videos";
    private static final String PATH_REVIEWS = "/movie/$s1/reviews";
    private static final int VIEW_PAGER_COUNT = 2;
    private Movie movie;
    private TextView tvOverView;
    private TextView tvRating;
    private TextView tvReleaseDate;
    private ImageView ivPoster;
    private BaseModelAsyncTask<TrailerResponse> trailerAsyncTask;
    private BaseModelAsyncTask<ReviewResponse> reviewsAsyncTask;
    private TrailersView trailersView;
    private ViewPager viewPager;
    private ReviewsView reviewsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getIntent().getParcelableExtra(ARG_MOVIE);
        bindViews();
        setupViews();
        fetchTrailers();
        fetchReviews();
    }

    private void fetchTrailers() {
        trailerAsyncTask = new BaseModelAsyncTask<>(trailerAsyncTaskListener, TrailerResponse.class);
        URL endpoint = TMDBNetworkHelper.buildUrl(this, PATH_TRAILERS.replace("$s1", movie.getId()));
        trailerAsyncTask.execute(endpoint);
    }

    private void fetchReviews() {
        reviewsAsyncTask = new BaseModelAsyncTask<>(reviewAsyncTaskListener, ReviewResponse.class);
        URL endpoint = TMDBNetworkHelper.buildUrl(this, PATH_REVIEWS.replace("$s1", movie.getId()));
        reviewsAsyncTask.execute(endpoint);
    }

    private void setupViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(movie.getOriginalTitle());
        }
        tvReleaseDate.setText(movie.getReleaseDate());
        String ratingString = getString(R.string.MovieDetail_OutOfTen, String.valueOf(movie.getVoteAverage()));
        tvRating.setText(ratingString);
        tvOverView.setText(movie.getOverview());
        Picasso.with(this)
                .load(TMDBNetworkHelper.BASE_IMAGE_URL + movie.getPosterThumbnail())
                .fit()
                .into(ivPoster);
        trailersView = new TrailersView(this);
        reviewsView = new ReviewsView(this);
        viewPager.setAdapter(new ViewPagerAdapter());
    }

    private void bindViews() {
        tvOverView = (TextView) findViewById(R.id.tv_overview);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (trailerAsyncTask != null) {
            trailerAsyncTask.cancel(true);
            trailerAsyncTask = null;
        }
        if (reviewsAsyncTask != null) {
            reviewsAsyncTask.cancel(true);
            reviewsAsyncTask = null;
        }
    }

    private final BaseModelAsyncTask.AsyncTaskListener<TrailerResponse> trailerAsyncTaskListener = new BaseModelAsyncTask.AsyncTaskListener<TrailerResponse>() {
        @Override
        public void onPreExecute() {
            trailersView.setLoading(true);
        }

        @Override
        public void onPostExecute(TrailerResponse result) {
            if (result != null
                    && result.getResults() != null
                    && !result.getResults().isEmpty()) {
                trailersView.setTrailers(result.getResults());
            }
            trailersView.setLoading(false);
        }
    };

    private final BaseModelAsyncTask.AsyncTaskListener<ReviewResponse> reviewAsyncTaskListener = new BaseModelAsyncTask.AsyncTaskListener<ReviewResponse>() {
        @Override
        public void onPreExecute() {
            reviewsView.setLoading(true);
        }

        @Override
        public void onPostExecute(ReviewResponse result) {
            if (result != null
                    && result.getResults() != null
                    && !result.getResults().isEmpty()) {
                reviewsView.setReviews(result.getResults());
            }
            reviewsView.setLoading(false);
        }
    };

    public class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return VIEW_PAGER_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? getString(R.string.MovieDetail_TrailersTitle) : getString(R.string.MovieDetail_ReviewsTitle);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = position == 0 ? trailersView : reviewsView;
            container.addView(view);
            return view;
        }
    }
}
