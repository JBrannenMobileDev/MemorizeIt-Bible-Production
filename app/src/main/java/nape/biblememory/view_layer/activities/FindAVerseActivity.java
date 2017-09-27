package nape.biblememory.view_layer.activities;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.faithcomesbyhearing.dbt.model.Verse;

import java.util.List;

import butterknife.BindView;
import nape.biblememory.R;
import nape.biblememory.data_layer.dbt_api.DBTApi;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.adapters.ViewPagerAdapterVerseSelector;
import nape.biblememory.view_layer.fragments.BooksFragment;
import nape.biblememory.view_layer.fragments.ChapterFragment;
import nape.biblememory.view_layer.fragments.VerseFragment;
import nape.biblememory.view_layer.fragments.dialogs.SelectVersionAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.VerseSelectedDialogFragment;
import nape.biblememory.view_layer.views.SlidingTabLayout;

public class FindAVerseActivity extends AppCompatActivity implements BooksFragment.BooksFragmentListener, ChapterFragment.ChaptersFragmentListener,
        VerseFragment.OnVerseSelected, VerseSelectedDialogFragment.addVerseDialogActions, SelectVersionAlertDialog.VersionSelected{

    @BindView(R.id.pager_verse_selector)ViewPager pagerVerseSelector;
    @BindView(R.id.tabs_verse_selector)SlidingTabLayout tabsVerseSelector;

    private ViewPagerAdapterVerseSelector adapterVerseSelector;
    private CharSequence VerseSelectorTitles[]={"BOOKS","CHAPTER","VERSE"};
    private int Numboftabs = 3;
    private String bookName;
    private String chapterNum;
    private String verseNum;
    private UserPreferences mPrefs;
    private DBTApi bibleApi;
    private BaseCallback<List<Verse>> selectedVerseCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_averse);
        mPrefs = new UserPreferences();
        setTitle("Find a verse");
        setSlidingTabViewVerseSelector();
    }

    private void setSlidingTabViewVerseSelector() {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterVerseSelector =  new ViewPagerAdapterVerseSelector(getSupportFragmentManager(),VerseSelectorTitles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pagerVerseSelector = (ViewPager) findViewById(R.id.pager_verse_selector);
        pagerVerseSelector.setAdapter(adapterVerseSelector);
        pagerVerseSelector.setVisibility(View.VISIBLE);

        // Assiging the Sliding Tab Layout View
        tabsVerseSelector = (SlidingTabLayout) findViewById(R.id.tabs_verse_selector);
        tabsVerseSelector.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabsVerseSelector.setVisibility(View.VISIBLE);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabsVerseSelector.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabsVerseSelector.setViewPager(pagerVerseSelector);
    }

    @Override
    public void onBookSelected(String bookName) {
        this.bookName = bookName;
        if(mPrefs.getNumberOfChapters(getApplicationContext()) == 0) {
            mPrefs.setNumberOfChapters(getNumOfChapters(bookName), getApplicationContext());
        }
        pagerVerseSelector.setCurrentItem(1,true);
    }

    private int getNumOfChapters(String bookName) {
        Resources res = getResources();
        String[] books = res.getStringArray(R.array.books_of_the_bible);
        String[] chapters = res.getStringArray(R.array.number_of_chapters_list);
        int size = books.length;
        int result = 0;
        for(int i = 0 ; i < size ; i++){
            if(books[i].equalsIgnoreCase(bookName)){
                result = Integer.valueOf(chapters[i]);
                break;
            }
        }
        return result;
    }

    @Override
    public void onChapterSelected(String chapterNum) {
        this.chapterNum = chapterNum;
        pagerVerseSelector.setCurrentItem(2,true);
    }

    @Override
    public void onVerseSelected(final String verseNumber) {
        bibleApi = new DBTApi(getApplicationContext());
        selectedVerseCallback = new BaseCallback<List<Verse>>() {
            @Override
            public void onResponse(List<Verse> response) {
                adapterVerseSelector.setVerseGridViewVisible();
                FragmentManager fm = getSupportFragmentManager();
                VerseSelectedDialogFragment verseSelectedDialog = new VerseSelectedDialogFragment();

                if(response.size() > 0) {
                    Bundle args = new Bundle();
                    args.putString("num", verseNumber);
                    args.putString("verseText", response.get(0).getVerseText());
                    args.putString("verseLocation", mPrefs.getSelectedBook(getApplicationContext()) + " " + mPrefs.getSelectedChapter(getApplicationContext()) + ":" + verseNumber);
                    args.putLong("numOfVersesInChapter", mPrefs.getNumberOfVerses(getApplicationContext()));
                    verseSelectedDialog.setArguments(args);

                    verseSelectedDialog.show(fm, "verseSelectedFragment");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Exception t = e;
            }
        };

        String damId;
        if(mPrefs.isBookLocationOT(getApplicationContext())){
            switch(mPrefs.getSelectedVersion(getApplicationContext())){
                case "ESV":
                    damId = getString(R.string.ESVVersionEnglishOldTestament);
                    break;
                case "WEB":
                    damId = getString(R.string.WEBVersionEnglishOldTestament);
                    break;
                case "CEB":
                    damId = getString(R.string.CEBVersionEnglishOldTestament);
                    break;
                case "KJV":
                    damId = getString(R.string.KJVVersionEnglishOldTestament);
                    break;
                default:
                    damId = getString(R.string.ESVVersionEnglishOldTestament);
            }
        }else {
            switch (mPrefs.getSelectedVersion(getApplicationContext())) {
                case "ESV":
                    damId = getString(R.string.ESVVersionEnglishNewTestament);
                    break;
                case "WEB":
                    damId = getString(R.string.WEBVersionEnglishNewTestament);
                    break;
                case "CEB":
                    damId = getString(R.string.CEBVersionEnglishNewTestament);
                    break;
                case "KJV":
                    damId = getString(R.string.KJVVersionEnglishNewTestament);
                    break;
                default:
                    damId = getString(R.string.ESVVersionEnglishNewTestament);
            }
        }
        bibleApi.getVerse(selectedVerseCallback, damId, mPrefs.getSelectedBookId(getApplicationContext()), verseNumber, mPrefs.getSelectedChapter(getApplicationContext()));
    }

    @Override
    public void onVerseAdded(boolean comingFromNewVerses) {
        finish();
    }

    @Override
    public void includeNextVerseSelected(String verseLocation, final BaseCallback<Verse> callback, String selectedVerseNum) {
        DBTApi api = new DBTApi(getApplicationContext());
        BaseCallback<List<Verse>> nextVerseCallback = new BaseCallback<List<Verse>>() {
            @Override
            public void onResponse(List<Verse> response) {
                callback.onResponse(response.get(0));
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        };
        String damId;
        if(mPrefs.isBookLocationOT(getApplicationContext())){
            switch(mPrefs.getSelectedVersion(getApplicationContext())){
                case "ESV":
                    damId = getString(R.string.ESVVersionEnglishOldTestament);
                    break;
                case "WEB":
                    damId = getString(R.string.WEBVersionEnglishOldTestament);
                    break;
                case "CEB":
                    damId = getString(R.string.CEBVersionEnglishOldTestament);
                    break;
                case "KJV":
                    damId = getString(R.string.KJVVersionEnglishOldTestament);
                    break;
                default:
                    damId = getString(R.string.ESVVersionEnglishOldTestament);
            }
        }else {
            switch (mPrefs.getSelectedVersion(getApplicationContext())) {
                case "ESV":
                    damId = getString(R.string.ESVVersionEnglishNewTestament);
                    break;
                case "WEB":
                    damId = getString(R.string.WEBVersionEnglishNewTestament);
                    break;
                case "CEB":
                    damId = getString(R.string.CEBVersionEnglishNewTestament);
                    break;
                case "KJV":
                    damId = getString(R.string.KJVVersionEnglishNewTestament);
                    break;
                default:
                    damId = getString(R.string.ESVVersionEnglishNewTestament);
            }
        }
        String nextVerseNum = String.valueOf(Integer.valueOf(selectedVerseNum) + 1);
        api.getVerse(nextVerseCallback, damId, mPrefs.getSelectedBookId(getApplicationContext()), nextVerseNum, mPrefs.getSelectedChapter(getApplicationContext()));
    }

    @Override
    public void onVersionSelectedFromDialog(String verseNum) {
        onVerseSelected(verseNum);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
