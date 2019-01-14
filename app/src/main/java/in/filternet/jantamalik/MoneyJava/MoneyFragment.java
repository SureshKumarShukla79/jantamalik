package in.filternet.jantamalik.MoneyJava;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

public class MoneyFragment extends Fragment {
    private GridView gridView;

    View view;

    private SharedPreferences mSharedPref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String current_language = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, null);
        if(current_language != null && current_language.equals(MainActivity.sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(getActivity(), "hi");
        }

        view = inflater.inflate(R.layout.money_fragment,container,false);
        gridView = view.findViewById(R.id.money_tab_gridview);
        gridView.setAdapter(new ImageAdapter(view.getContext()));
        return view;
    }
}
