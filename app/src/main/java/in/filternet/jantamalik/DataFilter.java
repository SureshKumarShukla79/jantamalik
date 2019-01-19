package in.filternet.jantamalik;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class DataFilter {


    public List<String> getStates(String lang){
         List<String> states;

        int i,J=0;

        if (lang.equals(sLANGUAGE_HINDI))
            J=1;//hindi states in 8th column

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
        int i,J=0,K=3;
        List<String> areas;
        if (lang.equals(sLANGUAGE_HINDI)) {
            J = 1;
            K = 6;
        }
        areas = new ArrayList<>();
         for(i=0;i<MPdata.all_MPs.length;i++){
             if (getState == MPdata.all_MPs[i][J])
                 areas.add(MPdata.all_MPs[i][K]); //7th column has hindi
             else
                 continue;
         }
            return areas;
    }

    public class MP_info{
        public  String name,phone,email,address;
    }


    public MP_info getMPInfo(String lang,String MPArea) {
        Log.e("",lang+" "+MPArea);
        int i,J=3,nameCol=4,phoneCol=9,emailCol=10,addCol=11;

        MP_info mp_info = new MP_info();
        if (lang.equals(sLANGUAGE_HINDI)) {
            J = 6;
            nameCol = 7;
            phoneCol =9;
            emailCol =10;
            addCol =   11;
        }
        for(i=0;i<MPdata.all_MPs.length;i++){
            if (MPArea == MPdata.all_MPs[i][J]) {
                mp_info.name = MPdata.all_MPs[i][nameCol];
                mp_info.phone = MPdata.all_MPs[i][phoneCol];
                mp_info.email = MPdata.all_MPs[i][emailCol];
                mp_info.address = MPdata.all_MPs[i][addCol];
            } else
                continue;
        }
        return mp_info;
    }
}
