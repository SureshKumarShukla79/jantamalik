package in.filternet.jantamalik.VoteJava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import in.filternet.jantamalik.DataFilter;
import in.filternet.jantamalik.MainActivity;
import in.filternet.jantamalik.R;
import in.filternet.jantamalik.Contact;
import in.filternet.jantamalik.infographics;

import static in.filternet.jantamalik.MainActivity.sLANGUAGE_HINDI;
import static in.filternet.jantamalik.VoteJava.VoteFragment.DEFAULT_MP;
import static in.filternet.jantamalik.VoteJava.VoteFragment.hiDEFAULT_MP;
import static in.filternet.jantamalik.VoteJava.VoteFragment.sMP;

public class VoteMP extends AppCompatActivity {
    String TAG = "VoteMP";
    private Toolbar toolbar;
    private TextView name,phone,email,area, address;
    private de.hdodenhof.circleimageview.CircleImageView profile_pic;
    DataFilter.MP_info mp;
    public static final String TAB_NUMBER = "tab_number";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vote_mp_layout);

        toolbar = findViewById(R.id.toolbar_MP_layout);
        name = findViewById(R.id.MP_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        area = findViewById(R.id.Area);
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
        String MPArea = mSharedPref.getString(sMP,DEFAULT_MP);
        String current_language = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);

        DataFilter dataFilter = new DataFilter();
        //mp = dataFilter.new MP_info();
        mp = dataFilter.getMPInfo(current_language, MPArea);

      //  Log.e(TAG, MPArea + " " + mp.name + " " + mp.phone + " " + mp.email + " " + mp.address);
        name.setText(mp.name);
        phone.setText(mp.phone);
        email.setText(mp.email);
        area.setText(getString(R.string.mp_area) + MPArea);
        address.setText(mp.address);

        // Only Varanasi MP pic in app
        if(MPArea.equals(DEFAULT_MP) || MPArea.equals(hiDEFAULT_MP))
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
        intent.putExtra(TAB_NUMBER, 2);
        startActivity(intent);
    }

    public void onclick_call_mp(View view) {
        if(mp.phone.equals(""))
            return;

        Uri number = Uri.parse("tel:" + mp.phone);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        try { // Calling not available on Tablet devices
            startActivity(callIntent);
        } catch (Exception exception){
            Toast.makeText(this, "Unable to CALL", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }

    public void onclick_email_mp(View view) {
        if(mp.email.equals(""))
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

    public void onclick_update_mp(View view) {
        Intent intent = new Intent(this, Contact.class);
        intent.putExtra("update_mp", true);
        startActivity(intent);
    }

    public void onclick_MP(View view){
        Intent intent = new Intent(view.getContext(), VoteMP.class);
        startActivity(intent);
    }

    public void onclick_MLA(View view){
        Toast.makeText(view.getContext(),R.string.next_version, Toast.LENGTH_LONG).show();
    }

    public void onclick_Parshad(View view){
        Toast.makeText(view.getContext(),R.string.next_version, Toast.LENGTH_LONG).show();
    }

    public void openInfographics(View view) {
        Intent intent = new Intent(VoteMP.this, infographics.class);
        startActivity(intent);
    }
}
