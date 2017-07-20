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
 * @author Anthony Fermin
 */

public class BaseModelAsyncTask<T extends BaseModel> extends AsyncTask<URL, Void, T> {

    private static final int MS_READ_TIMEOUT = 15000;
    private static final int MS_CONNECT_TIMEOUT = 15000;
    @NonNull
    private final AsyncTaskListener<T> listener;
    @NonNull
    private final Class<T> resultClass;

    public BaseModelAsyncTask(@NonNull AsyncTaskListener<T> listener, @NonNull Class<T> modelClazz) {
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
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(MS_CONNECT_TIMEOUT);
            connection.setReadTimeout(MS_READ_TIMEOUT);
            try {
                InputStream is = connection.getInputStream();
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
        Scanner scanner = new Scanner(bis);
        scanner.useDelimiter("\\A");

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            sb.append(scanner.next());
        }
        try {
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public interface AsyncTaskListener<T> {
        void onPreExecute();
        void onPostExecute(T result);
    }
}
