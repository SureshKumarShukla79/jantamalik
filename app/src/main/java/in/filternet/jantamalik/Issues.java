package in.filternet.jantamalik;

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

import in.filternet.jantamalik.Kendra.DataFilter;
import in.filternet.jantamalik.Kendra.MPdata;
import in.filternet.jantamalik.Kendra.VoteMP;
import in.filternet.jantamalik.LokSabha_Election_2019.Andaman_and_Nicobar_Islands;
import in.filternet.jantamalik.LokSabha_Election_2019.Andhra_Pradesh;
import in.filternet.jantamalik.LokSabha_Election_2019.Arunachal_Pradesh;
import in.filternet.jantamalik.LokSabha_Election_2019.Assam;
import in.filternet.jantamalik.LokSabha_Election_2019.Bihar;
import in.filternet.jantamalik.LokSabha_Election_2019.Chandigarh;
import in.filternet.jantamalik.LokSabha_Election_2019.Chhattisgarh;
import in.filternet.jantamalik.LokSabha_Election_2019.Dadra_and_Nagar_Haveli;
import in.filternet.jantamalik.LokSabha_Election_2019.Daman_and_Diu;
import in.filternet.jantamalik.LokSabha_Election_2019.Goa;
import in.filternet.jantamalik.LokSabha_Election_2019.Gujarat;
import in.filternet.jantamalik.LokSabha_Election_2019.Haryana;
import in.filternet.jantamalik.LokSabha_Election_2019.Himachal_Pradesh;
import in.filternet.jantamalik.LokSabha_Election_2019.Jammu_and_Kashmir;
import in.filternet.jantamalik.LokSabha_Election_2019.Jharkhand;
import in.filternet.jantamalik.LokSabha_Election_2019.Karnataka;
import in.filternet.jantamalik.LokSabha_Election_2019.Kerala;
import in.filternet.jantamalik.LokSabha_Election_2019.Lakshadweep;
import in.filternet.jantamalik.LokSabha_Election_2019.Madhya_Pradesh;
import in.filternet.jantamalik.LokSabha_Election_2019.Maharashtra;
import in.filternet.jantamalik.LokSabha_Election_2019.Manipur;
import in.filternet.jantamalik.LokSabha_Election_2019.Meghalaya;
import in.filternet.jantamalik.LokSabha_Election_2019.Mizoram;
import in.filternet.jantamalik.LokSabha_Election_2019.Nagaland;
import in.filternet.jantamalik.LokSabha_Election_2019.National_Capital_Territory_of_Delhi;
import in.filternet.jantamalik.LokSabha_Election_2019.Odisha;
import in.filternet.jantamalik.LokSabha_Election_2019.Puducherry;
import in.filternet.jantamalik.LokSabha_Election_2019.Punjab;
import in.filternet.jantamalik.LokSabha_Election_2019.Rajasthan;
import in.filternet.jantamalik.LokSabha_Election_2019.Sikkim;
import in.filternet.jantamalik.LokSabha_Election_2019.Tamil_Nadu;
import in.filternet.jantamalik.LokSabha_Election_2019.Telangana;
import in.filternet.jantamalik.LokSabha_Election_2019.Tripura;
import in.filternet.jantamalik.LokSabha_Election_2019.Uttar_Pradesh;
import in.filternet.jantamalik.LokSabha_Election_2019.Uttarakhand;
import in.filternet.jantamalik.LokSabha_Election_2019.West_Bengal;
import in.filternet.jantamalik.Rajya.VoteVidhayak;

import static in.filternet.jantamalik.MainActivity.TAB_ISSUE;
import static in.filternet.jantamalik.MainActivity.TAB_KENDRA;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.USER_SHARE_APP;
import static in.filternet.jantamalik.MainActivity.sMP_AREA;
import static in.filternet.jantamalik.MainActivity.sSTATE;

public class Issues extends AppCompatActivity {
    private final static String TAG ="Issues";

    private Toolbar toolbar;
    private TextView ui_coming_soon;
    private LinearLayout ui_source;
    private Spinner ui_spinner_state, ui_spinner_area;
    private TableLayout ui_green_table, ui_red_table;
    private ArrayAdapter<String> state_adapter;
    private ArrayAdapter<String> area_adapter;
    private DataFilter dataFilter;

