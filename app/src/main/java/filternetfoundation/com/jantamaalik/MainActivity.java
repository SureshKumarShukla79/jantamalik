package filternetfoundation.com.jantamaalik;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Toast;
import java.util.Locale;

import filternetfoundation.com.jantamaalik.DonateActivityJava.donate;


public class MainActivity extends AppCompatActivity {

    public final static String sUSER_CURRENT_LANGUAGE = "User_Current_Language";
    public final static String sLANGUAGE_HINDI = "hi";
    public final static String sLANGUAGE_ENGLISH = "en";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        String current_language = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI);
        if(current_language != null && current_language.equals(sLANGUAGE_HINDI)) {
            setUI_Lang(this, "hi");
        }

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager_main);
        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.Issues).setIcon(R.drawable.question_mark));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Money).setIcon(R.drawable.note_bundle));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Vote).setIcon(R.drawable.vote_icon));

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout ));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.donate_menuItem:
                Intent intent = new Intent(this, donate.class);
                startActivity(intent);
                break;
            case R.id.contact_menuItem:
                Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeLanguage(View view){
        String current_language = mSharedPref.getString(sUSER_CURRENT_LANGUAGE, null);

        if(current_language == null || current_language.equals(sLANGUAGE_ENGLISH)) {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_HINDI).commit();
            setUI_Lang(this, "hi");
            //Toast.makeText(getApplicationContext(),"भाषा को सफलतापूर्वक बदल दिया गया है", Toast.LENGTH_SHORT).show();
        } else {
            mEditor.putString(sUSER_CURRENT_LANGUAGE, sLANGUAGE_ENGLISH).commit();
            setUI_Lang(this, "en");
            //Toast.makeText(getApplicationContext(),"Language has successfully changed", Toast.LENGTH_SHORT).show();
        }

        this.recreate(); // refresh screen
    }

    public static void setUI_Lang(Activity activity, String lang) { // before setContentView
        String languageToLoad = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }
}
