package in.jantamalik.Rajya;


import static in.jantamalik.MainActivity.TAB_NUMBER;
import static in.jantamalik.MainActivity.TAB_RAJYA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import in.jantamalik.LogEvents;
import in.jantamalik.MainActivity;
import in.jantamalik.R;

public class TaxRajya extends AppCompatActivity {

    private SharedPreferences mSharedPref;
    private Toolbar toolbar;
    private final static String TAG ="TaxRajya";

    private int layoutResID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null) {
            layoutResID = savedInstanceState.getInt("layout_id");
            //Log.e(TAG, "layout_id: " + layoutResID);
        }

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(layoutResID);

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
