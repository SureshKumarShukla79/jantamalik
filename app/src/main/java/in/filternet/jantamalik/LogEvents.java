package in.filternet.jantamalik;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public final class LogEvents {
    private static String TAG = "LogEvents";

    public static void send(Context context, String s){
        if (BuildConfig.RELEASE_MODE) // during DEBUG or development comment this out
        {
            if (!Common.checkNetconnectivity(context)) {
                return;
            }

            new Event_2_Server(context, s).start();
        }
    }

    private static class Event_2_Server extends Thread {
        Context context;
        String event_name;

        Event_2_Server(Context cntxt, String s) {
            context = cntxt;
            event_name = s;
        }

        @Override
        public void run() {
            HttpURLConnection urlc = null;

            //Log.e(TAG, "sending");
            JsonReader reader = null;
            URL url = null;
            Exception exception = null;
            try {
                url = new URL(Common.URL_ANALYTICS);
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

                DataOutputStream os = new DataOutputStream(urlc.getOutputStream());
                os.writeBytes(Common.getPostDataString(param));
                os.flush();
                os.close();

                // Command response processing
                InputStream response = new BufferedInputStream(urlc.getInputStream());
                reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
                reader.beginObject();
                reader.setLenient(true);

                //Log.e(TAG, "Server response " + reader.toString());

                reader.close();
                response.close();
                urlc.disconnect();
                return;
            } catch (Exception e) {
                exception = e;
            } finally {
                //Log.e(TAG, "URL " + url);
                if (exception != null) {
                    exception.printStackTrace();
                }
                if (urlc != null) {
                    urlc.disconnect();
                }
            }
            return;
        }
    }
}
