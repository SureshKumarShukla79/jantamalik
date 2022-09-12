package in.jantamalik;

import android.content.Context;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


public final class LogEvents {
    private static final String TAG = "LogEvents";

    private static final String URL_ANALYTICS = Constants.URL_ANALYTICS;

    public static void send(Context context, String s){
        sendWithValue(context, s, ""); // only evant name, no value
    }

    public static void sendWithValue(Context context, String name, String value) {
        if (BuildConfig.RELEASE_MODE) // during DEBUG or development comment this out
        {
            if (!Common.checkNetconnectivity(context)) {
                return;
            }

            //TODO new Event_2_Server(context, name, value).start();
            Log.e(TAG, name + "" + value);
        }
    }

    private static class Event_2_Server extends Thread {
        Context context;
        String event_name;
        String event_value;

        Event_2_Server(Context cntxt, String name, String value) {
            context = cntxt;
            event_name = name;
            event_value = value;
        }

        @Override
        public void run() {
            HttpURLConnection urlc = null;

            //Log.e(TAG, "sending");
            JsonReader reader;
            URL url;

            try {
                url = new URL(URL_ANALYTICS);
                Common.SSLv3_bugfix();
                urlc = (HttpURLConnection) url.openConnection();
                urlc.setDoOutput(true);
                urlc.setDoInput(true);
                urlc.setRequestMethod("POST");
                urlc.setRequestProperty("Content-Language", "en-US");
                urlc.setRequestProperty("Accept-Encoding", "identity");

                HashMap<String, String> param = new HashMap<>();

                String device_id = Common.get_AndroidID(context);
                if (device_id == null) {
                    Log.e(TAG, "Android ID null");
                    urlc.disconnect();
                    return;
                }
                param.put("did", device_id);
                param.put("event", event_name);
                if (!event_value.equals("")) // case of name-value pair
                    param.put("value", event_value);

                DataOutputStream os = new DataOutputStream(urlc.getOutputStream());
                os.writeBytes(Common.getPostDataString(param));
                os.flush();
                os.close();

                // Command response processing
                InputStream response = new BufferedInputStream(urlc.getInputStream());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    reader = new JsonReader(new InputStreamReader(response, StandardCharsets.UTF_8));
                } else {
                    reader = new JsonReader(new InputStreamReader(response));
                }
                reader.beginObject();
                reader.setLenient(true);

                //Log.e(TAG, "Server response " + reader.toString());
                reader.close();
                response.close();
                urlc.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                urlc.disconnect();
            }
        }
    }
}
