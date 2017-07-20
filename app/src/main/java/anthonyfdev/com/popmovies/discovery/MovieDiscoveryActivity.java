package anthonyfdev.com.popmovies.discovery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.net.URL;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.common.BaseModelAsyncTask;
import anthonyfdev.com.popmovies.common.NetworkUtils;
import anthonyfdev.com.popmovies.common.TMDBNetworkHelper;
import anthonyfdev.com.popmovies.discovery.model.MovieResponse;

public class MovieDiscoveryActivity extends AppCompatActivity {

    private static final String TAG = MovieDiscoveryActivity.class.getSimpleName();
    private static final String ENDPOINT_POPULAR = "/movie/popular";
    private static final String ENDPOINT_TOP_RATED = "/movie/top_rated";
    private static final String ARG_CURRENT_ENDPOINT = TAG + ":argCurrentEndpoint";
    private RecyclerView rvMovies;
    private MovieDiscoveryAdapter adapter;
    private BaseModelAsyncTask<MovieResponse> movieAsyncTask;
    private View flLoadingIndicator;
    private URL currentEndpoint;
    private View rlErrorContainer;
    private Button bRetry;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_CURRENT_ENDPOINT, currentEndpoint);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_discovery);
        bindViews();
        setupViews();
        initializeRecyclerView();
        restoreInstanceState(savedInstanceState);
        if (currentEndpoint == null) {
            buildUrl(ENDPOINT_POPULAR);
        }

        makeNewMovieRequest(currentEndpoint);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_discovery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                refreshListWithNewCall(ENDPOINT_POPULAR);
                return true;
            case R.id.action_sort_top_rated:
                refreshListWithNewCall(ENDPOINT_TOP_RATED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentEndpoint = (URL) savedInstanceState.getSerializable(ARG_CURRENT_ENDPOINT);
        }
    }

    private void initializeRecyclerView() {
        int spanCount = getResources().getInteger(R.integer.MovieDiscovery_PosterSpan);
        adapter = new MovieDiscoveryAdapter();
        rvMovies.setLayoutManager(new GridLayoutManager(this, spanCount));
        rvMovies.setAdapter(adapter);
    }

    private void refreshListWithNewCall(@NonNull String endpoint) {
        buildUrl(endpoint);
        refreshListWithCurrentEndpoint();
    }

    private void refreshListWithCurrentEndpoint() {
        if (NetworkUtils.isConnected(this)) {
            if (movieAsyncTask != null) {
                movieAsyncTask.cancel(true);
            }
            makeNewMovieRequest(currentEndpoint);
        } else {
            showErrorMessage();
        }
    }

    private void makeNewMovieRequest(URL endpoint) {
        if (endpoint != null && NetworkUtils.isConnected(this)) {
            movieAsyncTask = new BaseModelAsyncTask<>(movieAsyncTaskListener, MovieResponse.class);
            movieAsyncTask.execute(endpoint);
        } else {
            buildUrl(ENDPOINT_POPULAR);
            showErrorMessage();
        }
    }

    private void buildUrl(String endpoint) {
        currentEndpoint = TMDBNetworkHelper.buildUrl(this, endpoint);
    }

    private void bindViews() {
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        flLoadingIndicator = findViewById(R.id.fl_loading);
        rlErrorContainer = findViewById(R.id.rl_error_container);
        bRetry = (Button) findViewById(R.id.b_retry);
    }

    private void setupViews() {
        bRetry.setOnClickListener(retryOnClickListener);
    }

    private void showErrorMessage() {
        rlErrorContainer.setVisibility(View.VISIBLE);
    }

    private final BaseModelAsyncTask.AsyncTaskListener<MovieResponse> movieAsyncTaskListener = new BaseModelAsyncTask.AsyncTaskListener<MovieResponse>() {
        @Override
        public void onPreExecute() {
            flLoadingIndicator.setVisibility(View.VISIBLE);
            rlErrorContainer.setVisibility(View.GONE);
        }

        @Override
        public void onPostExecute(MovieResponse result) {
            flLoadingIndicator.setVisibility(View.GONE);
            if (result != null) {
                adapter.setMovieList(result.getResults());
                if (result.getResults().isEmpty()) {
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }
    };

    private final View.OnClickListener retryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshListWithCurrentEndpoint();
        }
    };
}
