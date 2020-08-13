package in.filternet.jantamalik.Rajya;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.Kendra.DataFilter;
import in.filternet.jantamalik.LogEvents;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.Kendra.VoteMP.getLoksabha_Group;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;
import static in.filternet.jantamalik.MainActivity.sMLA_AREA;

public class VoteVidhayak extends AppCompatActivity {
    String TAG = "VoteVidhayak";
    private Toolbar toolbar;
    private ImageView ui_image_address;
    private CircleImageView ui_expand_protest, ui_hide_protest;
    private TextView ui_name, ui_phone, ui_phone2, ui_phone3, ui_email, ui_email2, ui_address, ui_source_adr;
    private FloatingActionButton ui_call, ui_call2, ui_call3, ui_mail, ui_mail2;
    private LinearLayout ui_whatsapp_group, ui_protest, ui_source;
    private RelativeLayout ui_no_progress;
    private Spinner spinnerMLA;

    private DataFilter.MP_info mla;
    private ArrayAdapter<String> mla_adapter;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;
    private String mLanguage;
    private boolean mProtestVisibility = false;
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

        setContentView(R.layout.vote_mla_layout);

        LogEvents.send(this, TAG);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSharedPref.edit();

        toolbar = findViewById(R.id.toolbar);
        ui_name = findViewById(R.id.MLA_name);
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
        ui_source = findViewById(R.id.source);

        ui_whatsapp_group = findViewById(R.id.whatsapp_group);

        ui_source_adr = findViewById(R.id.source_adr);
        ui_source_adr.setMovementMethod(LinkMovementMethod.getInstance());

