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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import nape.biblememory.Fragments.Dialogs.FirstTimeUnlockDialog;
import nape.biblememory.Fragments.Dialogs.VerseMemorizedAlertDialog;
import nape.biblememory.Presenters.PhoneUnlockPresenter;
import nape.biblememory.Presenters.PhoneUnlockPresenterImp;
import nape.biblememory.UserPreferences;
import nape.biblememory.Fragments.PhoneUnlockView;
import nape.biblememory.R;

import com.google.firebase.analytics.FirebaseAnalytics;


public class PhoneUnlockActivity extends AppCompatActivity implements PhoneUnlockView, VerseMemorizedAlertDialog.YesSelected{
    private View mContentView;
    private View mControlsView;
    private TextView verse;
    private TextView verseLocation;
    private TextView pageTitle;
    private TextView checkAnswerText;
    private TextView hintText;
    private Button close_more_button;
    private FloatingActionButton checkAnswerFAB;
    private FloatingActionButton yesButton;
    private FloatingActionButton noButton;
    private FloatingActionButton hintButton;
    private AdView mAdView;
    private CheckBox moreVersesCheckbox;
    private FrameLayout moreVersesLayout;
    private LinearLayout verificationLayout;
    private UserPreferences mPrefs;

    private PhoneUnlockPresenter mPresenter;
    private FirstTimeUnlockDialog firstTimeDialog;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onResume() {
        super.onResume();
        hideUIControls();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_unlock);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(this, "Phone unlock quiz", null);

        InitializeBannerAd();

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.verse);
        firstTimeDialog = new FirstTimeUnlockDialog();
        verse = (TextView) findViewById(R.id.verse);
        verseLocation = (TextView) findViewById(R.id.verse_location);
        pageTitle = (TextView) findViewById(R.id.title_text);
        checkAnswerText = (TextView) findViewById(R.id.check_answer_textview);
        hintText = (TextView) findViewById(R.id.hint_textview);
        verificationLayout = (LinearLayout) findViewById(R.id.verification_layout);
        close_more_button = (Button) findViewById(R.id.close_button);
        checkAnswerFAB = (FloatingActionButton) findViewById(R.id.check_answer_button_fab);
        yesButton = (FloatingActionButton) findViewById(R.id.yes_button_fab);
        noButton = (FloatingActionButton) findViewById(R.id.no_button_fab);
        moreVersesCheckbox = (CheckBox) findViewById(R.id.moreVersesSwitch);
        moreVersesLayout = (FrameLayout) findViewById(R.id.moreVersesLayout);
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

        moreVersesCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.onMoreSwitchStateChanged(isChecked);
                mFirebaseAnalytics.logEvent("another_quiz_selected", null);
            }
        });

        mPresenter.onRequestData();

        mPrefs = new UserPreferences();
        if(mPrefs.getScreenWidth(getApplicationContext()) == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            mPrefs.setScreenWidth(width, getApplicationContext());
        }
        mFirebaseAnalytics.setMinimumSessionDuration(500);
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
            moreVersesCheckbox.setVisibility(View.VISIBLE);
            moreVersesLayout.setVisibility(View.VISIBLE);
        }else{
            moreVersesLayout.setVisibility(View.GONE);
            moreVersesCheckbox.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMoreVerseSwitchState(boolean state) {
        moreVersesCheckbox.setChecked(state);
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
    public void showMemorizedAlert(boolean memorizedAndLearningListIsEmpty) {
        VerseMemorizedAlertDialog alert = new VerseMemorizedAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("callOnFinish", memorizedAndLearningListIsEmpty);
        alert.setArguments(bundle);
        alert.show(getSupportFragmentManager(), null);
    }

    @Override
    public void setMoreVersesLayoutColor(int color) {
        moreVersesLayout.setBackgroundResource(color);
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

    @Override
    public void callOnFinished() {
        finish();
    }

    @Override
    public void onHideUIControls(){
        hideUIControls();
    }
}
