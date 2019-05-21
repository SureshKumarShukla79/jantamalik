package in.filternet.jantamalik;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.filternet.jantamalik.Corporation.CorporationFragment;
import in.filternet.jantamalik.Kendra.KendraFragment;
import in.filternet.jantamalik.Rajya.RajyaFragment;

import static in.filternet.jantamalik.MainActivity.TAB_CORPORATION;
import static in.filternet.jantamalik.MainActivity.TAB_ISSUE;
import static in.filternet.jantamalik.MainActivity.TAB_KENDRA;
import static in.filternet.jantamalik.MainActivity.TAB_RAJYA;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private  int numOfTabs;

    public MainViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case TAB_ISSUE:
                IssueFragment issues = new IssueFragment();
                return issues;
            case TAB_KENDRA:
                KendraFragment kendra = new KendraFragment();
                return kendra;
            case TAB_RAJYA:
                RajyaFragment rajya = new RajyaFragment();
                return rajya;
            case TAB_CORPORATION:
                CorporationFragment corporation = new CorporationFragment();
                return corporation;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
