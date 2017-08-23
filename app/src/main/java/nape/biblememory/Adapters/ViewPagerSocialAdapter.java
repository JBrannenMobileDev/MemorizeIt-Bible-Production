package nape.biblememory.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nape.biblememory.Fragments.FriendsFragment;
import nape.biblememory.Fragments.GroupsFragment;
import nape.biblememory.Fragments.MemorizedSetFragment;
import nape.biblememory.Fragments.MyVersesFragment;
import nape.biblememory.Fragments.RequestsFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerSocialAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    private FriendsFragment friendsFragment;
    private GroupsFragment groupsFragment;
    private RequestsFragment requestsFragment;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerSocialAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
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
                selectedFrag = new FriendsFragment();
                friendsFragment = (FriendsFragment) selectedFrag;
                break;
            case 1:
                selectedFrag = new GroupsFragment();
                groupsFragment = (GroupsFragment) selectedFrag;
                break;
            case 2:
                selectedFrag = new RequestsFragment();
                requestsFragment = (RequestsFragment) selectedFrag;
                break;
            default:
                selectedFrag = new FriendsFragment();
                friendsFragment = (FriendsFragment) selectedFrag;
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

    public void refreshRecyclerViews(){
        if(friendsFragment != null && friendsFragment.isVisible()){
//            friendsFragment.refreshRecyclerView();
        }
        if(groupsFragment != null && groupsFragment.isVisible()){
//            groupsFragment.RefreshRecyclerView();
        }
        if(requestsFragment != null && requestsFragment.isVisible()){
//            requestsFragment.RefreshRecyclerView();
        }
    }
}
