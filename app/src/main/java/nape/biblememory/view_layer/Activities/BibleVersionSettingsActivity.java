package nape.biblememory.view_layer.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Models.UserPreferencesModel;
import nape.biblememory.R;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.data_store.DataStore;

public class BibleVersionSettingsActivity extends AppCompatActivity {


    @BindView(R.id.version_esv_checkbox)CheckBox esvCB;
    @BindView(R.id.version_web_checkbox)CheckBox webCB;
    @BindView(R.id.version_kjv_checkbox)CheckBox kjvCB;
    private UserPreferences mPrefs;
    private UserPreferencesModel mPrefsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_version_settings);
        ButterKnife.bind(this);
        setTitle("Bible version settings");
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "BibleVersionSettings", null);
        mPrefs = new UserPreferences();
        mPrefsModel = new UserPreferencesModel();
        initCheckboxStates();
        initListeners();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        mPrefsModel.initAllData(getApplicationContext(), mPrefs);
        DataStore.getInstance().saveUserPrefs(mPrefsModel, getApplicationContext());
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_left);
    }

    @Override
    public void onStop(){
        super.onStop();
        mPrefsModel.initAllData(getApplicationContext(), mPrefs);
        DataStore.getInstance().saveUserPrefs(mPrefsModel, getApplicationContext());
    }

    private void initCheckboxStates() {
        switch(mPrefs.getSelectedVersion(getApplicationContext())){
            case "ESV":
                esvCB.setChecked(true);
                break;
            case "WEB":
                webCB.setChecked(true);
                break;
            case "KJV":
                kjvCB.setChecked(true);
        }
    }

    private void initListeners() {
        esvCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!esvCB.isChecked()){
                    esvCB.setChecked(true);
                    mPrefs.setSelectedVersion("ESV", getApplicationContext());
                }else {
                    webCB.setChecked(false);
                    kjvCB.setChecked(false);
                    mPrefs.setSelectedVersion("ESV", getApplicationContext());
                }
            }
        });

        webCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!webCB.isChecked()){
                    esvCB.setChecked(true);
                    mPrefs.setSelectedVersion("ESV", getApplicationContext());
                }else {
                    esvCB.setChecked(false);
                    kjvCB.setChecked(false);
                    mPrefs.setSelectedVersion("WEB", getApplicationContext());
                }
            }
        });

        kjvCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kjvCB.isChecked()){
                    esvCB.setChecked(true);
                    mPrefs.setSelectedVersion("ESV", getApplicationContext());
                }else {
                    esvCB.setChecked(false);
                    webCB.setChecked(false);
                    mPrefs.setSelectedVersion("KJV", getApplicationContext());
                }
            }
        });
    }
}
