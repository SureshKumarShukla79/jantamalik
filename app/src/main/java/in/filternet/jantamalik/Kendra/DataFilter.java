package in.filternet.jantamalik.Kendra;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.Rajya.Andhra_Pradesh;
import in.filternet.jantamalik.Rajya.Arunachal_Pradesh;
import in.filternet.jantamalik.Rajya.Assam;
import in.filternet.jantamalik.Rajya.Bihar;
import in.filternet.jantamalik.Rajya.Chhattisgarh;
import in.filternet.jantamalik.Rajya.Goa;
import in.filternet.jantamalik.Rajya.Gujarat;
import in.filternet.jantamalik.Rajya.Haryana;
import in.filternet.jantamalik.Rajya.Himachal_Pradesh;
import in.filternet.jantamalik.Rajya.Jammu_and_Kashmir;
import in.filternet.jantamalik.Rajya.Jharkhand;
import in.filternet.jantamalik.Rajya.Karnataka;
import in.filternet.jantamalik.Rajya.Kerala;
import in.filternet.jantamalik.Rajya.MP_2_MLA;
import in.filternet.jantamalik.Rajya.Madhya_Pradesh;
import in.filternet.jantamalik.Rajya.Maharashtra;
import in.filternet.jantamalik.Rajya.Manipur;
import in.filternet.jantamalik.Rajya.Meghalaya;
import in.filternet.jantamalik.Rajya.Mizoram;
import in.filternet.jantamalik.Rajya.Nagaland;
import in.filternet.jantamalik.Rajya.National_Capital_Territory_of_Delhi;
import in.filternet.jantamalik.Rajya.Odisha;
import in.filternet.jantamalik.Rajya.Puducherry;
import in.filternet.jantamalik.Rajya.Punjab;
import in.filternet.jantamalik.Rajya.Rajasthan;
import in.filternet.jantamalik.Rajya.Sikkim;
import in.filternet.jantamalik.Rajya.Tamil_Nadu;
import in.filternet.jantamalik.Rajya.Telangana;
import in.filternet.jantamalik.Rajya.Tripura;
import in.filternet.jantamalik.Rajya.Uttar_Pradesh;
import in.filternet.jantamalik.Rajya.Uttarakhand;
import in.filternet.jantamalik.Rajya.West_Bengal;

public class DataFilter {
    String TAG = "DataFilter";

    public List<String> getStates(String lang){
         List<String> states;

        int i,J=0;

        if (lang.equals(MainActivity.sLANGUAGE_HINDI) || lang.equals(MainActivity.sLANGUAGE_MARATHI))
            J=3;//hindi states in 3rd column

        states = new ArrayList<>();
         for(i = 0; i < MPdata.all_MPs.length; i++) {
             if (i == 0)
                 states.add(MPdata.all_MPs[i][J]);
             else {
                 // match with previous entry and skip if same. Basically to filter out duplicates.
                 if (MPdata.all_MPs[i - 1][J] != MPdata.all_MPs[i][J])
                     states.add(MPdata.all_MPs[i][J]);
                 else
                     continue;
             }
         }
         return states;
    }

    public List<String> getMPAreas(String lang,String getState) {
        int i,J=0,K=1;
        if (lang.equals(MainActivity.sLANGUAGE_HINDI) || lang.equals(MainActivity.sLANGUAGE_MARATHI)) {
            J = 3;      //hindi state
            K = 4;      //hindi constituency
        }

        // Treemap to keep data sorted names of Areas.
        TreeMap<String,Integer> areas = new TreeMap<>();
        for(i=0;i<MPdata.all_MPs.length;i++){
            if (getState.equals(MPdata.all_MPs[i][J]))
                areas.put(MPdata.all_MPs[i][K],i); //7th column has hindi
             else
                 continue;
        }

        Set<Map.Entry<String,Integer>> set = areas.entrySet();
        List<String> list = new ArrayList<>();
        for(Map.Entry<String,Integer> tree : set){
            list.add(tree.getKey());
        }
        return list;
    }

    public class MP_info{
        public String name;
        public String phone;
        public String phone2;
        public String phone3;
        public String email;
        public String email2;
        public String address;
    }


