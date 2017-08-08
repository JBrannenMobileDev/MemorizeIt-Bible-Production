package nape.biblememory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.R;
import nape.biblememory.UserPreferences;
import nape.biblememory.data_store.DataStore;
import nape.biblememory.data_store.Sqlite.BibleMemoryDbHelper;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

public class BootActivity extends Activity {
    private static final int RC_SIGN_IN = 2884;
    private UserPreferences mPrefs;

    private FirebaseAuth auth;

    @BindView(R.id.boot_button_layout)LinearLayout buttonLayout;
    @BindView(R.id.boot_sign_in_bt)Button signInBt;
    @BindView(R.id.boot_privacy_policy)TextView privacyPolicyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        hideSystemUI();
        ButterKnife.bind(this);
        mPrefs = new UserPreferences();
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Settings", null);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            mPrefs.setUserId(auth.getCurrentUser().getUid(), getApplicationContext());
            mPrefs.setUserEmail(auth.getCurrentUser().getEmail(), getApplicationContext());
            if(mPrefs.isRebuildError(getApplicationContext())){
                VerseOperations vOps = new VerseOperations(getApplicationContext());
                vOps.nukeDb();
                mPrefs.setUserId(auth.getCurrentUser().getUid(), getApplicationContext());
                DataStore.getInstance().rebuildLocalDb(getApplicationContext());
            }else {
                mPrefs.setUserId(auth.getCurrentUser().getUid(), getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            finish();
        } else {
            if(!mPrefs.isFirstTimeLogind(getApplicationContext())){
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(
                                        Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                                .build(),
                        RC_SIGN_IN);
            }
            buttonLayout.setVisibility(View.VISIBLE);
        }

        signInBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(
                                        Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        privacyPolicyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BootActivity.this, BootPrivacyPolicyActivity.class));
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        hideSystemUI();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = SYSTEM_UI_FLAG_IMMERSIVE_STICKY | SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                mPrefs.setFirstTimeSignIn(false, getApplicationContext());
                mPrefs.setUserEmail(auth.getCurrentUser().getEmail(), getApplicationContext());
                if(!mPrefs.getUserId(getApplicationContext()).equals(auth.getCurrentUser().getUid()) || mPrefs.isRebuildError(getApplicationContext())){
                    VerseOperations vOps = new VerseOperations(getApplicationContext());
                    vOps.nukeDb();
                    mPrefs.setUserId(auth.getCurrentUser().getUid(), getApplicationContext());
                    DataStore.getInstance().rebuildLocalDb(getApplicationContext());
                }else {
                    mPrefs.setUserId(auth.getCurrentUser().getUid(), getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    Toast.makeText(this, "Sign in failed",Toast.LENGTH_SHORT).show();
                    buttonLayout.setVisibility(View.VISIBLE);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No network",Toast.LENGTH_SHORT).show();
                    buttonLayout.setVisibility(View.VISIBLE);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Unknown error",Toast.LENGTH_SHORT).show();
                    buttonLayout.setVisibility(View.VISIBLE);
                    return;
                }
            }
        }
    }
}
