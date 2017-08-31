package nape.biblememory.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Adapters.ViewPagerSocialAdapter;
import nape.biblememory.Models.User;
import nape.biblememory.R;
import nape.biblememory.Views.SlidingTabLayout;
import nape.biblememory.data_store.DataStore;

public class SocialActivity extends AppCompatActivity{
    private static final int NUM_OF_TABS = 3;
    private static CharSequence mainTitles[]={"Friends","Groups","Requests"};

    @BindView(R.id.social_pager) ViewPager pager;
    @BindView(R.id.social_tabs) SlidingTabLayout tabs;

    private ViewPagerSocialAdapter adapterMain;
    private List<User> usersThaBlessedMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        ButterKnife.bind(this);
        setTitle("Social");
        setSlidingTabViewMain(getIntent().getBooleanExtra("coming_from_snackbar", false));
    }

    private void setSlidingTabViewMain(boolean coming_from_snackbar) {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterMain =  new ViewPagerSocialAdapter(getSupportFragmentManager(),mainTitles,NUM_OF_TABS);
        pager.setAdapter(adapterMain);

        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        //added for test commit
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        if(coming_from_snackbar){
            pager.setCurrentItem(2, true);
        }
    }
    private void setMenuIconColors(final Menu menu) {
        menu.findItem(R.id.action_notifications).getIcon().setColorFilter(getResources().getColor(R.color.bgColor), PorterDuff.Mode.SRC_IN);
        BaseCallback<List<User>> usersThatBlessedMeCallback = new BaseCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                if(response != null){
                    usersThaBlessedMe = response;
                    if(response.size() > 0){
                        menu.findItem(R.id.action_notifications).getIcon().setColorFilter(getResources().getColor(R.color.colorGreenText), PorterDuff.Mode.SRC_IN);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getUsersThatBlessedMe(usersThatBlessedMeCallback, getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.social_menu, menu);
        setMenuIconColors(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_notifications) {
            if(usersThaBlessedMe != null && usersThaBlessedMe.size() > 0){
                startActivity(new Intent(this, NotificationActivity.class));
            }else{
                Toast.makeText(this, "You currently have 0 notifications.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
