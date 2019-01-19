package in.filternet.jantamalik;

import java.util.ArrayList;
import java.util.List;

public class DataFilter {

    private int i,j;
    private  List<String> states;
    private  List<String> areas;

    public List<String> getStates(){
        states = new ArrayList<>();
         for(i = 0; i < MPdata.all_MPs.length; i++) {
             if (i == 0)
                 states.add(MPdata.all_MPs[i][0]);
             else {
                 // match with previous entry and skip if same. Basically to filter out duplicates.
                 if (MPdata.all_MPs[i - 1][0] != MPdata.all_MPs[i][0])
                     states.add(MPdata.all_MPs[i][0]);
                 else
                     continue;
             }
         }
         return states;
    }

    public List<String> getMPAreas(String getState) {
        areas = new ArrayList<>();
         for(i=0;i<MPdata.all_MPs.length;i++){
             if (getState == MPdata.all_MPs[i][0])
                 areas.add(MPdata.all_MPs[i][2]);
             else
                 continue;
         }
            return areas;
    }
  /*  public String getMPDetails(String MParea){

    }*/
}
