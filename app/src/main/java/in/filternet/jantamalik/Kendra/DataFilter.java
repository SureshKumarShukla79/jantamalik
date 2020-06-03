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
import in.filternet.jantamalik.Rajya.VidhayakData;

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

    public List<String> getMLAAreas(String lang, String getState) {
        int i,J=0,K=3;
        if (lang.equals(MainActivity.sLANGUAGE_HINDI) || lang.equals(MainActivity.sLANGUAGE_MARATHI)) {
            J = 1;
            K = 6;
        }

        // Treemap to keep data sorted names of Areas.
        TreeMap<String,Integer> areas = new TreeMap<>();
        for(i=0; i<VidhayakData.all_MLAs.length; i++){
            if (getState.equals(VidhayakData.all_MLAs[i][J]))
                areas.put(VidhayakData.all_MLAs[i][K],i); //7th column has hindi
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

    public MP_info getMLAInfo(String lang, String MLAArea) {
        //Log.e(TAG,lang+" "+MLAArea);
        int i,J=3,nameCol=4,phoneCol=9,emailCol=10,addCol=11;

        MP_info mla_info = new MP_info();
        if (lang.equals(MainActivity.sLANGUAGE_HINDI) || lang.equals(MainActivity.sLANGUAGE_MARATHI)) {
            J = 6;
            nameCol = 7;
        }
        for(i=0; i< VidhayakData.all_MLAs.length; i++){
            if (MLAArea.equals(VidhayakData.all_MLAs[i][J])) {
                mla_info.name = VidhayakData.all_MLAs[i][nameCol];
                mla_info.phone = VidhayakData.all_MLAs[i][phoneCol];
                mla_info.email = VidhayakData.all_MLAs[i][emailCol];
                mla_info.address = VidhayakData.all_MLAs[i][addCol];
            } else
                continue;
        }
        //Log.e(TAG, mla_info.name+" "+mla_info.email+" "+mla_info.phone+" "+mla_info.address);

        return mla_info;
    }
}
