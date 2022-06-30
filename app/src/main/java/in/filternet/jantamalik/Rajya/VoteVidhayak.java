package in.filternet.jantamalik.Rajya;

import static in.filternet.jantamalik.Kendra.VoteMP.getLoksabha_Group;
import static in.filternet.jantamalik.MainActivity.SELECT_MLA;
import static in.filternet.jantamalik.MainActivity.SELECT_MP;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;
import static in.filternet.jantamalik.MainActivity.sMLA_AREA;
import static in.filternet.jantamalik.MainActivity.sMP_AREA;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.Kendra.DataFilter;
import in.filternet.jantamalik.LogEvents;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

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
        toolbar.setNavigationOnClickListener(view -> back_button(view));

        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String mp_area = mSharedPref.getString(sMP_AREA, "");
        String mla_area = mSharedPref.getString(MainActivity.sMLA_AREA, "");
        Log.e(TAG, "USER Preference: " + mp_area + ", " + mla_area);

        DataFilter dataFilter = new DataFilter();

        // Load defaults
        if (mp_area != null && dataFilter.has_MP_2_MLA_mapping(mp_area)) {
            mla_adapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_text_style, dataFilter.get_MLA_area_as_per_MP_area(mp_area));
        } else {
            mla_adapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_text_style, dataFilter.get_MLA_area_as_per_state());
        }

        mla_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMLA.setAdapter(mla_adapter);

        int spinnerPosition = mla_adapter.getPosition(mla_area);
        spinnerMLA.setSelection(spinnerPosition);
        //Log.e(TAG, "MLA def: " + MLA);

        Activity activity = this;
        spinnerMLA.setOnTouchListener((v, event) -> {
            Log.e(TAG, "User click ");
            ask_user_preference(activity);
            return true;
        });

        updateMLA();

        ui_no_progress.setOnClickListener(view -> {
            if (!mProtestVisibility) {
                mProtestVisibility = true;
                show_protest();
            } else {
                mProtestVisibility = false;
                hide_protest();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void updateMLA() {
        DataFilter dataFilter = new DataFilter();
        String mla_area = mSharedPref.getString(MainActivity.sMLA_AREA, "");
        mla = dataFilter.getMLAInfo(mla_area);

        Log.e(TAG, "update MLA" + mla_area + " " + mla.name + " " + mla.phone + " " + mla.email + " " + mla.address);
        ui_name.setText(mla.name);

        if (mla.phone == null || mla.phone.equals("") || mla.phone.isEmpty()) {
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
            LogEvents.send(this, "MLA_Email");
        } catch (Exception ex) {
            Toast.makeText(this, "Gmail app didn't respond.", Toast.LENGTH_LONG).show();
        }
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

    public void ask_user_preference(Activity activity) {
        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor mEditor = mSharedPref.edit();

        String MP = mSharedPref.getString(sMP_AREA, SELECT_MP);
        String MLA = mSharedPref.getString(sMLA_AREA, SELECT_MLA);
        final String TAG = "USER_CHOICE";
        Log.e(TAG, "Def : " + MP + " " + MLA);

        LayoutInflater inflater = activity.getLayoutInflater();
        View ui_preference_layout = inflater.inflate(R.layout.user_preference, null);

        final LinearLayout ui_constituency = ui_preference_layout.findViewById(R.id.constituency);
        final Spinner ui_constituency_spinner = ui_preference_layout.findViewById(R.id.constituency_spinner);
        final LinearLayout ui_assembly = ui_preference_layout.findViewById(R.id.assembly);
        final Spinner ui_assembly_spinner = ui_preference_layout.findViewById(R.id.assembly_spinner);
        final FloatingActionButton ui_done = ui_preference_layout.findViewById(R.id.done);

        final DataFilter data_filter = new DataFilter();

        ui_constituency.setVisibility(View.VISIBLE);
        if (!MLA.equals(""))
            ui_assembly.setVisibility(View.VISIBLE);

        //populating constituency
        List<String> constituency_list = data_filter.getMPAreas();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity.getBaseContext(), R.layout.spinner_text_style, constituency_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ui_constituency_spinner.setAdapter(adapter);

        int MPPosition = adapter.getPosition(MP);
        ui_constituency_spinner.setSelection(MPPosition);

        //spinner constituency click handler
        ui_constituency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_constituency = adapterView.getItemAtPosition(i).toString();
                if (selected_constituency.equals(SELECT_MP)) {
                    return;
                }

                mEditor.putString(sMP_AREA, selected_constituency).commit();

                ui_assembly.setVisibility(View.VISIBLE);

                Log.e(TAG, "selected MP : " + selected_constituency);
                //populating assembly
                List<String> assembly_list;
                if (data_filter.has_MP_2_MLA_mapping(selected_constituency)) {
                    assembly_list = data_filter.get_MLA_area_as_per_MP_area(selected_constituency);
                } else {
                    assembly_list = data_filter.get_MLA_area_as_per_state();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(activity.getBaseContext(), R.layout.spinner_text_style, assembly_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ui_assembly_spinner.setAdapter(adapter);
                int MLAPosition = adapter.getPosition(MLA);
                ui_assembly_spinner.setSelection(MLAPosition);
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
                if (selected_assembly.equals(SELECT_MLA)) {
                    ui_done.setVisibility(View.INVISIBLE);
                    return;
                }

                mEditor.putString(MainActivity.sMLA_AREA, selected_assembly).commit();
                Log.e(TAG, "selected MLA : " + selected_assembly);
                ui_done.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(ui_preference_layout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        ui_done.setOnClickListener(v -> {
            String selected_constituency = mSharedPref.getString(sMP_AREA, "");
            String selected_assembly = mSharedPref.getString(MainActivity.sMLA_AREA, "");

            LogEvents.sendWithValue(activity.getBaseContext(), sMP_AREA, selected_constituency);
            LogEvents.sendWithValue(activity.getBaseContext(), sMLA_AREA, selected_assembly);

            dialog.dismiss();
            updateMLA();
        });

        dialog.show();
    }
}
