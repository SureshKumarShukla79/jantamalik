package in.filternet.jantamalik.Kendra;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import in.filternet.jantamalik.Rajya.MP_2_MLA;
import in.filternet.jantamalik.Rajya.Uttar_Pradesh;

public class DataFilter {
    String TAG = "DataFilter";

    public List<String> getStates(String lang){
         List<String> states;

        int i, J = 3;//hindi states in 3rd column

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

    public List<String> getMPAreas() {
        int i;

        // Treemap to keep data sorted names of Areas.
        TreeMap<String, Integer> areas = new TreeMap<>();
        for (i = 0; i < MPdata.all_MPs.length; i++) {
            areas.put(MPdata.all_MPs[i][0], i);
        }

        Set<Map.Entry<String, Integer>> set = areas.entrySet();
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

        int state_column = 0, area_column = 1, name_column = 2;
        int phone_column = 6, phone2_column = 7, phone3_column = 8;
        int email_column = 9, email2_column = 10;
        int address_column = 11;

        MP_info mp_info = new MP_info();
        state_column = 3;
        area_column = 4;
        name_column = 5;

        for (int i = 0; i < MPdata.all_MPs.length; i++) {
            if (MPArea.equals(MPdata.all_MPs[i][area_column])) {
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

    public List<String> get_MLA_area_as_per_state() {
        int i, J = 0, K = 1;

        String[][] infos = Uttar_Pradesh.MLAs;
        // Treemap to keep data sorted names of Areas.
        TreeMap<String, Integer> areas = new TreeMap<>();
        for (i = 0; i < infos.length; i++) {
            areas.put(infos[i][K], i);
        }

        Set<Map.Entry<String, Integer>> set = areas.entrySet();
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
        for (int i = 0; i < MP_2_MLA.mapping.length; i++) {
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

    public MP_info getMLAInfo(String mla_area) {
        //Log.e(TAG,lang + ", " + state + ", "+ mla_area);

        int area_column = 2, name_column = 3;
        int phone_column = 4, phone2_column = 5, phone3_column = 6;
        int email_column = 7, email2_column = 8;
        int address_column = 9;

        MP_info mla_info = new MP_info();

        String[][] infos = Uttar_Pradesh.MLAs;

        for (String[] info : infos) {
            if (mla_area.equals(info[area_column])) {
                mla_info.name = info[name_column];
                mla_info.phone = info[phone_column];
                mla_info.phone2 = info[phone2_column];
                mla_info.phone3 = info[phone3_column];
                mla_info.email = info[email_column];
                mla_info.email2 = info[email2_column];
                mla_info.address = info[address_column];
                break;
            }
        }
        //Log.e(TAG, mla_info.name+" "+mla_info.email+" "+mla_info.phone+" "+mla_info.address);

        return mla_info;
    }

    public boolean has_MP_2_MLA_mapping(String MP_area) {
        int mp_area_column = 0;

        for (int i = 0; i < MP_2_MLA.mapping.length; i++) {
            if (MP_area.equals(MP_2_MLA.mapping[i][mp_area_column])) {
                //Log.e(TAG, "has_MP_2_MLA_mapping: true");
                return true;
            }
        }

        return false;
    }
}
