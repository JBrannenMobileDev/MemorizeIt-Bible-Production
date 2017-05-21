package nape.biblememory.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import nape.biblememory.R;
import nape.biblememory.UserPreferences;

public class SettingsActivity extends AppCompatActivity {

    private Switch launchQuizOnUnlock;
    private UserPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        mPrefs = new UserPreferences();
        launchQuizOnUnlock = (Switch) findViewById(R.id.start_quiz_on_unlock);

        if(mPrefs.isStartQuizWhenPhoneUnlock(getApplicationContext())){
            launchQuizOnUnlock.setChecked(true);
        }else{
            launchQuizOnUnlock.setChecked(false);
        }

        launchQuizOnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrefs.setStartQuizWhenPhoneUnlocks(launchQuizOnUnlock.isChecked(), getApplicationContext());
            }
        });
    }
}
