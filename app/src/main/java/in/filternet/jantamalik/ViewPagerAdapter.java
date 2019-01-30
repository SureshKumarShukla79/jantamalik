package in.filternet.jantamalik;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.filternet.jantamalik.IssuesJava.IssuesFragment;
import in.filternet.jantamalik.Kendra.KendraFragment;
import in.filternet.jantamalik.VoteJava.VoteFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private  int numOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int numOfTabs) {
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
                IssuesFragment issuesFragment = new IssuesFragment();
                return issuesFragment;
            case 2:
                VoteFragment voteFragment = new VoteFragment();
                return voteFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
