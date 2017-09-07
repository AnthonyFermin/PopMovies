package anthonyfdev.com.popmovies.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anthony Fermin
 */

public abstract class ListResponse<TModel extends BaseModel> extends BaseModel {

    private final static String TAG = ListResponse.class.getSimpleName();
    protected static Class clazz;
    protected static String listPropertyName;
    private List<TModel> results = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    public void parseJson(JSONObject jsonObject) {
        super.parseJson(jsonObject);
        JSONArray jsonResults = jsonObject.optJSONArray(listPropertyName);
        if (jsonResults != null) {
            for (int i = 0; i < jsonResults.length(); i++) {
                JSONObject jsonModel = jsonResults.optJSONObject(i);
                if (jsonModel != null) {
                    try {
                        TModel instance = (TModel) clazz.newInstance();
                        instance.parseJson(jsonModel);
                        results.add(instance);
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Log.d(TAG, "No field \"" + listPropertyName + "\" was found.");
        }
    }

    public List<TModel> getResults() {
        return results;
    }

}
