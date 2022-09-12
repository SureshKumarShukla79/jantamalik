package in.jantamalik.Kendra;

import static in.jantamalik.MainActivity.TAB_KENDRA;
import static in.jantamalik.MainActivity.TAB_NUMBER;
import static in.jantamalik.MainActivity.sMP_AREA;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;
import in.jantamalik.Contact;
import in.jantamalik.Issues;
import in.jantamalik.LogEvents;
import in.jantamalik.LokSabha_Election_2019.Uttar_Pradesh;
import in.jantamalik.MainActivity;
import in.jantamalik.R;

public class VoteMP extends AppCompatActivity {
    static String TAG = "VoteMP";
    private Toolbar toolbar;
    private ImageView ui_image_address;
    private CircleImageView ui_expand_protest, ui_hide_protest, ui_expand_mp_option, ui_hide_mp_option;
    private TextView ui_name, ui_phone, ui_phone2, ui_phone3, ui_email, ui_email2, ui_address, ui_source_adr;
    private FloatingActionButton ui_call, ui_call2, ui_call3, ui_mail, ui_mail2;
    private LinearLayout ui_whatsapp_group, ui_protest, ui_mp_options, ui_source;
    private TableLayout ui_green_table, ui_red_table;
    private RelativeLayout ui_no_progress, ui_other_options;
    DataFilter.MP_info mp;

    private Spinner spinnerMP;
    private ArrayAdapter<String> arrayAdapterMP;
    private DataFilter dataFilter;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;

