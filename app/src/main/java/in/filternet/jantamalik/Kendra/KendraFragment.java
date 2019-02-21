package in.filternet.jantamalik.Kendra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class KendraFragment extends Fragment {
    String TAG = "VoteFragment";

    private View view;
    private TextView vote2, note2, govt1;
    private ImageButton vote1, vote3, note1, note3;
    private ImageView govt2;
    private LinearLayout duties;

    private Intent intent;
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

    private String mLanguage;
    public String AreaName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);
        if (mLanguage.equals(sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(getActivity(), "hi");
        }

        editor = mSharedPref.edit();

        view = inflater.inflate(R.layout.kendra, container, false);

        vote1 = view.findViewById(R.id.vote1);
        vote2 = view.findViewById(R.id.vote2);
        vote3 = view.findViewById(R.id.vote3);

        note1 = view.findViewById(R.id.note1);
        note2 = view.findViewById(R.id.note2);
        note3 = view.findViewById(R.id.note3);

        govt1 = view.findViewById(R.id.govt1);
        govt2 = view.findViewById(R.id.govt2);

        duties = view.findViewById(R.id.duties);

        spinnerState = view.findViewById(R.id.state_spinner);
        spinnerMP = view.findViewById(R.id.MP_spinner);

        // Populating GUI
        dataFilter = new DataFilter();

        String State = mSharedPref.getString(sSTATE,DEFAULT_STATE);
        String MP = mSharedPref.getString(sMP,DEFAULT_MP);
        String MLA = mSharedPref.getString(sMLA,DEFAULT_MLA);
        String Ward = mSharedPref.getString(sWARD,DEFAULT_WARD);
        //Log.e(TAG, "state : " + State + " " + MP + " " + MLA + " " + Ward);

        // In case of Hindi, change the defaults
        if (mLanguage.equals(sLANGUAGE_HINDI)) {
            if(State.equals(DEFAULT_STATE))
                State = hiDEFAULT_STATE;
            if(MP.equals(DEFAULT_MP))
                MP = hiDEFAULT_MP;
            if(MLA.equals(DEFAULT_MLA))
                MLA = hiDEFAULT_MLA;
            if(Ward.equals(DEFAULT_WARD))
                Ward = hiDEFAULT_WARD;
        }
        //Log.e(TAG, "state : " + State + " " + MP + " " + MLA + " " + Ward);

        // In case the db isn't initialised, do it now
        editor.putString(sSTATE, State).commit();
        editor.putString(sMP, MP).commit();
        editor.putString(sMLA, MLA).commit();
        editor.putString(sWARD, Ward).commit();

        // Load defaults
        arrayAdapterState = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item,
                dataFilter.getStates(mLanguage));
        arrayAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(arrayAdapterState);

        int spinnerPosition = arrayAdapterState.getPosition(State);
        spinnerState.setSelection(spinnerPosition);
        //Log.e(TAG, "state def: " + State);

        //populating MP Area
        arrayAdapterMP = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                dataFilter.getMPAreas(mLanguage,State));
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
               // Log.e(TAG, "spin state : " + i + " " + l + " " + State);
                editor.putString(sSTATE, State).commit();

                // Reload the state MP areas
                arrayAdapterMP = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                        dataFilter.getMPAreas(mLanguage,State));
                arrayAdapterMP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMP.setAdapter(arrayAdapterMP);

                String MP = mSharedPref.getString(sMP,DEFAULT_MP);
                int spinnerPosition = arrayAdapterMP.getPosition(MP);
                spinnerMP.setSelection(spinnerPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //spinner constituency click handler
        spinnerMP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AreaName = adapterView.getItemAtPosition(i).toString();
                //Log.e(TAG, "spin MP : " + i + " " + l + " " + AreaName);
                editor.putString(sMP, AreaName ).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        vote_Click();
        note_Click();
        govt_Click();
        duties_Click();

        return view;
    }

    public void vote_Click(){
        vote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), VoteMP.class);
                startActivity(intent);
            }
        });
        vote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             intent = new Intent(view.getContext(), VoteMP.class);
             startActivity(intent);
            }
        });
       vote3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               intent = new Intent(view.getContext(), VoteMP.class);
               startActivity(intent);
           }
       });
    }

    public void note_Click(){
        note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), TaxKendra.class);
                startActivity(intent);
            }
        });
        note2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), TaxKendra.class);
                startActivity(intent);
            }
        });
        note3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), TaxKendra.class);
                startActivity(intent);
            }
        });
    }

    public void govt_Click(){
        govt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), Infographics.class);
                startActivity(intent);
            }
        });
        govt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), Infographics.class);
                startActivity(intent);
            }
        });
    }

    public void duties_Click() {
        duties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), DutiesKendra.class);
                startActivity(intent);
            }
        });
    }
}

