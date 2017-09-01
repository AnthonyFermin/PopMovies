package anthonyfdev.com.popmovies.db;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;

import anthonyfdev.com.popmovies.discovery.model.Movie;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class InsertFavoriteAsyncTask extends AsyncTask<Movie, Void, Boolean> {

    private final ContentProviderClient provider;
    private Listener listener;

    public InsertFavoriteAsyncTask(Context context, Listener listener) {
        this.listener = listener;
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
        if (params != null && params[0] != null) {
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
