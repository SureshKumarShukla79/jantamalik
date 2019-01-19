package in.filternet.jantamalik.VoteJava;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import in.filternet.jantamalik.DataFilter;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

public class VoteFragment extends Fragment {
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

    public static final String sSTATE = "Uttar Pradesh";
    public static final String sMP = "Varanasi";
    public static final String sMLA = "Varanasi Cantt";
    public static final String sWARD = "Chittupur, Sigra";

    String current_language;
    public String AreaName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        current_language = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, null);
        if (current_language != null && current_language.equals(MainActivity.sLANGUAGE_HINDI)) {
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

        dataFilter = new DataFilter();
        spinnerState = view.findViewById(R.id.state_spinner);
        spinnerMP = view.findViewById(R.id.MP_spinner);
        spinnerMLA = view.findViewById(R.id.MLA_spinner);
        spinnerWard = view.findViewById(R.id.Ward_spinner);

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String State = spinnerState.getItemAtPosition(spinnerState.getSelectedItemPosition()).toString();
                editor.putString(sSTATE, State).commit();

                arrayAdapterMP = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                        dataFilter.getMPAreas(State));
                arrayAdapterMP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMP.setAdapter(arrayAdapterMP);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //spinner constituency click handler
        spinnerMP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AreaName = adapterView.getItemAtPosition(i).toString();

                editor.putString(sSTATE, AreaName ).commit();

                Toast.makeText(getContext(),AreaName, Toast.LENGTH_SHORT).show();
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

        // Populating GUI
        arrayAdapterState = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item,
                dataFilter.getStates());
        arrayAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(arrayAdapterState);

        String State = mSharedPref.getString(sSTATE,"");
        String MP = mSharedPref.getString(sMP,"");
        String MLA = mSharedPref.getString(sMLA,"");
        String Ward = mSharedPref.getString(sWARD,"");
        if (current_language != null && current_language.equals(MainActivity.sLANGUAGE_HINDI)) {
            //TODO hindi equivalents
        }

        //populating Constiuencies
        arrayAdapterMP = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                dataFilter.getMPAreas(State));
        arrayAdapterMP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMP.setAdapter(arrayAdapterMP);

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
                Toast.makeText(view.getContext(),"Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        imageButtonMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Coming soon", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

