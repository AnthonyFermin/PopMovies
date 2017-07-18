package anthonyfdev.com.popmovies.discovery.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import anthonyfdev.com.popmovies.common.BaseModel;

/**
 * @author Anthony Fermin
 */

public class MovieResponse extends BaseModel {
    private List<Movie> results = new ArrayList<>();

    @Override
    public void parseJson(JSONObject jsonObject) {
        super.parseJson(jsonObject);
        JSONArray jsonResults = jsonObject.optJSONArray("results");
        for (int i = 0; i < jsonResults.length(); i++) {
            JSONObject jsonMovie = jsonResults.optJSONObject(i);
            if (jsonMovie != null) {
                results.add(new Movie(jsonMovie));
            }
        }
    }

    public List<Movie> getResults() {
        return results;
    }
}
