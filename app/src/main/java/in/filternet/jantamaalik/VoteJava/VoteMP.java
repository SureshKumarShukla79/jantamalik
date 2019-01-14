package in.filternet.jantamaalik.VoteJava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import in.filternet.jantamaalik.R;

public class VoteMP extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vote_mp_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onclick_call_mp(View view) {
        Uri number = Uri.parse("tel:" + getString(R.string.contact_no_modi));
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        try { // Calling not available on Tablet devices
            startActivity(callIntent);
        } catch (Exception exception){
            Toast.makeText(this, "Unable to CALL", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }

    public void onclick_email_mp(View view) {
        String[] TO = {getString(R.string.email_contact_modi)};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");

        intent.setPackage("com.google.android.gm");
        intent.putExtra(Intent.EXTRA_EMAIL, TO);

        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                //Log.v(TAG, "1st option");
                startActivity(intent);
            } else {
                //Log.v(TAG, "2nd option");
                startActivity(Intent.createChooser(intent, "Sending mail..."));
                finish();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Gmail app didn't respond.", Toast.LENGTH_LONG).show();
        }
    }
}
