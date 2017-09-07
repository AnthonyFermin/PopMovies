package anthonyfdev.com.popmovies.discovery;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.net.URL;
import java.util.ArrayList;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.common.BaseModelAsyncTask;
import anthonyfdev.com.popmovies.common.Constants;
import anthonyfdev.com.popmovies.common.TMDBNetworkHelper;
import anthonyfdev.com.popmovies.db.QueryFavoritesAsyncTask;
import anthonyfdev.com.popmovies.discovery.model.Movie;
import anthonyfdev.com.popmovies.discovery.model.MovieResponse;

public class MovieDiscoveryActivity extends AppCompatActivity {

    private static final String TAG = MovieDiscoveryActivity.class.getSimpleName();
    private static final String ENDPOINT_POPULAR = "/movie/popular";
    private static final String ENDPOINT_TOP_RATED = "/movie/top_rated";
    private static final String SIS_KEY_CURRENT_ENDPOINT = TAG + ":argCurrentEndpoint";
    private static final String SIS_KEY_MODE = TAG + ":argMode";
    private static final String SIS_KEY_CURRENT_MOVIES = TAG + ":currentMovies";
    private static final String SIS_KEY_CURRENT_FAVORITES = TAG + ":currentFavorites";
    private RecyclerView rvMovies;
    private MovieDiscoveryAdapter adapter;
    private BaseModelAsyncTask<MovieResponse> movieAsyncTask;
    private QueryFavoritesAsyncTask favoritesAsyncTask;
    private View flLoadingIndicator;
    private URL currentEndpoint;
    private int mode;
    private SharedPreferences favSharedPrefs;
    private ArrayList<Movie> currentFavorites;
    private ArrayList<Movie> currentMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_discovery);
        bindViews();
        initializeRecyclerView();
        setupSharedPrefsListener();
        restoreInstanceState(savedInstanceState);
        if (currentEndpoint == null) {
            buildUrl(ENDPOINT_POPULAR);
        }
        if (mode == R.string.MovieDiscovery_MyFavoriteMovies) {
            if (currentFavorites != null) {
                favoritesAsyncTaskListener.onPostExecute(currentFavorites);
            } else {
                refreshListWithFavorites();
            }
        } else {
            if (currentMovies != null) {
                MovieResponse movieResponse = new MovieResponse();
                movieResponse.setResults(currentMovies);
                movieAsyncTaskListener.onPostExecute(movieResponse);
            } else {
                refreshListWithNewCall(mode);
            }
        }
    }

    private void setupSharedPrefsListener() {
        favSharedPrefs = getSharedPreferences(Constants.SHARED_PREFS_FAVORITES, MODE_PRIVATE);
        favSharedPrefs.registerOnSharedPreferenceChangeListener(favSharedPrefListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SIS_KEY_CURRENT_MOVIES, currentMovies);
        outState.putParcelableArrayList(SIS_KEY_CURRENT_FAVORITES, currentFavorites);
        outState.putSerializable(SIS_KEY_CURRENT_ENDPOINT, currentEndpoint);
        outState.putInt(SIS_KEY_MODE, mode);
    }

    private void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
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
                refreshListWithNewCall(ENDPOINT_POPULAR, R.string.MovieDiscovery_PopularMovies);
                return true;
            case R.id.action_sort_top_rated:
                refreshListWithNewCall(ENDPOINT_TOP_RATED, R.string.MovieDiscovery_TopRatedMovies);
                return true;
            case R.id.action_show_favorites:
                refreshListWithFavorites();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (favoritesAsyncTask != null) {
            favoritesAsyncTask.cancel(true);
            favoritesAsyncTask = null;
        }
        if (movieAsyncTask != null) {
            movieAsyncTask.cancel(true);
            movieAsyncTask = null;
        }
        favSharedPrefs.unregisterOnSharedPreferenceChangeListener(favSharedPrefListener);
    }

    private void refreshListWithFavorites() {
        currentFavorites = null;
        if (favoritesAsyncTask != null) {
            favoritesAsyncTask.cancel(true);
        }
        favoritesAsyncTask = new QueryFavoritesAsyncTask(this, favoritesAsyncTaskListener);
        favoritesAsyncTask.execute();
        setMode(R.string.MovieDiscovery_MyFavoriteMovies);
    }

    private void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentEndpoint = (URL) savedInstanceState.getSerializable(SIS_KEY_CURRENT_ENDPOINT);
            mode = savedInstanceState.getInt(SIS_KEY_MODE);
            currentMovies = savedInstanceState.getParcelableArrayList(SIS_KEY_CURRENT_MOVIES);
            currentFavorites = savedInstanceState.getParcelableArrayList(SIS_KEY_CURRENT_FAVORITES);
        }
    }

    private void initializeRecyclerView() {
        int spanCount = getResources().getInteger(R.integer.MovieDiscovery_PosterSpan);
        adapter = new MovieDiscoveryAdapter();
        rvMovies.setLayoutManager(new GridLayoutManager(this, spanCount));
        rvMovies.setAdapter(adapter);
    }

    private void refreshListWithNewCall(@NonNull String endpoint, @StringRes int mode) {
        buildUrl(endpoint);
        refreshListWithNewCall(mode);
    }

    private void refreshListWithNewCall(@StringRes int mode) {
        currentMovies = null;
        if (movieAsyncTask != null) {
            movieAsyncTask.cancel(true);
        }
        makeNewMovieRequest(currentEndpoint);
        setMode(mode);
    }

    private void setMode(@StringRes int mode) {
        switch (mode) {
            case R.string.MovieDiscovery_MyFavoriteMovies:
                setActionBarTitle(getString(mode));
                this.mode = mode;
                break;
            case R.string.MovieDiscovery_TopRatedMovies:
                setActionBarTitle(getString(mode));
                this.mode = mode;
                break;
            default:
                //case R.string.MovieDiscovery_PopularMovies:
                setActionBarTitle(getString(R.string.MovieDiscovery_PopularMovies));
                this.mode = R.string.MovieDiscovery_PopularMovies;
        }
    }

    private void makeNewMovieRequest(URL endpoint) {
        movieAsyncTask = new BaseModelAsyncTask<>(movieAsyncTaskListener, MovieResponse.class);
        movieAsyncTask.execute(endpoint);
    }

    private void buildUrl(String endpoint) {
        currentEndpoint = TMDBNetworkHelper.buildUrl(this, endpoint);
    }

    private void bindViews() {
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        flLoadingIndicator = findViewById(R.id.fl_loading);
    }

    private final QueryFavoritesAsyncTask.Listener favoritesAsyncTaskListener = new QueryFavoritesAsyncTask.Listener() {
        @Override
        public void onPreExecute() {
            flLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPostExecute(ArrayList<Movie> favorites) {
            flLoadingIndicator.setVisibility(View.GONE);
            currentFavorites = favorites;
            if (favorites != null) {
                adapter.setMovieList(favorites);
                if (favorites.isEmpty()) {
                    showErrorDialog(getString(R.string.MovieDiscovery_NoFavoritesFound));
                }
            } else {
                showErrorDialog(getString(R.string.GenericErrorString));
            }
        }
    };

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener favSharedPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (mode == R.string.MovieDiscovery_MyFavoriteMovies) {
                refreshListWithFavorites();
            }
        }
    };

    private final BaseModelAsyncTask.AsyncTaskListener<MovieResponse> movieAsyncTaskListener = new BaseModelAsyncTask.AsyncTaskListener<MovieResponse>() {
        @Override
        public void onPreExecute() {
            flLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPostExecute(MovieResponse result) {
            flLoadingIndicator.setVisibility(View.GONE);
            if (result != null) {
                currentMovies = result.getResults();
                adapter.setMovieList(currentMovies);
                if (currentMovies.isEmpty()) {
                    showErrorDialog(getString(R.string.MovieDiscovery_NoMoviesFound));
                }
            } else {
                showErrorDialog(getString(R.string.GenericErrorString));
            }
        }
    };
}
