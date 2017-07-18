package anthonyfdev.com.popmovies.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import anthonyfdev.com.popmovies.common.BaseModel;

/**
 * Pojo representing a movie from TMDB
 *
 * @author Anthony Fermin
 */

public class Movie extends BaseModel implements Parcelable {
    private String title;
    private String posterThumbnail;
    private String overview;
    private int voteAverage;
    private String releaseDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterThumbnail() {
        return posterThumbnail;
    }

    public void setPosterThumbnail(String posterThumbnail) {
        this.posterThumbnail = posterThumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void parseJson(JSONObject jsonObject) {
        title = jsonObject.optString("title");
        posterThumbnail = jsonObject.optString("poster_path");
        overview = jsonObject.optString("overview");
        voteAverage = jsonObject.optInt("vote_average");
        releaseDate = jsonObject.optString("release_date");
    }

    public Movie() {
        //no-op
    }

    public Movie(JSONObject jsonObject) {
        parseJson(jsonObject);
    }


    protected Movie(Parcel in) {
        title = in.readString();
        posterThumbnail = in.readString();
        overview = in.readString();
        voteAverage = in.readInt();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterThumbnail);
        parcel.writeString(overview);
        parcel.writeInt(voteAverage);
        parcel.writeString(releaseDate);
    }
}
