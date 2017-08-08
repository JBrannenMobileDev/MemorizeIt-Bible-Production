package nape.biblememory.Activities;

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
import nape.biblememory.R;
import nape.biblememory.UserPreferences;
import nape.biblememory.data_store.Sqlite.BibleMemoryDbHelper;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.settings_quiz_settings_tv)TextView quizSettingsBt;
    @BindView(R.id.settings_privacy_policy)TextView privacyPolicyBt;
    @BindView(R.id.settings_sign_out)TextView signOutBt;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Activity thisActivity;
    private UserPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        thisActivity = this;
        mPrefs = new UserPreferences();
        setTitle("Settings");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Settings", null);
        initListeners();
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

        signOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut((FragmentActivity) thisActivity)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                mPrefs.setFirstTimeSignIn(true, getApplicationContext());
//                                BibleMemoryDbHelper.deleteLocalDb(getApplicationContext());
                                startActivity(new Intent(SettingsActivity.this, BootActivity.class));
                                finish();
                            }
                        });
            }

        });
    }
}
