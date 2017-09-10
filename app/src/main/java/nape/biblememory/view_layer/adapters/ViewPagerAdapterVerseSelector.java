package nape.biblememory.view_layer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nape.biblememory.view_layer.fragments.BooksFragment;
import nape.biblememory.view_layer.fragments.ChapterFragment;
import nape.biblememory.view_layer.fragments.VerseFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapterVerseSelector extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private VerseFragment verseFragment;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterVerseSelector(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
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
                selectedFrag = new BooksFragment();
                break;
            case 1:
                selectedFrag = new ChapterFragment();
                break;
            case 2:
                selectedFrag = new VerseFragment();
                verseFragment = (VerseFragment)selectedFrag;
                break;
            default:
                selectedFrag = new BooksFragment();
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

    public void setVerseGridViewVisible() {
        if(verseFragment != null){
            verseFragment.setGridViewVisible();
        }
    }
}
