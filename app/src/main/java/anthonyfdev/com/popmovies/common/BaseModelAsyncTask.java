package anthonyfdev.com.popmovies.common;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class BaseModelAsyncTask<T extends BaseModel> extends AsyncTask<URL, Void, T> {

    @NonNull
    private final AsyncTaskListener listener;
    @NonNull
    private final Class<T> resultClass;

    public BaseModelAsyncTask(@NonNull AsyncTaskListener listener, @NonNull Class<T> modelClazz) {
        this.listener = listener;
        this.resultClass = modelClazz;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreExecute();
    }

    @Override
    protected T doInBackground(URL... urls) {
        URL url = urls[0];
        T results = null;
        String resultString = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            try {
                is = connection.getInputStream();
                if (is != null) {
                    resultString = readStream(is);
                }
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (resultString != null) {
            try {
                JSONObject jsonObject = new JSONObject(resultString);
                T tempResults = resultClass.newInstance();
                tempResults.parseJson(jsonObject);
                results = tempResults;
            } catch (JSONException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        listener.onPostExecute(t);
    }

    private String readStream(InputStream is) {
        BufferedInputStream bis = new BufferedInputStream(is);
        String resultString = null;
        Scanner scanner = new Scanner(bis);

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            sb.append(scanner.next());
        }
        resultString = sb.toString();
        try {
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public interface AsyncTaskListener {
        void onPreExecute();

        <T> void onPostExecute(T result);
    }
}
