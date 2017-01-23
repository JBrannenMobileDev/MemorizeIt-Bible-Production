package nape.biblememory.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.faithcomesbyhearing.dbt.model.Verse;

import java.util.List;

import nape.biblememory.Activities.Adapters.ViewPagerAdapter;
import nape.biblememory.Activities.Adapters.ViewPagerAdapterVerseSelector;
import nape.biblememory.Activities.DBTApi.DBTApi;
import nape.biblememory.Activities.Fragments.Dialogs.BooksFragment;
import nape.biblememory.Activities.Fragments.Dialogs.ChapterFragment;
import nape.biblememory.Activities.Fragments.Dialogs.MyVersesFragment;
import nape.biblememory.Activities.Fragments.Dialogs.VerseFragment;
import nape.biblememory.Activities.Fragments.Dialogs.VerseSelection;
import nape.biblememory.Activities.Views.SlidingTabLayout;
import nape.biblememory.Activities.Views.VerseSelectedDialogFragment;
import nape.biblememory.R;

public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener, MyVersesFragment.OnAddVerseSelectedListener, VerseSelection.FragmentToActivity, BooksFragment.BooksFragmentListener, ChapterFragment.ChaptersFragmentListener, VerseFragment.OnVerseSelected{

    private ViewPager pagerMain;
    private ViewPagerAdapter adapterMain;
    private SlidingTabLayout tabsMain;
    private FrameLayout fragmentContainer;
    private CharSequence mainTitles[]={"My Verses","In Progress","Memorized"};
    private ViewPager pagerVerseSelector;
    private ViewPagerAdapterVerseSelector adapterVerseSelector;
    private SlidingTabLayout tabsVerseSelector;
    private CharSequence VerseSelectorTitles[]={"BOOKS","CHAPTER","VERSE"};
    private int Numboftabs =3;
    private TextView startQuiz;
    private UserPreferences mPrefs;
    private String bookName;
    private String chapterNum;
    private String verseNum;

    private DBTApi REST;
    private BaseCallback<List<Verse>> selectedVerseCallback;
    private Context context;

    private static final String BACK = "BACK";
    private static final String START_QUIZ = "START QUIZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = new UserPreferences();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setSlidingTabViewMain();

        startQuiz = (TextView) findViewById(R.id.toolbar_start_quiz);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagerMain.getVisibility() == View.GONE){
                    onBackPressed();
                    return;
                }
                if(pagerVerseSelector != null && pagerVerseSelector.getVisibility() == View.VISIBLE){
                    onBackPressedFromNewVerseSelector();
                }
                Intent myIntent = new Intent(getApplicationContext(), PhoneUnlockActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void setSlidingTabViewMain() {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterMain =  new ViewPagerAdapter(getSupportFragmentManager(),mainTitles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pagerMain = (ViewPager) findViewById(R.id.pager);
        pagerMain.setAdapter(adapterMain);

        // Assiging the Sliding Tab Layout View
        tabsMain = (SlidingTabLayout) findViewById(R.id.tabs);
        tabsMain.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabsMain.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        //added for test commit
        // Setting the ViewPager For the SlidingTabsLayout
        tabsMain.setViewPager(pagerMain);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void addVerseSelected() {
        this.setTitle("Verse Selection");
        pagerMain.setVisibility(View.GONE);
        tabsMain.setVisibility(View.GONE);
        startQuiz.setText(BACK);
        startQuiz.setTypeface(Typeface.DEFAULT_BOLD);
        setSlidingTabViewVerseSelector();
    }

    @Override
    public void onBackPressed(){
        this.setTitle("MemorizeIt-Bible");

        if(pagerVerseSelector != null && pagerVerseSelector.getVisibility() == View.VISIBLE){
            onBackPressedFromNewVerseSelector();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onNewVerseClicked() {
        pagerMain.setVisibility(View.GONE);
        tabsMain.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.GONE);
        setSlidingTabViewVerseSelector();
    }

    private void onBackPressedFromNewVerseSelector(){
        tabsVerseSelector.setVisibility(View.GONE);
        pagerVerseSelector.setVisibility(View.GONE);
        pagerMain.setVisibility(View.VISIBLE);
        tabsMain.setVisibility(View.VISIBLE);
        startQuiz.setText(START_QUIZ);
        startQuiz.setTypeface(Typeface.DEFAULT);
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
        REST = new DBTApi(getApplicationContext());
        context = getApplicationContext();
        selectedVerseCallback = new BaseCallback<List<Verse>>() {
            @Override
            public void onResponse(List<Verse> response) {
                FragmentManager fm = getSupportFragmentManager();
                VerseSelectedDialogFragment verseSelectedDialog = new VerseSelectedDialogFragment();

                Bundle args = new Bundle();
                args.putString("num", verseNumber);
                args.putString("verseText", response.get(0).getVerseText());
                args.putString("verseLocation", mPrefs.getSelectedBook(context) + " " + mPrefs.getSelectedChapter(context) + ":" + verseNumber);
                verseSelectedDialog.setArguments(args);

                verseSelectedDialog.show(fm, "verseSelectedFragment");
            }

            @Override
            public void onFailure(Exception e) {
                Exception t = e;
            }
        };

        String damId;
        if(mPrefs.isBookLocationOT(context)){
            damId = mPrefs.getDamIdOldTestament(context);
        }else{
            damId = mPrefs.getDamIdNewTestament(context);
        }
        REST.getVerse(selectedVerseCallback, damId, mPrefs.getSelectedBookId(context), verseNumber, mPrefs.getSelectedChapter(context));


    }
}
