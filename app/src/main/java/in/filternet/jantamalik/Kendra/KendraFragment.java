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
import in.filternet.jantamalik.Issues;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_KENDRA;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class KendraFragment extends Fragment {
    String TAG = "VoteFragment";

    private View view;
    private TextView vote2, note2, govt1;
    private ImageButton vote1, vote3, note1, note3;
    private ImageView govt2;
    private LinearLayout duties;
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

        vote1 = view.findViewById(R.id.vote1);
        vote2 = view.findViewById(R.id.vote2);
        vote3 = view.findViewById(R.id.vote3);

        note1 = view.findViewById(R.id.note1);
        note2 = view.findViewById(R.id.note2);
        note3 = view.findViewById(R.id.note3);

        govt1 = view.findViewById(R.id.govt1);
        govt2 = view.findViewById(R.id.govt2);

        duties = view.findViewById(R.id.duties);

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

        vote_Click();
        note_Click();
        govt_Click();
        duties_Click();

        issue_media();
        issue_mp_no_response();
        issue_train();
        issue_business();
        issue_curroption();
        issue_election_2019();

        return view;
    }

    public void vote_Click(){
        vote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), VoteMP.class);
                startActivity(intent);
            }
        });
        vote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             intent = new Intent(view.getContext(), VoteMP.class);
             startActivity(intent);
            }
        });
       vote3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               intent = new Intent(view.getContext(), VoteMP.class);
               startActivity(intent);
           }
       });
    }

    public void note_Click(){
        note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), TaxKendra.class);
                startActivity(intent);
            }
        });
        note2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), TaxKendra.class);
                startActivity(intent);
            }
        });
        note3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), TaxKendra.class);
                startActivity(intent);
            }
        });
    }

    public void govt_Click(){
        govt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), Infographics.class);
                startActivity(intent);
            }
        });
        govt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), Infographics.class);
                startActivity(intent);
            }
        });
    }

    public void duties_Click() {
        duties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), DutiesKendra.class);
                startActivity(intent);
            }
        });
    }

    private void issue_media() {
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

