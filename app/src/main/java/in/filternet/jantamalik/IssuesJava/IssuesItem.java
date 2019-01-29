package in.filternet.jantamalik.IssuesJava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;

public class IssuesItem extends AppCompatActivity {
   private TextView janta, saansad, vidhayak, parshad, parliament, centerGovt, stateGovt, municipality;
   private String current_language;
    private SharedPreferences mSharedPref;
    private CardView one, two, three, four, five, six, seven, eight;

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

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);

        janta = findViewById(R.id.janta_issue);
        saansad = findViewById(R.id.saansad_text);
        vidhayak = findViewById(R.id.vidhayak_text);
        parshad = findViewById(R.id.parshad_text);
        parliament = findViewById(R.id.parliament);
        centerGovt = findViewById(R.id.centerGovt_issue);

        stateGovt = findViewById(R.id.stateGovt_issue);
        municipality = findViewById(R.id.municipality_issue);

        Intent intent = getIntent();
        int index = intent.getIntExtra(IssuesFragment.issueID, 0);

        if (current_language.equals(sLANGUAGE_HINDI)) {
            getSupportActionBar().setTitle(IssuesData.issues[index][9]);
            janta.setText(IssuesData.issues[index][10]);
            saansad.setText(IssuesData.issues[index][11]);
            vidhayak.setText(IssuesData.issues[index][12]);
            parshad.setText(IssuesData.issues[index][13]);
            parliament.setText(IssuesData.issues[index][14]);
            centerGovt.setText(IssuesData.issues[index][15]);
            stateGovt.setText(IssuesData.issues[index][16]);
            municipality.setText(IssuesData.issues[index][17]);
        }
        else {
            getSupportActionBar().setTitle(IssuesData.issues[index][0]);
            janta.setText(IssuesData.issues[index][1]);
            saansad.setText(IssuesData.issues[index][2]);
            vidhayak.setText(IssuesData.issues[index][3]);
            parshad.setText(IssuesData.issues[index][4]);
            parliament.setText(IssuesData.issues[index][5]);
            centerGovt.setText(IssuesData.issues[index][6]);
            stateGovt.setText(IssuesData.issues[index][7]);
            municipality.setText(IssuesData.issues[index][8]);
        }

        //setting visibility of views
        if (janta.getText().equals(""))one.setVisibility(View.GONE);
        if (saansad.getText().equals(""))two.setVisibility(View.GONE);
        if (vidhayak.getText().equals(""))three.setVisibility(View.GONE);
        if (parshad.getText().equals(""))four.setVisibility(View.GONE);
        if (parliament.getText().equals(""))five.setVisibility(View.GONE);
        if (centerGovt.getText().equals(""))six.setVisibility(View.GONE);
        if (stateGovt.getText().equals(""))seven.setVisibility(View.GONE);
        if (municipality.getText().equals(""))eight.setVisibility(View.GONE);
    }
}