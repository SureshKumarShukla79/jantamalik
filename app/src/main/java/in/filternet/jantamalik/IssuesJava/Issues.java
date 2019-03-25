package in.filternet.jantamalik.IssuesJava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

        if(layoutResID == R.layout.issue_media_or_afeem) {
            make_clickable_links();
        }

    }

    private void make_clickable_links() {
        TextView l1 = findViewById(R.id.l1);
        l1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l2 = findViewById(R.id.l2);
        l2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l3 = findViewById(R.id.l3);
        l3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l4 = findViewById(R.id.l4);
        l4.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l5 = findViewById(R.id.l5);
        l5.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l6 = findViewById(R.id.l6);
        l6.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l7 = findViewById(R.id.l7);
        l7.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l8 = findViewById(R.id.l8);
        l8.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l9 = findViewById(R.id.l9);
        l9.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l10 = findViewById(R.id.l10);
        l10.setMovementMethod(LinkMovementMethod.getInstance());

        TextView l11 = findViewById(R.id.l11);
        l11.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l12 = findViewById(R.id.l12);
        l12.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l13 = findViewById(R.id.l13);
        l13.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l14 = findViewById(R.id.l14);
        l14.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l15 = findViewById(R.id.l15);
        l15.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l16 = findViewById(R.id.l16);
        l16.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l17 = findViewById(R.id.l17);
        l17.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l18 = findViewById(R.id.l18);
        l18.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l19 = findViewById(R.id.l19);
        l19.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l20 = findViewById(R.id.l20);
        l20.setMovementMethod(LinkMovementMethod.getInstance());

        TextView l21 = findViewById(R.id.l21);
        l21.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l22 = findViewById(R.id.l22);
        l22.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l23 = findViewById(R.id.l23);
        l23.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l24 = findViewById(R.id.l24);
        l24.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l25 = findViewById(R.id.l25);
        l25.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l26 = findViewById(R.id.l26);
        l26.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l27 = findViewById(R.id.l27);
        l27.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l28 = findViewById(R.id.l28);
        l28.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l29 = findViewById(R.id.l29);
        l29.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l30 = findViewById(R.id.l30);
        l30.setMovementMethod(LinkMovementMethod.getInstance());

        TextView l31 = findViewById(R.id.l31);
        l31.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l32 = findViewById(R.id.l32);
        l32.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l33 = findViewById(R.id.l33);
        l33.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l34 = findViewById(R.id.l34);
        l34.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l35 = findViewById(R.id.l35);
        l35.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l36 = findViewById(R.id.l36);
        l36.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l37 = findViewById(R.id.l37);
        l37.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l38 = findViewById(R.id.l38);
        l38.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l39 = findViewById(R.id.l39);
        l39.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l40 = findViewById(R.id.l40);
        l40.setMovementMethod(LinkMovementMethod.getInstance());

        TextView l41 = findViewById(R.id.l41);
        l41.setMovementMethod(LinkMovementMethod.getInstance());
        TextView l42 = findViewById(R.id.l42);
        l42.setMovementMethod(LinkMovementMethod.getInstance());
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
