package anthonyfdev.com.popmovies.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database. This class is not necessary, but keeps
 * the code organized.
 *
 * @author Anthony Fermin (Fuzz)
 */
public class FavoriteContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "com.anthonyfdev.popmovies";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for Sunshine.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*
     * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that Sunshine
     * can handle. For instance,
     *
     *     content://com.example.android.sunshine/weather/
     *     [           BASE_CONTENT_URI         ][ PATH_FAVORITE ]
     *
     * is a valid path for looking at weather data.
     *
     *      content://com.example.android.sunshine/givemeroot/
     *
     * will fail, as the ContentProvider hasn't been given any information on what to do with
     * "givemeroot". At least, let's hope not. Don't be that dev, reader. Don't be that dev.
     */
    public static final String PATH_FAVORITE = "favorite";

    /* Inner class that defines the table contents of the weather table */
    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE)
                .build();

        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_THUMBNAIL = "poster_thumbnail";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static Uri buildFavoriteUriWithId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }

    }
}


