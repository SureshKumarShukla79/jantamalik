package in.jantamalik.Kendra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import in.jantamalik.Rajya.MP_2_MLA;

public class DataFilter {
    String TAG = "DataFilter";

    public List<String> getMPAreas() {
        // Treemap to keep data sorted names of Areas.
        TreeMap<String, Integer> areas = new TreeMap<>();
        int mp_area_column = 0;
        for (int i = 0; i < MP_2_MLA.mapping.length; i++) {
            areas.put(MP_2_MLA.mapping[i][mp_area_column], i);
        }

        Set<Map.Entry<String, Integer>> set = areas.entrySet();
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Integer> tree : set) {
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

}
