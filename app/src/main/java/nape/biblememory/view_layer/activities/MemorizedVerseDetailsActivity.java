package nape.biblememory.view_layer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.adapters.ViewPagerAdapterMemorized;
import nape.biblememory.view_layer.fragments.dialogs.DeleteVerseAlertDialog;
import nape.biblememory.view_layer.views.SlidingTabLayout;

public class MemorizedVerseDetailsActivity extends AppCompatActivity implements DeleteVerseAlertDialog.YesSelected{

    private MemorizedVerse verse;
    private String verseLocation;
    @BindView(R.id.memorized_pager)ViewPager pagerMain;
    @BindView(R.id.memorized_tabs) SlidingTabLayout tabsMain;
    private ViewPagerAdapterMemorized adapterMain;
    private CharSequence mainTitles[]={"Details","Review"};
    private boolean comingFromReviewNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorized_verse_details);
        ButterKnife.bind(this);
        verseLocation = getIntent().getStringExtra("verseLocation");
        setTitle(Html.fromHtml("<h2>" +verseLocation+ "</h2>"));
        Realm realm = Realm.getDefaultInstance();
        verse = realm.where(MemorizedVerse.class).equalTo("verseLocation", verseLocation).findFirst();
        UserPreferences mPrefs = new UserPreferences();
        mPrefs.setComingFromMemorizedDetails(true, getApplicationContext());
        comingFromReviewNow = getIntent().getBooleanExtra("reviewNow", false);
        setSlidingTabViewVerseSelector(comingFromReviewNow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.verse_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_share) {
            sendShareIntent();
            return true;
        }
        if (id == R.id.action_delete_verse) {
            DeleteVerseAlertDialog deleteDialog = new DeleteVerseAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putString("verse_location", verse.getVerseLocation());
            deleteDialog.setArguments(bundle);
            deleteDialog.show(getSupportFragmentManager(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendShareIntent(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, verse.getVerseLocation() + "  " + "Memorize It - Bible");
            String sAux = "\n" +verse.getVerseLocation() + "  " + verse.getVerse() + "\n\n";
            sAux = sAux + "Hey! I just memorized this verse using this app.  It gives me a quiz every time i open my phone.\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }
    private void setSlidingTabViewVerseSelector(boolean comingFromReviewNow) {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterMain =  new ViewPagerAdapterMemorized(getSupportFragmentManager(),mainTitles,2, verseLocation, comingFromReviewNow);

        // Assigning ViewPager View and setting the adapter
        pagerMain.setAdapter(adapterMain);
        pagerMain.setVisibility(View.VISIBLE);

        // Assiging the Sliding Tab Layout View
        tabsMain.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabsMain.setVisibility(View.VISIBLE);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabsMain.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabsMain.setViewPager(pagerMain);
        pagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 1) {
                    adapterMain.setEditTextFocus();
                }
                if(position == 0){
                    hideSoftKeyboard();
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    adapterMain.setEditTextFocus();
                }
                if(position == 0){
                    hideSoftKeyboard();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(comingFromReviewNow){
            pagerMain.setCurrentItem(1);
        }
    }

    private void hideSoftKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @Override
    public void onDeleteVerse(String verseLocation) {
        MemorizedVerse temp = new MemorizedVerse();
        temp.setVerseLocation(verseLocation);
        DataStore.getInstance().deleteMemorizedVerse(temp, getApplicationContext());
        finish();
    }
}
