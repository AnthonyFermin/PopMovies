package anthonyfdev.com.popmovies.discovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anthonyfdev.com.popmovies.R;
import anthonyfdev.com.popmovies.common.TMDBNetworkHelper;
import anthonyfdev.com.popmovies.discovery.model.Movie;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String ARG_MOVIE = TAG + ":argMovie";
    private Movie movie;
    private TextView tvOverView;
    private TextView tvTitle;
    private TextView tvRating;
    private TextView tvReleaseDate;
    private ImageView ivPoster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getIntent().getParcelableExtra(ARG_MOVIE);
        bindViews();
        setupViews();
    }

    private void setupViews() {
        tvTitle.setText(movie.getOriginalTitle());
        tvReleaseDate.setText(movie.getReleaseDate());
        String ratingString = getString(R.string.MovieDetail_OutOfTen, String.valueOf(movie.getVoteAverage()));
        tvRating.setText(ratingString);
        tvOverView.setText(movie.getOverview());
        Picasso.with(this)
                .load(TMDBNetworkHelper.BASE_IMAGE_URL + movie.getPosterThumbnail())
                .fit()
                .into(ivPoster);
    }

    private void bindViews() {
        tvOverView = (TextView) findViewById(R.id.tv_overview);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
    }
}
