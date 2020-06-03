/*
 * Copyright (c) 2015 Karpa IT Solutions LLP
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package in.filternet.jantamalik;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Updated by karpa on 22/08/2016
 */

public class Common {
    private final static String TAG = "Common";

    //public final static String CLOUD_VERSION = "devel";  // Cloud side PHP version
    public final static String CLOUD_VERSION = "20.06.08";  // Cloud side PHP version
    public final static String URL_BASE_SSL = "https://db.filternet.in/jantamalik/" + CLOUD_VERSION;

    public static final String URL_ANALYTICS = URL_BASE_SSL + "/" + "analytics.php"; // logs / UX tracer

    public static boolean checkNetconnectivity(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Returns Network object for the current default network or null if no default network is currently active
                Network active_network = connMgr.getActiveNetwork();
                return active_network != null;
            } else {
                return checkNetconnectivity_for_lower_versions(connMgr);
            }
        } else
            return false;
    }

    @SuppressWarnings("deprecation")
    private static boolean checkNetconnectivity_for_lower_versions(ConnectivityManager connMgr) {
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String get_AndroidID(Context context) {
        // for older devices, just use older style.
        // http://android-developers.blogspot.in/2011/03/identifying-app-installations.html
        //A 64-bit number (as a hex string) that is randomly generated when the user first sets up
        // the device and should remain constant for the lifetime of the user's device.
        // The value may change if a factory reset is performed on the device.
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getPostDataString(HashMap<String, String> param2) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : param2.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private static boolean isProviderUpdated = false, isSSL_fixapplied = false;
    public static void SSLv3_bugfix() {

        if (isProviderUpdated) // TODO not sure if TLSv1 fallback is required with updated Provider
            return;
        if (isSSL_fixapplied) // singleton
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) // Lollipop - 21 . TODO may need more tweaking
            return;

        // Apply minimum TLS and subtract SSLv3 fallback - to Lollipop and below ONLY

        /* https://developer.android.com/reference/javax/net/ssl/SSLEngine.html
        Client socket:
        Protocol	Supported (API Levels)	Enabled by default (API Levels)
        SSLv3	        1+	                    1–22
        TLSv1	        1+	                    1+
        TLSv1.1	        20+	                    20+
        TLSv1.2	        20+	                    20+
        TLSv1.3	        29+	                    29+
        */

        SSLContext sslcontext;
        SSLSocketFactory NoSSLv3Factory;
        try {
            sslcontext = SSLContext.getInstance("TLSv1"); // API 14 and below 20
            sslcontext.init(null, null, null);
            NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            // Subtract SSLv3 forcefully for Https class
            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
            isSSL_fixapplied = true; // fix works appl-wide, so needed only one time
        } catch (Exception e) {
            Log.e(TAG, "SSLv3-" + e.toString());
        }
    }
}
