package in.jantamalik;

import static in.jantamalik.MainActivity.TAB_243;
import static in.jantamalik.MainActivity.TAB_KENDRA;
import static in.jantamalik.MainActivity.TAB_RAJYA;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.jantamalik.Kendra.KendraFragment;
import in.jantamalik.Rajya.RajyaFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private final int numOfTabs;

    MainViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case TAB_243:
                IssueFragment issues = new IssueFragment();
                return issues;
            case TAB_KENDRA:
                KendraFragment kendra = new KendraFragment();
                return kendra;
            case TAB_RAJYA:
                RajyaFragment rajya = new RajyaFragment();
                return rajya;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
