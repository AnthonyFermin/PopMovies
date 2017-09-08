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
import anthonyfdev.com.popmovies.discovery.model.Trailer;

/**
 * @author Anthony Fermin ()
 */

public class TrailersView extends FrameLayout {
    private TextView tvEmptyText;
    private RecyclerView rvTrailers;
    private View progressBar;
    private TrailerAdapter trailerAdapter;

    public TrailersView(@NonNull Context context) {
        super(context);
        init();
    }

    public TrailersView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrailersView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TrailersView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_trailers, this, true);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        rvTrailers = (RecyclerView) findViewById(R.id.rv_trailers);
        tvEmptyText = (TextView) findViewById(R.id.tv_emptyText);
        progressBar = findViewById(R.id.progress_bar);
        trailerAdapter = new TrailerAdapter();
        rvTrailers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTrailers.setAdapter(trailerAdapter);
    }

    public void setTrailers(List<Trailer> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            trailerAdapter.setTrailers(trailers);
            tvEmptyText.setVisibility(View.GONE);
            rvTrailers.setVisibility(View.VISIBLE);
        } else {
            tvEmptyText.setVisibility(View.VISIBLE);
            rvTrailers.setVisibility(View.GONE);
        }
    }

    public void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    public RecyclerView getRvTrailers() {
        return rvTrailers;
    }
}
