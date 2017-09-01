package anthonyfdev.com.popmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class FavoritesOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                        FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                        FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT, " +
                        FavoriteContract.FavoriteEntry.COLUMN_POSTER_THUMBNAIL + " TEXT, " +
                        FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        FavoriteContract.FavoriteEntry.COLUMN_ID + " TEXT NOT NULL, " +
                        " UNIQUE (" + FavoriteContract.FavoriteEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
