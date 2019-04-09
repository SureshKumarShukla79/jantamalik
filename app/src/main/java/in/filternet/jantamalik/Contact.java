package in.filternet.jantamalik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;
import static in.filternet.jantamalik.MainActivity.sUSER_CURRENT_LANGUAGE;
import static in.filternet.jantamalik.MainActivity.setUI_Lang;

public class Contact extends AppCompatActivity {

    private FloatingActionButton ui_email_us;
    private RadioButton ui_issue, ui_update, ui_feedback;

    private String TAG = "Contact";
    private boolean mAddIssue, mUpdateMP, mFeedback;
    private int mTABnumber = 0;
    private String mIssueSubject = null;
    private String subject = "";
    private Toolbar toolbar;

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
            mTABnumber = savedInstanceState.getInt(TAB_NUMBER);

            mIssueSubject = savedInstanceState.getString("subject");
        }

        setContentView(R.layout.contact);

        ui_issue = findViewById(R.id.radio_issue);
        ui_update = findViewById(R.id.radio_update);
        ui_feedback = findViewById(R.id.radio_feedback);
        ui_email_us = findViewById(R.id.email_us);

        if(mAddIssue || mUpdateMP || mFeedback) {

            if(mAddIssue) {
                if(mIssueSubject != null)
                    subject = mIssueSubject;
                else
                    subject = getString(R.string.add_issue);
                ui_issue.setChecked(true);
            }
            else if(mUpdateMP) {
                subject = getString(R.string.update_contact_info);
                ui_update.setChecked(true);
            }
            else if(mFeedback) {
                subject = getString(R.string.feedback);
                ui_feedback.setChecked(true);
            }
        }

        toolbar = findViewById(R.id.toolbar_contact);
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
        intent.putExtra(TAB_NUMBER, mTABnumber);
        startActivity(intent);
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
    }

    public void onclick_email_us(View view) {
        String state = mSharedPref.getString(MainActivity.sSTATE, null);
        String area = mSharedPref.getString(MainActivity.sMP, null);

        String[] TO = {getString(R.string.support_email)};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");

        intent.setPackage("com.google.android.gm");
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, area + ", " + state + "\n" + subject);
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
        Uri uri = Uri.parse("https://www.filternet.in/donate/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onclick_github(View view) {
        Uri uri = Uri.parse("https://github.com/SureshKumarShukla79/jantamalik");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onclick_whatsapp_us(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String whatsapp = "com.whatsapp";

        if(isPackageExist(this, intent, whatsapp)) {
            String state = mSharedPref.getString(MainActivity.sSTATE, null);
            String area = mSharedPref.getString(MainActivity.sMP, null);
            try {
                if (intent != null) {
                    Uri url = Uri.parse("https://wa.me/917570000787?text="+ area
                            + ", "+ state + "\n\n" + subject + "\n");
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(url);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Sending failed", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isPackageExist(Context context, Intent intent, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);

            for(int i=0; i<activities.size(); i++){
                //Log.e(TAG, activities.get(i).toString());
                String current_resolve_info = activities.get(i).toString();
                if(current_resolve_info.contains(packageName)){
                    //Log.e(TAG, "PackageExist " + packageName);
                    return true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
