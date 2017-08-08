package nape.biblememory.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.faithcomesbyhearing.dbt.model.Book;
import com.faithcomesbyhearing.dbt.model.Verse;
import com.faithcomesbyhearing.dbt.model.Volume;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import nape.biblememory.Adapters.ViewPagerAdapter;
import nape.biblememory.Adapters.ViewPagerAdapterVerseSelector;
import nape.biblememory.DBTApi.DBTApi;
import nape.biblememory.Fragments.BooksFragment;
import nape.biblememory.Fragments.ChapterFragment;
import nape.biblememory.Fragments.Dialogs.InProgressEmptyAlertDialog;
import nape.biblememory.Fragments.Dialogs.NoInternetAlertDialog;
import nape.biblememory.Fragments.Dialogs.RebuildingDbErrorAlertDialog;
import nape.biblememory.Fragments.Dialogs.RemoveVerseFromInProgressAlertDialog;
import nape.biblememory.Fragments.Dialogs.RemoveVerseFromNewVersesAlertDialog;
import nape.biblememory.Fragments.MyVersesFragment;
import nape.biblememory.Fragments.VerseFragment;
import nape.biblememory.Fragments.VerseSelection;
import nape.biblememory.Managers.NetworkManager;
import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.data_store.DataStore;
import nape.biblememory.data_store.Sqlite.MemoryListContract;
import nape.biblememory.UserPreferences;
import nape.biblememory.Views.SlidingTabLayout;
import nape.biblememory.Fragments.Dialogs.VerseSelectedDialogFragment;
import nape.biblememory.R;

