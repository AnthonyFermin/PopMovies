package anthonyfdev.com.popmovies.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import anthonyfdev.com.popmovies.common.BaseModel;

/**
 * @author Anthony Fermin ()
 */

public class Trailer extends BaseModel implements Parcelable {

    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private int size;
    private String site;
    private String type;

    public Trailer() {
    }

    protected Trailer(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
        key = in.readString();
        name = in.readString();
        size = in.readInt();
        site = in.readString();
        type = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public void parseJson(JSONObject jsonObject) {
        super.parseJson(jsonObject);
        id = jsonObject.optString("id");
        iso_639_1 = jsonObject.optString("iso_639_1");
        iso_3166_1 = jsonObject.optString("iso_3166_1");
        key = jsonObject.optString("key");
        name = jsonObject.optString("name");
        site = jsonObject.optString("site");
        size = jsonObject.optInt("size");
        type = jsonObject.optString("type");
    }

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeInt(size);
        dest.writeString(site);
        dest.writeString(type);
    }
}
