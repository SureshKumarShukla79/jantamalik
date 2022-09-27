package in.jantamalik;

import android.annotation.SuppressLint;
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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import in.jantamalik.Kendra.DataFilter;

public class MainActivity extends AppCompatActivity {

    private final static String TAG ="MainActivity";

    public final static String bDATE_CHANGE = "Date_Change";
    public final static String bNOTIFICATION_TIME_SET = "Notification_Time_Set";
    public final static String sCHANNEL_ID_SUNDAY = "Sunday";
    public final static String sCHANNEL_ID_UPDATE = "Update";

    public final static String bUSER_AGREE = "User_Agree";
    public final static String bSMART_VOTER = "Smart_Voter";
    public final static String bAPP_UPDATE_LATER = "App_Update_Later";   //boolean

    public static final String TAB_NUMBER = "tab_number";
    public static final int TAB_243 = 0, TAB_KENDRA = 2, TAB_RAJYA = 1;

    public static final String SELECT_MP = "जिला चुनें";
    public static final String SELECT_MLA = "विधानसभा चुनें";

    public static final String sMP_AREA = "MP_Area";
    public static final String sMLA_AREA = "MLA_Area";
    public static final String sWARD = "Ward";

    public final static String URL_PLAYSTORE_MARKET = "https://play.google.com/store/apps/details?id=in.jantamalik";
    public static final String USER_SHARE_APP = "\n" + URL_PLAYSTORE_MARKET + "&referrer=utm_source%3Dr";

