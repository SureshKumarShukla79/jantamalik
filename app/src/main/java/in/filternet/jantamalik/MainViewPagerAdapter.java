package in.filternet.jantamalik;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.filternet.jantamalik.IssuesJava.IssuesFragment;
import in.filternet.jantamalik.Kendra.KendraFragment;
import in.filternet.jantamalik.Rajya.RajyaFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private  int numOfTabs;

    public MainViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                KendraFragment kendra = new KendraFragment();
                return kendra;
            case 1:
                RajyaFragment rajya = new RajyaFragment();
                return rajya;
            case 2:
                IssuesFragment issuesFragment = new IssuesFragment();
                return issuesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