        spinnerMLA = findViewById(R.id.MLA_spinner);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_button(view);
            }
        });

        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, MainActivity.sLANGUAGE_HINDI);

        String State = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);
        String mp_area = mSharedPref.getString(MainActivity.sMP_AREA, MainActivity.DEFAULT_MP);
        String mla_area = mSharedPref.getString(MainActivity.sMLA_AREA, MainActivity.DEFAULT_MLA);
        //Log.e(TAG, "USER Preference: " + State + ", " + mp_area + ", " + mla_area);

        DataFilter dataFilter = new DataFilter();

        // Load defaults
        if (mp_area != null && dataFilter.has_MP_2_MLA_mapping(State, mp_area)) {
            mla_adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_text_style, dataFilter.get_MLA_area_as_per_MP_area(mp_area));
        } else {
            mla_adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_text_style, dataFilter.get_MLA_area_as_per_state(mLanguage, State));
        }

        mla_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMLA.setAdapter(mla_adapter);

        int spinnerPosition = mla_adapter.getPosition(mla_area);
        spinnerMLA.setSelection(spinnerPosition);
        //Log.e(TAG, "MLA def: " + MLA);

        //spinner constituency click handler
        spinnerMLA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String MLAArea = adapterView.getItemAtPosition(i).toString();
                //Log.e(TAG, "spin MLA : " + i + " " + l + " " + MLAArea);
                editor.putString(MainActivity.sMLA_AREA, MLAArea).commit();

                String tmp = MLAArea;
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {// Firebase needs English, cant handle Hindi
                    tmp = MainActivity.get_MLA_area(getBaseContext(), MainActivity.sLANGUAGE_ENGLISH);
                }
                tmp = tmp.replace(" ", "_");
                tmp = tmp.replace("&", "and");
                LogEvents.sendWithValue(getBaseContext(), sMLA_AREA, tmp);

                updateMLA();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        updateMLA();

        ui_no_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mProtestVisibility) {
                    mProtestVisibility = true;
                    show_protest();
                }
                else {
                    mProtestVisibility = false;
                    hide_protest();
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void updateMLA() {
        DataFilter dataFilter = new DataFilter();
        String State = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);
        String mla_area = mSharedPref.getString(MainActivity.sMLA_AREA, MainActivity.DEFAULT_MLA);
        mla = dataFilter.getMLAInfo(mLanguage, State, mla_area);

        //Log.e(TAG, mla_area + " " + mla.name + " " + mla.phone + " " + mla.email + " " + mla.address);
        ui_name.setText(mla.name);

        if(mla.phone == null || mla.phone.equals("") || mla.phone.isEmpty()) {
            ui_call.setVisibility(View.GONE);
            ui_phone.setVisibility(View.GONE);
        } else {
            ui_phone.setText(mla.phone);
            ui_call.setVisibility(View.VISIBLE);
            ui_phone.setVisibility(View.VISIBLE);
        }

        if(mla.phone2 == null || mla.phone2.equals("") || mla.phone2.isEmpty()) {
            ui_call2.setVisibility(View.GONE);
            ui_phone2.setVisibility(View.GONE);
        }
        else {
            ui_phone2.setText(mla.phone2);
            ui_call2.setVisibility(View.VISIBLE);
            ui_phone2.setVisibility(View.VISIBLE);
        }

        if(mla.phone3 == null || mla.phone3.equals("") || mla.phone3.isEmpty()) {
            ui_call3.setVisibility(View.GONE);
            ui_phone3.setVisibility(View.GONE);
        }
        else {
            ui_phone3.setText(mla.phone3);
            ui_call3.setVisibility(View.VISIBLE);
            ui_phone3.setVisibility(View.VISIBLE);
        }

        if(mla.email == null || mla.email.equals("") || mla.email.isEmpty()) {
            ui_mail.setVisibility(View.GONE);
            ui_email.setVisibility(View.GONE);
        }
        else {
            ui_email.setText(mla.email);
            ui_mail.setVisibility(View.VISIBLE);
            ui_email.setVisibility(View.VISIBLE);
        }

        if(mla.email2 == null || mla.email2.equals("") || mla.email2.isEmpty()) {
            ui_mail2.setVisibility(View.GONE);
            ui_email2.setVisibility(View.GONE);
        }
        else {
            ui_email2.setText(mla.email2);
            ui_mail2.setVisibility(View.VISIBLE);
            ui_email2.setVisibility(View.VISIBLE);
        }

        if(mla.address == null || mla.address.equals("") || mla.address.isEmpty()) {
            ui_address.setVisibility(View.GONE);
            ui_image_address.setVisibility(View.GONE);
        } else {
            ui_address.setText(mla.address);
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

    public void onclick_WhatsApp(View view) {
        String whatsapp_group = getLoksabha_Group(this);

        if (whatsapp_group.equals(""))
            return;
        Uri uri = Uri.parse(whatsapp_group);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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
            intent.putExtra(TAB_NUMBER, TAB_RAJYA);
            startActivity(intent);
        }
    }

    public void onclick_call_mla(View view) {
        if(mla.phone.equals("") && mla.phone2.equals("") && mla.phone3.equals(""))
            return;

        Uri number = null;
        switch (view.getId()) {
            case R.id.phone:
            case R.id.call:
                number = Uri.parse("tel:" + mla.phone);
                break;
            case R.id.phone2:
            case R.id.call2:
                number = Uri.parse("tel:" + mla.phone2);
                break;
            case R.id.phone3:
            case R.id.call3:
                number = Uri.parse("tel:" + mla.phone3);
                break;
        }

        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        try { // Calling not available on Tablet devices
            startActivity(callIntent);
            LogEvents.send(this, "MLA_Phone");
        } catch (Exception exception){
            Toast.makeText(this, "Unable to CALL", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }

    public void onclick_email_mla(View view) {
        if(mla.email.equals("") && mla.email2.equals(""))
            return;

        String[] TO = new String[0];

        switch (view.getId()) {
            case R.id.email:
            case R.id.mail:
                TO = new String[]{mla.email};
                break;
            case R.id.email2:
            case R.id.mail2:
                TO = new String[]{mla.email2};
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
            LogEvents.send(this, "MLA_Email");
        } catch (Exception ex) {
            Toast.makeText(this, "Gmail app didn't respond.", Toast.LENGTH_LONG).show();
        }
    }

    public void onclick_update_Vidhayak(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mla", true);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }

    public void onclick_Vidhayak(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mla", true);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
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

}
