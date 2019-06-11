package in.filternet.jantamalik;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
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
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import in.filternet.jantamalik.Kendra.MPdata;

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
    public final static String bSMART_VOTER = "Smart_Voter";
    public final static String bAPP_UPDATE_LATER = "App_Update_Later";   //boolean
    public final static String sUSER_SELECT_LANGUAGE = "User_Select_Language";

    public static final String TAB_NUMBER = "tab_number";
    public static final int TAB_ISSUE = 0, TAB_KENDRA = 1, TAB_RAJYA = 2, TAB_CORPORATION = 3;

    public static final String DEFAULT_STATE = "Uttar Pradesh";
    public static final String DEFAULT_MP = "Varanasi";
    public static final String DEFAULT_MLA = "Varanasi Cantt";
    public static final String DEFAULT_WARD = "Chittupur, Sigra";

    public static final String hiDEFAULT_STATE = "उत्तर प्रदेश";
    public static final String hiDEFAULT_MP = "वाराणसी";
    public static final String hiDEFAULT_MLA = "वाराणसी कैंट";
    public static final String hiDEFAULT_WARD = "छित्तुपुर, सिगरा";

    public static final String sSTATE = "State";
    public static final String sMP_AREA = "MP_Area";
    public static final String sMLA_AREA = "MLA_Area";
    public static final String sWARD = "Ward";

    public final static String URL_PLAYSTORE_MARKET = "https://play.google.com/store/apps/details?id=in.filternet.jantamalik";
    public static final String USER_SHARE_APP = "\n" + URL_PLAYSTORE_MARKET + "&referrer=utm_source%3Dr";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ImageView ui_green_badge;
    private Button ui_language_button, ui_puzzle_button;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    String mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        if (BuildConfig.RELEASE_MODE) { // To avoid developers screen recordings
            //UXCam.startWithKey(""); // uxcam SDK. This activity is entry activity
        }

        String user_select_language = mSharedPref.getString(sUSER_SELECT_LANGUAGE, null);
        if (user_select_language == null) {
            choose_language();
            return;
        }

        if(user_select_language.equals("Hindi")) {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI).commit();
        } else {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_ENGLISH).commit();
        }

        mLanguage = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, null); // first launch
        if (mLanguage == null){
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI).commit();
            mLanguage = sLANGUAGE_HINDI; // default - Hindi 58 crore. English 2 crore.
        }

        if(mLanguage.equals(sLANGUAGE_HINDI)) {
            setUI_Lang(this, "hi");
            FirebaseLogger.send(this, "App_Lang_Hindi");
        }

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager_main);
        toolbar = findViewById(R.id.appbar);
        ui_language_button = findViewById(R.id.lanugage_button);
        ui_puzzle_button = findViewById(R.id.puzzle_button);
        ui_green_badge = findViewById(R.id.green_badge);

        // Fresh Install, Save SharedPreferences
        String State = mSharedPref.getString(sSTATE, DEFAULT_STATE);
        String MP = mSharedPref.getString(sMP_AREA, DEFAULT_MP);
        String MLA = mSharedPref.getString(sMLA_AREA, DEFAULT_MLA);
        String Ward = mSharedPref.getString(sWARD, DEFAULT_WARD);
        //Log.e(TAG, "state : " + State + " " + MP + " " + MLA + " " + Ward);

        // In case of Hindi, change the defaults
        if (mLanguage.equals(sLANGUAGE_HINDI)) {
            if (State.equals(DEFAULT_STATE))
                State = hiDEFAULT_STATE;
            if (MP.equals(DEFAULT_MP))
                MP = hiDEFAULT_MP;
            if (MLA.equals(DEFAULT_MLA))
                MLA = hiDEFAULT_MLA;
            if (Ward.equals(DEFAULT_WARD))
                Ward = hiDEFAULT_WARD;
        }
        //Log.e(TAG, "state : " + State + " " + MP + " " + MLA + " " + Ward);

        mEditor.putString(MainActivity.sSTATE, State).commit();
        mEditor.putString(MainActivity.sMP_AREA, MP).commit();
        mEditor.putString(MainActivity.sMLA_AREA, MLA).commit();
        mEditor.putString(MainActivity.sWARD, Ward).commit();

        setSupportActionBar(toolbar);

        boolean smart_voter = mSharedPref.getBoolean(bSMART_VOTER, false);
        if(smart_voter) {
            ui_puzzle_button.setVisibility(View.GONE);
            ui_green_badge.setVisibility(View.VISIBLE);
            FirebaseLogger.send(this, "Smart_Voter");
        }

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
        mEditor.remove(bAPP_UPDATE_LATER).commit();
        new VersionPrompt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        set_notification_time(this, true);

        prompt_user_agree();
    }

    private void choose_language() {
        Intent intent = new Intent(this, Language.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void prompt_user_agree() {
        boolean user_agree = mSharedPref.getBoolean(bUSER_AGREE, false);
        if(user_agree) {
            // nothong to do
        } else {
            TextView message = new TextView(this);
            message.setTextSize((float) 20);
            message.setHeight(325);
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

            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog_interface) {
                    Button positive = dialog.getButton(Dialog.BUTTON_POSITIVE);
                    positive.setTextSize((float) 20);
                    Button negative = dialog.getButton(Dialog.BUTTON_NEGATIVE);
                    negative.setTextSize((float) 20);

                    positive.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    positive.setTypeface(positive.getTypeface(), Typeface.BOLD);
                    negative.setTextColor(getResources().getColor(R.color.red));
                    negative.setTypeface(positive.getTypeface(), Typeface.BOLD);
                }
            });

            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public void onclick_puzzle(View view) {
        boolean smart_voter = mSharedPref.getBoolean(bSMART_VOTER, false);
        if(!smart_voter) {
            Intent intent = new Intent(this, Puzzle.class);
            startActivity(intent);
        }
    }

    //One copy in Puzzle
    public void user_became_smart(View view) {
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.green_badge);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle(R.string.congratulation);
        builder.setMessage(R.string.smart_voter);
        builder.setView(image);
        builder.setPositiveButton(R.string.user_thanks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                FirebaseLogger.send(MainActivity.this, "Smart_Voter");
            }
        });
        builder.setNeutralButton(R.string.share, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                FirebaseLogger.send(MainActivity.this, "Share_Puzzle");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = getString(R.string.share_puzzle);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Important");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + USER_SHARE_APP);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
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
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + USER_SHARE_APP);
        startActivity(intent);
    }

    public void onclick_open_donate(View view) {
        FirebaseLogger.send(this, "Tap_Donate");

        Uri uri = Uri.parse("https://www.filternet.in/donate/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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

        String language = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, null);
        String state = get_state(this, language);
        String area = get_area(this, language);
        mEditor.putString(sSTATE, state).commit();
        mEditor.putString(sMP_AREA, area).commit();

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

    public static String get_state(Context context, String language){
        String state = " "; // TODO why one space

        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String state_in = shared_pref.getString(sSTATE, DEFAULT_STATE);

        if (language.equals(sLANGUAGE_HINDI)) {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state_in.equals(MPdata.all_MPs[i][0])) {
                    state = MPdata.all_MPs[i][3];
                }
            }
        } else {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state_in.equals(MPdata.all_MPs[i][3])) {
                    state = MPdata.all_MPs[i][0];
                }
            }
        }
        return state;
    }

    public static String get_area(Context context, String language){
        String area = " "; // TODO why one space

        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String state_in = shared_pref.getString(sSTATE, DEFAULT_STATE);
        String area_in = shared_pref.getString(sMP_AREA, DEFAULT_MP);

        if (language.equals(sLANGUAGE_HINDI)) {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state_in.equals(MPdata.all_MPs[i][0]) && area_in.equals(MPdata.all_MPs[i][1])) {
                    area = MPdata.all_MPs[i][4];
                }
            }
        } else {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state_in.equals(MPdata.all_MPs[i][3]) && area_in.equals(MPdata.all_MPs[i][4])) {
                    area = MPdata.all_MPs[i][1];
                }
            }
        }
        return area;
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
            int question_num = new Random().nextInt(Puzzle_Ques.questions.length);
            if(current_language.equals(sLANGUAGE_HINDI)) {
                notification_text = Puzzle_Ques.questions[question_num][5]; //context.getString(R.string.sunday_msg_hi);
            } else {
                notification_text = Puzzle_Ques.questions[question_num][0]; //context.getString(R.string.sunday_msg);
            }
            //Log.e(TAG, question_num + notification_text);
            notification_name = sCHANNEL_ID_SUNDAY;
            notification_id = 2;
        }

        NotificationCompat.Builder mBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mBuilder = new NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setSmallIcon(R.drawable.small_icon)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_text))
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
            String urlOfAppFromPlayStore = URL_PLAYSTORE_MARKET;
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
