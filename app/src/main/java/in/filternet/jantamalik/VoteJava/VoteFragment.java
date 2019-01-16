package in.filternet.jantamalik.VoteJava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

public class VoteFragment extends Fragment {
    private View view;
    private Button buttonMP;
    private ImageButton imageButtonMP;
    private Button buttonMP2;
    private ImageButton imageButtonMP2;
    private Button buttonMP3;
    private ImageButton imageButtonMP3;
    private Intent intent;

    private SharedPreferences mSharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String current_language = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, null);
        if (current_language != null && current_language.equals(MainActivity.sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(getActivity(), "hi");
        }

        view = inflater.inflate(R.layout.vote_fragment, container, false);

        buttonMP = view.findViewById(R.id.Vote_Item_Button);
        imageButtonMP = view.findViewById(R.id.Vote_Item_imageButton);
        //second
        buttonMP2 = view.findViewById(R.id.Vote_Item_Button2);
        imageButtonMP2 = view.findViewById(R.id.Vote_Item_imageButton2);
        //third
        buttonMP3 = view.findViewById(R.id.Vote_Item_Button3);
        imageButtonMP3 = view.findViewById(R.id.Vote_Item_imageButton3);

        MP_Click();
        MLA_Click();
        Parshad_Click();

      return view;
    }

    public void MP_Click(){
        buttonMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             intent = new Intent(view.getContext(), VoteMP.class);
             startActivity(intent);
            }
        });

       imageButtonMP.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               intent = new Intent(view.getContext(), VoteMP.class);
               startActivity(intent);
           }
       });
    }

    public void MLA_Click(){
        buttonMP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(view.getContext(),"Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        imageButtonMP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void Parshad_Click(){
        buttonMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        imageButtonMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

