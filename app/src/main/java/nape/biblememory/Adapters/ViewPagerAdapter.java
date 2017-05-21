package nape.biblememory.Adapters;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nape.biblememory.Fragments.MyVersesFragment;
import nape.biblememory.Fragments.LearningSetFragment;
import nape.biblememory.Fragments.MemorizedSetFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    private MyVersesFragment myVersesFragment;
    private LearningSetFragment learningSetFragment;
    private MemorizedSetFragment memorizedSetFragment;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Fragment selectedFrag;
        switch(position){
            case 0:
                selectedFrag = new MyVersesFragment();
                myVersesFragment = (MyVersesFragment) selectedFrag;
                break;
            case 1:
                selectedFrag = new LearningSetFragment();
                learningSetFragment = (LearningSetFragment) selectedFrag;
                break;
            case 2:
                selectedFrag = new MemorizedSetFragment();
                memorizedSetFragment = (MemorizedSetFragment) selectedFrag;
                break;
            default:
                selectedFrag = new MyVersesFragment();
                myVersesFragment = (MyVersesFragment) selectedFrag;
        }
        return selectedFrag;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public void refreshrecyclerViews(){
        if(myVersesFragment != null && myVersesFragment.isVisible()){
            myVersesFragment.refreshRecyclerView();
        }
        if(learningSetFragment != null && learningSetFragment.isVisible()){
            learningSetFragment.RefreshRecyclerView();
        }
        if(memorizedSetFragment != null && memorizedSetFragment.isVisible()){
            memorizedSetFragment.RefreshRecyclerView();
        }
    }
}