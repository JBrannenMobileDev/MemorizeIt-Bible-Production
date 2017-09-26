package nape.biblememory.view_layer.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.faithcomesbyhearing.dbt.model.Verse;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.adapters.ViewPagerAdapter;
import nape.biblememory.view_layer.adapters.ViewPagerAdapterVerseSelector;
import nape.biblememory.data_layer.dbt_api.DBTApi;
import nape.biblememory.view_layer.fragments.BooksFragment;
import nape.biblememory.view_layer.fragments.ChapterFragment;
import nape.biblememory.view_layer.fragments.MyVersesFragment;
import nape.biblememory.view_layer.fragments.dialogs.MyVersesEmptyAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.NoInternetAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.NoStarsAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.SelectVersionAlertDialog;
import nape.biblememory.view_layer.fragments.VerseFragment;
import nape.biblememory.managers.NetworkManager;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.models.User;
import nape.biblememory.models.UserPreferencesModel;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.views.SlidingTabLayout;
import nape.biblememory.view_layer.fragments.dialogs.VerseSelectedDialogFragment;
import nape.biblememory.R;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener, BooksFragment.BooksFragmentListener,
        ChapterFragment.ChaptersFragmentListener, VerseFragment.OnVerseSelected, VerseSelectedDialogFragment.addVerseDialogActions,
        SelectVersionAlertDialog.VersionSelected, MyVersesFragment.myVersesListener {

    private ViewPager pagerMain;
    private ViewPagerAdapter adapterMain;
    private SlidingTabLayout tabsMain;
    private FrameLayout fragmentContainer;
    private CharSequence mainTitles[]={"My verses","Memorized"};
    private ViewPager pagerVerseSelector;
    private ViewPagerAdapterVerseSelector adapterVerseSelector;
    private SlidingTabLayout tabsVerseSelector;
    private CharSequence VerseSelectorTitles[]={"BOOKS","CHAPTER","VERSE"};
    private int Numboftabs = 3;
    private FloatingActionButton startQuiz;
    private FloatingActionButton addVerseFab;
    private UserPreferences mPrefs;
    private String bookName;
    private String chapterNum;
    private String verseNum;
    private NavigationView navigationView;
    private FrameLayout startQuizFabFrame;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView userEmail;
    private CoordinatorLayout coordinatorLayout;

    private DBTApi REST;
    private BaseCallback<List<Verse>> selectedVerseCallback;
    private Context context;
    private Snackbar snackbar;

    private TourGuide mTourGuideHandler;
    private Realm realm;
    private RealmResults<MyVerse> quizList;
    private boolean nextBackVerseSelection;



    private static final String BACK = "BACK";
    private static final String START_QUIZ = "START QUIZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_left);
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

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_activity_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        setNavIconColors();
        View headerView = navigationView.getHeaderView(0);
        userEmail = (TextView) headerView.findViewById(R.id.nav_drawer_user_email);
        userEmail.setText(mPrefs.getUserEmail(getApplicationContext()));
        context = getApplicationContext();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSlidingTabViewMain();

        initData();

        startQuizFabFrame = (FrameLayout) findViewById(R.id.start_quiz_fab_frame);
        startQuiz = (FloatingActionButton) findViewById(R.id.start_quiz_fab);
        addVerseFab = (FloatingActionButton) findViewById(R.id.add_verse_fab);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("start_quiz_Fab_selected", null);
                if(quizList != null && quizList.size() > 0) {
                    if (hasGoldStartVerses()) {
                        Intent myIntent = new Intent(getApplicationContext(), PhoneUnlockActivity.class);
                        myIntent.putExtra("moreVerses", true);
                        startActivity(myIntent);
                    } else {
                        new NoStarsAlertDialog().show(getSupportFragmentManager(), null);
                    }
                }else{
                    new MyVersesEmptyAlertDialog().show(getSupportFragmentManager(), null);
                }
            }
        });

        addVerseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), VerseSelectionActivity.class), 0);
                if(mTourGuideHandler != null) {
                    mTourGuideHandler.cleanUp();
                }
            }
        });

        if(mPrefs.isTourStep1Complete(getApplicationContext()) && mPrefs.isTourStep2Complete(getApplicationContext())){
            mPrefs.setFirstTimeUser(false, context);
        }

        if(!mPrefs.isFirstTimeUser(getApplicationContext())) {
            BaseCallback<List<User>> friendRequestCallback = new BaseCallback<List<User>>() {
                @Override
                public void onResponse(List<User> response) {
                    if (response.size() > 0) {
                        navigationView.getMenu()
                                .findItem(R.id.nav_social)
                                .getIcon()
                                .setColorFilter(getResources().getColor(R.color.colorProgressBg), PorterDuff.Mode.SRC_IN);

                        snackbar = Snackbar
                                .make(coordinatorLayout, String.valueOf(response.size()) + " Friend request", Snackbar.LENGTH_INDEFINITE).
                                        setAction("VIEW", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (NetworkManager.getInstance().isInternet(getApplicationContext())) {
                                                    Intent intent = new Intent(getApplicationContext(), SocialActivity.class);
                                                    intent.putExtra("coming_from_snackbar", true);
                                                    startActivity(intent);
                                                } else {
                                                    new NoInternetAlertDialog().show(getSupportFragmentManager(), null);
                                                }
                                            }
                                        });

                        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                startQuizFabFrame.animate().translationY(0);
                                mPrefs.setSnackbarVisible(false, getApplicationContext());
                                super.onDismissed(transientBottomBar, event);
                            }

                            @Override
                            public void onShown(Snackbar transientBottomBar) {
                                mPrefs.setSnackbarVisible(true, getApplicationContext());
                                super.onShown(transientBottomBar);
                            }
                        });


                        // Changing message text color
                        snackbar.setActionTextColor(getResources().getColor(R.color.colorGreenText));
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorCloseButtonTextUnselected));
                        float distance = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, -45,
                                getResources().getDisplayMetrics()
                        );
                        startQuizFabFrame.animate().translationY(distance);
                        snackbar.show();

                        BaseCallback<List<User>> friendsThatBlessedCallbackForNavDrawer = new BaseCallback<List<User>>() {
                            @Override
                            public void onResponse(List<User> response) {
                                if (response != null && response.size() > 0) {
                                    navigationView.getMenu()
                                            .findItem(R.id.nav_social)
                                            .getIcon()
                                            .setColorFilter(getResources().getColor(R.color.colorGreenText), PorterDuff.Mode.SRC_IN);
                                } else {
                                    navigationView.getMenu()
                                            .findItem(R.id.nav_social)
                                            .getIcon()
                                            .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        };
                        DataStore.getInstance().getUsersThatBlessedMe(friendsThatBlessedCallbackForNavDrawer, getApplicationContext());
                    } else {
                        BaseCallback<List<User>> friendsThatBlessedCallback = new BaseCallback<List<User>>() {
                            @Override
                            public void onResponse(List<User> response) {
                                if (response != null && response.size() > 0) {
                                    navigationView.getMenu()
                                            .findItem(R.id.nav_social)
                                            .getIcon()
                                            .setColorFilter(getResources().getColor(R.color.colorGreenText), PorterDuff.Mode.SRC_IN);

                                    snackbar = Snackbar
                                            .make(coordinatorLayout, String.valueOf(response.size()) + " Blessing received!", Snackbar.LENGTH_INDEFINITE).
                                                    setAction("VIEW", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (NetworkManager.getInstance().isInternet(getApplicationContext())) {
                                                                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                                                                startActivity(intent);
                                                            } else {
                                                                new NoInternetAlertDialog().show(getSupportFragmentManager(), null);
                                                            }
                                                        }
                                                    });

                                    snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                        @Override
                                        public void onDismissed(Snackbar transientBottomBar, int event) {
                                            startQuizFabFrame.animate().translationY(0);
                                            mPrefs.setSnackbarVisible(false, getApplicationContext());
                                            super.onDismissed(transientBottomBar, event);
                                        }

                                        @Override
                                        public void onShown(Snackbar transientBottomBar) {
                                            mPrefs.setSnackbarVisible(true, getApplicationContext());
                                            super.onShown(transientBottomBar);
                                        }
                                    });


                                    // Changing message text color
                                    snackbar.setActionTextColor(getResources().getColor(R.color.colorGreenText));
                                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorCloseButtonTextUnselected));
                                    float distance = TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 40,
                                            getResources().getDisplayMetrics()
                                    );
                                    startQuizFabFrame.animate().translationY(-distance);
                                    snackbar.show();
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        };
                        DataStore.getInstance().getUsersThatBlessedMe(friendsThatBlessedCallback, getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            };
            DataStore.getInstance().registerForFriendRequests(friendRequestCallback);
        }else{
            if(!mPrefs.isTourStep1Complete(getApplicationContext())) {
                mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                        .setPointer(new Pointer())
                        .setToolTip(new ToolTip().setTitle("Welcome!").setDescription("Click the plus button to add a verse.").
                                setGravity(Gravity.TOP).setBackgroundColor(getResources().getColor(R.color.colorProgressBg)))
                        .setOverlay(new Overlay())
                        .playOn(addVerseFab);
            }
        }
        DataStore.getInstance().updateUserData(mPrefs.getUserId(getApplicationContext()));
        if(getIntent().getBooleanExtra("launch_add_verse", false)){
            findVerseSelected();
        }
        if(getIntent().getBooleanExtra("launch_share", false)){
            final String verseLocation = getIntent().getStringExtra("verseLocation");
            final String verse = getIntent().getStringExtra("verse");
            sendShareIntent(verseLocation, verse);
        }
        if(getIntent().getBooleanExtra("launch_rate", false)){
            sendRateThisAppIntent();
        }
    }

    private boolean hasGoldStartVerses() {
        for(MyVerse verse : quizList){
            if(verse.getGoldStar() == 1){
                return true;
            }
        }
        return false;
    }

    private void initData() {
        realm = Realm.getDefaultInstance();
        quizList = realm.where(MyVerse.class).findAll();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(snackbar != null)
            snackbar.dismiss();
    }

    private void setNavIconColors() {
        navigationView.getMenu()
                .findItem(R.id.nav_home)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu()
                .findItem(R.id.nav_add_new_verse)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu()
                .findItem(R.id.nav_start_quiz)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu()
                .findItem(R.id.nav_social)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu()
                .findItem(R.id.nav_settings)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu()
                .findItem(R.id.nav_support_the_dev)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
        BaseCallback<List<MemorizedVerse>> memorizedVersesCallback = new BaseCallback<List<MemorizedVerse>>() {
            @Override
            public void onResponse(List<MemorizedVerse> response) {
                if(response != null && response.size() > 5){
                    navigationView.getMenu()
                            .findItem(R.id.nav_support_the_dev)
                            .getIcon()
                            .setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getMemorizedVerses(memorizedVersesCallback, getApplicationContext());
        navigationView.getMenu()
                .findItem(R.id.nav_share)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu()
                .findItem(R.id.nav_rate)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu()
                .findItem(R.id.nav_feedback)
                .getIcon()
                .setColorFilter(getResources().getColor(R.color.greyIcon), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void finish(){
        super.finish();
    }


    @Override
    public void onRestart(){
        super.onRestart();
        if(startQuiz.getVisibility() == View.VISIBLE){
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DataStore.getInstance().getFriendRequests(getApplicationContext());
        setSlidingTabViewMain();
        if(pagerMain != null){
            if(mPrefs.isComingFromMemorized(getApplicationContext())){
                pagerMain.setCurrentItem(1);
                mPrefs.setComingFromMemorizedDetails(false, getApplicationContext());
            }
        }
    }



    private void setSlidingTabViewMain() {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterMain =  new ViewPagerAdapter(getSupportFragmentManager(),mainTitles);

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

        pagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 1){
                    addVerseFab.hide();
                }
                if(position == 0){
                    addVerseFab.show();
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    addVerseFab.hide();
                }
                if(position == 0){
                    addVerseFab.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
            startActivityForResult(new Intent(getApplicationContext(), VerseSelectionActivity.class), 0);
        } else if (id == R.id.nav_start_quiz) {
            startQuiz.callOnClick();
            mFirebaseAnalytics.logEvent("start_quiz_nav_draw_selected", null);
        } else  if (id == R.id.nav_settings) {
            mFirebaseAnalytics.logEvent("settings_nav_draw_selected", null);
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(settingsIntent, 1);
        } else if (id == R.id.nav_social) {
            mFirebaseAnalytics.logEvent("stats_nav_draw_selected", null);
            if(NetworkManager.getInstance().isInternet(getApplicationContext())) {
                Intent settingsIntent = new Intent(getApplicationContext(), SocialActivity.class);
                startActivity(settingsIntent);
            }else{
                new NoInternetAlertDialog().show(getSupportFragmentManager(), null);
            }
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
            intent = new Intent(getApplicationContext(), SendFeedbackActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 2) {
            finish();
        }
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
            i.putExtra(Intent.EXTRA_SUBJECT, "Check this out!");
            String sAux = "Hey!  check out this app! Super useful for memorizing bible verses. Every time i open my phone a short quiz pops up. \n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }

    private void sendShareIntent(String verseLocation, String verse){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Check this out!");
            String sAux;
            if(verseLocation != null && verse != null){
                sAux = verseLocation + "\n" + verse + "\n\n" + "Hey! I just memorized " + verseLocation + " using the MemorizeIt Bible app!  ";
            }else {
                sAux = "Hey!  check out this app! Super useful for memorizing bible verses. Every time i open my phone a short quiz pops up. \n\n";
            }            sAux = sAux + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }

    public void findVerseSelected() {
        if(NetworkManager.getInstance().isInternet(getApplicationContext())) {
            this.setTitle("Verse Selection");
            pagerMain.setVisibility(View.GONE);
            tabsMain.setVisibility(View.GONE);
            setSlidingTabViewVerseSelector();
            startQuiz.setVisibility(View.GONE);
            startQuizFabFrame.setVisibility(View.GONE);
            if(snackbar != null) {
                snackbar.dismiss();
            }
        }else{
            new NoInternetAlertDialog().show(getSupportFragmentManager(), null);
        }
    }

    @Override
    public void onBackPressed(){
        this.setTitle("MemorizeIt-Bible");

        if(pagerVerseSelector != null && pagerVerseSelector.getVisibility() == View.VISIBLE){
            onBackPressedFromNewVerseSelector();
            return;
        }
        finish();
        super.onBackPressed();
    }

    private void onBackPressedFromNewVerseSelector(){
        if(tabsVerseSelector != null) {
            if(snackbar != null) {
                snackbar.show();
            }
            tabsVerseSelector.setVisibility(View.GONE);
            pagerVerseSelector.setVisibility(View.GONE);
            pagerMain.setVisibility(View.VISIBLE);
            tabsMain.setVisibility(View.VISIBLE);
            startQuiz.setVisibility(View.VISIBLE);
            startQuizFabFrame.setVisibility(View.VISIBLE);
            setSlidingTabViewMain();
        }
        if((quizList == null || quizList.size() == 0) && !mPrefs.isTourStep2Complete(getApplicationContext())){
            ScriptureData newVerse = new ScriptureData();
            newVerse.setVerseLocation("Romans 6:23");
            newVerse.setVerse("For the wages of sin is death, but the free gift of God is eternal life in Christ Jesus our Lord.");
            newVerse.setVersionCode("ESV");
            DataStore.getInstance().saveQuizVerse(newVerse, getApplicationContext());
            adapterMain.refreshMyVersesList();
        }
        mPrefs.setTourStep1Complete(true, getApplicationContext());
        UserPreferencesModel model = new UserPreferencesModel();
        model.initAllData(getApplicationContext(), mPrefs);
        DataStore.getInstance().saveUserPrefs(model, getApplicationContext());

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

                if(response.size() > 0) {
                    Bundle args = new Bundle();
                    args.putString("num", verseNumber);
                    args.putString("verseText", response.get(0).getVerseText());
                    args.putString("verseLocation", mPrefs.getSelectedBook(context) + " " + mPrefs.getSelectedChapter(context) + ":" + verseNumber);
                    args.putLong("numOfVersesInChapter", mPrefs.getNumberOfVerses(context));
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
        if(mPrefs.isBookLocationOT(context)){
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
        REST.getVerse(selectedVerseCallback, damId, mPrefs.getSelectedBookId(context), verseNumber, mPrefs.getSelectedChapter(context));
    }

    @Override
    public void onVerseAdded(boolean comingFromNewVerses){
        if(!comingFromNewVerses) {
            onBackPressed();
        }
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
    public void onVerseDeleted(final ScriptureData verse, final int position) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, verse.getVerseLocation() + " deleted", Snackbar.LENGTH_LONG).
                        setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapterMain.undoDeletedVerse(verse, getApplicationContext(), position);
                            }
                        });

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                startQuizFabFrame.animate().translationY(0);
                mPrefs.setSnackbarVisible(false, getApplicationContext());
                super.onDismissed(transientBottomBar, event);
            }

            @Override
            public void onShown(Snackbar transientBottomBar) {
                mPrefs.setSnackbarVisible(true, getApplicationContext());
                super.onShown(transientBottomBar);
            }
        });


        // Changing message text color
        snackbar.setActionTextColor(getResources().getColor(R.color.colorGreenText));
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorCloseButtonTextUnselected));
        float distance = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40,
                getResources().getDisplayMetrics()
        );
        startQuizFabFrame.animate().translationY(-distance);
        snackbar.show();
    }
}
