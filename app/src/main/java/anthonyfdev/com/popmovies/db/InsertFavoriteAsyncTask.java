package anthonyfdev.com.popmovies.db;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;

import anthonyfdev.com.popmovies.discovery.model.Movie;

/**
 * @author Anthony Fermin
 */

public class InsertFavoriteAsyncTask extends AsyncTask<Movie, Void, Boolean> {

    private final ContentProviderClient provider;
    private final SharedPreferences sharedPreferences;
    private Listener listener;
    private String id;

    public InsertFavoriteAsyncTask(Context context, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        listener = new UpdatePrefListener();
        provider = context.getContentResolver().acquireContentProviderClient(FavoriteContract.BASE_CONTENT_URI);
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
    protected Boolean doInBackground(Movie... params) {
        boolean success = false;
        if (params != null
                && params[0] != null
                && params[0].getId() != null
                && !params[0].getId().isEmpty()) {
            id = params[0].getId();
            ContentValues contentValues = retrieveContentValue(params[0]);
            Uri uri = new Uri.Builder()
                    .authority(FavoriteContract.CONTENT_AUTHORITY)
                    .appendPath(FavoriteContract.PATH_FAVORITE)
                    .build();
            Uri result = null;
            try {
                result = provider.insert(uri, contentValues);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            success = result != null;
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (listener != null) {
            listener.onPostExecute(aBoolean);
        }
    }

    public interface Listener {
        void onPreExecute();
        void onPostExecute(Boolean bool);
    }

    public class UpdatePrefListener implements Listener {
        @Override
        public void onPreExecute() {

        }

        @Override
        public void onPostExecute(Boolean bool) {
            if (id != null) {
                if (bool) {
                    sharedPreferences.edit().putBoolean(id, true).apply();
                } else {
                    sharedPreferences.edit().remove(id).apply();
                }
            }
        }
    }

    private ContentValues retrieveContentValue(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_ID, movie.getId());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_THUMBNAIL, movie.getPosterThumbnail());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        return contentValues;
    }
}
