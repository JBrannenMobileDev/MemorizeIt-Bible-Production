package nape.biblememory.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import nape.biblememory.Fragments.Dialogs.TimeSelectionDialogFragment;
import nape.biblememory.R;
import nape.biblememory.UserPreferences;

public class SettingsActivity extends AppCompatActivity implements TimeSelectionDialogFragment.TimeSelectedListener {

    private Switch launchQuizOnUnlock;
    private Switch onTheseDaysSwitch;
    private UserPreferences mPrefs;
    private TextView startTime;
    private TextView endTime;
    private TextView textViewToSet;
    private Date startDate;
    private Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        mPrefs = new UserPreferences();
        launchQuizOnUnlock = (Switch) findViewById(R.id.start_quiz_on_unlock);
        startTime = (TextView) findViewById(R.id.start_time_settings);
        endTime = (TextView) findViewById(R.id.end_time_settings);
        initializeSettings();
        initializeListeners();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_left);
    }

    private void initializeSettings() {
        startDate = new Date();
        startDate.setHours(9);
        startDate.setMinutes(0);
        endDate = new Date();
        endDate.setHours(17);
        endDate.setMinutes(0);

        if(mPrefs.isStartQuizWhenPhoneUnlock(getApplicationContext())){
            launchQuizOnUnlock.setChecked(true);
        }else{
            launchQuizOnUnlock.setChecked(false);
        }
    }

    private void initializeListeners() {
        launchQuizOnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrefs.setStartQuizWhenPhoneUnlocks(launchQuizOnUnlock.isChecked(), getApplicationContext());
            }
        });

        onTheseDaysSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPrefs.setShowQuizOnMonday()
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewToSet = startTime;
                launchTimePickerDialog();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewToSet = endTime;
                launchTimePickerDialog();
            }
        });
    }

    @Override
    public void onTimeSelected(String time, Date date) {
        if(textViewToSet == startTime){
            startDate = date;
        }
        if(textViewToSet == endTime){
            endDate = date;
        }
        if(startDate != null && endDate != null && startDate.getHours() < endDate.getHours()){
            textViewToSet.setText(time);
        }else if(startDate.getHours() == endDate.getHours() && startDate.getMinutes() < endDate.getMinutes()){
            textViewToSet.setText(time);
        }else{
            Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_LONG).show();
        }
    }

    private void launchTimePickerDialog(){
        FragmentManager fm = getSupportFragmentManager();
        TimeSelectionDialogFragment verseSelectedDialog = new TimeSelectionDialogFragment();
        verseSelectedDialog.show(fm, "TimePickerFragment");
    }
}
