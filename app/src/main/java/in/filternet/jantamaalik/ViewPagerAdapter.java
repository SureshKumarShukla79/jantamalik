package in.filternet.jantamaalik;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.filternet.jantamaalik.IssuesJava.IssuesFragment;
import in.filternet.jantamaalik.MoneyJava.MoneyFragment;
import in.filternet.jantamaalik.VoteJava.VoteFragment;

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
                IssuesFragment issuesFragment = new IssuesFragment();
                return issuesFragment;
            case 1:
                MoneyFragment moneyFragment = new MoneyFragment();
                return moneyFragment;
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
