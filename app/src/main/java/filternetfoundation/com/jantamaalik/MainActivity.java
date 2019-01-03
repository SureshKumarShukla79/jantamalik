package filternetfoundation.com.jantamaalik;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
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


public class MainActivity extends AppCompatActivity {

     private ViewPager viewPager;
     private TabLayout tabLayout;
     private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager_main);
        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("Issues"));
        tabLayout.addTab(tabLayout.newTab().setText("Money"));
        tabLayout.addTab(tabLayout.newTab().setText("Vote"));

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
                Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_SHORT).show();
            case R.id.contact_menuItem:
                Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    public void changeLanguage(View view){
        Toast.makeText(getApplicationContext(),"Under progress.", Toast.LENGTH_SHORT).show();
    }
}
