package in.filternet.jantamalik.VoteJava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.filternet.jantamalik.DataFilter;
import in.filternet.jantamalik.MPdata;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class VoteFragment extends Fragment {
    String TAG = "VoteFragment";

    private View view;
    private Button buttonMP;
    private ImageButton imageButtonMP;
    private Button buttonMP2;
    private ImageButton imageButtonMP2;
    private Button buttonMP3;
    private ImageButton imageButtonMP3;
    private Intent intent;
    private Spinner spinnerState;
    private Spinner spinnerMP;
    private Spinner spinnerMLA;
    private Spinner spinnerWard;
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

        view = inflater.inflate(R.layout.vote_fragment, container, false);

        buttonMP = view.findViewById(R.id.Vote_Item_Button);
        imageButtonMP = view.findViewById(R.id.Vote_Item_imageButton);
        //second
        buttonMP2 = view.findViewById(R.id.Vote_Item_Button2);
        imageButtonMP2 = view.findViewById(R.id.Vote_Item_imageButton2);
        //third
        buttonMP3 = view.findViewById(R.id.Vote_Item_Button3);
        imageButtonMP3 = view.findViewById(R.id.Vote_Item_imageButton3);

        spinnerState = view.findViewById(R.id.state_spinner);
        spinnerMP = view.findViewById(R.id.MP_spinner);
        spinnerMLA = view.findViewById(R.id.MLA_spinner);
        spinnerWard = view.findViewById(R.id.Ward_spinner);

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
                //Log.e(TAG, "spin state : " + i + " " + l + " " + State);
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

        //spinnerMLA click handler
        spinnerMLA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        MP_Click();
        MLA_Click();
        Parshad_Click();

        return view;
    }

    public void MP_Click(){
        buttonMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             intent = new Intent(view.getContext(), VoteMP.class);
             startActivity(intent);
            }
        });

       imageButtonMP.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               intent = new Intent(view.getContext(), VoteMP.class);
               startActivity(intent);
           }
       });
    }

    public void MLA_Click(){
        buttonMP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(view.getContext(),"Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        imageButtonMP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Coming soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Parshad_Click(){
        buttonMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),R.string.next_version, Toast.LENGTH_SHORT).show();
            }
        });

        imageButtonMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),R.string.next_version, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

