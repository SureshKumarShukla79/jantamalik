package in.filternet.jantamalik.Rajya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.FirebaseLogger;
import in.filternet.jantamalik.Kendra.DataFilter;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;

public class VoteVidhayak extends AppCompatActivity {
    String TAG = "VoteVidhayak";
    private Toolbar toolbar;
    private TextView name, phone, email, address;
    private Spinner spinnerMLA;

    private DataFilter.MP_info mla;
    private ArrayAdapter mla_adapter;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;
    private String mLanguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vote_mla_layout);

        FirebaseLogger.send(this, "Rajya_VoteMLA");

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSharedPref.edit();

        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.MLA_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
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
        String MLA = mSharedPref.getString(MainActivity.sMLA_AREA, "");
        //Log.e(TAG, "state : " + State + " " + MP + " " + MLA + " " + Ward);

        DataFilter dataFilter = new DataFilter();

        // Load defaults
        mla_adapter = new ArrayAdapter(getBaseContext(), R.layout.spinner_text_style, dataFilter.getMLAAreas(mLanguage, State));
        mla_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMLA.setAdapter(mla_adapter);

        int spinnerPosition = mla_adapter.getPosition(MLA);
        spinnerMLA.setSelection(spinnerPosition);
        //Log.e(TAG, "MLA def: " + MLA);

        //spinner constituency click handler
        spinnerMLA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String MLAArea = adapterView.getItemAtPosition(i).toString();
                //Log.e(TAG, "spin MLA : " + i + " " + l + " " + MLAArea);
                editor.putString(MainActivity.sMLA_AREA, MLAArea).commit();

                updateMLA();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        updateMLA();

    }

    private void updateMLA() {
        DataFilter dataFilter = new DataFilter();
        String MLAArea = mSharedPref.getString(MainActivity.sMLA_AREA, "");
        mla = dataFilter.getMLAInfo(mLanguage, MLAArea);

        Log.e(TAG, MLAArea + " " + mla.name + " " + mla.phone + " " + mla.email + " " + mla.address);
        name.setText(mla.name);
        phone.setText(mla.phone);
        email.setText(mla.email);
        address.setText(mla.address);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_button(toolbar.getRootView()); // TODO animation is too strong
    }

    private void back_button(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }

    public void onclick_call_mla(View view) {
        if (mla.phone.equals(""))
            return;

        Uri number = Uri.parse("tel:" + mla.phone);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        try { // Calling not available on Tablet devices
            startActivity(callIntent);
        } catch (Exception exception) {
            Toast.makeText(this, "Unable to CALL", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }

    public void onclick_email_mla(View view) {
        if (mla.email.equals(""))
            return;

        String[] TO = {mla.email};
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
        } catch (Exception ex) {
            Toast.makeText(this, "Gmail app didn't respond.", Toast.LENGTH_LONG).show();
        }
    }

    public void onclick_update_Vidhayak(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mp", true);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }

    public void onclick_Vidhayak(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mp", true);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }
}
