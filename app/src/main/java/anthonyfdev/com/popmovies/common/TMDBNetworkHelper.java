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
 * @author Anthony Fermin (Fuzz)
 */

public class TMDBNetworkHelper {

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String PARAM_API_KEY = "api_key";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";
    private static final String BASE_YOUTUBE_IMAGE_URL = "http://img.youtube.com/vi";
    private static final String PATH_YOUTUBE_IMAGE = "/0.jpg";
    private static final String PARAM_YOUTUBE_VIDEO = "v";

    @Nullable
    public static URL buildUrl(@NonNull Context context, @NonNull String path) {
        URL url = null;
        try {
            url = new URL(Uri.parse(BASE_URL + path).buildUpon()
                    .appendQueryParameter(PARAM_API_KEY, context.getString(R.string.movie_db_api_key))
                    .build()
                    .toString());
        } catch (MalformedURLException e) {
            Log.d("MovieDiscoveryActivity", "Url not formed correctly!", e);
        }
        return url;
    }

    @Nullable
    public static Uri buildYoutubeUrl(@NonNull String videoKey) {
        return Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                    .appendQueryParameter(PARAM_YOUTUBE_VIDEO, videoKey)
                    .build();
    }

    @Nullable
    public static String buildYoutubeImageUrlString(@NonNull String videoKey) {
        return Uri.parse(BASE_YOUTUBE_IMAGE_URL + "/" + videoKey + PATH_YOUTUBE_IMAGE).buildUpon()
                .build()
                .toString();
    }

}
