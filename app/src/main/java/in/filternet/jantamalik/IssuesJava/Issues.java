package in.filternet.jantamalik.IssuesJava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import in.filternet.jantamalik.R;

public class Issues extends AppCompatActivity {
    private final static String TAG ="Issues";

    private int layoutResID, titleID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null) {
            layoutResID = savedInstanceState.getInt("layout_id");
            //Log.e(TAG, "layout_id: " + layoutResID);
            titleID = savedInstanceState.getInt("title_id");
            //Log.e(TAG, "title_id: " + titleID);
        }

        setContentView(layoutResID);
        this.setTitle(titleID);
    }

    public void onclick_update_issue(View view) {
        String[] TO = {getString(R.string.support_email)};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");

        intent.setPackage("com.google.android.gm");
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(titleID));
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
