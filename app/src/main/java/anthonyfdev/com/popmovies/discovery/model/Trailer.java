package anthonyfdev.com.popmovies.discovery.model;

import org.json.JSONObject;

import anthonyfdev.com.popmovies.common.BaseModel;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class Trailer extends BaseModel {

    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private int size;
    private String site;
    private String type;

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
}
