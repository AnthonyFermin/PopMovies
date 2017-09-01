package anthonyfdev.com.popmovies.db;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class DeleteFavoriteAsyncTask extends AsyncTask<String, Void, Boolean> {

    private final ContentProviderClient provider;
    private Listener listener;

    public DeleteFavoriteAsyncTask(Context context, Listener listener) {
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
    protected Boolean doInBackground(String... params) {
        boolean success = false;
        if (params != null && params[0] != null) {
            String id = params[0];
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
}
