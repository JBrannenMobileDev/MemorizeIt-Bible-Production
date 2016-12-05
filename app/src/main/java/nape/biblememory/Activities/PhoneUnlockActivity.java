package nape.biblememory.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import nape.biblememory.Activities.Fragments.Dialogs.FirstTimeUnlockDialog;
import nape.biblememory.Activities.Presenters.PhoneUnlockPresenter;
import nape.biblememory.Activities.Presenters.PhoneUnlockPresenterImp;
import nape.biblememory.Activities.Views.PhoneUnlockView;
import nape.biblememory.R;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.analytics.FirebaseAnalytics.Param;


public class PhoneUnlockActivity extends AppCompatActivity implements PhoneUnlockView{
    private View mContentView;
    private View mControlsView;
    private TextView verse;
    private TextView verseLocation;
    private TextView pageTitle;
    private TextView switchTextOn;
    private TextView switchTextOff;
    private TextView checkAnswerText;
    private TextView hintText;
    private Button close_more_button;
    private FloatingActionButton checkAnswerFAB;
    private FloatingActionButton yesButton;
    private FloatingActionButton noButton;
    private FloatingActionButton hintButton;
    private AdView mAdView;
    private Switch moreVersesSwitch;
    private LinearLayout verificationLayout;
    private UserPreferences mPrefs;

    private PhoneUnlockPresenter mPresenter;
    private FirstTimeUnlockDialog firstTimeDialog;

    private FirebaseAnalytics analytics;


    @Override
    protected void onResume() {
        super.onResume();
        hideUIControls();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_unlock);
        analytics = FirebaseAnalytics.getInstance( this );

        InitializeBannerAd();

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.verse);
        firstTimeDialog = new FirstTimeUnlockDialog();
        verse = (TextView) findViewById(R.id.verse);
        verseLocation = (TextView) findViewById(R.id.verse_location);
        pageTitle = (TextView) findViewById(R.id.title_text);
        switchTextOn = (TextView) findViewById(R.id.switch_track_on_text);
        switchTextOff = (TextView) findViewById(R.id.switch_track_off_text);
        checkAnswerText = (TextView) findViewById(R.id.check_answer_textview);
        hintText = (TextView) findViewById(R.id.hint_textview);
        verificationLayout = (LinearLayout) findViewById(R.id.verification_layout);
        close_more_button = (Button) findViewById(R.id.close_button);
        checkAnswerFAB = (FloatingActionButton) findViewById(R.id.check_answer_button_fab);
        yesButton = (FloatingActionButton) findViewById(R.id.yes_button_fab);
        noButton = (FloatingActionButton) findViewById(R.id.no_button_fab);
        moreVersesSwitch = (Switch) findViewById(R.id.moreVersesSwitch);
        hintButton = (FloatingActionButton) findViewById(R.id.hint_button_fab);

        mPresenter = new PhoneUnlockPresenterImp(this, getApplicationContext());

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onYesClicked();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onNoClicked();
            }
        });

        close_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {mPresenter.onCloseClicked();
            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onHintClicked();
            }
        });

        checkAnswerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onCheckAnswerClicked();
            }
        });

        moreVersesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { mPresenter.onMoreSwitchStateChanged(isChecked); }
        });

        mPresenter.onRequestData();

        mPrefs = new UserPreferences();
        if(mPrefs.getScreenWidth(getApplicationContext()) == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            mPrefs.setScreenWidth(width, getApplicationContext());
        }
        analytics.setMinimumSessionDuration(500);
    }

    private void hideUIControls() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void setTitlebarText(int text) {
        pageTitle.setText(text);
    }

    @Override
    public void setBasebarText(String text) {
        close_more_button.setText(text);
    }

    @Override
    public void setVerificationLayoutVisibility(int visibility) {
        if(visibility == View.VISIBLE){
            verificationLayout.setVisibility(visibility);
            yesButton.show();
            noButton.show();
        }else if(visibility == View.GONE){
            yesButton.hide();
            noButton.hide();
            verificationLayout.setVisibility(visibility);
        }
    }

    @Override
    public void setCheckAnswerButtonVisibility(int visibility) {
        if(visibility == View.GONE){
            checkAnswerFAB.hide();
            checkAnswerText.setVisibility(visibility);
        }else if(visibility == View.VISIBLE){
            checkAnswerFAB.show();
            checkAnswerText.setVisibility(visibility);
        }
    }

    @Override
    public void setVerseText(String verse) {
        this.verse.setText(verse);
    }

    @Override
    public void setSpannableVerseText(SpannableStringBuilder verse) {
        this.verse.setText(verse);
    }

    @Override
    public void setSpannableVerseLocationText(SpannableStringBuilder location) {
        this.verseLocation.setText(location);
    }

    @Override
    public void setVerseLocationText(String verseLocation) {
        this.verseLocation.setText(verseLocation);
    }

    @Override
    public void onFinishActivity() {
        finish();
    }

    @Override
    public void showFirstTimeDialog() {
        firstTimeDialog.show(getFragmentManager(), "firsTime");
    }

    @Override
    public void setCheckAnswerButtonText(int text) {
        checkAnswerText.setText(text);
    }

    @Override
    public void setDoneButtonFont(){
        checkAnswerText.setTextAppearance(getApplicationContext(), R.style.DoneButtonFontStyle);
    }

    @Override
    public void setCheckAnswerButtonFont() {
        checkAnswerText.setTextAppearance(getApplicationContext(), R.style.CheckAnswerButtonFontStyle);
    }

    @Override
    public void setMoreSwitchVisibility(boolean visible) {
        if(visible) {
            moreVersesSwitch.setVisibility(View.VISIBLE);
        }else{
            moreVersesSwitch.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMoreVerseSwitchState(boolean state) {
        moreVersesSwitch.setChecked(state);
    }

    @Override
    public void setMoreSwitchTrackText(boolean state) {
        if(state){
            switchTextOn.setVisibility(View.INVISIBLE);
            switchTextOff.setVisibility(View.VISIBLE);
            switchTextOff.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorCloseButtonTextUnselected));
        }else{
            switchTextOff.setVisibility(View.INVISIBLE);
            switchTextOn.setVisibility(View.VISIBLE);
            switchTextOn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorCloseButtonTextUnselected));
        }
    }

    @Override
    public void setSwitchTextInvisible() {
        switchTextOn.setVisibility(View.INVISIBLE);
        switchTextOff.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setHintButtonVisibility(int value) {
        if(value == View.VISIBLE){
            hintButton.show();
            hintText.setVisibility(View.VISIBLE);
        }else if(value == View.GONE) {
            hintText.setVisibility(value);
            hintButton.hide();
        }
    }

    @Override
    public void setVerseTextAlignmentCenter(boolean alignment) {
        if(alignment) {
            verse.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }else{
            verse.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setMoreVerseSwitchState(false);
    }

    private void InitializeBannerAd(){
        MobileAds.initialize(getApplicationContext(), String.valueOf(R.string.app_ad_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
