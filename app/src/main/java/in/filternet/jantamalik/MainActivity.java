package in.filternet.jantamalik;


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
import android.content.res.Configuration;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;
import in.filternet.jantamalik.Kendra.DataFilter;
import in.filternet.jantamalik.Kendra.MPdata;

public class MainActivity extends AppCompatActivity {

    private final static String TAG ="MainActivity";

    public final static String bDATE_CHANGE = "Date_Change";
    public final static String bNOTIFICATION_TIME_SET = "Notification_Time_Set";
    public final static String sCHANNEL_ID_SUNDAY = "Sunday";
    public final static String sCHANNEL_ID_UPDATE = "Update";
    public final static String sUSER_CURRENT_LANGUAGE = "User_Current_Language";
    public final static String sLANGUAGE_HINDI = "hi";
    public final static String sLANGUAGE_MARATHI = "mr";
    public final static String sLANGUAGE_ENGLISH = "en";
    public final static String bUSER_AGREE = "User_Agree";
    public final static String bSMART_VOTER = "Smart_Voter";
    public final static String bAPP_UPDATE_LATER = "App_Update_Later";   //boolean
    public final static String sUSER_SELECT_LANGUAGE = "User_Select_Language";

    public static final String TAB_NUMBER = "tab_number";
    public static final int TAB_ISSUE = 0, TAB_KENDRA = 1, TAB_RAJYA = 2;

    public static final String DEFAULT_STATE = "Select State";
    public static final String DEFAULT_MP = "Select Area";
    public static final String DEFAULT_MLA = "Select Area";

    public static final String hiDEFAULT_STATE = "राज्य चुनें";
    public static final String hiDEFAULT_MP = "क्षेत्र चुनें";
    public static final String hiDEFAULT_MLA = "क्षेत्र चुनें";

    public static final String mrDEFAULT_STATE = "राज्य निवडा";
    public static final String mrDEFAULT_MP = "क्षेत्र निवडा";
    public static final String mrDEFAULT_MLA = "क्षेत्र निवडा";

    public static final String sSTATE = "State";
    public static final String sMP_AREA = "MP_Area";
    public static final String sMLA_AREA = "MLA_Area";
    public static final String sWARD = "Ward";

