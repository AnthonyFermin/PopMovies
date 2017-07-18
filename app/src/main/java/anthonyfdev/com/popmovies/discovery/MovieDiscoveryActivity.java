package anthonyfdev.com.popmovies.discovery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import anthonyfdev.com.popmovies.R;

public class MovieDiscoveryActivity extends AppCompatActivity {

    private static final int SPAN_COUNT = 2;
    private RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_discovery);
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
    }
}
