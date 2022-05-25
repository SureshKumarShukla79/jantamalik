package in.filternet.jantamalik.Corporation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import in.filternet.jantamalik.LogEvents;
import in.filternet.jantamalik.R;

public class CorporationFragment extends Fragment {

    private final static String TAG ="CorporationFragment";

    View view;
    String current_language;

    private SharedPreferences mSharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        view = inflater.inflate(R.layout.corporate,container,false);

        LogEvents.send(getContext(), TAG);

        return view;
    }

}

