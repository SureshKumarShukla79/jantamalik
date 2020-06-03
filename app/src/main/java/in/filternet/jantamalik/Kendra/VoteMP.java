package in.filternet.jantamalik.Kendra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.LogEvents;
import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_KENDRA;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;

public class VoteMP extends AppCompatActivity {
    String TAG = "VoteMP";
    private Toolbar toolbar;
    private ImageView ui_image_address;
    private TextView ui_name, ui_phone, ui_phone2, ui_phone3, ui_email, ui_email2, ui_address;
    private FloatingActionButton ui_call, ui_call2, ui_call3, ui_mail, ui_mail2;
    private LinearLayout ui_whatsapp_group;
    DataFilter.MP_info mp;

    private Spinner spinnerState;
    private Spinner spinnerMP;
    private ArrayAdapter arrayAdapterState;
    private ArrayAdapter arrayAdapterMP;
    private DataFilter dataFilter;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;

    private String mLanguage;
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

        ui_whatsapp_group = findViewById(R.id.whatsapp_group);

        spinnerState = findViewById(R.id.state_spinner);
        spinnerMP = findViewById(R.id.MP_spinner);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_button(view);
            }
        });
        mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, MainActivity.sLANGUAGE_HINDI);

        String State = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);
        String MP = mSharedPref.getString(MainActivity.sMP_AREA, MainActivity.DEFAULT_MP);

        // Populating GUI
        dataFilter = new DataFilter();
        // Load defaults
        arrayAdapterState = new ArrayAdapter(getBaseContext(), R.layout.spinner_text_style, dataFilter.getStates(mLanguage));
        arrayAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(arrayAdapterState);

        int spinnerPosition = arrayAdapterState.getPosition(State);
        spinnerState.setSelection(spinnerPosition);
        //Log.e(TAG, "state def: " + State);

        //populating MP Area
        arrayAdapterMP = new ArrayAdapter(getBaseContext(), R.layout.spinner_text_style, dataFilter.getMPAreas(mLanguage, State));
        arrayAdapterMP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMP.setAdapter(arrayAdapterMP);

        spinnerPosition = arrayAdapterMP.getPosition(MP);
        spinnerMP.setSelection(spinnerPosition);
        //Log.e(TAG, "MP def: " + MP);
        // defaults over

        // set for dynamic handling
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String State = spinnerState.getItemAtPosition(spinnerState.getSelectedItemPosition()).toString();
                //Log.e(TAG, "spin state : " + i + " " + l + " " + State);
                editor.putString(MainActivity.sSTATE, State).commit();

                String tmp = State;
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {// Firebase needs English, cant handle Hindi
                    String state = MainActivity.get_state(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
                    tmp = state;
                }
                tmp = tmp.replace(" ", "_");
                tmp = tmp.replace("&", "and");
                LogEvents.send(getBaseContext(), tmp);

                // Reload the state MP areas
                arrayAdapterMP = new ArrayAdapter(getBaseContext(), R.layout.spinner_text_style, dataFilter.getMPAreas(mLanguage, State));
                arrayAdapterMP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMP.setAdapter(arrayAdapterMP);

                String MP = mSharedPref.getString(MainActivity.sMP_AREA, MainActivity.DEFAULT_MP);
                int spinnerPosition = arrayAdapterMP.getPosition(MP);
                spinnerMP.setSelection(spinnerPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //spinner constituency click handler
        spinnerMP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String MPArea = adapterView.getItemAtPosition(i).toString();
                //Log.e(TAG, "spin MP : " + i + " " + l + " " + MPArea);
                editor.putString(MainActivity.sMP_AREA, MPArea).commit();

                String tmp = MPArea;
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {// Firebase needs English, cant handle Hindi
                    String area = MainActivity.get_area(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
                    tmp = area;
                }
                tmp = tmp.replace(" ", "_");
                tmp = tmp.replace("&", "and");
                LogEvents.send(getBaseContext(), tmp);

                updateMP();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        updateMP();
    }

    @SuppressLint("RestrictedApi")
    private void updateMP() {
        DataFilter dataFilter = new DataFilter();
        String MPArea = mSharedPref.getString(MainActivity.sMP_AREA, MainActivity.DEFAULT_MP);
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

        // Get English version and then WhatsApp group link
        String state = "", area = "";
        if(mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
            state = MainActivity.get_state(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
            area = MainActivity.get_area(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
        } else {
            state = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);
            area = mSharedPref.getString(MainActivity.sMP_AREA, MainActivity.DEFAULT_MP);
        }
        //Log.e(TAG, state + "," + area);

        String tmp = getLoksabha_Group(getBaseContext(), state, area);
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
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");
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
        if (mWhatsapp_group.equals(""))
            return;
        Uri uri = Uri.parse(mWhatsapp_group);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private String mWhatsapp_group = "";

    private String getLoksabha_Group(Context context, String state, String area) {
        mWhatsapp_group = "";
        //Log.e(TAG, state + " " + area);

        for (int i = 0; i < LokSabhaGroups.all_groups.length; i++) {
            if (state.equals(LokSabhaGroups.all_groups[i][0])
                    && area.equals(LokSabhaGroups.all_groups[i][1])) {
                mWhatsapp_group = LokSabhaGroups.all_groups[i][2];
            } else
                continue;
        }
        //Log.e(TAG, tmp);

        return mWhatsapp_group;
    }
}
