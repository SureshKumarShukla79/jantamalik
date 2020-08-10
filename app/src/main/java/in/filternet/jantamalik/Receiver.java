package in.filternet.jantamalik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.preference.PreferenceManager;
import android.util.Log;

public class Receiver extends BroadcastReceiver {
    final static String TAG = "Receiver";
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onReceive(Context context_in, Intent intent) {
        String event = null;
        try {
            event = intent.getAction();
            Log.e(TAG, event);
        } catch (Exception e) {
            Log.e(TAG, "unexpected " + intent);
            return;
        }

        Context context = context_in.getApplicationContext(); // App global context
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context_in);
        mEditor = mSharedPref.edit();

        switch (event) {
            case Intent.ACTION_BOOT_COMPLETED:
                // After reboot, alarms are cleared. So, we have to set it again
                mEditor.putBoolean(MainActivity.bNOTIFICATION_TIME_SET, false).commit();
                MainActivity.set_notification_time(context, true);
                break;

            case Intent.ACTION_DATE_CHANGED:
                mEditor.putBoolean(MainActivity.bDATE_CHANGE, true).commit();
                break;

            case Intent.ACTION_SCREEN_ON:
            case Intent.ACTION_USER_PRESENT:
                boolean new_date = mSharedPref.getBoolean(MainActivity.bDATE_CHANGE, false);
                //Log.e(TAG, "Date Change: " + new_date);
                if(new_date) {
                    new MainActivity.Match_Versions(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;

            case "in.janatamalik.NOTIFICATION":
                mEditor.putBoolean(MainActivity.bNOTIFICATION_TIME_SET, false).commit();
                MainActivity.showNotification(context, MainActivity.sCHANNEL_ID_SUNDAY);
                MainActivity.set_notification_time(context, false);
                break;

            default:
                Log.e(TAG, "Event without handler " + event);
        }
    }
}
