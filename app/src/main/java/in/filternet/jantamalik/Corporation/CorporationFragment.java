package in.filternet.jantamalik.Corporation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.FirebaseLogger;
import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_CORPORATION;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class CorporationFragment extends android.support.v4.app.Fragment {
    View view;
    String current_language;

    private SharedPreferences mSharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        current_language = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);
        if(current_language != null && current_language.equals(sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(getActivity(), "hi");
        }

        view = inflater.inflate(R.layout.corporate,container,false);

        FirebaseLogger.send(getContext(), "Corporation_Screen");

        return view;
    }

}

