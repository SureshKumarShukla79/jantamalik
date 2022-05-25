package in.filternet.jantamalik.Rajya;

import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import in.filternet.jantamalik.LogEvents;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

public class RajyaInfographics extends AppCompatActivity {

    private final static String TAG ="RajyaInfographics";

    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.infographics_rajya);

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
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }
}
