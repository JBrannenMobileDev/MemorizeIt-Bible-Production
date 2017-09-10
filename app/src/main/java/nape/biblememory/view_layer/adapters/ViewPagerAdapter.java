package nape.biblememory.view_layer.adapters;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nape.biblememory.view_layer.fragments.MemorizedSetFragment;
import nape.biblememory.view_layer.fragments.MyVersesFragment;
import nape.biblememory.models.ScriptureData;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private static final int NumbOfTabs = 2; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    private MyVersesFragment versesFragment;
    private MemorizedSetFragment memorizedSetFragment;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[]) {
        super(fm);
        this.Titles = mTitles;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Fragment selectedFrag;
        switch(position){
            case 0:
                selectedFrag = new MyVersesFragment();
                versesFragment = (MyVersesFragment) selectedFrag;
                break;
            case 1:
                selectedFrag = new MemorizedSetFragment();
                memorizedSetFragment = (MemorizedSetFragment) selectedFrag;
                break;
            default:
                selectedFrag = new MyVersesFragment();
                versesFragment = (MyVersesFragment) selectedFrag;
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
        if(memorizedSetFragment != null && memorizedSetFragment.isVisible()){
            memorizedSetFragment.RefreshRecyclerView();
        }
    }
}