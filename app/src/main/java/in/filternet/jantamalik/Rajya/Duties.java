package in.filternet.jantamalik.Rajya;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import in.filternet.jantamalik.Constants;
import in.filternet.jantamalik.LogEvents;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;

public class Duties extends AppCompatActivity {

    private final static String TAG ="Duties";

    private SharedPreferences mSharedPref;
    private Toolbar toolbar;
    private String mLanguage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, null);
        if (mLanguage != null && mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(this, "hi");
        }

        if (mLanguage != null && mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)) {
            MainActivity.setUI_Lang(this, "mr");
        }

        setContentView(R.layout.duties_rajya);

        LogEvents.send(this, "Rajya_Duties");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_button(view);
            }
        });
    }

    private void back_button(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }

    public void onclick_source(View view) {
        LogEvents.send(this, "7th_schedule");

        String url = Constants.COI_English_7th_schedule;
        if (mLanguage.equals(MainActivity.sLANGUAGE_HINDI) || mLanguage.equals(MainActivity.sLANGUAGE_MARATHI)){
            url = Constants.COI_Hindi_7th_schedule;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
