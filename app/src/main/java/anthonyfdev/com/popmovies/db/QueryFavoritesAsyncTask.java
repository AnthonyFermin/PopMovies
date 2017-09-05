package anthonyfdev.com.popmovies.db;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import anthonyfdev.com.popmovies.discovery.model.Movie;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class QueryFavoritesAsyncTask extends AsyncTask<Void, Void, List<Movie>> {

    private final ContentProviderClient provider;
    private Listener listener;

    public QueryFavoritesAsyncTask(Context context, Listener listener) {
        provider = context.getContentResolver().acquireContentProviderClient(FavoriteContract.BASE_CONTENT_URI);
        this.listener = listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onPreExecute();
        }
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        Uri uri = new Uri.Builder()
                .authority(FavoriteContract.CONTENT_AUTHORITY)
                .appendPath(FavoriteContract.PATH_FAVORITE)
                .build();
        Cursor cursor = null;
        List<Movie> movies = new ArrayList<>();
        try {
            cursor = provider.query(uri, null, null, null, null);
            extractFromCursor(cursor, movies);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        if (listener != null) {
            listener.onPostExecute(movies);
        }
    }

    private void extractFromCursor(Cursor cursor, @NonNull List<Movie> movies) {
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW)));
            movie.setPosterThumbnail(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_THUMBNAIL)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE)));
            movie.setVoteAverage(cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE)));
            movie.setId(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_ID)));
            movies.add(movie);
        }
    }

    public interface Listener {
        void onPreExecute();
        void onPostExecute(List<Movie> favorites);
    }
}
