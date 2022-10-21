package in.jantamalik;

import static in.jantamalik.MainActivity.TAB_243;
import static in.jantamalik.MainActivity.TAB_KENDRA;
import static in.jantamalik.MainActivity.TAB_NUMBER;
import static in.jantamalik.MainActivity.USER_SHARE_APP;
import static in.jantamalik.MainActivity.sMP_AREA;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import in.jantamalik.Kendra.DataFilter;
import in.jantamalik.Kendra.VoteMP;
import in.jantamalik.Rajya.VoteVidhayak;

public class Issues extends AppCompatActivity {
    private final static String TAG ="Issues";

    private Toolbar toolbar;
    private TextView ui_coming_soon;
    private LinearLayout ui_source;
    private Spinner ui_spinner_area;
    private TableLayout ui_green_table, ui_red_table;
    private ArrayAdapter<String> area_adapter;
    private DataFilter dataFilter;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;
    private int layoutResID, titleID;
    private String titleName;
    private boolean issues, kendra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null) {
            layoutResID = savedInstanceState.getInt("layout_id");
            //Log.e(TAG, "layout_id: " + layoutResID);
            titleID = savedInstanceState.getInt("title_id");
            //Log.e(TAG, "title_id: " + titleID);
            titleName = savedInstanceState.getString("title");
            issues = savedInstanceState.getBoolean("issues", false);
            kendra = savedInstanceState.getBoolean("kendra", false);
        }

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSharedPref.edit();

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
            make_clickable_links_media(); // href alone doesn't work
        } else if (layoutResID == R.layout.issue_election_2019) {
            update_gui();
        } else if (layoutResID == R.layout.issue_employment) {
            TextView ui_toi_link = findViewById(R.id.toi_link);
            ui_toi_link.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

    private void back_button(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        if(kendra) {
            intent.putExtra(TAB_NUMBER, TAB_KENDRA);
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_button(toolbar.getRootView());
    }

    private void update_gui() {
        ui_spinner_area = findViewById(R.id.area_spinner);
        ui_green_table = findViewById(R.id.green_table);
        ui_red_table = findViewById(R.id.red_table);
        ui_source = findViewById(R.id.source);
        ui_coming_soon = findViewById(R.id.coming_soon);

        TextView ui_filter_text = findViewById(R.id.filter_text);
        ui_filter_text.setMovementMethod(LinkMovementMethod.getInstance());
        TextView ui_source_adr = findViewById(R.id.source_adr);
        ui_source_adr.setMovementMethod(LinkMovementMethod.getInstance());

        String area = mSharedPref.getString(sMP_AREA, "");

        // Populating GUI
        dataFilter = new DataFilter();

        area_adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_text_style, dataFilter.getMPAreas());
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ui_spinner_area.setAdapter(area_adapter);

        int spinnerPosition = area_adapter.getPosition(area);
        ui_spinner_area.setSelection(spinnerPosition);

        //spinner constituency click handler
        ui_spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String area = adapterView.getItemAtPosition(i).toString();
                editor.putString(sMP_AREA, area).commit();

                String tmp = area;
                tmp = tmp.replace(" ", "_");
                tmp = tmp.replace("&", "and");
                LogEvents.sendWithValue(getBaseContext(), sMP_AREA, tmp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
                        text.setText(for_lower_version(url_link));
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

    @SuppressWarnings("deprecation")
    public static Spanned for_lower_version(String text) {
        return Html.fromHtml(text);
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

    private int get_starting_index(String[][] bucket){
        int index = 0, area_column = 1;
        String constituency = mSharedPref.getString(sMP_AREA, "");

        for (int i=0; i<bucket.length; i++) {
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
            text_appearance_for_lower_version(this, text);
        }

        text.setPadding(5,5,5,5);
        text.setTextColor(Color.BLACK);
        text.setGravity(Gravity.CENTER);
        text.setBackgroundResource(color);
        text.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));
    }

    @SuppressWarnings("deprecation")
    public static void text_appearance_for_lower_version(Context context, TextView text){
        text.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
    }

    private int num_of_candidate(String[][] bucket) {
        int num = 0, area_column = 1;
        String constituency = mSharedPref.getString(sMP_AREA, "");

        for (String[] i : bucket) {
            String area_name = i[area_column];
            if (constituency.equals(area_name))
                num++;
        }

        return num;
    }

    public void onclick_read_online_full(View view) {
        LogEvents.send(this, "Read_Online");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.COI_Hindi)));
    }

    public void onclick_mp_mla_contact(View view) {
        LogEvents.send(this, "MP_MLA_contact");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MP_MLA_contact)));
    }

    public void onclick_jantamalik_pdf(View view) {
        LogEvents.send(this, "JantaMalik_PDF");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.JantaMalik_PDF)));
    }

    private void make_clickable_links_media() {
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
        if(issues) {
            intent.putExtra(TAB_NUMBER, TAB_243);
        }
        startActivity(intent);
    }

    public void onclick_share_button(View view) {
        LogEvents.send(this, "Share");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        String shareBody = getString(R.string.share_message);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Important");
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                shareBody + USER_SHARE_APP);
        startActivity(intent);
    }

    public void onclick_open_donate(View view) {
        startActivity(Common.open_donate(view));
    }

    public void onclick_my_MP_screen(View view) {
        LogEvents.send(this, titleName + "_MP");

        Intent intent = new Intent(view.getContext(), VoteMP.class);
        intent.putExtra("layout_id", layoutResID);
        intent.putExtra("title_id", titleID);
        startActivity(intent);
    }

    public void onclick_my_MLA_screen(View view) {

            LogEvents.send(this, titleName + "_MLA");

            Intent intent = new Intent(view.getContext(), VoteVidhayak.class);
            intent.putExtra("layout_id", layoutResID);
            intent.putExtra("title_id", titleID);
            startActivity(intent);

    }

    public void onclick_my_Parshad_screen(View view) {
        LogEvents.send(this, titleName + "_Parshad");

        Toast.makeText(getApplicationContext(), getText(R.string.parshad_missing), Toast.LENGTH_LONG).show();
    }
}
