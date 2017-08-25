package nape.biblememory.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Adapters.ViewPagerSocialAdapter;
import nape.biblememory.R;
import nape.biblememory.Views.SlidingTabLayout;

public class SocialActivity extends AppCompatActivity{
    private static final int NUM_OF_TABS = 3;
    private static CharSequence mainTitles[]={"Friends","Groups","Requests"};

    @BindView(R.id.social_pager) ViewPager pager;
    @BindView(R.id.social_tabs) SlidingTabLayout tabs;

    private ViewPagerSocialAdapter adapterMain;

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
}
