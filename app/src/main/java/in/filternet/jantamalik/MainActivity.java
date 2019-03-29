package in.filternet.jantamalik;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import in.filternet.jantamalik.IssuesJava.Issues;

public class MainActivity extends AppCompatActivity {

    private final static String TAG ="MainActivity";

    public final static String bDATE_CHANGE = "Date_Change";
    public final static String bNOTIFICATION_TIME_SET = "Notification_Time_Set";
    public final static String sCHANNEL_ID_SUNDAY = "Sunday";
    public final static String sCHANNEL_ID_UPDATE = "Update";
    public final static String sUSER_CURRENT_LANGUAGE = "User_Current_Language";
    public final static String sLANGUAGE_HINDI = "hi";
    public final static String sLANGUAGE_ENGLISH = "en";
    public final static String bUSER_AGREE = "User_Agree";

    public static final String TAB_NUMBER = "tab_number";
    public static final int TAB_ISSUES = 0, TAB_RAJYA = 1, TAB_KENDRA = 2;

    public static final String DEFAULT_STATE = "Uttar Pradesh";
    public static final String DEFAULT_MP = "Varanasi";
    public static final String DEFAULT_MLA = "Varanasi Cantt";
    public static final String DEFAULT_WARD = "Chittupur, Sigra";

    public static final String hiDEFAULT_STATE = "उत्तर प्रदेश";
    public static final String hiDEFAULT_MP = "वाराणसी";
    public static final String hiDEFAULT_MLA = "वाराणसी कैंट";
    public static final String hiDEFAULT_WARD = "छित्तुपुर, सिगरा";

