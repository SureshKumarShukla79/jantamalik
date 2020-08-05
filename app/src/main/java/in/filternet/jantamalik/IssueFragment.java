package in.filternet.jantamalik;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static in.filternet.jantamalik.MainActivity.TAB_ISSUE;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_MARATHI;

public class IssueFragment extends Fragment {

    private final static String TAG = "Issue";

    private FloatingActionButton ui_add_issue;
    private View view;
    private SharedPreferences mSharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);
        if (mLanguage.equals(sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(getActivity(), "hi");
        }

        if (mLanguage.equals(sLANGUAGE_MARATHI)) {
            MainActivity.setUI_Lang(getActivity(), "mr");
        }

        view = inflater.inflate(R.layout.issues, container, false);

        LogEvents.send(getContext(), TAG);

        ui_add_issue = view.findViewById(R.id.add_issue);
        ui_add_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Contact.class);
                intent.putExtra("add_issue", true);
                intent.putExtra(TAB_NUMBER, TAB_ISSUE);
                startActivity(intent);
            }
        });

        play_puzzle();
        open_constitution();
        issue_inflation();
        issue_employment();
        issue_media();

        issue_train();
        issue_business();
        issue_curroption();

        issue_hospital();
        issue_drinking_water();
        issue_agriculture_water();
        issue_agriculture_loan();
        issue_price_realization();
        issue_agriculture_subsidy();

        issue_electricity();
        issue_police();
        issue_road();
        issue_public_transport();
        issue_traffic();

        issue_clean();
        issue_sewage();

        issue_spiritual();
        issue_girl_safety();

        return view;
    }

    private void open_constitution() {
        LinearLayout ui_constitution = view.findViewById(R.id.constitution_layout);

        ui_constitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Constitution";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_constitution;
                int title_id = R.string.constitution_text;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void play_puzzle() {
        LinearLayout ui_puzzle = view.findViewById(R.id.puzzle);

        ui_puzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean smart_voter = mSharedPref.getBoolean(MainActivity.bSMART_VOTER, false);
                if(smart_voter) {
                    user_became_smart();
                } else {
                    Intent intent = new Intent(view.getContext(), Puzzle.class);
                    startActivity(intent);
                }
            }
        });
    }

    //One copy in Puzzle and MainActivity
    private void user_became_smart() {
        ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.green_badge);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle(R.string.congratulation);
        builder.setMessage(R.string.smart_voter);
        builder.setView(image);
        builder.setPositiveButton(R.string.user_thanks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                LogEvents.send(getContext(), "Smart_Voter");
            }
        });
        builder.setNeutralButton(R.string.share, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                LogEvents.send(getContext(), "Share_Puzzle");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = getString(R.string.share_puzzle);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Important");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + MainActivity.USER_SHARE_APP);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void issue_spiritual() {
        LinearLayout spiritual_layout = view.findViewById(R.id.spiritual_layout);

        spiritual_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Spiritual";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_spiritual;
                int title_id = R.string.spiritual;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_girl_safety() {
        LinearLayout spiritual_layout = view.findViewById(R.id.girl_safety_layout);

        spiritual_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "GirlSafety";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_girl_safety;
                int title_id = R.string.girl_safety;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_employment() {
        LinearLayout employment_layout = view.findViewById(R.id.employment_layout);

        employment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Employment";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_employment;
                int title_id = R.string.employment;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_media() {
        LinearLayout media_or_afeem_layout = view.findViewById(R.id.media_or_afeem_layout);

        media_or_afeem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Media";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_media_or_afeem;
                int title_id = R.string.media_or_afeem;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_curroption() {
        LinearLayout corruption_layout = view.findViewById(R.id.corruption_layout);

        corruption_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Corruption";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_corruption;
                int title_id = R.string.corruption;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_train() {
        LinearLayout train_delay_layout = view.findViewById(R.id.train_delay_layout);

        train_delay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Train_Delay";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_train_delay;
                int title_id = R.string.train;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_business() {
        LinearLayout business_layout = view.findViewById(R.id.business_layout);

        business_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Business";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_business;
                int title_id = R.string.business;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_electricity() {
        LinearLayout electricity_layout = view.findViewById(R.id.electricity_layout);

        electricity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Electricity";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_electricity;
                int title_id = R.string.electricity;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_police() {
        LinearLayout police_layout = view.findViewById(R.id.police_layout);

        police_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Police";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_police;
                int title_id = R.string.police;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_road() {
        LinearLayout road_layout = view.findViewById(R.id.road_layout);

        road_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Poor_Road";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_poor_road;
                int title_id = R.string.poor_road;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_traffic() {
        LinearLayout traffic_layout = view.findViewById(R.id.traffic_layout);

        traffic_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Traffic";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_traffic;
                int title_id = R.string.traffic;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_hospital() {
        LinearLayout electricity_layout = view.findViewById(R.id.hospital_layout);

        electricity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Hospital";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_hospital;
                int title_id = R.string.hospital;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_drinking_water() {
        LinearLayout electricity_layout = view.findViewById(R.id.drinking_water_layout);

        electricity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Drink_Water";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_drinking_water;
                int title_id = R.string.drinking_water;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_agriculture_water() {
        LinearLayout electricity_layout = view.findViewById(R.id.agriculture_water_layout);

        electricity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Agri_Water";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_agriculture_water;
                int title_id = R.string.agriculture_water;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_agriculture_loan() {
        LinearLayout electricity_layout = view.findViewById(R.id.agriculture_loan_layout);

        electricity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Agri_Loan";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_agriculture_loan;
                int title_id = R.string.agriculture_loan;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_price_realization() {
        LinearLayout electricity_layout = view.findViewById(R.id.price_realization_layout);

        electricity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Price_Realization";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_price_realization;
                int title_id = R.string.price_realization;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_agriculture_subsidy() {
        LinearLayout electricity_layout = view.findViewById(R.id.agriculture_seed_layout);

        electricity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Agri_Subsidy";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_agriculture_subsidy;
                int title_id = R.string.agriculture_subsidy;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_public_transport() {
        LinearLayout electricity_layout = view.findViewById(R.id.public_transport_layout);

        electricity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Public_Transport";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_public_transport;
                int title_id = R.string.public_transport;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_sewage() {
        LinearLayout clean_layout = view.findViewById(R.id.sewage_layout);

        clean_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Sewage";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_sewage;
                int title_id = R.string.sewage;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_clean() {
        LinearLayout clean_layout = view.findViewById(R.id.cleanliness_layout);

        clean_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Clean";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_cleanliness;
                int title_id = R.string.cleanliness;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }

    private void issue_inflation() {
        //http://gstcouncil.gov.in/sites/default/files/NOTIFICATION%20PDF/goods-rates-booklet-03July2017.pdf
        LinearLayout clean_layout = view.findViewById(R.id.inflation_layout);

        clean_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Inflation";
                LogEvents.send(getContext(), event_name);

                int layout_id = R.layout.issue_inflation;
                int title_id = R.string.inflation;

                Intent intent = new Intent(view.getContext(), Issues.class);
                intent.putExtra("layout_id", layout_id);
                intent.putExtra("title_id", title_id);
                intent.putExtra("title", event_name);
                intent.putExtra("issues", true);
                startActivity(intent);
            }
        });
    }
}
