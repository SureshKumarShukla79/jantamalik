package in.filternet.jantamalik;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public final class FirebaseLogger {
    private static FirebaseAnalytics mFirebaseAnalytics;

    public static void send(Context context, String s){
        if (BuildConfig.RELEASE_MODE) {
            if (mFirebaseAnalytics == null)
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

            mFirebaseAnalytics.logEvent(s, new Bundle());
        }
    }
}
