package nape.biblememory.view_layer.activities;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.fragments.dialogs.CloseSelectedTooManyTimesAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.FirstTimeUnlockDialog;
import nape.biblememory.view_layer.fragments.dialogs.VerseMemorizedAlertDialog;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.view_layer.fragments.interfaces.PhoneUnlockPresenter;
import nape.biblememory.view_layer.fragments.presenters.PhoneUnlockPresenterImp;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.fragments.interfaces.PhoneUnlockView;
import nape.biblememory.R;
import nape.biblememory.utils.DpConverterUtil;

import com.google.firebase.analytics.FirebaseAnalytics;


public class PhoneUnlockActivity extends AppCompatActivity implements PhoneUnlockView,
        VerseMemorizedAlertDialog.YesSelected, CloseSelectedTooManyTimesAlertDialog.YesSelected{
    private View mContentView;
    private TextView verse;
    private TextView verseLocation;
    private TextView pageTitle;
    private TextView checkAnswerText;
    private TextView hintText;
    private TextView quizReviewTitle;
    private TextView bibleVersionTv;
    private Button close_more_button;
    private FloatingActionButton checkAnswerFAB;
    private FloatingActionButton yesButton;
    private FloatingActionButton noButton;
    private FloatingActionButton hintButton;
    private FrameLayout hintLayout;
    private AdView mAdView;
    private LinearLayout verificationLayout;
    private UserPreferences mPrefs;

    private PhoneUnlockPresenter mPresenter;
    private FirstTimeUnlockDialog firstTimeDialog;

    private FirebaseAnalytics mFirebaseAnalytics;
    private Handler handler;
    private MyVerse myVerse;
    private boolean moreVerses;


    @Override
    protected void onResume() {
        super.onResume();
        hideUIControls();
        InitializeBannerAd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_unlock);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(this, "Phone_unlock_quiz", null);


        InitializeBannerAd();

        mContentView = findViewById(R.id.verse);
        firstTimeDialog = new FirstTimeUnlockDialog();
        verse = (TextView) findViewById(R.id.verse);
        verseLocation = (TextView) findViewById(R.id.verse_location);
        pageTitle = (TextView) findViewById(R.id.title_text);
        checkAnswerText = (TextView) findViewById(R.id.check_answer_textview);
        hintText = (TextView) findViewById(R.id.hint_textview);
        quizReviewTitle = (TextView) findViewById(R.id.quiz_review_title);
        verificationLayout = (LinearLayout) findViewById(R.id.verification_layout);
        close_more_button = (Button) findViewById(R.id.close_button);
        checkAnswerFAB = (FloatingActionButton) findViewById(R.id.check_answer_button_fab);
        yesButton = (FloatingActionButton) findViewById(R.id.yes_button_fab);
        noButton = (FloatingActionButton) findViewById(R.id.no_button_fab);
        hintButton = (FloatingActionButton) findViewById(R.id.hint_button_fab);
        hintLayout = (FrameLayout) findViewById(R.id.hint_fab_frame);
        bibleVersionTv = (TextView) findViewById(R.id.phone_unlock_bible_version_tv);
        mPrefs = new UserPreferences();
        moreVerses = getIntent().getBooleanExtra("moreVerses", false);
        mPresenter = new PhoneUnlockPresenterImp(this, getApplicationContext(), myVerse, moreVerses);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("quiz_yes_selected", null);
                mPresenter.onYesClicked();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("quiz_no_selected", null);
                mPresenter.onNoClicked();
            }
        });

        close_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("quiz_close_selected", null);
                mPresenter.onCloseClicked();
            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("quiz_hint_selected", null);
                mPresenter.onHintClicked();
            }
        });

        checkAnswerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handler != null) {
                    handler.removeMessages(0);
                }
                mFirebaseAnalytics.logEvent("quiz_check_answer_selected", null);
                mPresenter.onCheckAnswerClicked();
            }
        });

        if(getIntent().getStringExtra("from_review") != null){
            if(getIntent().getStringExtra("from_review").equals("from_review")){
                mPresenter.onRequestReviewData();
            }
        }
        mPresenter.onRequestData();

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
        bibleVersionTv.setText("(" + mPrefs.getSelectedVersion(getApplicationContext()) + ")");
    }

    @Override
    public void onFinishActivity() {
        finish();
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
    public void setHintButtonVisibility(int value, MyVerse scripture) {
        if(value == View.VISIBLE){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (!yesButton.isShown()) {
                        hintButton.show();
                        hintLayout.animate().translationY(DpConverterUtil.dpToPx(-100));
                        hintButton.animate().scaleX(1.15f);
                        hintButton.animate().scaleY(1.15f);
                        hintText.animate().scaleX(1.15f);
                        hintText.animate().scaleY(1.15f);
                        hintText.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                hintLayout.animate().translationY(0);
                                hintButton.animate().scaleX(1);
                                hintButton.animate().scaleY(1);
                                hintText.animate().scaleX(1);
                                hintText.animate().scaleY(1);
                            }
                        }, 200);
                    }
                }
            }, scripture.getVerse().length() * 120);

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
        if(myVerse != null) {
            bundle.putString("verseLocation", myVerse.getVerseLocation());
            bundle.putString("verse", myVerse.getVerse());
        }
        alert.setArguments(bundle);
        alert.show(getSupportFragmentManager(), null);
    }

    @Override
    public void setReviewTitleVisibility(int visible) {
        quizReviewTitle.setVisibility(visible);
    }

    @Override
    public void setReviewTitleText(String s) {
        quizReviewTitle.setText(s);
    }

    @Override
    public void setReviewTitleColor(int colorNoText) {
        quizReviewTitle.setTextColor(getResources().getColor(colorNoText));
    }

    @Override
    public void setTitlebarTextColor(int color) {
        pageTitle.setTextColor(color);
    }

    @Override
    public void showCloseSelectedTooManyTimesDialog() {
        new CloseSelectedTooManyTimesAlertDialog().show(getSupportFragmentManager(), null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
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
