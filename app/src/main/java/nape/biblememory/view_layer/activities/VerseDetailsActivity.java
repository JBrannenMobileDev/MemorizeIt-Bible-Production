package nape.biblememory.view_layer.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.MyVerse;
import nape.biblememory.utils.MyVerseCopyer;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.adapters.ViewPagerAdapterMyVerses;
import nape.biblememory.view_layer.fragments.dialogs.DeleteVerseAlertDialog;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.R;
import nape.biblememory.view_layer.fragments.dialogs.VerseMemorizedAlertDialog;
import nape.biblememory.view_layer.views.SlidingTabLayout;

public class VerseDetailsActivity extends AppCompatActivity implements DeleteVerseAlertDialog.YesSelected, VerseMemorizedAlertDialog.YesSelected{

    @BindView(R.id.my_verses_pager)ViewPager pagerMain;
    @BindView(R.id.my_verses_tabs) SlidingTabLayout tabsMain;
    private ViewPagerAdapterMyVerses adapterMain;
    private CharSequence mainTitles[]={"Details","Learn verse"};
    private MyVerse verse;
    private String verseLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_details);
        ButterKnife.bind(this);
        verseLocation = getIntent().getStringExtra("verseLocation");
        setTitle(verseLocation);
        Realm realm = Realm.getDefaultInstance();
        verse = realm.where(MyVerse.class).equalTo("verseLocation", verseLocation).findFirst();
        setSlidingTabViewVerseSelector();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.verse_details_menu, menu);
        return true;
    }

    private void setSlidingTabViewVerseSelector() {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterMain =  new ViewPagerAdapterMyVerses(getSupportFragmentManager(),mainTitles,2, verseLocation);

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
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    setTitle("Learning");
                    adapterMain.setEditTextFocus();
                }
                if(position == 0){
                    setTitle(verseLocation);
                    hideSoftKeyboard();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
        if (id == R.id.action_reset) {
            MyVerse temp = MyVerseCopyer.getCopy(verse);
            temp.setMemoryStage(0);
            temp.setMemorySubStage(0);
            DataStore.getInstance().updateQuizVerse(temp, getApplicationContext());
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
            sAux = sAux + "Hey! I just started memorizing " + verse.getVerseLocation() +  " using this app. You should check it out. It gives me a quiz every time I open my phone.\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

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
        DataStore.getInstance().deleteQuizVerse(new MyVerse("", verseLocation), getApplicationContext());
        finish();
    }

    @Override
    public void callOnFinished() {
        finish();
    }

    @Override
    public void onHideUIControls() {
        hideSoftKeyboard();
    }
}
