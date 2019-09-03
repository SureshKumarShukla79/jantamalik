package in.filternet.jantamalik.Rajya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import android.widget.Toast;

import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.FirebaseLogger;
import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.Kendra.DataFilter;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;

public class RajyaFragment extends Fragment {
    String TAG = "RajyaFragment";

    private View view;
    private TextView vote2, note2, govt1;
    private ImageButton vote1, vote3, note1, note3;
    private ImageView govt2;
    private CardView duties;
    private Spinner spinnerState;

    private String mLanguage;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;

    final private String[][] state_budget = {
            //    "STATE"	    	                     "BUDGET"		                        "Winner List"                                                           "राज्य"                     "बजट"
            {	"Andaman and Nicobar Islands"	,	"? lakh crore"	    ,  " " ,       "अण्डमान और निकोबार द्वीपसमूह"  ,     "? लाख करोड़" ,    " " }	,
            {	"Andhra Pradesh"	            ,	"? lakh crore"      , "http://myneta.info/andhrapradesh2019/index.php?action=show_winners&sort=default"     ,       "आन्ध्र प्रदेश"      ,	    "? लाख करोड़",        }	,
            {	"Arunachal Pradesh"	            ,	"? lakh crore"      , "http://myneta.info/ArunachalPradesh2019/index.php?action=show_winners&sort=default"  ,       "अरुणाचल प्रदेश"   ,	    "? लाख करोड़"        }	,
            {	"Assam"	                        ,	"? lakh crore" 	    , "http://myneta.info/assam2016/index.php?action=show_winners&sort=default"             ,        "असम"          ,	  "? लाख करोड़"        }	,
            {	"Bihar"	                        ,	"1.77 lakh crore"   , "http://myneta.info/bihar2015/index.php?action=show_winners&sort=default"             ,        "बिहार"          ,     "1.77 लाख करोड़"     }	,
            {	"Chandigarh"	                ,	"? lakh crore"      ,  " "                     ,       "चण्डीगढ़"                  ,	    "? लाख करोड़"        }	,
            {	"Chhattisgarh"	                ,	"? lakh crore"      , "http://myneta.info/chhattisgarh2018/index.php?action=show_winners&sort=default"      ,       "छत्तीसगढ़"         ,	    "? लाख करोड़"        }	,
            {	"Dadra and Nagar Haveli"	    ,	"? lakh crore"      , " " ,       "दादरा और नगर हवेली"        ,	    "? लाख करोड़"        }	,
            {	"Daman and Diu"	                ,	"? lakh crore"      , " " ,       "दमन और दीव"              ,     "? लाख करोड़"        }	,
            {	"Delhi"	                        ,	"? lakh crore" 	    , "http://myneta.info/delhi2015/index.php?action=show_winners&sort=default"             ,       "दिल्ली"           ,	    "? लाख करोड़"        }	,
            {	"Goa"	                        ,	"? lakh crore"      , "http://myneta.info/goa2017/index.php?action=show_winners&sort=default"               ,       "गोवा"            ,	    "? लाख करोड़"        }	,
            {	"Gujarat"	                    ,	"? lakh crore"      , "http://myneta.info/Gujarat2017/index.php?action=show_winners&sort=default"           ,       "गुजरात"           ,	    "? लाख करोड़"        }	,
            {	"Haryana"	                    ,	"? lakh crore"      , "http://myneta.info/haryana2014/index.php?action=show_winners&sort=default"           ,       "हरियाणा"          ,	    "? लाख करोड़"        }	,
            {	"Himachal Pradesh"	            ,	"? lakh crore"	    , "http://myneta.info/HimachalPradesh2017/index.php?action=show_winners&sort=default"   ,       "हिमाचल प्रदेश"      ,     "? लाख करोड़"        }	,
            {	"Jammu and Kashmir"	            ,	"? lakh crore"      , "http://myneta.info/jk2014/index.php?action=show_winners&sort=default"                ,       "जम्मू और कश्मीर"    ,	    "? लाख करोड़"        }	,
            {	"Jharkhand"	                    ,	"? lakh crore"      , "http://myneta.info/jharkhand2014/index.php?action=show_winners&sort=default"         ,       "झारखण्ड"          ,	    "? लाख करोड़"        }	,
            {	"Karnataka"	                    ,	"? lakh crore"      , "http://myneta.info/karnataka2018/index.php?action=show_winners&sort=default"         ,       "कर्नाटक"          ,	    "? लाख करोड़"        }	,
            {	"Kerala"	                    ,	"? lakh crore" 	    , "http://myneta.info/kerala2016/index.php?action=show_winners&sort=default"            ,       "केरल"            ,	    "? लाख करोड़"        }	,
            {	"Lakshadweep"	                ,	"? lakh crore"      , " "                     ,       "लक्षद्वीप"                    ,    "? लाख करोड़"        }	,
            {	"Madhya Pradesh"	            ,	"? lakh crore"      , "http://myneta.info/madhyapradesh2018/index.php?action=show_winners&sort=default"     ,       "मध्य प्रदेश"         ,	    "? लाख करोड़"        }	,
            {	"Maharashtra"	                ,	"? lakh crore"      , "http://myneta.info/maharashtra2014/index.php?action=show_winners&sort=default"       ,       "महाराष्ट्र"           ,	    "? लाख करोड़"        }	,
            {	"Manipur"	                    ,	"? lakh crore" 	    , "http://myneta.info/manipur2017/index.php?action=show_winners&sort=default"           ,       "मणिपुर"           ,	    "? लाख करोड़"        }	,
            {	"Meghalaya"	                    ,	"? lakh crore"      , "http://myneta.info/meghalaya2018/index.php?action=show_winners&sort=default"         ,       "मेघालय"           ,    "? लाख करोड़"         }	,
            {	"Mizoram"	                    ,	"? lakh crore"      , "http://myneta.info/mizoram2018/index.php?action=show_winners&sort=default"           ,       "मिज़ोरम"           ,	    "? लाख करोड़"        }	,
            {	"Nagaland"	                    ,	"? lakh crore"      , "http://myneta.info/nagaland2018/index.php?action=show_winners&sort=default"          ,       "नागालैण्ड"          ,	    "? लाख करोड़"        }	,
            {	"Odisha"	                    ,	"? lakh crore"      , "http://myneta.info/odisha2019/index.php?action=show_winners&sort=default"            ,       "ओडिशा"           ,    "? लाख करोड़"        }	,
            {	"Puducherry"	                ,	"? lakh crore"      , "http://myneta.info/puducherry2016/index.php?action=show_winners&sort=default"        ,       "पुदुच्चेरी"           ,	    "? लाख करोड़"        }	,
            {	"Punjab"	                    ,	"? lakh crore" 	    , "http://myneta.info/punjab2017/index.php?action=show_winners&sort=default"            ,       "पंजाब"            ,	    "? लाख करोड़"        }	,
            {	"Rajasthan"	                    ,	"? lakh crore"      , "http://myneta.info/rajasthan2018/index.php?action=show_winners&sort=default"         ,       "राजस्थान"          ,    "? लाख करोड़"        }	,
            {	"Sikkim"	                    ,	"? lakh crore"      , "http://myneta.info/sikkim2019/index.php?action=show_winners&sort=default"            ,       "सिक्किम"          ,	    "? लाख करोड़"        }	,
            {	"Tamil Nadu"	                ,	"? lakh crore"      , "http://myneta.info/tamilnadu2016/index.php?action=show_winners&sort=default"         ,       "तमिल नाडु"         ,	    "? लाख करोड़"       }	,
            {	"Telangana"	                    ,	"? lakh crore"      , "http://myneta.info/telangana2018/index.php?action=show_winners&sort=default"         ,       "तेलंगाना"           ,	    "? लाख करोड़"       }	,
            {	"Tripura"	                    ,	"? lakh crore" 	    , "http://myneta.info/tripura2018/index.php?action=show_winners&sort=default"           ,       "त्रिपुरा"            ,	    "? लाख करोड़"       }	,
            {	"Uttar Pradesh"	                ,	"? lakh crore"      , "http://myneta.info/uttarpradesh2017/index.php?action=show_winners&sort=default"      ,       "उत्तर प्रदेश"         ,	    "? लाख करोड़"       }	,
            {	"Uttarakhand"	                ,	"? lakh crore"      , "http://myneta.info/uttarakhand2017/index.php?action=show_winners&sort=default"       ,       "उत्तराखण्ड"         ,     "? लाख करोड़"       }	,
            {	"West Bengal"	                ,	"? lakh crore"      , "http://myneta.info/westbengal2016/index.php?action=show_winners&sort=default"        ,       "पश्चिम बंगाल"      ,	  "? लाख करोड़"         }	,
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, MainActivity.sLANGUAGE_HINDI);
        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(getActivity(), "hi");
        }

        editor = mSharedPref.edit();

        view = inflater.inflate(R.layout.rajya, container, false);

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

        // Populating GUI
        DataFilter dataFilter = new DataFilter();

        String State = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);

        ArrayAdapter arrayAdapterState = new ArrayAdapter(view.getContext(), R.layout.spinner_text_style, dataFilter.getStates(mLanguage));
        arrayAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(arrayAdapterState);

        int spinnerPosition = arrayAdapterState.getPosition(State);
        spinnerState.setSelection(spinnerPosition);
        //Log.e(TAG, "state def: " + State);

        // set for dynamic handling
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String State = spinnerState.getItemAtPosition(spinnerState.getSelectedItemPosition()).toString();
                editor.putString(MainActivity.sSTATE, State).commit();

                String tmp = State;
                if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {// Firebase needs English, cant handle Hindi
                    String state = MainActivity.get_state(getContext(), MainActivity.sLANGUAGE_ENGLISH);
                    tmp = state;
                }
                tmp = tmp.replace(" ", "_");
                tmp = tmp.replace("&", "and");
                FirebaseLogger.send(getContext(), tmp);

                update_state_budget(State);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        FirebaseLogger.send(getContext(), "Rajya_Screen");

        govt_Click();
        duties_Click();
        vote_Click();
        note_Click();

        return view;
    }

    private void update_state_budget(String state) {
        int state_column = 0, budget_column = 1;

        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {    //In case of Hindi
            state_column = 3;
            budget_column = 4;
        }

        for (String[] i : state_budget) {
            if (i[state_column].equals(state)) {
                note2.setText(i[budget_column]);
                break;
            }
        }
    }

    private String get_state_url() {
        String url = " ";
        int state_column = 0, mla_link_column = 2;

        String state = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);

        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {    //In case of Hindi
            state_column = 3;
        }

        for (String[] i : state_budget) {
            if (i[state_column].equals(state)) {
                url = i[mla_link_column];
                break;
            }
        }
        return url;
    }

    public void vote_Click() {
        vote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = get_state_url();
                if(url != null && !url.equals(" ")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
        vote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = get_state_url();
                if(url != null && !url.equals(" ")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
        vote3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = get_state_url();
                if(url != null && !url.equals(" ")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
    }

    public void note_Click() {
        note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = get_layout_file();

                if(layout_id != 0) {
                    Intent intent = new Intent(view.getContext(), TaxRajya.class);
                    intent.putExtra("layout_id", layout_id);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
        note2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = get_layout_file();

                if(layout_id != 0) {
                    Intent intent = new Intent(view.getContext(), TaxRajya.class);
                    intent.putExtra("layout_id", layout_id);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
        note3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = get_layout_file();

                if(layout_id != 0) {
                    Intent intent = new Intent(view.getContext(), TaxRajya.class);
                    intent.putExtra("layout_id", layout_id);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
    }

    private int get_layout_file() {
        int layout_id;
        String state = mSharedPref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);

        if(state.equals("Bihar") || state.equals("बिहार"))
            layout_id = R.layout.budget_bihar;
        else
            layout_id = 0;

        return layout_id;
    }

    public void govt_Click() {
        govt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RajyaInfographics.class);
                startActivity(intent);
            }
        });
        govt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RajyaInfographics.class);
                startActivity(intent);
            }
        });
    }

    public void duties_Click() {
        duties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Duties.class);
                startActivity(intent);
            }
        });
    }

    public void tobedone(View view) {
        // Take the user to contact screen
        Intent intent = new Intent(view.getContext(), Contact.class);
        intent.putExtra("update_mp", true);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
        // Invite to join the team
        Toast.makeText(getContext(), R.string.next_version, Toast.LENGTH_LONG).show();
    }
}

