package in.filternet.jantamalik.Kendra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_KENDRA;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class VoteMP extends AppCompatActivity {
    String TAG = "VoteMP";
    private Toolbar toolbar;
    private TextView name,phone,email, address;
    private de.hdodenhof.circleimageview.CircleImageView profile_pic;
    DataFilter.MP_info mp;

    private Spinner spinnerState;
    private Spinner spinnerMP;
    private ArrayAdapter arrayAdapterState;
    private ArrayAdapter arrayAdapterMP;
    private DataFilter dataFilter;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;

    public static final String DEFAULT_STATE = "Uttar Pradesh";
    public static final String DEFAULT_MP = "Varanasi";
    public static final String DEFAULT_MLA = "Varanasi Cantt";
    public static final String DEFAULT_WARD = "Chittupur, Sigra";

    public static final String hiDEFAULT_STATE = "उत्तर प्रदेश";
    public static final String hiDEFAULT_MP = "वाराणसी";
    public static final String hiDEFAULT_MLA = "वाराणसी कैंट";
    public static final String hiDEFAULT_WARD = "छित्तुपुर, सिगरा";

    public static final String sSTATE = DEFAULT_STATE;
    public static final String sMP = DEFAULT_MP;
    public static final String sMLA = DEFAULT_MLA;
    public static final String sWARD = DEFAULT_WARD;

    private String MPArea;
    private String mLanguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vote_mp_layout);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSharedPref.edit();

        toolbar = findViewById(R.id.toolbar_MP_layout);
        name = findViewById(R.id.MP_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        profile_pic = findViewById(R.id.profile_image);

        spinnerState = findViewById(R.id.state_spinner);
        spinnerMP = findViewById(R.id.MP_spinner);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_button(view);
            }
        });

        final SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        MPArea = mSharedPref.getString(sMP, DEFAULT_MP);
        mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);

        String State = mSharedPref.getString(sSTATE, DEFAULT_STATE);
        String MP = mSharedPref.getString(sMP, DEFAULT_MP);
        String MLA = mSharedPref.getString(sMLA, DEFAULT_MLA);
        String Ward = mSharedPref.getString(sWARD, DEFAULT_WARD);
        //Log.e(TAG, "state : " + State + " " + MP + " " + MLA + " " + Ward);

        // In case of Hindi, change the defaults
        if (mLanguage.equals(sLANGUAGE_HINDI)) {
            if (State.equals(DEFAULT_STATE))
                State = hiDEFAULT_STATE;
            if (MP.equals(DEFAULT_MP))
                MP = hiDEFAULT_MP;
            if (MLA.equals(DEFAULT_MLA))
                MLA = hiDEFAULT_MLA;
            if (Ward.equals(DEFAULT_WARD))
                Ward = hiDEFAULT_WARD;
        }
        //Log.e(TAG, "state : " + State + " " + MP + " " + MLA + " " + Ward);

        // In case the db isn't initialised, do it now
        editor.putString(sSTATE, State).commit();
        editor.putString(sMP, MP).commit();
        editor.putString(sMLA, MLA).commit();
        editor.putString(sWARD, Ward).commit();

        // Populating GUI
        dataFilter = new DataFilter();
        // Load defaults
        arrayAdapterState = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item,
                dataFilter.getStates(mLanguage));
        arrayAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(arrayAdapterState);

        int spinnerPosition = arrayAdapterState.getPosition(State);
        spinnerState.setSelection(spinnerPosition);
        //Log.e(TAG, "state def: " + State);

        //populating MP Area
        arrayAdapterMP = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item,
                dataFilter.getMPAreas(mLanguage, State));
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
                editor.putString(sSTATE, State).commit();

                // Reload the state MP areas
                arrayAdapterMP = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item,
                        dataFilter.getMPAreas(mLanguage, State));
                arrayAdapterMP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMP.setAdapter(arrayAdapterMP);

                String MP = mSharedPref.getString(sMP, DEFAULT_MP);
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
                MPArea = adapterView.getItemAtPosition(i).toString();
                //Log.e(TAG, "spin MP : " + i + " " + l + " " + MPArea);
                editor.putString(sMP, MPArea).commit();

                updateMP();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        updateMP();
    }

    private void updateMP() {
        DataFilter dataFilter = new DataFilter();
        //mp = dataFilter.new MP_info();
        mp = dataFilter.getMPInfo(mLanguage, MPArea);

        //Log.e(TAG, MPArea + " " + mp.name + " " + mp.phone + " " + mp.email + " " + mp.address);
        name.setText(mp.name);
        phone.setText(mp.phone);
        email.setText(mp.email);
        address.setText(mp.address);

        // Only Varanasi MP pic in app
        if (MPArea.equals(DEFAULT_MP) || MPArea.equals(hiDEFAULT_MP))
            profile_pic.setImageResource(R.drawable.narendra_modi_pic);
        else
            profile_pic.setImageResource(R.drawable.politician_illustration);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_button(toolbar.getRootView()); // TODO animation is too strong
    }

    private void back_button(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra(TAB_NUMBER, TAB_KENDRA);
        startActivity(intent);
    }

    public void onclick_call_mp(View view) {
        if(mp.phone.equals(""))
            return;

        Uri number = Uri.parse("tel:" + mp.phone);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        try { // Calling not available on Tablet devices
            startActivity(callIntent);
        } catch (Exception exception){
            Toast.makeText(this, "Unable to CALL", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }

    public void onclick_email_mp(View view) {
        if(mp.email.equals(""))
            return;

        String[] TO = {mp.email};
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

    public void onclick_update_mp(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mp", true);
        startActivity(intent);
    }

    public void onclick_MP(View view){
        Intent intent = new Intent(view.getContext(), VoteMP.class);
        startActivity(intent);
    }
}
