package anthonyfdev.com.popmovies.db;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class DeleteFavoriteAsyncTask extends AsyncTask<String, Void, Boolean> {

    private final ContentProviderClient provider;
    private final SharedPreferences sharedPreferences;
    private Listener listener;
    private String id;

    public DeleteFavoriteAsyncTask(Context context, SharedPreferences sharedPreferences) {
        listener = new UpdatePrefListener();
        provider = context.getContentResolver().acquireContentProviderClient(FavoriteContract.BASE_CONTENT_URI);
        this.sharedPreferences = sharedPreferences;
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
    protected Boolean doInBackground(String... params) {
        boolean success = false;
        if (params != null && params[0] != null) {
            id = params[0];
            String selection = FavoriteContract.FavoriteEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = new String[1];
            selectionArgs[0] = id;
            Uri uri = new Uri.Builder()
                    .authority(FavoriteContract.CONTENT_AUTHORITY)
                    .appendPath(FavoriteContract.PATH_FAVORITE)
                    .build();
            int amtDeleted = 0;
            try {
                amtDeleted = provider.delete(uri, selection, selectionArgs);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            success = amtDeleted != 0;
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
                    sharedPreferences.edit().remove(id).apply();
                } else {
                    sharedPreferences.edit().putBoolean(id, true).apply();
                }
            }
        }
    }
}
