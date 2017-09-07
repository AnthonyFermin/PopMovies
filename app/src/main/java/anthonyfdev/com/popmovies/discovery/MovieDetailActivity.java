package anthonyfdev.com.popmovies.discovery;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.common.BaseModelAsyncTask;
import anthonyfdev.com.popmovies.common.Constants;
import anthonyfdev.com.popmovies.common.TMDBNetworkHelper;
import anthonyfdev.com.popmovies.db.DeleteFavoriteAsyncTask;
import anthonyfdev.com.popmovies.db.InsertFavoriteAsyncTask;
import anthonyfdev.com.popmovies.discovery.model.Movie;
import anthonyfdev.com.popmovies.discovery.model.Review;
import anthonyfdev.com.popmovies.discovery.model.ReviewResponse;
import anthonyfdev.com.popmovies.discovery.model.Trailer;
import anthonyfdev.com.popmovies.discovery.model.TrailerResponse;

/**
 * @author Anthony Fermin ()
 */

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String ARG_MOVIE = TAG + ":argMovie";
    private static final String PATH_TRAILERS = "/movie/$s1/videos";
    private static final String PATH_REVIEWS = "/movie/$s1/reviews";
    private static final int VIEW_PAGER_COUNT = 2;
    private static final String SIS_KEY_INITIALLY_FAVORITED = TAG + ":initiallyFavorited";
    private static final String SIS_KEY_IS_FAVORITED = TAG + ":isFavorited";
    private static final String SIS_KEY_BOTTOM_SHEET_STATE = TAG + ":bottomSheetState";
    private static final String SIS_KEY_CURRENT_TRAILERS = TAG + ":currentTrailers";
    private static final String SIS_KEY_CURRENT_REVIEWS = TAG + ":currentReviews";
    private Movie movie;
    private TextView tvOverView;
    private TextView tvRating;
    private TextView tvReleaseDate;
    private ImageView ivPoster;
    private BaseModelAsyncTask<TrailerResponse> trailerAsyncTask;
    private BaseModelAsyncTask<ReviewResponse> reviewsAsyncTask;
    private InsertFavoriteAsyncTask insertFavoriteAsyncTask;
    private DeleteFavoriteAsyncTask deleteFavoriteAsyncTask;
    private TrailersView trailersView;
    private ViewPager viewPager;
    private ReviewsView reviewsView;
    private SharedPreferences favSharedPrefs;
    private boolean initiallyFavorited;
    private boolean isFavorited;
    private View bottomSheet;
    private int bottomSheetState;
    private ArrayList<Review> currentReviews;
    private ArrayList<Trailer> currentTrailers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getIntent().getParcelableExtra(ARG_MOVIE);
        favSharedPrefs = getSharedPreferences(Constants.SHARED_PREFS_FAVORITES, MODE_PRIVATE);
        if (savedInstanceState != null) {
            initiallyFavorited = savedInstanceState.getBoolean(SIS_KEY_INITIALLY_FAVORITED, isMovieFavorited());
            isFavorited = savedInstanceState.getBoolean(SIS_KEY_IS_FAVORITED, initiallyFavorited);
            bottomSheetState = savedInstanceState.getInt(SIS_KEY_BOTTOM_SHEET_STATE, BottomSheetBehavior.STATE_COLLAPSED);
            currentTrailers = savedInstanceState.getParcelableArrayList(SIS_KEY_CURRENT_TRAILERS);
            currentReviews = savedInstanceState.getParcelableArrayList(SIS_KEY_CURRENT_REVIEWS);
        } else {
            initiallyFavorited = isFavorited = isMovieFavorited();
            bottomSheetState = BottomSheetBehavior.STATE_COLLAPSED;
        }
        bindViews();
        setupViews();
        if (currentReviews != null) {
            ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse.setResults(currentReviews);
            reviewAsyncTaskListener.onPostExecute(reviewResponse);
        } else {
            fetchReviews();
        }
        if (currentTrailers != null) {
            TrailerResponse trailerResponse = new TrailerResponse();
            trailerResponse.setResults(currentTrailers);
            trailerAsyncTaskListener.onPostExecute(trailerResponse);
        } else {
            fetchTrailers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int colorId = isFavorited ? R.color.colorAccent : android.R.color.darker_gray;
        int color = ContextCompat.getColor(this, colorId);
        menu.findItem(R.id.action_favorite).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SIS_KEY_INITIALLY_FAVORITED, initiallyFavorited);
        outState.putBoolean(SIS_KEY_IS_FAVORITED, isFavorited);
        outState.putInt(SIS_KEY_BOTTOM_SHEET_STATE, bottomSheetState);
        outState.putParcelableArrayList(SIS_KEY_CURRENT_REVIEWS, currentReviews);
        outState.putParcelableArrayList(SIS_KEY_CURRENT_TRAILERS, currentTrailers);
    }

    private boolean isMovieFavorited() {
        return favSharedPrefs.getBoolean(movie.getId(), false);
    }

    private void favoriteMovie() {
        isFavorited = true;
        invalidateOptionsMenu();
    }

    private void unfavoriteMovie() {
        isFavorited = false;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favorite:
                toggleFavorite();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleFavorite() {
        if (isFavorited) {
            unfavoriteMovie();
        } else {
            favoriteMovie();
        }
    }

    private void fetchTrailers() {
        currentTrailers = null;
        trailerAsyncTask = new BaseModelAsyncTask<>(trailerAsyncTaskListener, TrailerResponse.class);
        URL endpoint = TMDBNetworkHelper.buildUrl(this, PATH_TRAILERS.replace("$s1", movie.getId()));
        trailerAsyncTask.execute(endpoint);
    }

    private void fetchReviews() {
        currentReviews = null;
        reviewsAsyncTask = new BaseModelAsyncTask<>(reviewAsyncTaskListener, ReviewResponse.class);
        URL endpoint = TMDBNetworkHelper.buildUrl(this, PATH_REVIEWS.replace("$s1", movie.getId()));
        reviewsAsyncTask.execute(endpoint);
    }

    private void setupViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(movie.getOriginalTitle());
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
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

        //Bottom sheet
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(bottomSheetState);
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        bottomSheetBehavior.setPeekHeight(
                getResources()
                        .getDimensionPixelSize(R.dimen.MovieDetail_BottomSheetPeekAmount));
    }

    private void bindViews() {
        tvOverView = (TextView) findViewById(R.id.tv_overview);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        bottomSheet = findViewById(R.id.bottom_sheet);
    }

    private void deleteMovieFromFavorites() {
        if (insertFavoriteAsyncTask != null) {
            insertFavoriteAsyncTask.cancel(true);
        }
        deleteFavoriteAsyncTask = new DeleteFavoriteAsyncTask(this, favSharedPrefs);
        deleteFavoriteAsyncTask.execute(movie.getId());
    }

    private void saveMovieToFavorites() {
        if (deleteFavoriteAsyncTask != null) {
            deleteFavoriteAsyncTask.cancel(true);
        }
        insertFavoriteAsyncTask = new InsertFavoriteAsyncTask(this, favSharedPrefs);
        insertFavoriteAsyncTask.execute(movie);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFavorited != initiallyFavorited) {
            if (isFavorited) {
                saveMovieToFavorites();
            } else {
                deleteMovieFromFavorites();
            }
        }
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

    private final BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED
                    || newState == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetState = newState;
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //no op
        }
    };

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
                currentTrailers = result.getResults();
                trailersView.setTrailers(currentTrailers);
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
                currentReviews = result.getResults();
                reviewsView.setReviews(currentReviews);
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
