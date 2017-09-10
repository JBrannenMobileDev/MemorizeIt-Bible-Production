package nape.biblememory.view_layer.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.data_layer.realm_db.RealmManager;
import nape.biblememory.view_layer.fragments.dialogs.ClearAppDataAlertDialog;
import nape.biblememory.R;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.data_layer.DataStore;

public class SettingsActivity extends AppCompatActivity implements ClearAppDataAlertDialog.YesSelected{

    @BindView(R.id.settings_quiz_settings_tv)TextView quizSettingsBt;
    @BindView(R.id.settings_privacy_policy)TextView privacyPolicyBt;
    @BindView(R.id.settings_sign_out)TextView signOutBt;
    @BindView(R.id.settings_bible_version_settings_tv)TextView bibleVerseionSettingsBt;
    @BindView(R.id.settings_clear_app_data_tv)TextView clearDataTv;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Activity thisActivity;
    private UserPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        thisActivity = this;
        mPrefs = new UserPreferences();
        setTitle("Settings");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Settings", null);
        initListeners();
        bibleVerseionSettingsBt.setText("Default bible version (" + mPrefs.getSelectedVersion(getApplicationContext()) + ")");
        Intent returnIntent = new Intent();
        setResult(2,returnIntent);
    }

    private void initListeners() {
        quizSettingsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAnalytics.logEvent("quiz_settings_settings_selected", null);
                startActivity(new Intent(getApplicationContext(), QuizSettingsActivity.class));
            }
        });

        privacyPolicyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAnalytics.logEvent("privacy_policy_settings_selected", null);
                startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
            }
        });

        bibleVerseionSettingsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAnalytics.logEvent("bible_version_settings_selected", null);
                startActivity(new Intent(getApplicationContext(), BibleVersionSettingsActivity.class));
            }
        });

        signOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut((FragmentActivity) thisActivity)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                signOut();
                            }
                        });
            }

        });

        clearDataTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ClearAppDataAlertDialog().show(getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @Override
    public void onClearAppDataSelected() {
        DataStore.getInstance().nukeAllData(getApplicationContext());
        signOut();
    }

    private void signOut(){
        RealmManager realmManager = new RealmManager();
        realmManager.nukeDb();
        mPrefs.setFirstTimeSignIn(true, getApplicationContext());
        startActivity(new Intent(SettingsActivity.this, BootActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        Intent returnIntent = new Intent();
        setResult(1,returnIntent);
        finish();
    }
}
