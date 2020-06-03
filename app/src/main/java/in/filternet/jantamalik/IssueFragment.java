package in.filternet.jantamalik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static in.filternet.jantamalik.MainActivity.TAB_ISSUE;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_MARATHI;

public class IssueFragment extends Fragment {

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

        FirebaseLogger.send(getContext(), "Issue_Screen");

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

        issue_inflation();
        issue_employment();
        issue_media();
        issue_mp_no_response();
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

    private void issue_spiritual() {
        LinearLayout spiritual_layout = view.findViewById(R.id.spiritual_layout);

        spiritual_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Issue_Spiritual";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Girl_Safety";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Employment";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Media";
                FirebaseLogger.send(getContext(), event_name);

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

    private void issue_mp_no_response() {
        LinearLayout mp_no_response_layout = view.findViewById(R.id.mp_no_response_layout);

        mp_no_response_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = "Issue_MP_No_Response";
                FirebaseLogger.send(getContext(), event_name);

                int layout_id = R.layout.issue_mp_no_response;
                int title_id = R.string.mp_no_response;

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
                String event_name = "Issue_Corruption";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Train_Delay";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Business";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Electricity";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Police";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Road";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Traffic";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Hospital";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Drinking_Water";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Agriculture_Water";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Agriculture_Loan";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Price_Realization";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Agriculture_Subsidy";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Public_Transport";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Sewage";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Clean";
                FirebaseLogger.send(getContext(), event_name);

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
                String event_name = "Issue_Inflation";
                FirebaseLogger.send(getContext(), event_name);

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
