package in.filternet.jantamalik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;
import static in.filternet.jantamalik.MainActivity.sUSER_CURRENT_LANGUAGE;
import static in.filternet.jantamalik.MainActivity.setUI_Lang;

public class Contact extends AppCompatActivity {

    private Button ui_email_us;
    private RadioButton ui_issue, ui_update, ui_feedback;

    private String TAG = "Contact";
    private boolean mAddIssue, mUpdateMP, mFeedback;
    private String subject = "";

    private SharedPreferences mSharedPref;
    String mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mLanguage = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI); // first launch
        if(mLanguage.equals(sLANGUAGE_HINDI)) {
            setUI_Lang(this, "hi");
        }

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null){
            mAddIssue = savedInstanceState.getBoolean("add_issue");
            mUpdateMP = savedInstanceState.getBoolean("update_mp");
            mFeedback = savedInstanceState.getBoolean("feedback");
        }

        setContentView(R.layout.contact);
        setTitle(R.string.contact);

        ui_issue = findViewById(R.id.radio_issue);
        ui_update = findViewById(R.id.radio_update);
        ui_feedback = findViewById(R.id.radio_feedback);
        ui_email_us = findViewById(R.id.email_us);

        if(mAddIssue || mUpdateMP || mFeedback) {
            ui_email_us.setVisibility(View.VISIBLE);

            if(mAddIssue) {
                subject = "Add Issue";
                ui_issue.setChecked(true);
            }
            else if(mUpdateMP) {
                subject = "Update Information";
                ui_update.setChecked(true);
            }
            else if(mFeedback) {
                subject = "Feedback";
                ui_feedback.setChecked(true);
            }
        }
    }

    public void onclick_add_subject(View view) {
        boolean checked =  ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_issue:
                if(checked)
                    subject = getString(R.string.add_issue);
                break;
            case R.id.radio_update:
                if(checked)
                    subject = getString(R.string.update_contact_info);
                break;
            case R.id.radio_feedback:
                if(checked)
                    subject = getString(R.string.feedback);
                break;
        }
        ui_email_us.setVisibility(View.VISIBLE);
    }

    public void onclick_email_us(View view) {
        Log.e(TAG, "Subject: " + subject);
        String[] TO = {getString(R.string.support_email)};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");

        intent.setPackage("com.google.android.gm");
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                //Log.e(TAG, "1st option");
                startActivity(intent);
            } else {
                //Log.e(TAG, "2nd option");
                startActivity(Intent.createChooser(intent, "Sending mail..."));
                finish();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Gmail app didn't respond.", Toast.LENGTH_LONG).show();
        }
    }


    public void onclick_open_donate(View view) {
        Uri uri = Uri.parse("https://imjo.in/7mJBDn");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onclick_github(View view) {
        Uri uri = Uri.parse("https://github.com/SureshKumarShukla79/jantamalik");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
