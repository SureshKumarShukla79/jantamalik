package in.filternet.jantamalik.IssuesJava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;
import android.widget.TextView;

import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class IssuesItem extends AppCompatActivity {
   private TextView janta, loksabha, government;
   private String current_language;
    private SharedPreferences mSharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Fade());
        }
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(IssuesItem.this);

        current_language = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);

        setContentView(R.layout.issues_fragment_item_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        janta = findViewById(R.id.janta_issue);
        loksabha = findViewById(R.id.loksabha_issue);
        government = findViewById(R.id.govt_issue);

        Intent intent = getIntent();
        int index = intent.getIntExtra(IssuesFragment.issueID, 0);
        //setting the title

        if (current_language.equals(sLANGUAGE_HINDI)) {
            getSupportActionBar().setTitle(IssuesData.issues[index][4]);
            janta.setText(IssuesData.issues[index][5]);
            loksabha.setText(IssuesData.issues[index][6]);
            government.setText(IssuesData.issues[index][7]);
        }
        else {
            getSupportActionBar().setTitle(IssuesData.issues[index][0]);
            janta.setText(IssuesData.issues[index][1]);
            loksabha.setText(IssuesData.issues[index][2]);
            government.setText(IssuesData.issues[index][3]);
        }
    }
}