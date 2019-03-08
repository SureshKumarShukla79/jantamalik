package in.filternet.jantamalik.Kendra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class KendraFragment extends Fragment {
    String TAG = "VoteFragment";

    private View view;
    private TextView vote2, note2, govt1;
    private ImageButton vote1, vote3, note1, note3;
    private ImageView govt2;
    private LinearLayout duties;

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

        vote_Click();
        note_Click();
        govt_Click();
        duties_Click();

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
}

