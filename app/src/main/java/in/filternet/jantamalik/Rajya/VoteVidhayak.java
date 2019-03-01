package in.filternet.jantamalik.Rajya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.Kendra.DataFilter;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;

import static in.filternet.jantamalik.MainActivity.TAB_NUMBER;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;
import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;
import static in.filternet.jantamalik.VoteJava.VoteFragment.DEFAULT_MP;
import static in.filternet.jantamalik.VoteJava.VoteFragment.hiDEFAULT_MP;
import static in.filternet.jantamalik.VoteJava.VoteFragment.sMP;

public class VoteVidhayak extends AppCompatActivity {
    String TAG = "VoteVidhayak";
    private Toolbar toolbar;
    private TextView name, phone, email, address;
    private de.hdodenhof.circleimageview.CircleImageView profile_pic;
    DataFilter.MP_info mp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vote_mp_layout);

        toolbar = findViewById(R.id.toolbar_MP_layout);
        name = findViewById(R.id.MP_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        profile_pic = findViewById(R.id.profile_image);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_button(view);
            }
        });

        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String MPArea = mSharedPref.getString(sMP, DEFAULT_MP);
        String current_language = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);

        DataFilter dataFilter = new DataFilter();
        //mp = dataFilter.new MP_info();
        mp = dataFilter.getMPInfo(current_language, MPArea);

        //  Log.e(TAG, MPArea + " " + mp.name + " " + mp.phone + " " + mp.email + " " + mp.address);
        name.setText(mp.name);
        phone.setText(mp.phone);
        email.setText(mp.email);
        address.setText(mp.address);

        // Only Varanasi MP pic in app
        if (MPArea.equals(DEFAULT_MP) || MPArea.equals(hiDEFAULT_MP))
            profile_pic.setImageResource(R.drawable.narendra_modi_pic);
        else
            profile_pic.setImageResource(R.drawable.politician_illustration);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_button(toolbar.getRootView()); // TODO animation is too strong
    }

    private void back_button(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }

    public void onclick_call_mp(View view) {
        if (mp.phone.equals(""))
            return;

        Uri number = Uri.parse("tel:" + mp.phone);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        try { // Calling not available on Tablet devices
            startActivity(callIntent);
        } catch (Exception exception) {
            Toast.makeText(this, "Unable to CALL", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }

    public void onclick_email_mp(View view) {
        if (mp.email.equals(""))
            return;

        String[] TO = {mp.email};
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

    public void onclick_update_Vidhayak(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mp", true);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }

    public void onclick_Vidhayak(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mp", true);
        intent.putExtra(TAB_NUMBER, TAB_RAJYA);
        startActivity(intent);
    }
}