    public final static String URL_PLAYSTORE_MARKET = "https://play.google.com/store/apps/details?id=in.filternet.jantamalik";
    public static final String USER_SHARE_APP = "\n" + URL_PLAYSTORE_MARKET + "&referrer=utm_source%3Dr";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    String mLanguage;
    boolean rajya;
    private static String prev_user_select_language = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null) {
            rajya = savedInstanceState.getBoolean("rajya");
        }

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

        mLanguage = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, null); // first launch
        if (user_select_language.equals("English")) {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_ENGLISH).commit();
            mLanguage = sLANGUAGE_ENGLISH;
        } else if (user_select_language.equals("Marathi")){
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_MARATHI).commit();
            mLanguage = sLANGUAGE_MARATHI;
        } else { // All others will default to Hindi till we get translators.
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI).commit();
            mLanguage = sLANGUAGE_HINDI;
        }

        if (mLanguage.equals(sLANGUAGE_ENGLISH)){
            setUI_Lang(this, "en");
        } else if(mLanguage.equals(sLANGUAGE_MARATHI)) {
            setUI_Lang(this, "mr");
        } else {
            setUI_Lang(this, "hi");
        }

        if(mLanguage.equals(sLANGUAGE_HINDI) && !user_select_language.equals("Hindi") && !prev_user_select_language.equals(user_select_language)) {
            prompt_no_language_support();
            prev_user_select_language = user_select_language;
        } else {
            prompt_user_agree();
        }

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager_main);
        toolbar = findViewById(R.id.appbar);

        // Fresh Install, Save SharedPreferences
        String State = mSharedPref.getString(sSTATE, DEFAULT_STATE);
        String MP = mSharedPref.getString(sMP_AREA, DEFAULT_MP);
        String MLA = mSharedPref.getString(sMLA_AREA, DEFAULT_MLA);
        //Log.e(TAG, "Def : " + State + " " + MP + " " + MLA );

        if(State.equals(DEFAULT_STATE) || State.equals(hiDEFAULT_STATE) || State.equals(mrDEFAULT_STATE)) {

            if (mLanguage.equals(sLANGUAGE_ENGLISH)) {  // In case of English, change the defaults
                State = DEFAULT_STATE;
                MP = DEFAULT_MP;
                MLA = DEFAULT_MLA;
            } else if (mLanguage.equals(sLANGUAGE_MARATHI)) {   // In case of Marathi, change the defaults
                State = mrDEFAULT_STATE;
                MP = mrDEFAULT_MP;
                MLA = mrDEFAULT_MLA;
            } else {    // In case of Hindi, change the defaults
                State = hiDEFAULT_STATE;
                MP = hiDEFAULT_MP;
                MLA = hiDEFAULT_MLA;
            }

            mEditor.putString(MainActivity.sSTATE, State).commit();
            mEditor.putString(MainActivity.sMP_AREA, MP).commit();
            mEditor.putString(MainActivity.sMLA_AREA, MLA).commit();

            boolean user_agree = mSharedPref.getBoolean(bUSER_AGREE, false);
            if(user_agree) {
                ask_user_preference();
            }
        } else {
            String state = MainActivity.get_state(this, mLanguage);
            String mp_area = MainActivity.get_MP_area(this, mLanguage);
            String mla_area = MainActivity.get_MLA_area(this, mLanguage);

            mEditor.putString(MainActivity.sSTATE, state).commit();
            mEditor.putString(MainActivity.sMP_AREA, mp_area).commit();
            mEditor.putString(MainActivity.sMLA_AREA, mla_area).commit();
        }

        setSupportActionBar(toolbar);

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
            message.setHeight(250);
            message.setPadding(30,30,30,0);
            message.setLinkTextColor(Color.BLUE);
            message.setGravity(Gravity.CENTER);

            String terms_and_condition = "", button_yes = "", button_no = "";
            if(mLanguage.equals(sLANGUAGE_ENGLISH)) {
                terms_and_condition = getResources().getString(R.string.terms_and_condition_en);
                button_yes = getResources().getString(R.string.button_yes_en);
                button_no = getResources().getString(R.string.button_no_en);
            } else if(mLanguage.equals(sLANGUAGE_MARATHI)) {
                terms_and_condition = getResources().getString(R.string.terms_and_condition_mr);
                button_yes = getResources().getString(R.string.button_yes_mr);
                button_no = getResources().getString(R.string.button_no_mr);
            } else {
                terms_and_condition = getResources().getString(R.string.terms_and_condition_hi);
                button_yes = getResources().getString(R.string.button_yes_hi);
                button_no = getResources().getString(R.string.button_no_hi);
            }

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
                        positive.setTextColor(getResources().getColor(R.color.colorPrimaryDark, null));
                        negative.setTextColor(getResources().getColor(R.color.colorPrimaryDark, null));
                    } else {
                        positive.setTextColor(get_color_for_lower_version(R.color.colorPrimaryDark));
                        negative.setTextColor(get_color_for_lower_version(R.color.colorPrimaryDark));
                    }

                    positive.setTypeface(positive.getTypeface(), Typeface.BOLD);
                    negative.setTypeface(positive.getTypeface(), Typeface.BOLD);
                }
            });

            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @SuppressWarnings("deprecation")
    private int get_color_for_lower_version(int color) {
        return getResources().getColor(color);
    }

    private void prompt_no_language_support() {
        TextView message = new TextView(this);
        message.setTextSize((float) 20);
        message.setHeight(250);
        message.setPadding(30,30,30,0);
        message.setGravity(Gravity.CENTER);

        String text_to_show = "", button_ok = "";
        if(mLanguage.equals(sLANGUAGE_ENGLISH)) {
            text_to_show = getResources().getString(R.string.no_language_support);
            button_ok = getResources().getString(R.string.button_ok);
        } else if(mLanguage.equals(sLANGUAGE_MARATHI)) {
            text_to_show = getResources().getString(R.string.no_language_support);
            button_ok = getResources().getString(R.string.button_ok);
        } else {
            text_to_show = getResources().getString(R.string.no_language_support);
            button_ok = getResources().getString(R.string.button_ok);
        }

        SpannableString s = new SpannableString(text_to_show);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setView(message);
        builder.setPositiveButton(button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                prompt_user_agree();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog_interface) {
                Button button = dialog.getButton(Dialog.BUTTON_POSITIVE);
                button.setTextSize((float) 20);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    button.setTextColor(getResources().getColor(R.color.colorPrimaryDark, null));
                } else {
                    button.setTextColor(get_color_for_lower_version(R.color.colorPrimaryDark));
                }

                button.setTypeface(button.getTypeface(), Typeface.BOLD);
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void ask_user_preference() {
        LayoutInflater inflater = getLayoutInflater();
        View ui_preference_layout = inflater.inflate(R.layout.user_preference, null);
        final Spinner ui_state_spinner = ui_preference_layout.findViewById(R.id.state_spinner);
        final LinearLayout ui_constituency = ui_preference_layout.findViewById(R.id.constituency);
        final Spinner ui_constituency_spinner = ui_preference_layout.findViewById(R.id.constituency_spinner);
        final LinearLayout ui_assembly = ui_preference_layout.findViewById(R.id.assembly);
        final Spinner ui_assembly_spinner = ui_preference_layout.findViewById(R.id.assembly_spinner);
        final FloatingActionButton ui_done = ui_preference_layout.findViewById(R.id.done);

        final DataFilter data_filter = new DataFilter();

        //populating state
        final List<String> state_list = data_filter.getStates(mLanguage);
        if (mLanguage.equals(MainActivity.sLANGUAGE_ENGLISH)) {
            state_list.add(DEFAULT_STATE);
        } else if (mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
            state_list.add(mrDEFAULT_STATE);
        } else {
            state_list.add(hiDEFAULT_STATE);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_text_style, state_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ui_state_spinner.setAdapter(adapter);
        ui_state_spinner.setSelection(state_list.size() - 1);

        ui_state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_state = ui_state_spinner.getItemAtPosition(ui_state_spinner.getSelectedItemPosition()).toString();
                if (selected_state.equals(MainActivity.DEFAULT_STATE)
                        || selected_state.equals(MainActivity.hiDEFAULT_STATE)
                        || selected_state.equals(MainActivity.mrDEFAULT_STATE)) {
                    return;
                }

                state_list.remove(state_list.size() - 1);
                ui_constituency.setVisibility(View.VISIBLE);
                mEditor.putString(MainActivity.sSTATE, selected_state).commit();

                //populating constituency
                List<String> constituency_list = data_filter.getMPAreas(mLanguage, selected_state);
                if (mLanguage.equals(MainActivity.sLANGUAGE_ENGLISH)) {
                    constituency_list.add(DEFAULT_MP);
                } else if (mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
                    constituency_list.add(mrDEFAULT_MP);
                } else {
                    constituency_list.add(hiDEFAULT_MP);
                }

                ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), R.layout.spinner_text_style, constituency_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ui_constituency_spinner.setAdapter(adapter);
                ui_constituency_spinner.setSelection(constituency_list.size() - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //spinner constituency click handler
        ui_constituency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_constituency = adapterView.getItemAtPosition(i).toString();
                if (selected_constituency.equals(MainActivity.DEFAULT_MP)
                        || selected_constituency.equals(MainActivity.hiDEFAULT_MP)
                        || selected_constituency.equals(MainActivity.mrDEFAULT_MP)) {
                    return;
                }

                mEditor.putString(MainActivity.sMP_AREA, selected_constituency).commit();

                String state = mSharedPref.getString(MainActivity.sSTATE, hiDEFAULT_STATE);
                if (mLanguage.equals(MainActivity.sLANGUAGE_ENGLISH)) {
                    state = mSharedPref.getString(MainActivity.sSTATE, DEFAULT_STATE);
                } else if (mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
                    state = mSharedPref.getString(MainActivity.sSTATE, mrDEFAULT_STATE);
                }

                //Log.e(TAG, "state: " + state);

                if(state!=null && !state.equals("") && (DataFilter.is_union_territory(state)
                        || (state.equals("Arunachal Pradesh") || state.equals("अरुणाचल प्रदेश"))
                        || (state.equals("Gujarat") || state.equals("गुजरात"))
                        || (state.equals("Himachal Pradesh") || state.equals("हिमाचल प्रदेश")))) {

                    ui_assembly.setVisibility(View.GONE);
                    ui_done.setVisibility(View.VISIBLE);
                } else {

                    ui_assembly.setVisibility(View.VISIBLE);

                    //Log.e(TAG, "selected_constituency: " + selected_constituency);
                    //populating assembly
                    List<String> assembly_list = null;
                    if (data_filter.has_MP_2_MLA_mapping(state, selected_constituency)) {

                        assembly_list = data_filter.get_MLA_area_as_per_MP_area(selected_constituency);
                        if (mLanguage.equals(MainActivity.sLANGUAGE_ENGLISH)) {
                            assembly_list.add(DEFAULT_MLA);
                        } else if (mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
                            assembly_list.add(mrDEFAULT_MLA);
                        } else {
                            assembly_list.add(hiDEFAULT_MLA);
                        }

                    } else {

                        assembly_list = data_filter.get_MLA_area_as_per_state(mLanguage, state);
                        if (mLanguage.equals(MainActivity.sLANGUAGE_ENGLISH)) {
                            assembly_list.add(DEFAULT_MLA);
                        } else if (mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
                            assembly_list.add(mrDEFAULT_MLA);
                        } else {
                            assembly_list.add(hiDEFAULT_MLA);
                        }
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), R.layout.spinner_text_style, assembly_list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ui_assembly_spinner.setAdapter(adapter);
                    ui_assembly_spinner.setSelection(assembly_list.size() - 1);

                }
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
                if (selected_assembly.equals(MainActivity.DEFAULT_MLA)
                        || selected_assembly.equals(MainActivity.hiDEFAULT_MLA)
                        || selected_assembly.equals(MainActivity.mrDEFAULT_MLA)) {
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
        final AlertDialog dialog = alert.create();

        ui_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected_state = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);
                String selected_constituency = mSharedPref.getString(MainActivity.sMP_AREA, MainActivity.DEFAULT_MP);
                String selected_assembly = mSharedPref.getString(MainActivity.sMLA_AREA, MainActivity.DEFAULT_MLA);

                // Send selected entries (state & constituency) to Firebase
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {// Firebase needs English, cant handle Hindi
                    selected_state = MainActivity.get_state(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
                }
                selected_state = selected_state.replace(" ", "_");
                selected_state = selected_state.replace("&", "and");
                LogEvents.sendWithValue(getBaseContext(), sSTATE, selected_state);

                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {// Firebase needs English, cant handle Hindi
                    selected_constituency = MainActivity.get_MP_area(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
                }
                selected_constituency = selected_constituency.replace(" ", "_");
                selected_constituency = selected_constituency.replace("&", "and");
                LogEvents.sendWithValue(getBaseContext(), sMP_AREA, selected_constituency);

                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {// Firebase needs English, cant handle Hindi
                    selected_assembly = MainActivity.get_MLA_area(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
                }
                selected_assembly = selected_assembly.replace(" ", "_");
                selected_assembly = selected_assembly.replace("&", "and");
                LogEvents.sendWithValue(getBaseContext(), sMLA_AREA, selected_assembly);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onclick_puzzle(View view) {
        boolean smart_voter = mSharedPref.getBoolean(bSMART_VOTER, false);
        if(!smart_voter) {
            Intent intent = new Intent(this, Puzzle.class);
            startActivity(intent);
        }
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

    public void onclick_fb_button(View view) {
        LogEvents.send(this, "Facebook");

        Uri uri = Uri.parse("https://facebook.com/जनता-मालिक-ऐप-Janta-Malik-App-103698761402979");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onclick_open_donate(View view) {
        LogEvents.send(this, "Donate");

        Uri uri = Uri.parse("https://www.filternet.in/donate/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void changeLanguage(View view){
        Intent intent = new Intent(this, Language.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

        if (language.equals(sLANGUAGE_ENGLISH)) {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state_in.equals(MPdata.all_MPs[i][3])) {
                    state = MPdata.all_MPs[i][0];
                }
            }

            //State is still empty which means user selected preference is already in English then no need to change
            if(state.equals(" ")) {
                for (int i = 0; i < MPdata.all_MPs.length; i++) {
                    if (state_in.equals(MPdata.all_MPs[i][0])) {
                        state = state_in;
                    }
                }
            }
        } else {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state_in.equals(MPdata.all_MPs[i][0])) {
                    state = MPdata.all_MPs[i][3];
                }
            }

            //State is still empty which means user selected preference is already in Hindi then no need to change
            if(state.equals(" ")) {
                for (int i = 0; i < MPdata.all_MPs.length; i++) {
                    if (state_in.equals(MPdata.all_MPs[i][3])) {
                        state = state_in;
                    }
                }
            }
        }
        return state;
    }

    public static String get_MP_area(Context context, String language){
        String area = " "; // TODO why one space

        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String state_in = shared_pref.getString(sSTATE, DEFAULT_STATE);
        String area_in = shared_pref.getString(sMP_AREA, DEFAULT_MP);

        if (language.equals(sLANGUAGE_ENGLISH)) {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state_in.equals(MPdata.all_MPs[i][3]) && area_in.equals(MPdata.all_MPs[i][4])) {
                    area = MPdata.all_MPs[i][1];
                }
            }

            //Area is still empty which means user selected preference is already in Hindi then no need to change
            if(area.equals(" ")) {
                for (int i = 0; i < MPdata.all_MPs.length; i++) {
                    if (state_in.equals(MPdata.all_MPs[i][0]) && area_in.equals(MPdata.all_MPs[i][1])) {
                        area = area_in;
                    }
                }
            }
        } else {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state_in.equals(MPdata.all_MPs[i][0]) && area_in.equals(MPdata.all_MPs[i][1])) {
                    area = MPdata.all_MPs[i][4];
                }
            }

            //Area is still empty which means user selected preference is already in Hindi then no need to change
            if(area.equals(" ")) {
                for (int i = 0; i < MPdata.all_MPs.length; i++) {
                    if (state_in.equals(MPdata.all_MPs[i][3]) && area_in.equals(MPdata.all_MPs[i][4])) {
                        area = area_in;
                    }
                }
            }
        }
        return area;
    }

    public static String get_MLA_area(Context context, String language){
        String mla_area = " "; // TODO why one space
        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String state_in = shared_pref.getString(sSTATE, DEFAULT_STATE);
        String mla_area_in = shared_pref.getString(sMLA_AREA, DEFAULT_MLA);
        String[][] infos = DataFilter.get_MLA_areas_of_state(state_in);

        if (language.equals(sLANGUAGE_ENGLISH)) {
            for (String[] info : infos) {
                if (state_in.equals(info[3]) && mla_area_in.equals(info[4])) {
                    mla_area = info[1];
                }
            }
            //Area is still empty which means user selected preference is already in English then no need to change
            if(mla_area.equals(" ")) {
                for (String[] info : infos) {
                    if (state_in.equals(info[0]) && mla_area_in.equals(info[1])) {
                        mla_area = mla_area_in;
                    }
                }
            }
        } else {
            for (String[] info : infos) {
                if (state_in.equals(info[0]) && mla_area_in.equals(info[1])) {
                    mla_area = info[4];
                }
            }
            //Area is still empty which means user selected preference is already in Hindi then no need to change
            if(mla_area.equals(" ")) {
                for (String[] info : infos) {
                    if (state_in.equals(info[3]) && mla_area_in.equals(info[4])) {
                        mla_area = mla_area_in;
                    }
                }
            }
        }
        return mla_area;
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
            if(current_language.equals(sLANGUAGE_ENGLISH)) {
                notification_text = context.getString(R.string.notification_update);
            } else if(current_language.equals(sLANGUAGE_MARATHI)) {
                notification_text = context.getString(R.string.notification_update_mr);
            } else {
                notification_text = context.getString(R.string.notification_update_hi);
            }
            notification_name = sCHANNEL_ID_UPDATE;
            notification_id = 1;
        }

        else if(CHANNEL_ID.equals(sCHANNEL_ID_SUNDAY)) {
            // Event handling chain doesn't handle language, so using tricks to achieve the effect
            int question_num = new Random().nextInt(Puzzle_Ques.questions.length);
            if(current_language.equals(sLANGUAGE_ENGLISH)) {
                notification_text = Puzzle_Ques.questions[question_num][0]; //context.getString(R.string.sunday_msg_hi);
            } else {
                notification_text = Puzzle_Ques.questions[question_num][6]; //context.getString(R.string.sunday_msg);
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
                        mAlertDialog = builder.create();
                        //if (!getBaseContext().isFinishing()) // Activity shudn't be finished or finishing
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
