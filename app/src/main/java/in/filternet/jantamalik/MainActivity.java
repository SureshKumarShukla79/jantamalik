package in.filternet.jantamalik;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;
import java.util.regex.Pattern;

import in.filternet.jantamalik.DonateActivityJava.donate;
import in.filternet.jantamalik.VoteJava.VoteMP;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {

    private final static String TAG ="MainActivity";
    public final static String bDATE_CHANGE = "Date_Change";
    public final static String sUSER_CURRENT_LANGUAGE = "User_Current_Language";
    public final static String sLANGUAGE_HINDI = "hi";
    public final static String sLANGUAGE_ENGLISH = "en";

    private static int prev_tab = 0;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Button ui_language_button;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        String current_language = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);
        if(current_language != null && current_language.equals(sLANGUAGE_HINDI)) {
            setUI_Lang(this, "hi");
        }

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager_main);
        toolbar = findViewById(R.id.appbar);
        ui_language_button = findViewById(R.id.lanugage_button);

        setSupportActionBar(toolbar);

        if(current_language != null && current_language.equals(sLANGUAGE_HINDI)) {
            ui_language_button.setText("EN");
        } else {
            ui_language_button.setText("हिन्दी");
        }

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(prev_tab); // restore old tab

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout ));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                prev_tab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //As per documentation, Starting with Build.VERSION_CODES.HONEYCOMB, tasks are executed on a single thread to avoid
        //common application errors caused by parallel execution. If we need parallel execution, then use executeOnExecutor()
        new VersionPrompt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.donate_menuItem:
                intent = new Intent(this, donate.class);
                startActivity(intent);
                break;
            case R.id.contact_menuItem:
                intent = new Intent(this, Contact.class);
                intent.putExtra("feedback", true);
                startActivity(intent);
                break;
            case R.id.share_menuItem:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                String shareBody = getString(R.string.share_message);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Important");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + "\nhttps://play.google.com/store/apps/details?id=in.filternet.jantamalik");
               startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeLanguage(View view){
        String current_language = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, null);

        if(current_language == null || current_language.equals(sLANGUAGE_ENGLISH)) {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI).commit();
            setUI_Lang(this, "hi");
            //Toast.makeText(getApplicationContext(),"भाषा को सफलतापूर्वक बदल दिया गया है", Toast.LENGTH_SHORT).show();
        } else {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_ENGLISH).commit();
            setUI_Lang(this, "en");
            //Toast.makeText(getApplicationContext(),"Language has successfully changed", Toast.LENGTH_SHORT).show();
        }

        this.recreate(); // refresh screen
    }

    public static void setUI_Lang(Activity activity, String lang) { // before setContentView
        String languageToLoad = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    private class VersionPrompt extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.e(TAG, "VersionPrompt");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //Log.e(TAG, "doInBackground");
            String mobileVersion, playstoreVersion;
            try {
                mobileVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                //Log.e(TAG, "Mobile version = " + mobileVersion);
                playstoreVersion = get_PlayStoreVersion();

                if (playstoreVersion != null && !mobileVersion.equals(playstoreVersion)) {
                    //Log.e(TAG, "Versions not matched");
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        //Shared Preference key
        public final static String bAPP_UPDATE_LATER = "App_Update_Later";   //boolean
        private AlertDialog mAlertDialog;

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                boolean app_update_later = mSharedPref.getBoolean(bAPP_UPDATE_LATER, false);
                if (!app_update_later) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                    builder.setTitle("New version available, please update");
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE_MARKET)));
                        }
                    });
                    builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            mEditor.putBoolean(bAPP_UPDATE_LATER, true).commit();
                        }
                    });


                    // Bugfix : Activity may be in background and this may crash
                    try {
                        mAlertDialog = builder.create();
                        //if (!getBaseContext().isFinishing()) // Activity shudn't be finished or finishing
                            mAlertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //FirebaseLogger.send("Got_Update");
                }
            }
        }

        public final static String URL_PLAYSTORE_MARKET = "https://play.google.com/store/apps/details?id=in.filternet.jantamalik";

        //It retrieves the latest version by scraping the content of current version from play store at runtime
        public String get_PlayStoreVersion(){
            String playstoreVersion = null;
            String urlOfAppFromPlayStore = URL_PLAYSTORE_MARKET;

            try {
                Document doc = Jsoup
                        .connect(urlOfAppFromPlayStore)
                        .timeout(60 * 1000) //millisec
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36") // 2018-Apr-11
                        .referrer("http://www.google.com")
                        .get();

                Elements Version = doc.select(".htlgb ");
                for (int i = 0; i < 15; i++) {
                    String tmp = Version.get(i).text();
                    //Log.e(TAG, tmp);
                    if (Pattern.matches("^[0-9]{2}.[0-9]{2}.[0-9]{2}$", tmp)) {
                        playstoreVersion = tmp;
                        break;
                    }
                }
                //Log.e(TAG, "Playstore version = " + playstoreVersion);
                return playstoreVersion;
            } catch (Exception exception){
                //exception.printStackTrace();
            }
            return null;
        }
    }

    public static class Match_Versions extends AsyncTask<Void, Void, Boolean> {
        Context context_v;
        private SharedPreferences mSharedPref;
        private SharedPreferences.Editor mEditor;

        protected Match_Versions(Context context) {
            context_v = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.e(TAG, "VersionPrompt");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //Log.e(TAG, "doInBackground");
            String mobileVersion, playstoreVersion = null;
            String urlOfAppFromPlayStore = VersionPrompt.URL_PLAYSTORE_MARKET;
            try {
                mobileVersion = context_v.getPackageManager().getPackageInfo(context_v.getPackageName(), 0).versionName;
                Log.e(TAG, "Mobile version = " + mobileVersion);

                Document doc = Jsoup
                        .connect(urlOfAppFromPlayStore)
                        .timeout(60 * 1000) //millisec
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36") // 2018-Apr-11
                        .referrer("http://www.google.com")
                        .get();

                Elements Version = doc.select(".htlgb ");
                for (int i = 0; i < 15; i++) {
                    String tmp = Version.get(i).text();
                    //Log.e(TAG, tmp);
                    if (Pattern.matches("^[0-9]{2}.[0-9]{2}.[0-9]{2}$", tmp)) {
                        playstoreVersion = tmp;
                        break;
                    }
                }
                Log.e(TAG, "Playstore version = " + playstoreVersion);

                if (playstoreVersion != null) {
                    mSharedPref = PreferenceManager.getDefaultSharedPreferences(context_v);
                    mEditor = mSharedPref.edit();
                    mEditor.putBoolean(bDATE_CHANGE, false).commit();
                }

                if (playstoreVersion != null && !mobileVersion.equals(playstoreVersion)) {
                    Log.e(TAG, "Versions not matched");
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                showNotification(context_v);
            }
        }

        void showNotification(Context context) {
            String CHANNEL_ID = "Update";
            NotificationCompat.Builder mBuilder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                mBuilder = new NotificationCompat.Builder(context)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText("App needs to be updated")
                        .setChannelId(CHANNEL_ID);
            } else {
                mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText("App needs to be updated")
                        .setChannelId(CHANNEL_ID);
            }

            Intent resultIntent = new Intent(context, MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);

            // Gets an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //NotificationCompat.Builder constructor requires a channel ID to show notifications on Android 8.0 (API level 26) and higher
            //Before showing notification on Android 8.0 and higher, we must register app's notification channel
            //with the system by passing an instance of NotificationChannel to createNotificationChannel()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Update", NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(1, mBuilder.build());
        }
    }
}
