package nape.biblememory.view_layer.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.view_layer.fragments.BooksFragment;
import nape.biblememory.view_layer.fragments.ChapterFragment;
import nape.biblememory.view_layer.fragments.MemorizedReviewFragment;
import nape.biblememory.view_layer.fragments.MemorizedVerseDetailsFragment;
import nape.biblememory.view_layer.fragments.VerseFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapterMemorized extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private String verseLocation;
    private MemorizedReviewFragment reviewFragment;
    private boolean comingFromReviewNow;

    public ViewPagerAdapterMemorized(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, String verseLocation, boolean comingFromReviewNow) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.verseLocation = verseLocation;
        this.comingFromReviewNow = comingFromReviewNow;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Fragment selectedFrag;
        switch(position){
            case 0:
                selectedFrag = new MemorizedVerseDetailsFragment();
                break;
            case 1:
                reviewFragment = new MemorizedReviewFragment();
                selectedFrag = reviewFragment;
                break;
            default:
                selectedFrag = new MemorizedVerseDetailsFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("verseLocation", verseLocation);
        bundle.putBoolean("reviewNow", comingFromReviewNow);
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
        if(reviewFragment != null){
            reviewFragment.requestEditTextFocus();
        }
    }
}