    private String mLanguage;
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
        ui_spinner_state = findViewById(R.id.state_spinner);
        ui_spinner_area = findViewById(R.id.area_spinner);
        ui_green_table = findViewById(R.id.green_table);
        ui_red_table = findViewById(R.id.red_table);
        ui_source = findViewById(R.id.source);
        ui_coming_soon = findViewById(R.id.coming_soon);

        TextView ui_filter_text = findViewById(R.id.filter_text);
        ui_filter_text.setMovementMethod(LinkMovementMethod.getInstance());
        TextView ui_source_adr = findViewById(R.id.source_adr);
        ui_source_adr.setMovementMethod(LinkMovementMethod.getInstance());

        String state = mSharedPref.getString(sSTATE, MainActivity.DEFAULT_STATE);
        String area = mSharedPref.getString(sMP_AREA, MainActivity.DEFAULT_MP);

        // Populating GUI
        dataFilter = new DataFilter();
        state_adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_text_style, dataFilter.getStates(mLanguage));
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ui_spinner_state.setAdapter(state_adapter);

        int spinnerPosition = state_adapter.getPosition(state);
        ui_spinner_state.setSelection(spinnerPosition);

        area_adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_text_style, dataFilter.getMPAreas(mLanguage, state));
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ui_spinner_area.setAdapter(area_adapter);

        spinnerPosition = area_adapter.getPosition(area);
        ui_spinner_area.setSelection(spinnerPosition);

        ui_spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String state = ui_spinner_state.getItemAtPosition(ui_spinner_state.getSelectedItemPosition()).toString();
                editor.putString(sSTATE, state).commit();

                String tmp = state;
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {// Firebase needs English, cant handle Hindi
                    String state_out = MainActivity.get_state(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
                    tmp = state_out;
                }
                tmp = tmp.replace(" ", "_");
                tmp = tmp.replace("&", "and");
                LogEvents.sendWithValue(getBaseContext(), sSTATE, tmp);

                area_adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_text_style, dataFilter.getMPAreas(mLanguage, state));
                area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ui_spinner_area.setAdapter(area_adapter);

                String MP = mSharedPref.getString(sMP_AREA, MainActivity.DEFAULT_MP);
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
                editor.putString(sMP_AREA, area).commit();

                String tmp = area;
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {// Firebase needs English, cant handle Hindi
                    String area_out = MainActivity.get_MP_area(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
                    tmp = area_out;
                }
                tmp = tmp.replace(" ", "_");
                tmp = tmp.replace("&", "and");
                LogEvents.sendWithValue(getBaseContext(), sMP_AREA, tmp);

                update_candidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        update_candidate();
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
            ui_coming_soon.setVisibility(View.VISIBLE);
            ui_source.setVisibility(View.GONE);
            ui_green_table.setVisibility(View.GONE);
            ui_red_table.setVisibility(View.GONE);
        } else if(total_red_candidate == 0) {
            ui_source.setVisibility(View.VISIBLE);
            ui_red_table.setVisibility(View.GONE);
            ui_coming_soon.setVisibility(View.GONE);
        } else if(total_green_candidate == 0) {
            ui_source.setVisibility(View.VISIBLE);
            ui_green_table.setVisibility(View.GONE);
            ui_coming_soon.setVisibility(View.GONE);
        } else {
            ui_source.setVisibility(View.VISIBLE);
            ui_coming_soon.setVisibility(View.GONE);
        }
    }

    private String[][] get_green_bucket() {
        String[][] bucket = new String[0][];

        String state = mSharedPref.getString(sSTATE, null);
        switch (state) {
            case "Andaman & Nicobar Islands":
            case "अण्डमान और निकोबार द्वीपसमूह":
                bucket = Andaman_and_Nicobar_Islands.green_bucket;
                break;

            case "Andhra Pradesh":
            case "आन्ध्र प्रदेश":
                bucket = Andhra_Pradesh.green_bucket;
                break;

            case "Arunachal Pradesh":
            case "अरुणाचल प्रदेश":
                bucket = Arunachal_Pradesh.green_bucket;
                break;

            case "Assam":
            case "असम":
                bucket = Assam.green_bucket;
                break;

            case "Bihar":
            case "बिहार":
                bucket = Bihar.green_bucket;
                break;

            case "Chandigarh":
            case "चण्डीगढ़":
                bucket = Chandigarh.green_bucket;
                break;

            case "Chhattisgarh":
            case "छत्तीसगढ़":
                bucket = Chhattisgarh.green_bucket;
                break;

            case "Dadra & Nagar Haveli":
            case "दादरा और नगर हवेली":
                bucket = Dadra_and_Nagar_Haveli.green_bucket;
                break;

            case "Daman & Diu":
            case "दमन और दीव":
                bucket = Daman_and_Diu.green_bucket;
                break;

            case "Goa":
            case "गोवा":
                bucket = Goa.green_bucket;
                break;

            case "Gujarat":
            case "गुजरात":
                bucket = Gujarat.green_bucket;
                break;

            case "Haryana":
            case "हरियाणा":
                bucket = Haryana.green_bucket;
                break;

            case "Himachal Pradesh":
            case "हिमाचल प्रदेश":
                bucket = Himachal_Pradesh.green_bucket;
                break;

            case "Jammu & Kashmir":
            case "जम्मू और कश्मीर":
                bucket = Jammu_and_Kashmir.green_bucket;
                break;

            case "Jharkhand":
            case "झारखण्ड":
                bucket = Jharkhand.green_bucket;
                break;

            case "Karnataka":
            case "कर्नाटक":
                bucket = Karnataka.green_bucket;
                break;

            case "Kerala":
            case "केरल":
                bucket = Kerala.green_bucket;
                break;

            case "Lakshadweep":
            case "लक्षद्वीप":
                bucket = Lakshadweep.green_bucket;
                break;

            case "Madhya Pradesh":
            case "मध्य प्रदेश":
                bucket = Madhya_Pradesh.green_bucket;
                break;

            case "Maharashtra":
            case "महाराष्ट्र":
                bucket = Maharashtra.green_bucket;
                break;

            case "Manipur":
            case "मणिपुर":
                bucket = Manipur.green_bucket;
                break;

            case "Meghalaya":
            case "मेघालय":
                bucket = Meghalaya.green_bucket;
                break;

            case "Mizoram":
            case "मिज़ोरम":
                bucket = Mizoram.green_bucket;
                break;

            case "Nagaland":
            case "नागालैण्ड":
                bucket = Nagaland.green_bucket;
                break;

            case "National Capital Territory of Delhi":
            case "दिल्ली":
                bucket = National_Capital_Territory_of_Delhi.green_bucket;
                break;

            case "Odisha":
            case "ओडिशा":
                bucket = Odisha.green_bucket;
                break;

            case "Puducherry":
            case "पुदुच्चेरी":
                bucket = Puducherry.green_bucket;
                break;

            case "Punjab":
            case "पंजाब":
                bucket = Punjab.green_bucket;
                break;

            case "Rajasthan":
            case "राजस्थान":
                bucket = Rajasthan.green_bucket;
                break;

            case "Sikkim":
            case "सिक्किम":
                bucket = Sikkim.green_bucket;
                break;

            case "Tamil Nadu":
            case "तमिल नाडु":
                bucket = Tamil_Nadu.green_bucket;
                break;

            case "Telangana":
            case "तेलंगाना":
                bucket = Telangana.green_bucket;
                break;

            case "Tripura":
            case "त्रिपुरा":
                bucket = Tripura.green_bucket;
                break;

            case "Uttar Pradesh":
            case "उत्तर प्रदेश":
                bucket = Uttar_Pradesh.green_bucket;
                break;

            case "Uttarakhand":
            case "उत्तराखण्ड":
                bucket = Uttarakhand.green_bucket;
                break;

            case "West Bengal":
            case "पश्चिम बंगाल":
                bucket = West_Bengal.green_bucket;
                break;
        }

        return bucket;
    }

    private String[][] get_red_bucket() {
        String[][] bucket = new String[0][];

        String state = mSharedPref.getString(sSTATE, null);
        switch (state) {
            case "Andaman & Nicobar Islands":
            case "अण्डमान और निकोबार द्वीपसमूह":
                bucket = Andaman_and_Nicobar_Islands.red_bucket;
                break;

            case "Andhra Pradesh":
            case "आन्ध्र प्रदेश":
                bucket = Andhra_Pradesh.red_bucket;
                break;

            case "Arunachal Pradesh":
            case "अरुणाचल प्रदेश":
                bucket = Arunachal_Pradesh.red_bucket;
                break;

            case "Assam":
            case "असम":
                bucket = Assam.red_bucket;
                break;

            case "Bihar":
            case "बिहार":
                bucket = Bihar.red_bucket;
                break;

            case "Chandigarh":
            case "चण्डीगढ़":
                bucket = Chandigarh.red_bucket;
                break;

            case "Chhattisgarh":
            case "छत्तीसगढ़":
                bucket = Chhattisgarh.red_bucket;
                break;

            case "Dadra & Nagar Haveli":
            case "दादरा और नगर हवेली":
                bucket = Dadra_and_Nagar_Haveli.red_bucket;
                break;

            case "Daman & Diu":
            case "दमन और दीव":
                bucket = Daman_and_Diu.red_bucket;
                break;

            case "Goa":
            case "गोवा":
                bucket = Goa.red_bucket;
                break;

            case "Gujarat":
            case "गुजरात":
                bucket = Gujarat.red_bucket;
                break;

            case "Haryana":
            case "हरियाणा":
                bucket = Haryana.red_bucket;
                break;

            case "Himachal Pradesh":
            case "हिमाचल प्रदेश":
                bucket = Himachal_Pradesh.red_bucket;
                break;

            case "Jammu & Kashmir":
            case "जम्मू और कश्मीर":
                bucket = Jammu_and_Kashmir.red_bucket;
                break;

            case "Jharkhand":
            case "झारखण्ड":
                bucket = Jharkhand.red_bucket;
                break;

            case "Karnataka":
            case "कर्नाटक":
                bucket = Karnataka.red_bucket;
                break;

            case "Kerala":
            case "केरल":
                bucket = Kerala.red_bucket;
                break;

            case "Lakshadweep":
            case "लक्षद्वीप":
                bucket = Lakshadweep.red_bucket;
                break;

            case "Madhya Pradesh":
            case "मध्य प्रदेश":
                bucket = Madhya_Pradesh.red_bucket;
                break;

            case "Maharashtra":
            case "महाराष्ट्र":
                bucket = Maharashtra.red_bucket;
                break;

            case "Manipur":
            case "मणिपुर":
                bucket = Manipur.red_bucket;
                break;

            case "Meghalaya":
            case "मेघालय":
                bucket = Meghalaya.red_bucket;
                break;

            case "Mizoram":
            case "मिज़ोरम":
                bucket = Mizoram.red_bucket;
                break;

            case "Nagaland":
            case "नागालैण्ड":
                bucket = Nagaland.red_bucket;
                break;

            case "National Capital Territory of Delhi":
            case "दिल्ली":
                bucket = National_Capital_Territory_of_Delhi.red_bucket;
                break;

            case "Odisha":
            case "ओडिशा":
                bucket = Odisha.red_bucket;
                break;

            case "Puducherry":
            case "पुदुच्चेरी":
                bucket = Puducherry.red_bucket;
                break;

            case "Punjab":
            case "पंजाब":
                bucket = Punjab.red_bucket;
                break;

            case "Rajasthan":
            case "राजस्थान":
                bucket = Rajasthan.red_bucket;
                break;

            case "Sikkim":
            case "सिक्किम":
                bucket = Sikkim.red_bucket;
                break;

            case "Tamil Nadu":
            case "तमिल नाडु":
                bucket = Tamil_Nadu.red_bucket;
                break;

            case "Telangana":
            case "तेलंगाना":
                bucket = Telangana.red_bucket;
                break;

            case "Tripura":
            case "त्रिपुरा":
                bucket = Tripura.red_bucket;
                break;

            case "Uttar Pradesh":
            case "उत्तर प्रदेश":
                bucket = Uttar_Pradesh.red_bucket;
                break;

            case "Uttarakhand":
            case "उत्तराखण्ड":
                bucket = Uttarakhand.red_bucket;
                break;

            case "West Bengal":
            case "पश्चिम बंगाल":
                bucket = West_Bengal.red_bucket;
                break;
        }

        return bucket;
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
                    text.setPadding(5,5,5,5);
                    text.setLinkTextColor(Color.BLUE);
                    text.setGravity(Gravity.CENTER);
                    text.setBackgroundResource(R.drawable.table_border_style);
                    text.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));

                    String assets = bucket[i][j + 2];
                    if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
                        if (assets.contains("Lac"))
                            assets = assets.replace("Lac", " लाख");
                        else if (assets.contains("Crore"))
                            assets = assets.replace("Crore", " करोड़");
                    }

                    String url_link = assets + "<a href='"+ bucket[i][j + 3] + "'> " + getString(R.string.know_more) + "</a>";// IMP: Don't lead space on left/right side of url, that doesn't work
                    //Log.e(TAG, "Link: " + url_link);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //API 24
                        text.setText(Html.fromHtml(url_link, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        text.setText(for_lower_version(url_link));
                    }

                } else {
                    if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
                        int j_hi = j + 6;
                        text.setText(bucket[i][j_hi + 2]);
                    } else {
                        text.setText(bucket[i][j + 2]);
                    }
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
                    if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
                        int j_hi = j + 5;
                        text.setText(bucket[i][j_hi + 2]);
                    } else {
                        text.setText(bucket[i][j + 2]);
                    }
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
        String state = mSharedPref.getString(sSTATE, MainActivity.DEFAULT_STATE);
        String constituency = mSharedPref.getString(sMP_AREA, MainActivity.DEFAULT_MP);

        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state.equals(MPdata.all_MPs[i][3]) && constituency.equals(MPdata.all_MPs[i][4])){
                    state = MPdata.all_MPs[i][0];
                    constituency = MPdata.all_MPs[i][1];
                }
            }
            //area_column = 7;
        }

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
        String state = mSharedPref.getString(sSTATE, MainActivity.DEFAULT_STATE);
        String constituency = mSharedPref.getString(sMP_AREA, MainActivity.DEFAULT_MP);

        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
            for(int i=0; i< MPdata.all_MPs.length; i++){
                if (state.equals(MPdata.all_MPs[i][3]) && constituency.equals(MPdata.all_MPs[i][4])) {
                    state = MPdata.all_MPs[i][0];
                    constituency = MPdata.all_MPs[i][1];
                }
            }
            //area_column = 7;
        }

        for (String[] i : bucket) {
            String area_name = i[area_column];
            if (constituency.equals(area_name))
                num++;
        }

        return num;
    }

    public void onclick_read_online_short(View view) {
        LogEvents.send(this, "Read_Online");

        String url = Constants.COI_English_short;
        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
            url = Constants.COI_Hindi_short;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onclick_read_online_full(View view) {
        LogEvents.send(this, "Read_Online");

        String url = Constants.COI_English_full;
        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)){
            url = Constants.COI_Hindi_full;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onclick_amazon(View view) {
        LogEvents.send(this, "Buy_Amazon");

        String url = Constants.COI_English_buy_amazon;
        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)){
            url = Constants.COI_Hindi_buy_amazon;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onclick_post(View view) {
        LogEvents.send(this, "Buy_Post");

        String url = Constants.COI_English_buy_IndiaPost;
        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)){
            url = Constants.COI_Hindi_buy_IndiaPost;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onclick_play_video(View view) {
        LogEvents.send(this, "COI_Youtube");

        String url = Constants.COI_Youtube;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
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
            intent.putExtra(TAB_NUMBER, TAB_ISSUE);
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
        LogEvents.send(this, "Donate");

        Uri uri = Uri.parse(Constants.URL_DONATE);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onclick_my_MP_screen(View view) {
        LogEvents.send(this, titleName + "_MP");

        Intent intent = new Intent(view.getContext(), VoteMP.class);
        intent.putExtra("layout_id", layoutResID);
        intent.putExtra("title_id", titleID);
        startActivity(intent);
    }

    public void onclick_my_MLA_screen(View view) {
        String state = mSharedPref.getString(sSTATE, MainActivity.DEFAULT_STATE);
        if(DataFilter.is_union_territory(state)) {

            Uri uri = Uri.parse("https://myneta.info/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        } else if((state.equals("Arunachal Pradesh") || state.equals("अरुणाचल प्रदेश"))
                || (state.equals("Gujarat") || state.equals("गुजरात"))
                || (state.equals("Himachal Pradesh") || state.equals("हिमाचल प्रदेश"))){

            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("rajya", true);
            startActivity(intent);

        } else {
            LogEvents.send(this, titleName + "_MLA");

            Intent intent = new Intent(view.getContext(), VoteVidhayak.class);
            intent.putExtra("layout_id", layoutResID);
            intent.putExtra("title_id", titleID);
            startActivity(intent);
        }
    }

    public void onclick_my_Parshad_screen(View view) {
        LogEvents.send(this, titleName + "_Parshad");

        Toast.makeText(getApplicationContext(), getText(R.string.parshad_missing), Toast.LENGTH_LONG).show();
    }
}
