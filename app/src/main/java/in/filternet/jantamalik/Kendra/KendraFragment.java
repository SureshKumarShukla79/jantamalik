package in.filternet.jantamalik.Kendra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.LogEvents;
import in.filternet.jantamalik.R;

public class KendraFragment extends Fragment {
    String TAG = "Kendra";

    private View view;
    private Intent intent;

    private SharedPreferences mSharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        view = inflater.inflate(R.layout.kendra, container, false);

        LogEvents.send(getContext(), TAG);

        vote();
        note();
        infographics();
        responsibility();
        issue_election_2019();

        return view;
    }

    private void infographics() {
        LinearLayout ui_infographics = view.findViewById(R.id.infographics);

        ui_infographics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Infographics.class);
                startActivity(intent);
            }
        });
    }

    private void responsibility() {
        LinearLayout ui_responsibility = view.findViewById(R.id.responsibility);

        ui_responsibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), DutiesKendra.class);
                startActivity(intent);
            }
        });
    }

    private void note() {
        LinearLayout ui_note = view.findViewById(R.id.note);

        ui_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), TaxKendra.class);
                startActivity(intent);
            }
        });
    }

    private void vote() {
        LinearLayout ui_vote = view.findViewById(R.id.vote);

        ui_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), VoteMP.class);
                startActivity(intent);
            }
        });
    }

    private void issue_election_2019() {
        LinearLayout election_2019_layout = view.findViewById(R.id.election_2019_layout);

        election_2019_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogEvents.send(getContext(), "Election_2019");

                int layout_id = R.layout.issue_election_2019;
                int title_id = R.string.election_2019;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("kendra", true);
                startActivity(intent);
            }
        });
    }
}

