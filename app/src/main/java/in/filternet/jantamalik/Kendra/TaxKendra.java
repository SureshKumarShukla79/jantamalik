package in.filternet.jantamalik.Kendra;


import static in.filternet.jantamalik.MainActivity.TAB_KENDRA;
import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import in.filternet.jantamalik.LogEvents;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

public class TaxKendra extends AppCompatActivity {

    private final static String TAG ="TaxKendra";

    private SharedPreferences mSharedPref;
    private Toolbar toolbar;
    private TextView interest, education, health;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.tax_kendra);
        LogEvents.send(this, TAG);

        toolbar = findViewById(R.id.toolbar_Tax_Kendra);
        interest = findViewById(R.id.interest);
        education = findViewById(R.id.education);
        health = findViewById(R.id.health);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            interest.setText(getString(R.string.interest) + " \uD83D\uDD25\uD83D\uDD25");
            education.setText(getString(R.string.education) + " \uD83D\uDD25");
            health.setText(getString(R.string.health) + " \uD83D\uDD25");
        }

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
}