    private Activity activity;
    private ViewPager viewPager;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    boolean rajya;
    AlertDialog m_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null) {
            rajya = savedInstanceState.getBoolean("rajya");
        }

        activity = this;

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        prompt_user_agree();

        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager_main);
        Toolbar toolbar = findViewById(R.id.appbar);

        boolean user_agree = mSharedPref.getBoolean(bUSER_AGREE, false);

        if (user_agree)
            ask_user_preference();

        setSupportActionBar(toolbar);

        final MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(mainViewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        Intent intent = getIntent();
        int num = intent.getIntExtra(TAB_NUMBER, 0);
        if(rajya) {
            num = intent.getIntExtra(TAB_NUMBER, TAB_RAJYA);
        }
        viewPager.setCurrentItem(num);

        //As per documentation, Starting with Build.VERSION_CODES.HONEYCOMB, tasks are executed on a single thread to avoid
        //common application errors caused by parallel execution. If we need parallel execution, then use executeOnExecutor()
        mEditor.remove(bAPP_UPDATE_LATER).commit();
        new VersionPrompt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        set_notification_time(this, true);
    }

    private void prompt_user_agree() {
        boolean user_agree = mSharedPref.getBoolean(bUSER_AGREE, false);
        if (user_agree) // nothing to do
            return;

        // else
        TextView message = new TextView(this);
        message.setTextSize((float) 20);
        message.setHeight(250);
        message.setPadding(30, 30, 30, 0);
        message.setLinkTextColor(Color.BLUE);
        message.setGravity(Gravity.CENTER);

        String terms_and_condition = "", button_yes = "", button_no = "";
        terms_and_condition = getResources().getString(R.string.terms_and_condition_hi);
        button_yes = getResources().getString(R.string.button_yes_hi);
        button_no = getResources().getString(R.string.button_no_hi);

        SpannableString s = new SpannableString(terms_and_condition);
        Linkify.addLinks(s, Linkify.WEB_URLS);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setView(message);
        builder.setPositiveButton(button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                LogEvents.send(MainActivity.this, "Agree");
                mEditor.putBoolean(bUSER_AGREE, true).commit();

                ask_user_preference();
            }
        });
        builder.setNegativeButton(button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                LogEvents.send(MainActivity.this, "Not_Agree");
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    positive.setTextColor(getResources().getColor(R.color.black, null));
                    negative.setTextColor(getResources().getColor(R.color.black, null));
                } else {
                    positive.setTextColor(get_color_for_lower_version(R.color.black));
                    negative.setTextColor(get_color_for_lower_version(R.color.black));
                }

                positive.setTypeface(positive.getTypeface(), Typeface.BOLD);
                negative.setTypeface(positive.getTypeface(), Typeface.BOLD);
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    @SuppressWarnings("deprecation")
    private int get_color_for_lower_version(int color) {
        return getResources().getColor(color);
    }

    private void ask_user_preference() {
        // Fresh Install, Save SharedPreferences
        String MP = mSharedPref.getString(sMP_AREA, "");
        String MLA = mSharedPref.getString(sMLA_AREA, "");
        Log.e(TAG, "Def : " + MP + " " + MLA);

        if (!MP.equals("") && !MLA.equals("")) // Both MP, MLA are saved
            return;

        LayoutInflater inflater = getLayoutInflater();
        View ui_preference_layout = inflater.inflate(R.layout.user_preference, null);

        final Spinner ui_constituency_spinner = ui_preference_layout.findViewById(R.id.constituency_spinner);
        final Spinner ui_assembly_spinner = ui_preference_layout.findViewById(R.id.assembly_spinner);
        final FloatingActionButton ui_done = ui_preference_layout.findViewById(R.id.done);

        final DataFilter data_filter = new DataFilter();

        //populating constituency
        List<String> constituency_list = data_filter.getMPAreas();
        constituency_list.add(SELECT_MP);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_text_style, constituency_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ui_constituency_spinner.setAdapter(adapter);
        ui_constituency_spinner.setSelection(constituency_list.size() - 1);

        //spinner constituency click handler
        ui_constituency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_constituency = adapterView.getItemAtPosition(i).toString();
                if (selected_constituency.equals(MainActivity.SELECT_MP)) {
                    return;
                }

                mEditor.putString(MainActivity.sMP_AREA, selected_constituency).commit();

                ui_assembly_spinner.setVisibility(View.VISIBLE);

                //Log.e(TAG, "selected_constituency: " + selected_constituency);
                //populating assembly
                List<String> assembly_list = data_filter.get_MLA_area_as_per_MP_area(selected_constituency);
                assembly_list.add(SELECT_MLA);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_text_style, assembly_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ui_assembly_spinner.setAdapter(adapter);
                ui_assembly_spinner.setSelection(assembly_list.size() - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //spinner constituency click handler
        ui_assembly_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_assembly = adapterView.getItemAtPosition(i).toString();
                if (selected_assembly.equals(MainActivity.SELECT_MLA)) {
                    ui_done.setVisibility(View.INVISIBLE);
                    return;
                }

                mEditor.putString(MainActivity.sMLA_AREA, selected_assembly).commit();
                ui_done.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(ui_preference_layout);
        alert.setCancelable(false);

        m_dialog = alert.create();
        m_dialog.show();
    }

    public void onclick_done(View view) {
        m_dialog.hide();
        LogEvents.send(this, "Preference_saved");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
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
        LogEvents.send(this, "Share");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        String shareBody = getString(R.string.share_message);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Important");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + USER_SHARE_APP);
        startActivity(intent);
    }

    public void onclick_twitter_button(View view) {
        LogEvents.send(this, "Twitter");

        Uri uri = Uri.parse("https://twitter.com/sureshkrshukla");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onclick_open_donate(View view) {
        startActivity(Common.open_donate(view));
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

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        //Log.e(TAG, "start " + pendingIntent);
        AlarmManager manager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));

        Calendar calendar = Calendar.getInstance();

        if (fresh) {// Fresh install: If time has elapsed then set from next Sunday 11:00 AM
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

        String notification_text = null;
        CharSequence notification_name = null;
        int notification_id = 0;

        if(CHANNEL_ID.equals(sCHANNEL_ID_UPDATE)) {
            notification_text = context.getString(R.string.notification_update);
            notification_name = sCHANNEL_ID_UPDATE;
            notification_id = 1;
        }

        else if(CHANNEL_ID.equals(sCHANNEL_ID_SUNDAY)) {
            // Event handling chain doesn't handle language, so using tricks to achieve the effect
            int question_num = new Random().nextInt(Puzzle_Ques.questions.length);
            notification_text = Puzzle_Ques.questions[question_num][0]; //context.getString(R.string.sunday_msg_hi);

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
        PendingIntent resultPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
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

    //One copy in MainActivity
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
                LogEvents.send(MainActivity.this, "Smart_Voter");

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNeutralButton(R.string.share, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                LogEvents.send(MainActivity.this, "Share_Puzzle");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = getString(R.string.share_puzzle);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Important");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + MainActivity.USER_SHARE_APP);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
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
                            LogEvents.send(MainActivity.this, "Update_App");

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE_MARKET)));
                        }
                    });
                    builder.setNegativeButton(R.string.Option_Later, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            mEditor.putBoolean(bAPP_UPDATE_LATER, true).commit();

                            LogEvents.send(MainActivity.this, "Update_Later");
                        }
                    });


                    // Bugfix : Activity may be in background and this may crash
                    try {
                        if (!activity.isFinishing()) // Activity shudn't be finished or finishing
                            mAlertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //LogEvents.send("Got_Update");
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