    private String mLanguage;
    private boolean mProtestVisibility = false, mOtherOptions = false;
    private int layoutResID = 0, titleID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null) {
            layoutResID = savedInstanceState.getInt("layout_id");
            //Log.e(TAG, "layout_id: " + layoutResID);
            titleID = savedInstanceState.getInt("title_id");
            //Log.e(TAG, "title_id: " + titleID);
        }

        setContentView(R.layout.vote_mp_layout);
        LogEvents.send(this, TAG);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSharedPref.edit();

        toolbar = findViewById(R.id.toolbar_MP_layout);
        ui_name = findViewById(R.id.MP_name);
        ui_phone = findViewById(R.id.phone);
        ui_call = findViewById(R.id.call);
        ui_phone2 = findViewById(R.id.phone2);
        ui_call2 = findViewById(R.id.call2);
        ui_phone3 = findViewById(R.id.phone3);
        ui_call3 = findViewById(R.id.call3);
        ui_email = findViewById(R.id.email);
        ui_mail = findViewById(R.id.mail);
        ui_email2 = findViewById(R.id.email2);
        ui_mail2 = findViewById(R.id.mail2);
        ui_image_address = findViewById(R.id.image_address);
        ui_address = findViewById(R.id.address);
        ui_no_progress = findViewById(R.id.no_progress);
        ui_expand_protest = findViewById(R.id.expand_more);
        ui_hide_protest = findViewById(R.id.expand_less);
        ui_protest = findViewById(R.id.protest);
        ui_other_options = findViewById(R.id.other_options);
        ui_mp_options = findViewById(R.id.mp_options);
        ui_expand_mp_option = findViewById(R.id.expand_option);
        ui_hide_mp_option = findViewById(R.id.hide_option);
        ui_green_table = findViewById(R.id.green_table);
        ui_red_table = findViewById(R.id.red_table);
        ui_source = findViewById(R.id.source);

        ui_whatsapp_group = findViewById(R.id.whatsapp_group);

        ui_source_adr = findViewById(R.id.source_adr);
        ui_source_adr.setMovementMethod(LinkMovementMethod.getInstance());

        spinnerMP = findViewById(R.id.MP_spinner);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(this::back_button);

        String MP = mSharedPref.getString(sMP_AREA, "");

        // Populating GUI
        dataFilter = new DataFilter();
        // Load defaults

        //populating MP Area
        arrayAdapterMP = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_text_style, dataFilter.getMPAreas());
        arrayAdapterMP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMP.setAdapter(arrayAdapterMP);

        int spinnerPosition = arrayAdapterMP.getPosition(MP);
        spinnerMP.setSelection(spinnerPosition);
        //Log.e(TAG, "MP def: " + MP);
        // defaults over

        // set for dynamic handling
        //spinner constituency click handler
        spinnerMP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String MPArea = adapterView.getItemAtPosition(i).toString();
                //Log.e(TAG, "spin MP : " + i + " " + l + " " + MPArea);
                editor.putString(sMP_AREA, MPArea).commit();

                LogEvents.sendWithValue(getBaseContext(), sMP_AREA, MPArea);

                updateMP();
                update_candidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        updateMP();
        update_candidate();

        ui_no_progress.setOnClickListener(view -> {
            if (!mProtestVisibility) {
                mProtestVisibility = true;
                show_protest();
            } else {
                mProtestVisibility = false;
                hide_protest();
            }
        });

        ui_other_options.setOnClickListener(view -> {
            if (!mOtherOptions) {
                mOtherOptions = true;
                show_options();
            } else {
                mOtherOptions = false;
                hide_options();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void updateMP() {
        DataFilter dataFilter = new DataFilter();
        String MPArea = mSharedPref.getString(sMP_AREA, "");
        //mp = dataFilter.new MP_info();
        mp = dataFilter.getMPInfo(this, mLanguage, MPArea);

        //Log.e(TAG, MPArea + " " + mp.name + " " + mp.phone + " " + mp.email + " " + mp.address);
        ui_name.setText(mp.name);

        if(mp.phone == null || mp.phone.equals("")) {
            ui_call.setVisibility(View.GONE);
            ui_phone.setVisibility(View.GONE);
        } else {
            ui_phone.setText(mp.phone);
            ui_call.setVisibility(View.VISIBLE);
            ui_phone.setVisibility(View.VISIBLE);
        }

        if(mp.phone2 == null || mp.phone2.equals("")) {
            ui_call2.setVisibility(View.GONE);
            ui_phone2.setVisibility(View.GONE);
        }
        else {
            ui_phone2.setText(mp.phone2);
            ui_call2.setVisibility(View.VISIBLE);
            ui_phone2.setVisibility(View.VISIBLE);
        }

        if(mp.phone3 == null || mp.phone3.equals("")) {
            ui_call3.setVisibility(View.GONE);
            ui_phone3.setVisibility(View.GONE);
        }
        else {
            ui_phone3.setText(mp.phone3);
            ui_call3.setVisibility(View.VISIBLE);
            ui_phone3.setVisibility(View.VISIBLE);
        }

        if(mp.email == null || mp.email.equals("")) {
            ui_mail.setVisibility(View.GONE);
            ui_email.setVisibility(View.GONE);
        }
        else {
            ui_email.setText(mp.email);
            ui_mail.setVisibility(View.VISIBLE);
            ui_email.setVisibility(View.VISIBLE);
        }

        if(mp.email2 == null || mp.email2.equals("")) {
            ui_mail2.setVisibility(View.GONE);
            ui_email2.setVisibility(View.GONE);
        }
        else {
            ui_email2.setText(mp.email2);
            ui_mail2.setVisibility(View.VISIBLE);
            ui_email2.setVisibility(View.VISIBLE);
        }

        if(mp.address == null || mp.address.equals("")) {
            ui_address.setVisibility(View.GONE);
            ui_image_address.setVisibility(View.GONE);
        } else {
            ui_address.setText(mp.address);
            ui_address.setVisibility(View.VISIBLE);
            ui_image_address.setVisibility(View.VISIBLE);
        }

        String tmp = getLoksabha_Group(this);
        if (tmp.equals("")) {
            ui_whatsapp_group.setVisibility(View.GONE);
        } else {
            ui_whatsapp_group.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_button(toolbar.getRootView()); // TODO animation is too strong
    }

    private void back_button(View view) {
        if(layoutResID != 0 && titleID != 0) {
            Intent intent = new Intent(view.getContext(), Issues.class);
            intent.putExtra("layout_id", layoutResID);
            intent.putExtra("title_id", titleID);
            startActivity(intent);
        } else {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra(TAB_NUMBER, TAB_KENDRA);
            startActivity(intent);
        }
    }

    public void onclick_call_mp(View view) {
        if(mp.phone.equals("") && mp.phone2.equals("") && mp.phone3.equals(""))
            return;

        Uri number = null;
        switch (view.getId()) {
            case R.id.phone:
            case R.id.call:
                number = Uri.parse("tel:" + mp.phone);
                break;
            case R.id.phone2:
            case R.id.call2:
                number = Uri.parse("tel:" + mp.phone2);
                break;
            case R.id.phone3:
            case R.id.call3:
                number = Uri.parse("tel:" + mp.phone3);
                break;
        }

        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        try { // Calling not available on Tablet devices
            startActivity(callIntent);
            LogEvents.send(this, "MP_Phone");
        } catch (Exception exception){
            Toast.makeText(this, "Unable to CALL", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }

    public void onclick_email_mp(View view) {
        if(mp.email.equals("") && mp.email2.equals(""))
            return;

        String[] TO = new String[0];

        switch (view.getId()) {
            case R.id.email:
            case R.id.mail:
                TO = new String[]{mp.email};
                break;
            case R.id.email2:
            case R.id.mail2:
                TO = new String[]{mp.email2};
                break;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setDataAndType(Uri.parse("mailto:"), "text/plain");
        intent.setPackage("com.google.android.gm");
        intent.putExtra(Intent.EXTRA_EMAIL, TO);

        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                //Log.v(TAG, "1st option");
                startActivity(intent);
            } else {
                //Log.v(TAG, "2nd option");
                startActivity(Intent.createChooser(intent, "Sending mail..."));
                finish();
            }
            LogEvents.send(this, "MP_Email");
        } catch (Exception ex) {
            Toast.makeText(this, "Gmail app didn't respond.", Toast.LENGTH_LONG).show();
        }
    }

    public void onclick_update_mp(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mp", true);
        startActivity(intent);
    }

    public void onclick_MP(View view){
        Intent intent = new Intent(view.getContext(), VoteMP.class);
        startActivity(intent);
    }

    public void onclick_WhatsApp(View view) {
        //Log.e(TAG, "whats icon clicked" + mWhatsapp_group);
        String whatsapp_group = getLoksabha_Group(this);
        if (whatsapp_group.equals(""))
            return;
        Uri uri = Uri.parse(whatsapp_group);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public static String getLoksabha_Group(Context context) {
        String whatsapp_group = "";

        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String area = shared_pref.getString(sMP_AREA, "");
        area = "Varanasi"; // MVP let one group grow to 500 then unleash

        for (int i = 0; i < LokSabhaGroups.all_groups.length; i++) {
            if (area.equals(LokSabhaGroups.all_groups[i][0])) {
                whatsapp_group = LokSabhaGroups.all_groups[i][1];
                break;
            }
        }
        //Log.e(TAG, tmp);

        return whatsapp_group;
    }

    public void onclick_show_protest(View view) {
        show_protest();
    }

    private void show_protest() {
        LogEvents.send(this, "Protest");

        ui_expand_protest.setVisibility(View.GONE);
        ui_protest.setVisibility(View.VISIBLE);
        ui_hide_protest.setVisibility(View.VISIBLE);
    }

    public void onclick_hide_protest(View view) {
        hide_protest();
    }

    private void hide_protest() {
        ui_expand_protest.setVisibility(View.VISIBLE);
        ui_protest.setVisibility(View.GONE);
        ui_hide_protest.setVisibility(View.GONE);
    }

    public void onclick_show_options(View view) {
        show_options();
    }

    private void show_options() {
        LogEvents.send(this, "Election_2019");

        ui_expand_mp_option.setVisibility(View.GONE);
        ui_mp_options.setVisibility(View.VISIBLE);
        ui_hide_mp_option.setVisibility(View.VISIBLE);

        update_candidate();
    }

    public void onclick_hide_options(View view) {
        hide_options();
    }

    private void hide_options() {
        ui_expand_mp_option.setVisibility(View.VISIBLE);
        ui_mp_options.setVisibility(View.GONE);
        ui_hide_mp_option.setVisibility(View.GONE);
    }

    private void update_candidate() {
        String[][] green_bucket = get_green_bucket();
        String[][] red_bucket = get_red_bucket();

        int total_green_candidate = num_of_candidate(green_bucket);
        int total_red_candidate = num_of_candidate(red_bucket);

        if(total_green_candidate > 0) {
            ui_green_table.removeAllViews();
            load_good_candidate(total_green_candidate, green_bucket);
            ui_green_table.setVisibility(View.VISIBLE);
        }

        if(total_red_candidate > 0) {
            ui_red_table.removeAllViews();
            load_bad_candidate(total_red_candidate, red_bucket);
            ui_red_table.setVisibility(View.VISIBLE);
        }

        if(total_green_candidate == 0 && total_red_candidate == 0){
            ui_green_table.setVisibility(View.GONE);
            ui_red_table.setVisibility(View.GONE);
        } else if(total_red_candidate == 0) {
            ui_red_table.setVisibility(View.GONE);
        } else if(total_green_candidate == 0) {
            ui_source.setVisibility(View.VISIBLE);
            ui_green_table.setVisibility(View.GONE);
        }
    }

    private String[][] get_green_bucket() {
        return Uttar_Pradesh.green_bucket;
    }

    private String[][] get_red_bucket() {
        return Uttar_Pradesh.red_bucket;
    }

    private void load_good_candidate(int total_candidate, String[][] bucket) {
        int column = 3;

        // Set table heads
        TableRow row = new TableRow(this);
        for(int j=0; j<column; j++){
            TextView text = new TextView(this);
            if(j==0) {
                text.setText(getString(R.string.good_candidate));
                make_text_attractive(text, R.drawable.table_border_style);
                text.setTypeface(null, Typeface.BOLD);
            }
            else if(j==1) {
                text.setText(getString(R.string.political_party));
                make_text_attractive(text, R.drawable.table_border_style);
                text.setTypeface(null, Typeface.BOLD);
            }
            else if(j==2) {
                text.setText(getString(R.string.total_assets));
                make_text_attractive(text, R.drawable.table_border_style);
                text.setTypeface(null, Typeface.BOLD);
            }

            row.addView(text);
        }
        ui_green_table.addView(row);

        // Fill the elements
        int index = get_starting_index(bucket);
        for(int i=index; i<(index+total_candidate); i++) {
            row = new TableRow(this);
            // Set candidate data
            for(int j=0; j<column; j++) {
                TextView text = new TextView(this);
                if(j==2) {
                    text.setClickable(true);
                    text.setMovementMethod(LinkMovementMethod.getInstance());
                    text.setPadding(5, 5, 5, 5);
                    text.setLinkTextColor(Color.BLUE);
                    text.setGravity(Gravity.CENTER);
                    text.setBackgroundResource(R.drawable.table_border_style);
                    text.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));

                    String assets = bucket[i][j + 2];
                    if (assets.contains("Lac"))
                        assets = assets.replace("Lac", " लाख");
                    else if (assets.contains("Crore"))
                        assets = assets.replace("Crore", " करोड़");

                    String url_link = assets + "<a href='" + bucket[i][j + 3] + "'> " + getString(R.string.know_more) + "</a>";// IMP: Don't lead space on left/right side of url, that doesn't work
                    //Log.e(TAG, "Link: " + url_link);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //API 24
                        text.setText(Html.fromHtml(url_link, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        text.setText(Issues.for_lower_version(url_link));
                    }
                } else {
                    int j_hi = j + 6;
                    text.setText(bucket[i][j_hi + 2]);
                    make_text_attractive(text, R.drawable.table_border_style);
                }
                row.addView(text);
            }
            ui_green_table.addView(row);
            ui_green_table.setGravity(Gravity.CENTER);
            ui_green_table.setStretchAllColumns(false);
        }
    }

    private void load_bad_candidate(int total_candidate, String[][] bucket) {
        int column = 2;

        // Set table heads
        TableRow row = new TableRow(this);
        for(int j=0; j<column; j++){
            TextView text = new TextView(this);
            if(j==0)
                text.setText(getString(R.string.bad_candidate));
            else
                text.setText(getString(R.string.main_reason));
            make_text_attractive(text, R.drawable.table_border_red);
            text.setTypeface(null, Typeface.BOLD);
            row.addView(text);
        }
        ui_red_table.addView(row);

        // Fill the elements
        int index = get_starting_index(bucket);
        for(int i=index; i<(index+total_candidate); i++) {
            // Set candidate data
            row = new TableRow(this);
            for(int j=0; j<column; j++){
                TextView text = new TextView(this);

                if(j==1) {
                    switch (bucket[i][j + 2]) {
                        case "1":
                            text.setText(R.string.foreign_funding);
                            text.setMovementMethod(LinkMovementMethod.getInstance());
                            text.setLinksClickable(true);
                            break;
                        case "2":
                            text.setText(R.string.criminal_case);
                            break;
                        case "3":
                            text.setText(R.string.aged);
                            break;
                        case "4":
                            text.setText(R.string.not_graduate);
                            break;
                    }
                } else {
                    int j_hi = j + 5;
                    text.setText(bucket[i][j_hi + 2]);
                }

                make_text_attractive(text, R.drawable.table_border_red);
                text.setLinkTextColor(Color.BLUE);

                row.addView(text);
            }
            ui_red_table.addView(row);
            ui_red_table.setGravity(Gravity.CENTER);
        }
    }

    private int get_starting_index(String[][] bucket) {
        int index = 0, area_column = 1;
        String constituency = mSharedPref.getString(sMP_AREA, "");

        for (int i = 0; i < MPdata.all_MPs.length; i++) {
            if (constituency.equals(MPdata.all_MPs[i][2])) {
                constituency = MPdata.all_MPs[i][0];
            }
        }

        for (int i = 0; i < bucket.length; i++) {
            String area_name = bucket[i][area_column];
            if (constituency.equals(area_name))
                return i;
        }
        return index;
    }

    private void make_text_attractive(TextView text, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        } else {
            Issues.text_appearance_for_lower_version(this, text);
        }

        text.setPadding(5,5,5,5);
        text.setTextColor(Color.BLACK);
        text.setGravity(Gravity.CENTER);
        text.setBackgroundResource(color);
        text.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));
    }

    private int num_of_candidate(String[][] bucket) {
        int num = 0, area_column = 1;
        String constituency = mSharedPref.getString(sMP_AREA, "");

        for (int i = 0; i < MPdata.all_MPs.length; i++) {
            if (constituency.equals(MPdata.all_MPs[i][4])) {
                constituency = MPdata.all_MPs[i][1];
            }
        }

        for (String[] i : bucket) {
            String area_name = i[area_column];
            if (constituency.equals(area_name))
                num++;
        }

        return num;
    }
}
