package nape.biblememory.view_layer.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nape.biblememory.view_layer.fragments.MyVerseDetailsFragment;
import nape.biblememory.view_layer.fragments.MyVersesPracticeFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapterMyVerses extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private String verseLocation;
    private MyVersesPracticeFragment practiceFragment;

    public ViewPagerAdapterMyVerses(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, String verseLocation) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.verseLocation = verseLocation;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Fragment selectedFrag;
        switch(position){
            case 0:
                selectedFrag = new MyVerseDetailsFragment();
                break;
            case 1:
                practiceFragment = new MyVersesPracticeFragment();
                selectedFrag = practiceFragment;
                break;
            default:
                selectedFrag = new MyVerseDetailsFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("verseLocation", verseLocation);
        selectedFrag.setArguments(bundle);
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

    public void setEditTextFocus() {
        if(practiceFragment != null){
            practiceFragment.requestEditTextFocus();
        }
    }
}
