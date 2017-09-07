package anthonyfdev.com.popmovies.discovery.model;

import org.json.JSONObject;

import anthonyfdev.com.popmovies.common.BaseModel;

/**
 * @author Anthony Fermin ()
 */

public class Review extends BaseModel {

    private String id;
    private String author;
    private String content;
    private String url;

    @Override
    public void parseJson(JSONObject jsonObject) {
        super.parseJson(jsonObject);
        id = jsonObject.optString("id");
        author = jsonObject.optString("author");
        content = jsonObject.optString("content");
        url = jsonObject.optString("url");
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
