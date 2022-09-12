package in.jantamalik.Kendra;


import static in.jantamalik.MainActivity.TAB_KENDRA;
import static in.jantamalik.MainActivity.TAB_NUMBER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import in.jantamalik.Constants;
import in.jantamalik.LogEvents;
import in.jantamalik.MainActivity;
import in.jantamalik.R;

public class DutiesKendra extends AppCompatActivity {

    private final static String TAG ="DutiesKendra";

    private SharedPreferences mSharedPref;
    private Toolbar toolbar;
    private String mLanguage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.duties_kendra);
        LogEvents.send(this, TAG);

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
        intent.putExtra(TAB_NUMBER, TAB_KENDRA);
        startActivity(intent);
    }

    public void onclick_source(View view) {
        LogEvents.send(this, "7th_schedule");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.COI_Hindi));
        startActivity(intent);
    }
}
