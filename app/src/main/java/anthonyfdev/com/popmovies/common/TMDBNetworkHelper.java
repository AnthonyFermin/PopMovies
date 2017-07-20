package anthonyfdev.com.popmovies.common;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import anthonyfdev.com.popmovies.R;

/**
 * @author Anthony Fermin
 */

public class TMDBNetworkHelper {

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String PARAM_API_KEY = "api_key";

    @Nullable
    public static URL buildUrl(@NonNull Context context, @NonNull String endpoint) {
        URL url = null;
        try {
            url = new URL(Uri.parse(BASE_URL + endpoint).buildUpon()
                    .appendQueryParameter(PARAM_API_KEY, context.getString(R.string.movie_db_api_key))
                    .build()
                    .toString());
        } catch (MalformedURLException e) {
            Log.d("MovieDiscoveryActivity", "Url not formed correctly!", e);
        }
        return url;
    }

}