    public static final String sSTATE = DEFAULT_STATE;
    public static final String sMP = DEFAULT_MP;
    public static final String sMLA = DEFAULT_MLA;
    public static final String sWARD = DEFAULT_WARD;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Button ui_language_button;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    String mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        mLanguage = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, null); // first launch
        if (mLanguage == null){
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI).commit();
            mLanguage = sLANGUAGE_HINDI; // default - Hindi 58 crore. English 2 crore.
        }

        if(mLanguage.equals(sLANGUAGE_HINDI)) {
            setUI_Lang(this, "hi");
        }

        if (BuildConfig.RELEASE_MODE) { // To avoid developers screen recordings
            //UXCam.startWithKey(""); // TODO uxcam SDK. This activity is entry activity
        }

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager_main);
        toolbar = findViewById(R.id.appbar);
        ui_language_button = findViewById(R.id.lanugage_button);

        setSupportActionBar(toolbar);

        if(mLanguage.equals(sLANGUAGE_HINDI)) {
            ui_language_button.setText("EN");
        } else {
            ui_language_button.setText("हिन्दी");
        }

        final MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(mainViewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout ));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        Intent intent =getIntent();
        int num = intent.getIntExtra(TAB_NUMBER, 0);
        viewPager.setCurrentItem(num);

        //As per documentation, Starting with Build.VERSION_CODES.HONEYCOMB, tasks are executed on a single thread to avoid
        //common application errors caused by parallel execution. If we need parallel execution, then use executeOnExecutor()
        new VersionPrompt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        set_notification_time(this, true);

        prompt_user_agree();
    }

    private void prompt_user_agree() {
        boolean user_agree = mSharedPref.getBoolean(bUSER_AGREE, false);
        if(user_agree) {
            // nothong to do
        } else {
            TextView message = new TextView(this);
            message.setHeight(205);
            message.setPadding(30,30,30,0);
            message.setLinkTextColor(Color.BLUE);
            message.setGravity(Gravity.CENTER);
            SpannableString s = new SpannableString(getText(R.string.terms_and_condition));
            Linkify.addLinks(s, Linkify.WEB_URLS);
            message.setText(s);
            message.setMovementMethod(LinkMovementMethod.getInstance());

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            builder.setView(message);
            builder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    FirebaseLogger.send(MainActivity.this, "Agree");
                    mEditor.putBoolean(bUSER_AGREE, true).commit();
                }
            });
            builder.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    FirebaseLogger.send(MainActivity.this, "Not_Agree");
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //prompt_user_agree(); // Was firing twice for a normal use case
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
            case R.id.contact_menuItem:
                FirebaseLogger.send(this, "Tap_Contact_Us");
                intent = new Intent(this, Contact.class);
                intent.putExtra("feedback", true);
                startActivity(intent);
                break;
            case R.id.share_menuItem:
                onclick_share_button(getCurrentFocus());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onclick_share_button(View view) {
        FirebaseLogger.send(this, "Tap_Share");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        String shareBody = getString(R.string.share_message);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Important");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + "\nhttps://play.google.com/store/apps/details?id=in.filternet.jantamalik");
        startActivity(intent);
    }

    public void onclick_open_donate(View view) {
        FirebaseLogger.send(this, "Tap_Donate");

        Uri uri = Uri.parse("https://www.filternet.in/donate/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onclick_media(View view) {
        int layout_id = R.layout.issue_media_or_afeem;
        int title_id = R.string.media_or_afeem;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_mp_no_response(View view) {
        int layout_id = R.layout.issue_mp_no_response;
        int title_id = R.string.mp_no_response;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_clean(View view) {
        int layout_id = R.layout.issue_cleanliness;
        int title_id = R.string.cleanliness;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_road(View view) {
        int layout_id = R.layout.issue_poor_road;
        int title_id = R.string.poor_road;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_electricity(View view) {
        int layout_id = R.layout.issue_electricity;
        int title_id = R.string.electricity;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_police(View view) {
        int layout_id = R.layout.issue_police;
        int title_id = R.string.police;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_traffic(View view) {
        int layout_id = R.layout.issue_traffic;
        int title_id = R.string.traffic;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_curroption(View view) {
        int layout_id = R.layout.issue_corruption;
        int title_id = R.string.corruption;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_train(View view) {
        int layout_id = R.layout.issue_train_delay;
        int title_id = R.string.train;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void onclick_business(View view) {
        int layout_id = R.layout.issue_business;
        int title_id = R.string.business;

        Intent intent = new Intent(this, Issues.class);
        intent.putExtra("layout_id", layout_id);
        intent.putExtra("title_id", title_id);
        startActivity(intent);
    }

    public void changeLanguage(View view){
        String current_language = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, null);

        if(current_language == null || current_language.equals(sLANGUAGE_ENGLISH)) {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI).commit();
            setUI_Lang(this, "hi");

            FirebaseLogger.send(this, "App_Lang_Hindi");
            //Toast.makeText(getApplicationContext(),"भाषा को सफलतापूर्वक बदल दिया गया है", Toast.LENGTH_SHORT).show();
        } else {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_ENGLISH).commit();
            setUI_Lang(this, "en");

            FirebaseLogger.send(this, "App_Lang_Eng");
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

    public static void set_notification_time(Context context, boolean fresh) {
        //Log.e(TAG, "set_notification_time*****");

        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shared_pref.edit();

        boolean notification_time_set = shared_pref.getBoolean(MainActivity.bNOTIFICATION_TIME_SET, false);
        if (notification_time_set)
            return;

        String eventName = "in.janatamalik.NOTIFICATION";
        Intent intent = new Intent(context, Receiver.class);
        intent.setAction(eventName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Log.e(TAG, "start " + pendingIntent);
        AlarmManager manager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));

        Calendar calendar = Calendar.getInstance();

        if(fresh) {// Fresh install: If time has elapsed then set from next Sunday 11:00 AM
            int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
            int current_min = calendar.get(Calendar.MINUTE);
            if ((current_hour * 60 + current_min) > (11 * 60))
                calendar.add(Calendar.WEEK_OF_MONTH, 1);
        } else {
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
        }

        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //Log.e(TAG, "alarm time \n" + calendar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent);
        }

        editor.putBoolean(MainActivity.bNOTIFICATION_TIME_SET, true).commit();
    }

    public static void showNotification(Context context, String CHANNEL_ID) {
        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String current_language = shared_pref.getString(sUSER_CURRENT_LANGUAGE, null);

        String notification_text = null;
        CharSequence notification_name = null;
        int notification_id = 0;

        if(CHANNEL_ID.equals(sCHANNEL_ID_UPDATE)) {
            if(current_language.equals(sLANGUAGE_HINDI)) {
                notification_text = context.getString(R.string.notification_update_hi);
            } else {
                notification_text = context.getString(R.string.notification_update);
            }
            notification_name = sCHANNEL_ID_UPDATE;
            notification_id = 1;
        }

        else if(CHANNEL_ID.equals(sCHANNEL_ID_SUNDAY)) {
            // Event handling chain doesn't handle language, so using tricks to achieve the effect
            if(current_language.equals(sLANGUAGE_HINDI)) {
                notification_text = context.getString(R.string.sunday_msg_hi);
            } else {
                notification_text = context.getString(R.string.sunday_msg);
            }
            notification_name = sCHANNEL_ID_SUNDAY;
            notification_id = 2;
        }

        NotificationCompat.Builder mBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mBuilder = new NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentText(notification_text)
                    .setChannelId(CHANNEL_ID);
        } else {
            mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentText(notification_text)
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
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, notification_name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notification_id, mBuilder.build());
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
                    builder.setTitle(R.string.version_update);
                    builder.setPositiveButton(R.string.Option_Update, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseLogger.send(MainActivity.this, "Tap_Update_App");

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE_MARKET)));
                        }
                    });
                    builder.setNegativeButton(R.string.Option_Later, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            mEditor.putBoolean(bAPP_UPDATE_LATER, true).commit();

                            FirebaseLogger.send(MainActivity.this, "Tap_Update_Later");
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
                showNotification(context_v, MainActivity.sCHANNEL_ID_UPDATE);
            }
        }
    }
}
