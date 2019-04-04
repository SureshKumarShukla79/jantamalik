package in.filternet.jantamalik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import in.filternet.jantamalik.Kendra.DataFilter;

import static in.filternet.jantamalik.MainActivity.TAB_CORPORATION;
import static in.filternet.jantamalik.MainActivity.TAB_KENDRA;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class Issues extends AppCompatActivity {
    private final static String TAG ="Issues";

    private Toolbar toolbar;
    private TextView ui_coming_soon;
    private Spinner ui_spinner_state, ui_spinner_area;
    private TableLayout ui_green_table, ui_red_table;
    private ArrayAdapter state_adapter, area_adapter;
    private DataFilter dataFilter;

    private String mLanguage;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;
    private int layoutResID, titleID;
    private boolean kendra, rajya, corporation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null) {
            layoutResID = savedInstanceState.getInt("layout_id");
            //Log.e(TAG, "layout_id: " + layoutResID);
            titleID = savedInstanceState.getInt("title_id");
            //Log.e(TAG, "title_id: " + titleID);
            kendra = savedInstanceState.getBoolean("kendra", false);
            rajya = savedInstanceState.getBoolean("rajya", false);
            corporation = savedInstanceState.getBoolean("corporation", false);
        }

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSharedPref.edit();

        mLanguage  = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, MainActivity.sLANGUAGE_HINDI);

        setContentView(layoutResID);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_button(view);
            }
        });

        if(layoutResID == R.layout.issue_media_or_afeem) {
            make_clickable_links();
        }

        if(layoutResID == R.layout.issue_election_2019) {
            update_gui();
        }

    }

    private void back_button(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        if(kendra) {
            intent.putExtra(TAB_NUMBER, TAB_KENDRA);
        } else if(rajya) {
            intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        } else if(corporation) {
            intent.putExtra(TAB_NUMBER, TAB_CORPORATION);
        }
        startActivity(intent);
    }

    private void update_gui() {
        FirebaseLogger.send(this, "2019_election");
        ui_spinner_state = findViewById(R.id.state_spinner);
        ui_spinner_area = findViewById(R.id.area_spinner);
        ui_green_table = findViewById(R.id.green_table);
        ui_red_table = findViewById(R.id.red_table);
        ui_coming_soon = findViewById(R.id.coming_soon);

        String state = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);
        String area = mSharedPref.getString(MainActivity.sMP, MainActivity.DEFAULT_MP);

        // In case of Hindi, change the defaults
        if (mLanguage.equals(sLANGUAGE_HINDI)) {
            if (state.equals(MainActivity.DEFAULT_STATE))
                state = MainActivity.hiDEFAULT_STATE;
            if (area.equals(MainActivity.DEFAULT_MP))
                area = MainActivity.hiDEFAULT_MP;
        }

        editor.putString(MainActivity.sSTATE, state).commit();
        editor.putString(MainActivity.sMP, area).commit();

        // Populating GUI
        dataFilter = new DataFilter();
        state_adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, dataFilter.getStates(mLanguage));
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ui_spinner_state.setAdapter(state_adapter);

        int spinnerPosition = state_adapter.getPosition(state);
        ui_spinner_state.setSelection(spinnerPosition);

        area_adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item, dataFilter.getMPAreas(mLanguage, state));
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ui_spinner_area.setAdapter(area_adapter);

        spinnerPosition = area_adapter.getPosition(area);
        ui_spinner_area.setSelection(spinnerPosition);

        ui_spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String state = ui_spinner_state.getItemAtPosition(ui_spinner_state.getSelectedItemPosition()).toString();
                editor.putString(MainActivity.sSTATE, state).commit();

                area_adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item, dataFilter.getMPAreas(mLanguage, state));
                area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ui_spinner_area.setAdapter(area_adapter);

                String MP = mSharedPref.getString(MainActivity.sMP, MainActivity.DEFAULT_MP);
                int spinnerPosition = area_adapter.getPosition(MP);
                ui_spinner_area.setSelection(spinnerPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //spinner constituency click handler
        ui_spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String area = adapterView.getItemAtPosition(i).toString();
                editor.putString(MainActivity.sMP, area).commit();

                update_candidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        update_candidate();
    }

    private void update_candidate() {
        int total_green_candidate = num_of_candidate(Election_2019.green_bucket);
        int total_red_candidate = num_of_candidate(Election_2019.red_bucket);

        if(total_green_candidate > 0) {
            ui_green_table.removeAllViews();
            load_good_candidate(ui_green_table, total_green_candidate, Election_2019.green_bucket, R.drawable.table_border_style);
            ui_green_table.setVisibility(View.VISIBLE);
        }

        if(total_red_candidate > 0) {
            ui_red_table.removeAllViews();
            load_bad_candidate(ui_red_table, total_red_candidate, Election_2019.red_bucket, R.drawable.table_border_red);
            ui_red_table.setVisibility(View.VISIBLE);
        }

        if(total_green_candidate == 0 && total_red_candidate == 0){
            ui_coming_soon.setVisibility(View.VISIBLE);
            ui_green_table.setVisibility(View.GONE);
            ui_red_table.setVisibility(View.GONE);
        } else {
            ui_coming_soon.setVisibility(View.GONE);
        }
    }

    private void load_good_candidate(TableLayout table, int total_candidate, String[][] bucket, int color) {
        int column = 3;

        // Set table heads
        TableRow row = new TableRow(this);
        for(int j=0; j<=column; j++){
            TextView text = new TextView(this);
            if(j==0)
                text.setText("\t\t");
            else if(j==1)
                text.setText(getString(R.string.good_candidate));
            else if(j==2)
                text.setText(getString(R.string.political_party));
            else if(j==3)
                text.setText(getString(R.string.last_5_year));
            make_text_attractive(text, color);
            row.addView(text);
        }
        table.addView(row);

        // Fill the elements
        int index = get_starting_index(bucket);
        for(int i=index; i<(index+total_candidate); i++) {
            // Set serial number
            row = new TableRow(this);
            TextView text = new TextView(this);
            text.setText(bucket[i][1]);
            make_text_attractive(text, color);
            row.addView(text);

            // Set candidate data
            for(int j=0; j<column; j++) {
                text = new TextView(this);
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
                    int j_hi = j + 5;
                    text.setText(bucket[i][j_hi + 3]);
                } else {
                    text.setText(bucket[i][j + 3]);
                }
                make_text_attractive(text, color);
                row.addView(text);
            }
            table.addView(row);
            table.setGravity(Gravity.CENTER);
        }
    }

    private void load_bad_candidate(TableLayout table, int total_candidate, String[][] bucket, int color) {
        int column = 2;

        // Set table heads
        TableRow row = new TableRow(this);
        for(int j=0; j<=column; j++){
            TextView text = new TextView(this);
            if(j==0)
                text.setText("\t\t");
            else if(j==1)
                text.setText(getString(R.string.bad_candidate));
            else if(j==2)
                text.setText(getString(R.string.main_reason));
            make_text_attractive(text, color);
            row.addView(text);
        }
        table.addView(row);

        // Fill the elements
        int index = get_starting_index(bucket);
        for(int i=index; i<(index+total_candidate); i++) {
            // Set serial number
            row = new TableRow(this);
            TextView text = new TextView(this);
            text.setText(bucket[i][1]);
            make_text_attractive(text, color);
            row.addView(text);

            // Set candidate data
            for(int j=0; j<column; j++){
                text = new TextView(this);
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
                    int j_hi = j + 5;
                    text.setText(bucket[i][j_hi + 3]);
                } else {
                    text.setText(bucket[i][j + 3]);
                }
                make_text_attractive(text, color);
                row.addView(text);
            }
            table.addView(row);
            table.setGravity(Gravity.CENTER);
        }
    }


    private void make_text_attractive(TextView text, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        } else {
            text.setTextAppearance(this, R.style.TextAppearance_AppCompat_Large);
        }
        text.setPadding(5,5,5,5);
        text.setTextColor(Color.BLACK);
        text.setGravity(Gravity.CENTER);
        text.setBackgroundResource(color);
    }

    private int get_starting_index(String[][] bucket){
        int index = 0, area_column = 2;
        String constituency = mSharedPref.getString(MainActivity.sMP, null);
        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            area_column = 7;
        }

        for (int i=0; i<bucket.length; i++) {
            String area_name = bucket[i][area_column];
            if (constituency.equals(area_name))
                return i;
        }
        return index;
    }

    private int num_of_candidate(String[][] bucket) {
        int num = 0, area_column = 2;
        String constituency = mSharedPref.getString(MainActivity.sMP, null);
        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            area_column = 7;
        }

        for (String[] i : bucket) {
            String area_name = i[area_column];
            if (constituency.equals(area_name))
                num++;
        }
        return num;
    }

    private void make_clickable_links() {
        TextView l1 = findViewById(R.id.l1);
        l1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l2 = findViewById(R.id.l2);
        l2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l3 = findViewById(R.id.l3);
        l3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l4 = findViewById(R.id.l4);
        l4.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l5 = findViewById(R.id.l5);
        l5.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l6 = findViewById(R.id.l6);
        l6.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l7 = findViewById(R.id.l7);
        l7.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l8 = findViewById(R.id.l8);
        l8.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l9 = findViewById(R.id.l9);
        l9.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l10 = findViewById(R.id.l10);
        l10.setMovementMethod(LinkMovementMethod.getInstance());

        TextView l11 = findViewById(R.id.l11);
        l11.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l12 = findViewById(R.id.l12);
        l12.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l13 = findViewById(R.id.l13);
        l13.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l14 = findViewById(R.id.l14);
        l14.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l15 = findViewById(R.id.l15);
        l15.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l16 = findViewById(R.id.l16);
        l16.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l17 = findViewById(R.id.l17);
        l17.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l18 = findViewById(R.id.l18);
        l18.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l19 = findViewById(R.id.l19);
        l19.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l20 = findViewById(R.id.l20);
        l20.setMovementMethod(LinkMovementMethod.getInstance());

        TextView l21 = findViewById(R.id.l21);
        l21.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l22 = findViewById(R.id.l22);
        l22.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l23 = findViewById(R.id.l23);
        l23.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l24 = findViewById(R.id.l24);
        l24.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l25 = findViewById(R.id.l25);
        l25.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l26 = findViewById(R.id.l26);
        l26.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l27 = findViewById(R.id.l27);
        l27.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l28 = findViewById(R.id.l28);
        l28.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l29 = findViewById(R.id.l29);
        l29.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l30 = findViewById(R.id.l30);
        l30.setMovementMethod(LinkMovementMethod.getInstance());

        TextView l31 = findViewById(R.id.l31);
        l31.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l32 = findViewById(R.id.l32);
        l32.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l33 = findViewById(R.id.l33);
        l33.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l34 = findViewById(R.id.l34);
        l34.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l35 = findViewById(R.id.l35);
        l35.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l36 = findViewById(R.id.l36);
        l36.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l37 = findViewById(R.id.l37);
        l37.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l38 = findViewById(R.id.l38);
        l38.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l39 = findViewById(R.id.l39);
        l39.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l40 = findViewById(R.id.l40);
        l40.setMovementMethod(LinkMovementMethod.getInstance());

        TextView l41 = findViewById(R.id.l41);
        l41.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l42 = findViewById(R.id.l42);
        l42.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onclick_update_issue(View view) {
        Intent intent = new Intent(view.getContext(), Contact.class);
        intent.putExtra("add_issue", true);
        intent.putExtra("subject", getString(titleID));
        if(kendra) {
            intent.putExtra(TAB_NUMBER, TAB_KENDRA);
        } else if(rajya) {
            intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        } else if(corporation) {
            intent.putExtra(TAB_NUMBER, TAB_CORPORATION);
        }
        startActivity(intent);
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
}
