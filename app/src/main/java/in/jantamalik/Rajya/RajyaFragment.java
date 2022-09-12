package in.jantamalik.Rajya;

import static in.jantamalik.MainActivity.TAB_NUMBER;
import static in.jantamalik.MainActivity.TAB_RAJYA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import in.jantamalik.Contact;
import in.jantamalik.Kendra.DataFilter;
import in.jantamalik.LogEvents;
import in.jantamalik.R;

public class RajyaFragment extends Fragment {
    String TAG = "Rajya";

    private View view;
    private TextView note2, govt1;
    private ImageButton note1, note3;
    private ImageView govt2;
    private CardView duties;
    private LinearLayout ui_vote;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;

    final private String[] winners = {"http://myneta.info/uttarpradesh2017/index.php?action=show_winners&sort=default"};
    final private String[] state_budget = {"5 लाख करोड़"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = mSharedPref.edit();

        view = inflater.inflate(R.layout.rajya, container, false);

        note1 = view.findViewById(R.id.note1);
        note2 = view.findViewById(R.id.note2);
        note3 = view.findViewById(R.id.note3);

        govt1 = view.findViewById(R.id.govt1);
        govt2 = view.findViewById(R.id.govt2);

        duties = view.findViewById(R.id.duties);
        ui_vote = view.findViewById(R.id.vote);

        // Populating GUI
        DataFilter dataFilter = new DataFilter();

        // set for dynamic handling
        LogEvents.send(getContext(), TAG);

        govt_Click();
        duties_Click();
        vote();
        note_Click();

        return view;
    }

    private void update_state_budget(String state) {
        note2.setText(state_budget[0]);
    }

    private String get_state_url() {
        return winners[0];
    }

    private void vote() {
        ui_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VoteVidhayak.class);
                startActivity(intent);
            }
        });
    }

    public void note_Click() {
        note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = get_layout_file();

                if(layout_id != 0) {
                    Intent intent = new Intent(view.getContext(), TaxRajya.class);
                    intent.putExtra("layout_id", layout_id);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
        note2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = get_layout_file();

                if(layout_id != 0) {
                    Intent intent = new Intent(view.getContext(), TaxRajya.class);
                    intent.putExtra("layout_id", layout_id);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
        note3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = get_layout_file();

                if(layout_id != 0) {
                    Intent intent = new Intent(view.getContext(), TaxRajya.class);
                    intent.putExtra("layout_id", layout_id);
                    startActivity(intent);
                } else {
                    tobedone(view);
                }
            }
        });
    }

    private int get_layout_file() {
        int layout_id;
        layout_id = R.layout.budget_uttarpradesh;
        return layout_id;
    }

    public void govt_Click() {
        govt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RajyaInfographics.class);
                startActivity(intent);
            }
        });
        govt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RajyaInfographics.class);
                startActivity(intent);
            }
        });
    }

    public void duties_Click() {
        duties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Duties.class);
                startActivity(intent);
            }
        });
    }

    public void tobedone(View view) {
        // Take the user to contact screen
        Intent intent = new Intent(view.getContext(), Contact.class);
        intent.putExtra("update_mp", true);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
        // Invite to join the team
        Toast.makeText(getContext(), R.string.next_version, Toast.LENGTH_LONG).show();
    }
}

