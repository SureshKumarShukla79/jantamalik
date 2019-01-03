package filternetfoundation.com.jantamaalik;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import filternetfoundation.com.jantamaalik.IssuesJava.IssuesFragment;
import filternetfoundation.com.jantamaalik.MoneyJava.MoneyFragment;
import filternetfoundation.com.jantamaalik.VoteJava.VoteFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

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