    public MP_info getMPInfo(Context context, String lang, String MPArea) {
      //  Log.e(TAG,lang+" "+MPArea);
        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String state = shared_pref.getString(MainActivity.sSTATE, MainActivity.DEFAULT_STATE);

        int state_column = 0, area_column = 1, name_column = 2;
        int phone_column = 6, phone2_column = 7, phone3_column = 8;
        int email_column = 9, email2_column = 10;
        int address_column = 11;

        MP_info mp_info = new MP_info();
        if (lang.equals(MainActivity.sLANGUAGE_HINDI) || lang.equals(MainActivity.sLANGUAGE_MARATHI)) {
            state_column = 3;
            area_column = 4;
            name_column = 5;
        }
        for(int i=0; i<MPdata.all_MPs.length; i++){
            if (state.equals(MPdata.all_MPs[i][state_column]) && MPArea.equals(MPdata.all_MPs[i][area_column])) {
                mp_info.name = MPdata.all_MPs[i][name_column];
                mp_info.phone = MPdata.all_MPs[i][phone_column];
                mp_info.phone2 = MPdata.all_MPs[i][phone2_column];
                mp_info.phone3 = MPdata.all_MPs[i][phone3_column];
                mp_info.email = MPdata.all_MPs[i][email_column];
                mp_info.email2 = MPdata.all_MPs[i][email2_column];
                mp_info.address = MPdata.all_MPs[i][address_column];
            } else
                continue;
        }
      //  Log.e(TAG,temp + mp_info.name+" "+mp_info.email+" "+mp_info.phone+" "+mp_info.address);

        return mp_info;
    }

    public List<String> get_MLA_area_as_per_state(String lang, String state) {
        int i,J=0,K=1;
        if (lang.equals(MainActivity.sLANGUAGE_HINDI) || lang.equals(MainActivity.sLANGUAGE_MARATHI)) {
            J = 3;      //hindi state
            K = 4;      //hindi constituency
        }

        String[][] infos = get_MLA_areas_of_state(state);
        // Treemap to keep data sorted names of Areas.
        TreeMap<String,Integer> areas = new TreeMap<>();
        for(i=0; i<infos.length; i++){
            areas.put(infos[i][K], i);
        }

        Set<Map.Entry<String,Integer>> set = areas.entrySet();
        List<String> list = new ArrayList<>();
        for(Map.Entry<String,Integer> tree : set){
            list.add(tree.getKey());
        }
        return list;
    }

    public List<String> get_MLA_area_as_per_MP_area(String MP_area) {
        int mp_area_column = 0;

        // Treemap to keep data sorted names of Areas.
        TreeMap<String,Integer> areas = new TreeMap<>();
        for(int i=0; i< MP_2_MLA.mapping.length; i++){
            if (MP_area.equals(MP_2_MLA.mapping[i][mp_area_column])) {
                for (int k = 1; k < MP_2_MLA.mapping[i].length; k++) {
                    areas.put(MP_2_MLA.mapping[i][k], i);
                }
            }
        }

        Set<Map.Entry<String,Integer>> set = areas.entrySet();
        List<String> list = new ArrayList<>();
        for(Map.Entry<String,Integer> tree : set){
            list.add(tree.getKey());
        }
        return list;
    }

    public MP_info getMLAInfo(String lang, String state, String mla_area) {
        //Log.e(TAG,lang + ", " + state + ", "+ mla_area);

        int area_column = 1, name_column = 2;
        int phone_column = 6, phone2_column = 7, phone3_column = 8;
        int email_column = 9, email2_column = 10;
        int address_column = 11;

        MP_info mla_info = new MP_info();
        if (lang.equals(MainActivity.sLANGUAGE_HINDI) || lang.equals(MainActivity.sLANGUAGE_MARATHI)) {
            area_column = 4;
            name_column = 5;
        }

        String[][] infos = get_MLA_areas_of_state(state);

        for (String[] info : infos) {
            if (mla_area.equals(info[area_column])) {
                mla_info.name = info[name_column];
                mla_info.phone = info[phone_column];
                mla_info.phone2 = info[phone2_column];
                mla_info.phone3 = info[phone3_column];
                mla_info.email = info[email_column];
                mla_info.email2 = info[email2_column];
                mla_info.address = info[address_column];
            } else
                continue;
        }
        //Log.e(TAG, mla_info.name+" "+mla_info.email+" "+mla_info.phone+" "+mla_info.address);

        return mla_info;
    }

