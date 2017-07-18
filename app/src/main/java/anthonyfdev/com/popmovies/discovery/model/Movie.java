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
    private String imageThumbnail;
    private String overview;
    private String voteAverage;
    private String releaseDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
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
        //TODO: extract fields from jsonObject
    }

    protected Movie(Parcel in) {
        title = in.readString();
        imageThumbnail = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
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
        parcel.writeString(imageThumbnail);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
    }
}
