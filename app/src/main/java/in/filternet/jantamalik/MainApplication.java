package in.filternet.jantamalik;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;

public class MainApplication extends Application {
    private final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        In Android 6 - API 23(Nexus), USER_PRESENT fires after SCREEN_OFF when Screen Lock set to "Swipe"
        So, unregister USER_PRESENT for >= API 23 and done all operations in SCREEN_ON
        */
        BroadcastReceiver receiver_screen_on = new Receiver();
        registerReceiver(receiver_screen_on, new IntentFilter(Intent.ACTION_SCREEN_ON));

        BroadcastReceiver receiver_user_present = new Receiver();
        registerReceiver(receiver_user_present, new IntentFilter(Intent.ACTION_USER_PRESENT));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {    //API 23
            unregisterReceiver(receiver_user_present);      //Unregister USER_PRESENT, Do every operations in SCREEN_ON
        } else {
            unregisterReceiver(receiver_screen_on);     //Unregister SCREEN_ON, Do every operations in USER_PRESENT
        }
    }
}

