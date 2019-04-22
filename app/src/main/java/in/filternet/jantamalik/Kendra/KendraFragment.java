package in.filternet.jantamalik.Kendra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.FirebaseLogger;
import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_KENDRA;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class KendraFragment extends Fragment {
    String TAG = "VoteFragment";

    private View view;
    private FloatingActionButton ui_add_issue;

    private Intent intent;

    private SharedPreferences mSharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);
        if (mLanguage.equals(sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(getActivity(), "hi");
        }

        view = inflater.inflate(R.layout.kendra, container, false);
        ui_add_issue = view.findViewById(R.id.add_issue);
        ui_add_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Contact.class);
                intent.putExtra("add_issue", true);
                intent.putExtra(TAB_NUMBER, TAB_KENDRA);
                startActivity(intent);
            }
        });

        FirebaseLogger.send(getContext(), "Kendra_Screen");

        vote();
        note();
        infographics();
        responsibility();

        issue_media();
        issue_mp_no_response();
        issue_train();
        issue_business();
        issue_curroption();
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

    private void issue_media() {
        FirebaseLogger.send(getContext(), "Issue_Media");

        LinearLayout media_or_afeem_layout = view.findViewById(R.id.media_or_afeem_layout);

        media_or_afeem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = R.layout.issue_media_or_afeem;
                int title_id = R.string.media_or_afeem;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("kendra", true);
                startActivity(intent);
            }
        });
    }

    private void issue_mp_no_response() {
        FirebaseLogger.send(getContext(), "Issue_MP_No_Response");

        LinearLayout mp_no_response_layout = view.findViewById(R.id.mp_no_response_layout);

        mp_no_response_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = R.layout.issue_mp_no_response;
                int title_id = R.string.mp_no_response;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("kendra", true);
                startActivity(intent);
            }
        });
    }

    private void issue_curroption() {
        FirebaseLogger.send(getContext(), "Issue_Corruption");

        LinearLayout corruption_layout = view.findViewById(R.id.corruption_layout);

        corruption_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = R.layout.issue_corruption;
                int title_id = R.string.corruption;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("kendra", true);
                startActivity(intent);
            }
        });
    }

    private void issue_train() {
        FirebaseLogger.send(getContext(), "Issue_Train_Delay");

        LinearLayout train_delay_layout = view.findViewById(R.id.train_delay_layout);

        train_delay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = R.layout.issue_train_delay;
                int title_id = R.string.train;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("kendra", true);
                startActivity(intent);
            }
        });
    }

    private void issue_business() {
        FirebaseLogger.send(getContext(), "Issue_Business");

        LinearLayout business_layout = view.findViewById(R.id.business_layout);

        business_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layout_id = R.layout.issue_business;
                int title_id = R.string.business;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("kendra", true);
                startActivity(intent);
            }
        });
    }

    private void issue_election_2019() {
        FirebaseLogger.send(getContext(), "2019_Election");

        LinearLayout election_2019_layout = view.findViewById(R.id.election_2019_layout);

        election_2019_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

