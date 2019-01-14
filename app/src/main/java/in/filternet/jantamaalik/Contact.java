package in.filternet.jantamaalik;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Contact extends AppCompatActivity {

    private String TAG = "Contact";
    String subject = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
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
        Log.e(TAG, "Subject: " + subject);
    }

    public void onclick_email_us(View view) {
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


}
