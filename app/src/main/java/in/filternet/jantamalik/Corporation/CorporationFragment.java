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
import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_CORPORATION;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class CorporationFragment extends android.support.v4.app.Fragment {
    View view;
    private List<String> arr;
    private FloatingActionButton ui_add_issue;
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
        ui_add_issue = view.findViewById(R.id.add_issue);

        ui_add_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Contact.class);
                intent.putExtra("add_issue", true);
                intent.putExtra(TAB_NUMBER, TAB_CORPORATION);
                startActivity(intent);
            }
        });

        issue_clean();

        return view;
    }

    private void issue_clean() {
        LinearLayout clean_layout = view.findViewById(R.id.cleanliness_layout);

        clean_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = R.layout.issue_cleanliness;
                int title_id = R.string.cleanliness;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("corporation", true);
                startActivity(intent);
            }
        });
    }

}

