package anthonyfdev.com.popmovies.discovery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.net.URL;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.common.BaseModelAsyncTask;
import anthonyfdev.com.popmovies.common.TMDBNetworkHelper;
import anthonyfdev.com.popmovies.discovery.model.MovieResponse;

public class MovieDiscoveryActivity extends AppCompatActivity {

    private static final String ENDPOINT_POPULAR = "/movie/popular";
    private static final String ENDPOINT_TOP_RATED = "/movie/top_rated";
    private static final int SPAN_COUNT = 2;
    private RecyclerView rvMovies;
    private MovieDiscoveryAdapter adapter;
    private BaseModelAsyncTask<MovieResponse> movieAsyncTask;
    private View flLoadingIndicator;
    private URL currentEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_discovery);
        bindViews();

        adapter = new MovieDiscoveryAdapter();
        rvMovies.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        rvMovies.setAdapter(adapter);

        buildUrl(ENDPOINT_POPULAR);

        movieAsyncTask = new BaseModelAsyncTask<>(movieAsyncTaskListener, MovieResponse.class);
        movieAsyncTask.execute(currentEndpoint);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_discovery_sort, menu);
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

    private void refreshListWithNewCall(@NonNull String endpoint) {
        buildUrl(endpoint);
        movieAsyncTask.cancel(true);
        movieAsyncTask = new BaseModelAsyncTask<>(movieAsyncTaskListener, MovieResponse.class);
        movieAsyncTask.execute(currentEndpoint);
    }

    private void buildUrl(String endpoint) {
        currentEndpoint = TMDBNetworkHelper.buildUrl(this, endpoint);
    }

    private void bindViews() {
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        flLoadingIndicator = findViewById(R.id.fl_loading);
    }

    private final BaseModelAsyncTask.AsyncTaskListener movieAsyncTaskListener = new BaseModelAsyncTask.AsyncTaskListener<MovieResponse>() {
        @Override
        public void onPreExecute() {
            flLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPostExecute(MovieResponse result) {
            flLoadingIndicator.setVisibility(View.GONE);
            if (result != null) {
                adapter.setMovieList(result.getResults());
            }
        }
    };
}