public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener,
        MyVersesFragment.OnAddVerseSelectedListener, VerseSelection.FragmentToActivity, BooksFragment.BooksFragmentListener,
        ChapterFragment.ChaptersFragmentListener, VerseFragment.OnVerseSelected, VerseSelectedDialogFragment.addVerseDialogActions,
        RemoveVerseFromInProgressAlertDialog.YesSelected, RemoveVerseFromNewVersesAlertDialog.YesSelected{

    private ViewPager pagerMain;
    private ViewPagerAdapter adapterMain;
    private SlidingTabLayout tabsMain;
    private FrameLayout fragmentContainer;
    private CharSequence mainTitles[]={"Upcoming","Quiz verses","Memorized"};
    private ViewPager pagerVerseSelector;
    private ViewPagerAdapterVerseSelector adapterVerseSelector;
    private SlidingTabLayout tabsVerseSelector;
    private CharSequence VerseSelectorTitles[]={"BOOKS","CHAPTER","VERSE"};
    private int Numboftabs = 3;
    private FloatingActionButton startQuiz;
    private UserPreferences mPrefs;
    private String bookName;
    private String chapterNum;
    private String verseNum;
    private NavigationView navigationView;
    private FrameLayout startQuizFabFrame;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView userEmail;

    private DBTApi REST;
    private BaseCallback<List<Verse>> selectedVerseCallback;
    private Context context;



    private static final String BACK = "BACK";
    private static final String START_QUIZ = "START QUIZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        userEmail = (TextView) headerView.findViewById(R.id.nav_drawer_user_email);
        userEmail.setText(mPrefs.getUserEmail(getApplicationContext()));
        context = getApplicationContext();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSlidingTabViewMain();



        startQuizFabFrame = (FrameLayout) findViewById(R.id.start_quiz_fab_frame);
        startQuiz = (FloatingActionButton) findViewById(R.id.start_quiz_fab);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("start_quiz_Fab_selected", null);
                if(DataStore.getInstance().getLocalQuizListSize(getApplicationContext()) > 0) {
                    Intent myIntent = new Intent(getApplicationContext(), PhoneUnlockActivity.class);
                    startActivity(myIntent);
                }else{
                    new InProgressEmptyAlertDialog().show(getSupportFragmentManager(), null);
                }
            }
        });

        if(mPrefs.isRebuildError(getApplicationContext())){
            new RebuildingDbErrorAlertDialog().show(getSupportFragmentManager(), null);
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }


    @Override
    public void onRestart(){
        super.onRestart();
        if(startQuiz.getVisibility() == View.VISIBLE){
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
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
        Intent intent = null;

        int id = item.getItemId();

        if (id == R.id.nav_add_new_verse) {
            mFirebaseAnalytics.logEvent("add_verse_from_nav_draw_selected", null);
            addVerseSelected();
        } else if (id == R.id.nav_start_quiz) {
            startQuiz.callOnClick();
            mFirebaseAnalytics.logEvent("start_quiz_nav_draw_selected", null);
        } else if (id == R.id.nav_settings) {
            mFirebaseAnalytics.logEvent("settings_nav_draw_selected", null);
            intent = new Intent(getApplicationContext(), SettingsActivity.class);
        } else if (id == R.id.nav_home) {
            mFirebaseAnalytics.logEvent("home_nav_draw_selected", null);
            onBackPressedFromNewVerseSelector();
        } else if(id == R.id.nav_support_the_dev){
            mFirebaseAnalytics.logEvent("support_dev_nav_draw_selected", null);
            intent = new Intent(getApplicationContext(), SupportTheDeveloper.class);
        } else if(id == R.id.nav_share){
            mFirebaseAnalytics.logEvent("share_nav_draw_selected", null);
            sendShareIntent();
        } else if(id == R.id.nav_feedback){
            mFirebaseAnalytics.logEvent("feedback_nav_draw_selected", null);
            intent = new Intent(android.content.Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + "MemoizeItBible@gmail.com"));
        } else if(id == R.id.nav_rate){
            mFirebaseAnalytics.logEvent("rate_nav_draw_selected", null);
            sendRateThisAppIntent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if(intent != null) {
            startActivity(intent);
        }
        return true;
    }

    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

    public void sendRateThisAppIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse("market://details?id=nape.biblememory&hl=en"));
        if (!MyStartActivity(intent)) {
            //Market (Google play) app seems not installed, let's try to open a web browser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=nape.biblememory&hl=en"));
            if (!MyStartActivity(intent)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendShareIntent(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "MemorizeIt Bible");
            String sAux = "\nYou gotta try this app.\n\n";
            String sAux2 = "If you have ever had a hard time memorizing bible verses, this app makes it easy!\n\n";
            sAux = sAux + sAux2 + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }

    @Override
    public void addVerseSelected() {
        if(NetworkManager.getInstance().isInternet(getApplicationContext())) {
            this.setTitle("Verse Selection");
            pagerMain.setVisibility(View.GONE);
            tabsMain.setVisibility(View.GONE);
            setSlidingTabViewVerseSelector();
            startQuiz.setVisibility(View.GONE);
            startQuizFabFrame.setVisibility(View.GONE);
        }else{
            new NoInternetAlertDialog().show(getSupportFragmentManager(), null);
        }
    }

    @Override
    public void onBackPressed(){
        this.setTitle("MemorizeIt-Bible");

        navigationView.getMenu().getItem(0).setChecked(true);

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
        if(tabsVerseSelector != null) {
            tabsVerseSelector.setVisibility(View.GONE);
            pagerVerseSelector.setVisibility(View.GONE);
            pagerMain.setVisibility(View.VISIBLE);
            tabsMain.setVisibility(View.VISIBLE);
            startQuiz.setVisibility(View.VISIBLE);
            startQuizFabFrame.setVisibility(View.VISIBLE);
            setSlidingTabViewMain();
        }
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
        selectedVerseCallback = new BaseCallback<List<Verse>>() {
            @Override
            public void onResponse(List<Verse> response) {
                adapterVerseSelector.setVerseGridViewVisible();
                FragmentManager fm = getSupportFragmentManager();
                VerseSelectedDialogFragment verseSelectedDialog = new VerseSelectedDialogFragment();

                Bundle args = new Bundle();
                args.putString("num", verseNumber);
                args.putString("verseText", response.get(0).getVerseText());
                args.putString("verseLocation", mPrefs.getSelectedBook(context) + " " + mPrefs.getSelectedChapter(context) + ":" + verseNumber);
                args.putLong("numOfVersesInChapter", mPrefs.getNumberOfVerses(context));
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

    @Override
    public void onVerseAdded(boolean comingFromNewVerses){
        if(!comingFromNewVerses) {
            onBackPressed();
        }
        adapterMain.refreshrecyclerViews();
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
            damId = mPrefs.getDamIdOldTestament(context);
        }else{
            damId = mPrefs.getDamIdNewTestament(context);
        }
        String nextVerseNum = String.valueOf(Integer.valueOf(selectedVerseNum) + 1);
        api.getVerse(nextVerseCallback, damId, mPrefs.getSelectedBookId(getApplicationContext()), nextVerseNum, mPrefs.getSelectedChapter(getApplicationContext()));
    }

    @Override
    public void verseAddedToQuizVerses() {
        pagerMain.postDelayed(new Runnable() {

            @Override
            public void run() {
                pagerMain.setCurrentItem(1);
            }
        }, 300);
    }

    @Override
    public void onYesSelected(final String verseLocation) {
        BaseCallback<List<ScriptureData>> quizCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                List<ScriptureData> QuizScriptureList = response;
                ScriptureData scripture = null;

                for(ScriptureData verse : QuizScriptureList){
                    if(verse.getVerseLocation() != null) {
                        if (verse.getVerseLocation().equalsIgnoreCase(verseLocation)) {
                            scripture = verse;
                        }
                    }
                }

                if(scripture != null) {
                    DataStore.getInstance().saveUpcomingVerse(scripture, getApplicationContext());
                }

                ScriptureData verseToDelete = new ScriptureData("", verseLocation);
                DataStore.getInstance().deleteQuizVerse(verseToDelete, getApplicationContext());
                adapterMain.refreshrecyclerViews();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getLocalQuizVerses(quizCallback, getApplicationContext());
    }

    @Override
    public void onRemoveFromNewSelected(String verseLocation) {
        ScriptureData verse = new ScriptureData("", verseLocation);
        DataStore.getInstance().deleteUpcomingVerse(verse, getApplicationContext());
        adapterMain.refreshrecyclerViews();
    }
}