    public boolean has_MP_2_MLA_mapping(String MP_area) {
        int mp_area_column = 0;

        for(int i=0; i< MP_2_MLA.mapping.length; i++){
            if (MP_area.equals(MP_2_MLA.mapping[i][mp_area_column])) {
                //Log.e(TAG, "has_MP_2_MLA_mapping: true");
                return true;
            }
        }

        return false;
    }

    private String[][] get_MLA_areas_of_state(String state) {
        String[][] infos = new String[0][];

        switch (state) {
            case "Andhra Pradesh":
            case "आन्ध्र प्रदेश":
                infos = Andhra_Pradesh.MLAs;
                break;

            case "Arunachal Pradesh":
            case "अरुणाचल प्रदेश":
                infos = Arunachal_Pradesh.MLAs;
                break;

            case "Assam":
            case "असम":
                infos = Assam.MLAs;
                break;

            case "Bihar":
            case "बिहार":
                infos = Bihar.MLAs;
                break;

            case "Chhattisgarh":
            case "छत्तीसगढ़":
                infos = Chhattisgarh.MLAs;
                break;

            case "Goa":
            case "गोवा":
                infos = Goa.MLAs;
                break;

            case "Gujarat":
            case "गुजरात":
                infos = Gujarat.MLAs;
                break;

            case "Haryana":
            case "हरियाणा":
                infos = Haryana.MLAs;
                break;

            case "Himachal Pradesh":
            case "हिमाचल प्रदेश":
                infos = Himachal_Pradesh.MLAs;
                break;

            case "Jammu & Kashmir":
            case "जम्मू और कश्मीर":
                infos = Jammu_and_Kashmir.MLAs;
                break;

            case "Jharkhand":
            case "झारखण्ड":
                infos = Jharkhand.MLAs;
                break;

            case "Karnataka":
            case "कर्नाटक":
                infos = Karnataka.MLAs;
                break;

            case "Kerala":
            case "केरल":
                infos = Kerala.MLAs;
                break;

            case "Madhya Pradesh":
            case "मध्य प्रदेश":
                infos = Madhya_Pradesh.MLAs;
                break;

            case "Maharashtra":
            case "महाराष्ट्र":
                infos = Maharashtra.MLAs;
                break;

            case "Manipur":
            case "मणिपुर":
                infos = Manipur.MLAs;
                break;

            case "Meghalaya":
            case "मेघालय":
                infos = Meghalaya.MLAs;
                break;

            case "Mizoram":
            case "मिज़ोरम":
                infos = Mizoram.MLAs;
                break;

            case "Nagaland":
            case "नागालैण्ड":
                infos = Nagaland.MLAs;
                break;

            case "National Capital Territory of Delhi":
            case "दिल्ली":
                infos = National_Capital_Territory_of_Delhi.MLAs;
                break;

            case "Odisha":
            case "ओडिशा":
                infos = Odisha.MLAs;
                break;

            case "Puducherry":
            case "पुदुच्चेरी":
                infos = Puducherry.MLAs;
                break;

            case "Punjab":
            case "पंजाब":
                infos = Punjab.MLAs;
                break;

            case "Rajasthan":
            case "राजस्थान":
                infos = Rajasthan.MLAs;
                break;

            case "Sikkim":
            case "सिक्किम":
                infos = Sikkim.MLAs;
                break;

            case "Tamil Nadu":
            case "तमिल नाडु":
                infos = Tamil_Nadu.MLAs;
                break;

            case "Telangana":
            case "तेलंगाना":
                infos = Telangana.MLAs;
                break;

            case "Tripura":
            case "त्रिपुरा":
                infos = Tripura.MLAs;
                break;

            case "Uttar Pradesh":
            case "उत्तर प्रदेश":
                infos = Uttar_Pradesh.MLAs;
                break;

            case "Uttarakhand":
            case "उत्तराखण्ड":
                infos = Uttarakhand.MLAs;
                break;

            case "West Bengal":
            case "पश्चिम बंगाल":
                infos = West_Bengal.MLAs;
                break;
        }

        return infos;
    }
}
