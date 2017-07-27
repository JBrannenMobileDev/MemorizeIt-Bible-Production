package nape.biblememory.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Fragments.Dialogs.TimeSelectionDialogFragment;
import nape.biblememory.R;
import nape.biblememory.UserPreferences;

public class SettingsActivity extends AppCompatActivity implements TimeSelectionDialogFragment.TimeSelectedListener {

    @BindView(R.id.start_quiz_on_unlock) Switch launchQuizOnUnlock;
    @BindView(R.id.launch_quiz_monday) Switch mondaySwitch;
    @BindView(R.id.between_these_times_monday) Switch mondayTimeSwitch;
    @BindView(R.id.start_time_settings_monday) TextView mondayStartTimeTv;
    @BindView(R.id.end_time_settings_monday) TextView mondayEndTimeTv;
    @BindView(R.id.mondayTv2) TextView mondayTv2;
    @BindView(R.id.mondayTv3) TextView mondayTv3;
    @BindView(R.id.mondayTv4) TextView mondayTv4;
    @BindView(R.id.launch_quiz_tuesday) Switch tuesdaySwitch;
    @BindView(R.id.between_these_times_tuesday) Switch tuesdayTimeSwitch;
    @BindView(R.id.start_time_settings_tuesday) TextView tuesdayStartTimeTv;
    @BindView(R.id.end_time_settings_tuesday) TextView tuesdayEndTimeTv;
    @BindView(R.id.tuesdayTv2) TextView tuesdayTv2;
    @BindView(R.id.tuesdayTv3) TextView tuesdayTv3;
    @BindView(R.id.tuesdayTv4) TextView tuesdayTv4;
    @BindView(R.id.launch_quiz_wednesday) Switch wednesdaySwitch;
    @BindView(R.id.between_these_times_wednesday) Switch wednesdayTimeSwitch;
    @BindView(R.id.start_time_settings_wednesday) TextView wednesdayStartTimeTv;
    @BindView(R.id.end_time_settings_wednesday) TextView wednesdayEndTimeTv;
    @BindView(R.id.wednesdayTv2) TextView wednesdayTv2;
    @BindView(R.id.wednesdayTv3) TextView wednesdayTv3;
    @BindView(R.id.wednesdayTv4) TextView wednesdayTv4;
    @BindView(R.id.launch_quiz_thursday) Switch thursdaySwitch;
    @BindView(R.id.between_these_times_thursday) Switch thursdayTimeSwitch;
    @BindView(R.id.start_time_settings_thursday) TextView thursdayStartTimeTv;
    @BindView(R.id.end_time_settings_thursday) TextView thursdayEndTimeTv;
    @BindView(R.id.thursdayTv2) TextView thursdayTv2;
    @BindView(R.id.thursdayTv3) TextView thursdayTv3;
    @BindView(R.id.thursdayTv4) TextView thursdayTv4;
    @BindView(R.id.launch_quiz_friday) Switch fridaySwitch;
    @BindView(R.id.between_these_times_friday) Switch fridayTimeSwitch;
    @BindView(R.id.start_time_settings_friday) TextView fridayStartTimeTv;
    @BindView(R.id.end_time_settings_friday) TextView fridayEndTimeTv;
    @BindView(R.id.fridayTv2) TextView fridayTv2;
    @BindView(R.id.fridayTv3) TextView fridayTv3;
    @BindView(R.id.fridayTv4) TextView fridayTv4;
    @BindView(R.id.launch_quiz_saturday) Switch saturdaySwitch;
    @BindView(R.id.between_these_times_saturday) Switch saturdayTimeSwitch;
    @BindView(R.id.start_time_settings_saturday) TextView saturdayStartTimeTv;
    @BindView(R.id.end_time_settings_saturday) TextView saturdayEndTimeTv;
    @BindView(R.id.saturdayTv2) TextView saturdayTv2;
    @BindView(R.id.saturdayTv3) TextView saturdayTv3;
    @BindView(R.id.saturdayTv4) TextView saturdayTv4;
    @BindView(R.id.launch_quiz_sunday) Switch sundaySwitch;
    @BindView(R.id.between_these_times_sunday) Switch sundayTimeSwitch;
    @BindView(R.id.start_time_settings_sunday) TextView sundayStartTimeTv;
    @BindView(R.id.end_time_settings_sunday) TextView sundayEndTimeTv;
    @BindView(R.id.sundayTv2) TextView sundayTv2;
    @BindView(R.id.sundayTv3) TextView sundayTv3;
    @BindView(R.id.sundayTv4) TextView sundayTv4;
    @BindView(R.id.settings_hide_layout)LinearLayout hideLayout;


    private UserPreferences mPrefs;
    private TextView textViewToSet;
    private Date startDateMonday;
    private Date endDateMonday;
    private Date startDateTuesday;
    private Date endDateTuesday;
    private Date startDateWednesday;
    private Date endDateWednesday;
    private Date startDateThursday;
    private Date endDateThursday;
    private Date startDateFriday;
    private Date endDateFriday;
    private Date startDateSaturday;
    private Date endDateSaturday;
    private Date startDateSunday;
    private Date endDateSunday;
    private boolean startDateSelected;
    private String selectedDayOfWeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        mPrefs = new UserPreferences();
        ButterKnife.bind(this);
        initializeSettings();
        initializeListeners();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_left);
    }

    private void initializeSettings() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        Calendar calendar = Calendar.getInstance();

        /**
         * Monday
         */
        if(mPrefs.showQuizOnMonday(this)){
            mondaySwitch.setChecked(false);
            mondayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
            disableMondayTimes();
            mondayTimeSwitch.setEnabled(false);
            hideMondayTimeLayout();
        }else{
            mondaySwitch.setChecked(true);
            mondayTv2.setTextColor(getResources().getColor(R.color.bgColor));
            mondayTimeSwitch.setEnabled(true);
            showMondayTimeLayout();
        }
        if(mPrefs.showQuizTimeOnMondayChecked(this)){
            mondayTimeSwitch.setChecked(true);
            if(!mPrefs.showQuizOnMonday(this)) {
                enableMondayTimes();
            }else{
                disableMondayTimes();
                mondayTimeSwitch.setEnabled(false);
            }
        }else{
            mondayTimeSwitch.setChecked(false);
            disableMondayTimes();
        }

        if(mPrefs.getSettingsStartTimeMonday(this) == 0) {
            startDateMonday = new Date();
            startDateMonday.setHours(9);
            startDateMonday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsStartTimeMonday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            mondayStartTimeTv.setText(formatter.format(temp.getTime()));
        }
        if(mPrefs.getSettingsEndTimeMonday(this) == 0) {
            endDateMonday = new Date();
            endDateMonday.setHours(17);
            endDateMonday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsEndTimeMonday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            mondayEndTimeTv.setText(formatter.format(temp.getTime()));
        }
        /**
         * end monday
         */




        /**
         * Tuesday
         */
        if(mPrefs.showQuizOnTuesday(this)){
            tuesdaySwitch.setChecked(false);
            tuesdayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
            disableTuesdayTimes();
            tuesdayTimeSwitch.setEnabled(false);
            hideTuesdayTimeLayout();
        }else{
            tuesdaySwitch.setChecked(true);
            tuesdayTv2.setTextColor(getResources().getColor(R.color.bgColor));
            tuesdayTimeSwitch.setEnabled(true);
            showTuesdayTimeLayout();
        }
        if(mPrefs.showQuizTimeOnTuesdayChecked(this)){
            tuesdayTimeSwitch.setChecked(true);
            if(!mPrefs.showQuizOnTuesday(this)) {
                enableTuesdayTimes();
            }else{
                disableTuesdayTimes();
                tuesdayTimeSwitch.setEnabled(false);
            }
        }else{
            tuesdayTimeSwitch.setChecked(false);
            disableTuesdayTimes();
        }

        if(mPrefs.getSettingsStartTimeTuesday(this) == 0) {
            startDateTuesday = new Date();
            startDateTuesday.setHours(9);
            startDateTuesday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsStartTimeTuesday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            tuesdayStartTimeTv.setText(formatter.format(temp.getTime()));
        }
        if(mPrefs.getSettingsEndTimeTuesday(this) == 0) {
            endDateTuesday = new Date();
            endDateTuesday.setHours(17);
            endDateTuesday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsEndTimeTuesday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            tuesdayEndTimeTv.setText(formatter.format(temp.getTime()));
        }
        /**
         * end tuesday
         */

        /**
         * Wednesday
         */
        if(mPrefs.showQuizOnWednesday(this)){
            wednesdaySwitch.setChecked(false);
            wednesdayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
            disableWednesdayTimes();
            wednesdayTimeSwitch.setEnabled(false);
            hideWednesdayTimeLayout();
        }else{
            wednesdaySwitch.setChecked(true);
            wednesdayTv2.setTextColor(getResources().getColor(R.color.bgColor));
            wednesdayTimeSwitch.setEnabled(true);
            showWednesdayTimeLayout();
        }
        if(mPrefs.showQuizTimeOnWednesdayChecked(this)){
            wednesdayTimeSwitch.setChecked(true);
            if(!mPrefs.showQuizOnWednesday(this)) {
                enableWednesdayTimes();
            }else{
                disableWednesdayTimes();
                wednesdayTimeSwitch.setEnabled(false);
            }
        }else{
            wednesdayTimeSwitch.setChecked(false);
            disableWednesdayTimes();
        }

        if(mPrefs.getSettingsStartTimeWednesday(this) == 0) {
            startDateWednesday = new Date();
            startDateWednesday.setHours(9);
            startDateWednesday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsStartTimeWednesday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            wednesdayStartTimeTv.setText(formatter.format(temp.getTime()));
        }
        if(mPrefs.getSettingsEndTimeWednesday(this) == 0) {
            endDateWednesday = new Date();
            endDateWednesday.setHours(17);
            endDateWednesday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsEndTimeWednesday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            wednesdayEndTimeTv.setText(formatter.format(temp.getTime()));
        }
        /**
         * end wednesday
         */

        /**
         * Thursday
         */
        if(mPrefs.showQuizOnThursday(this)){
            thursdaySwitch.setChecked(false);
            thursdayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
            disableThursdayTimes();
            thursdayTimeSwitch.setEnabled(false);
            hideThursdayTimeLayout();
        }else{
            thursdaySwitch.setChecked(true);
            thursdayTv2.setTextColor(getResources().getColor(R.color.bgColor));
            thursdayTimeSwitch.setEnabled(true);
            showThursdayTimeLayout();
        }
        if(mPrefs.showQuizTimeOnThursdayChecked(this)){
            thursdayTimeSwitch.setChecked(true);
            if(!mPrefs.showQuizOnThursday(this)) {
                enableThursdayTimes();
            }else{
                disableThursdayTimes();
                thursdayTimeSwitch.setEnabled(false);
            }
        }else{
            thursdayTimeSwitch.setChecked(false);
            disableThursdayTimes();
        }

        if(mPrefs.getSettingsStartTimeThursday(this) == 0) {
            startDateThursday = new Date();
            startDateThursday.setHours(9);
            startDateThursday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsStartTimeThursday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            thursdayStartTimeTv.setText(formatter.format(temp.getTime()));
        }
        if(mPrefs.getSettingsEndTimeThursday(this) == 0) {
            endDateThursday = new Date();
            endDateThursday.setHours(17);
            endDateThursday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsEndTimeThursday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            thursdayEndTimeTv.setText(formatter.format(temp.getTime()));
        }
        /**
         * end thursday
         */


        /**
         * Friday
         */
        if(mPrefs.showQuizOnFriday(this)){
            fridaySwitch.setChecked(false);
            fridayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
            disableFridayTimes();
            fridayTimeSwitch.setEnabled(false);
            hideFridayTimeLayout();
        }else{
            fridaySwitch.setChecked(true);
            fridayTv2.setTextColor(getResources().getColor(R.color.bgColor));
            fridayTimeSwitch.setEnabled(true);
            showFridayTimeLayout();
        }
        if(mPrefs.showQuizTimeOnFridayChecked(this)){
            fridayTimeSwitch.setChecked(true);
            if(!mPrefs.showQuizOnFriday(this)) {
                enableFridayTimes();
            }else{
                disableFridayTimes();
                fridayTimeSwitch.setEnabled(false);
            }
        }else{
            fridayTimeSwitch.setChecked(false);
            disableFridayTimes();
        }

        if(mPrefs.getSettingsStartTimeFriday(this) == 0) {
            startDateFriday = new Date();
            startDateFriday.setHours(9);
            startDateFriday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsStartTimeFriday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            fridayStartTimeTv.setText(formatter.format(temp.getTime()));
        }
        if(mPrefs.getSettingsEndTimeFriday(this) == 0) {
            endDateFriday = new Date();
            endDateFriday.setHours(17);
            endDateFriday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsEndTimeFriday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            fridayEndTimeTv.setText(formatter.format(temp.getTime()));
        }
        /**
         * end friday
         */


        /**
         * Saturday
         */
        if(mPrefs.showQuizOnSaturday(this)){
            saturdaySwitch.setChecked(false);
            saturdayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
            disableSaturdayTimes();
            saturdayTimeSwitch.setEnabled(false);
            hideSaturdayTimeLayout();
        }else{
            saturdaySwitch.setChecked(true);
            saturdayTv2.setTextColor(getResources().getColor(R.color.bgColor));
            saturdayTimeSwitch.setEnabled(true);
            showSaturdayTimeLayout();
        }
        if(mPrefs.showQuizTimeOnSaturdayChecked(this)){
            saturdayTimeSwitch.setChecked(true);
            if(!mPrefs.showQuizOnSaturday(this)) {
                enableSaturdayTimes();
            }else{
                disableSaturdayTimes();
                saturdayTimeSwitch.setEnabled(false);
            }
        }else{
            saturdayTimeSwitch.setChecked(false);
            disableSaturdayTimes();
        }

        if(mPrefs.getSettingsStartTimeSaturday(this) == 0) {
            startDateSaturday = new Date();
            startDateSaturday.setHours(9);
            startDateSaturday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsStartTimeSaturday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            saturdayStartTimeTv.setText(formatter.format(temp.getTime()));
        }
        if(mPrefs.getSettingsEndTimeSaturday(this) == 0) {
            endDateSaturday = new Date();
            endDateSaturday.setHours(17);
            endDateSaturday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsEndTimeSaturday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            saturdayEndTimeTv.setText(formatter.format(temp.getTime()));
        }
        /**
         * end saturday
         */


        /**
         * Sunday
         */
        if(mPrefs.showQuizOnSunday(this)){
            sundaySwitch.setChecked(false);
            sundayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
            disableSundayTimes();
            sundayTimeSwitch.setEnabled(false);
            hideSundayTimeLayout();
        }else{
            sundaySwitch.setChecked(true);
            sundayTv2.setTextColor(getResources().getColor(R.color.bgColor));
            sundayTimeSwitch.setEnabled(true);
            showSundayTimeLayout();
        }
        if(mPrefs.showQuizTimeOnSundayChecked(this)){
            sundayTimeSwitch.setChecked(true);
            if(!mPrefs.showQuizOnSunday(this)) {
                enableSundayTimes();
            }else{
                disableSundayTimes();
                sundayTimeSwitch.setEnabled(false);
            }
        }else{
            sundayTimeSwitch.setChecked(false);
            disableSundayTimes();
        }

        if(mPrefs.getSettingsStartTimeSunday(this) == 0) {
            startDateSunday = new Date();
            startDateSunday.setHours(9);
            startDateSunday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsStartTimeSunday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            sundayStartTimeTv.setText(formatter.format(temp.getTime()));
        }
        if(mPrefs.getSettingsEndTimeSunday(this) == 0) {
            endDateSunday = new Date();
            endDateSunday.setHours(17);
            endDateSunday.setMinutes(0);
        }else{
            Calendar temp = Calendar.getInstance();
            temp.setTime(new Date(mPrefs.getSettingsEndTimeSunday(this)));
            calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
            sundayEndTimeTv.setText(formatter.format(temp.getTime()));
        }
        /**
         * end sunday
         */


        if(mPrefs.isStartQuizWhenPhoneUnlock(getApplicationContext())){
            launchQuizOnUnlock.setChecked(true);
            hideLayout.setVisibility(View.VISIBLE);
        }else{
            launchQuizOnUnlock.setChecked(false);
            hideLayout.setVisibility(View.GONE);
        }
    }

    private void initializeListeners() {
        launchQuizOnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(launchQuizOnUnlock.isChecked()){
                    hideLayout.setVisibility(View.VISIBLE);
                }else{
                    hideLayout.setVisibility(View.GONE);
                }
                mPrefs.setStartQuizWhenPhoneUnlocks(launchQuizOnUnlock.isChecked(), getApplicationContext());
            }
        });

        /**
         * monday listeners
         */
        mondayStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = true;
                selectedDayOfWeek = "monday";
                textViewToSet = mondayStartTimeTv;
                launchTimePickerDialog();
            }
        });

        mondayEndTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = false;
                selectedDayOfWeek = "monday";
                textViewToSet = mondayEndTimeTv;
                launchTimePickerDialog();
            }
        });

        mondaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mondaySwitch.isChecked()){
                    mPrefs.setShowQuizOnMonday(false, getApplicationContext());
                    mondayTv2.setTextColor(getResources().getColor(R.color.bgColor));
                    mondayTimeSwitch.setEnabled(true);
                    enableMondayTimes();
                    showMondayTimeLayout();
                }else{
                    mPrefs.setShowQuizOnMonday(true, getApplicationContext());
                    mondayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
                    mondayTimeSwitch.setEnabled(false);
                    disableMondayTimes();
                    hideMondayTimeLayout();
                }
            }
        });

        mondayTimeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mondayTimeSwitch.isChecked()){
                    mPrefs.setShowQuizTimeOnMondayChecked(true, getApplicationContext());
                    enableMondayTimes();
                }else{
                    mPrefs.setShowQuizTimeOnMondayChecked(false, getApplicationContext());
                    disableMondayTimes();
                }
            }
        });
        /**
         * end monday listeners
         */

        /**
         * tuesday listeners
         */
        tuesdayStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = true;
                selectedDayOfWeek = "tuesday";
                textViewToSet = tuesdayStartTimeTv;
                launchTimePickerDialog();
            }
        });

        tuesdayEndTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = false;
                selectedDayOfWeek = "tuesday";
                textViewToSet = tuesdayEndTimeTv;
                launchTimePickerDialog();
            }
        });

        tuesdaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tuesdaySwitch.isChecked()){
                    mPrefs.setShowQuizOnTuesday(false, getApplicationContext());
                    tuesdayTv2.setTextColor(getResources().getColor(R.color.bgColor));
                    tuesdayTimeSwitch.setEnabled(true);
                    enableTuesdayTimes();
                    showTuesdayTimeLayout();
                }else{
                    mPrefs.setShowQuizOnTuesday(true, getApplicationContext());
                    tuesdayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
                    tuesdayTimeSwitch.setEnabled(false);
                    disableTuesdayTimes();
                    hideTuesdayTimeLayout();
                }
            }
        });

        tuesdayTimeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tuesdayTimeSwitch.isChecked()){
                    mPrefs.setShowQuizTimeOnTuesdayChecked(true, getApplicationContext());
                    enableTuesdayTimes();
                }else{
                    mPrefs.setShowQuizTimeOnTuesdayChecked(false, getApplicationContext());
                    disableTuesdayTimes();
                }
            }
        });
        /**
         * end tuesday listeners
         */


        /**
         * wednesday listeners
         */
        wednesdayStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = true;
                selectedDayOfWeek = "wednesday";
                textViewToSet = wednesdayStartTimeTv;
                launchTimePickerDialog();
            }
        });

        wednesdayEndTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = false;
                selectedDayOfWeek = "wednesday";
                textViewToSet = wednesdayEndTimeTv;
                launchTimePickerDialog();
            }
        });

        wednesdaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wednesdaySwitch.isChecked()){
                    mPrefs.setShowQuizOnWednesday(false, getApplicationContext());
                    wednesdayTv2.setTextColor(getResources().getColor(R.color.bgColor));
                    wednesdayTimeSwitch.setEnabled(true);
                    enableWednesdayTimes();
                    showWednesdayTimeLayout();
                }else{
                    mPrefs.setShowQuizOnWednesday(true, getApplicationContext());
                    wednesdayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
                    wednesdayTimeSwitch.setEnabled(false);
                    disableWednesdayTimes();
                    hideWednesdayTimeLayout();
                }
            }
        });

        wednesdayTimeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wednesdayTimeSwitch.isChecked()){
                    mPrefs.setShowQuizTimeOnWednesdayChecked(true, getApplicationContext());
                    enableWednesdayTimes();
                }else{
                    mPrefs.setShowQuizTimeOnWednesdayChecked(false, getApplicationContext());
                    disableWednesdayTimes();
                }
            }
        });
        /**
         * end wednesday listeners
         */


        /**
         * thursday listeners
         */
        thursdayStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = true;
                selectedDayOfWeek = "thursday";
                textViewToSet = thursdayStartTimeTv;
                launchTimePickerDialog();
            }
        });

        thursdayEndTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = false;
                selectedDayOfWeek = "thursday";
                textViewToSet = thursdayEndTimeTv;
                launchTimePickerDialog();
            }
        });

        thursdaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thursdaySwitch.isChecked()){
                    mPrefs.setShowQuizOnThursday(false, getApplicationContext());
                    thursdayTv2.setTextColor(getResources().getColor(R.color.bgColor));
                    thursdayTimeSwitch.setEnabled(true);
                    enableThursdayTimes();
                    showThursdayTimeLayout();
                }else{
                    mPrefs.setShowQuizOnThursday(true, getApplicationContext());
                    thursdayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
                    thursdayTimeSwitch.setEnabled(false);
                    disableThursdayTimes();
                    hideThursdayTimeLayout();
                }
            }
        });

        thursdayTimeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thursdayTimeSwitch.isChecked()){
                    mPrefs.setShowQuizTimeOnThursdayChecked(true, getApplicationContext());
                    enableThursdayTimes();
                }else{
                    mPrefs.setShowQuizTimeOnThursdayChecked(false, getApplicationContext());
                    disableThursdayTimes();
                }
            }
        });
        /**
         * end thursday listeners
         */


        /**
         * friday listeners
         */
        fridayStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = true;
                selectedDayOfWeek = "friday";
                textViewToSet = fridayStartTimeTv;
                launchTimePickerDialog();
            }
        });

        fridayEndTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = false;
                selectedDayOfWeek = "friday";
                textViewToSet = fridayEndTimeTv;
                launchTimePickerDialog();
            }
        });

        fridaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fridaySwitch.isChecked()){
                    mPrefs.setShowQuizOnFriday(false, getApplicationContext());
                    fridayTv2.setTextColor(getResources().getColor(R.color.bgColor));
                    fridayTimeSwitch.setEnabled(true);
                    enableFridayTimes();
                    showFridayTimeLayout();
                }else{
                    mPrefs.setShowQuizOnFriday(true, getApplicationContext());
                    fridayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
                    fridayTimeSwitch.setEnabled(false);
                    disableFridayTimes();
                    hideFridayTimeLayout();
                }
            }
        });

        fridayTimeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fridayTimeSwitch.isChecked()){
                    mPrefs.setShowQuizTimeOnFridayChecked(true, getApplicationContext());
                    enableFridayTimes();
                }else{
                    mPrefs.setShowQuizTimeOnFridayChecked(false, getApplicationContext());
                    disableFridayTimes();
                }
            }
        });
        /**
         * end friday listeners
         */


        /**
         * saturday listeners
         */
        saturdayStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = true;
                selectedDayOfWeek = "saturday";
                textViewToSet = saturdayStartTimeTv;
                launchTimePickerDialog();
            }
        });

        saturdayEndTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = false;
                selectedDayOfWeek = "saturday";
                textViewToSet = saturdayEndTimeTv;
                launchTimePickerDialog();
            }
        });

        saturdaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saturdaySwitch.isChecked()){
                    mPrefs.setShowQuizOnSaturday(false, getApplicationContext());
                    saturdayTv2.setTextColor(getResources().getColor(R.color.bgColor));
                    saturdayTimeSwitch.setEnabled(true);
                    enableSaturdayTimes();
                    showSaturdayTimeLayout();
                }else{
                    mPrefs.setShowQuizOnSaturday(true, getApplicationContext());
                    saturdayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
                    saturdayTimeSwitch.setEnabled(false);
                    disableSaturdayTimes();
                    hideSaturdayTimeLayout();
                }
            }
        });

        saturdayTimeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saturdayTimeSwitch.isChecked()){
                    mPrefs.setShowQuizTimeOnSaturdayChecked(true, getApplicationContext());
                    enableSaturdayTimes();
                }else{
                    mPrefs.setShowQuizTimeOnSaturdayChecked(false, getApplicationContext());
                    disableSaturdayTimes();
                }
            }
        });
        /**
         * end saturday listeners
         */


        /**
         * sunday listeners
         */
        sundayStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = true;
                selectedDayOfWeek = "sunday";
                textViewToSet = sundayStartTimeTv;
                launchTimePickerDialog();
            }
        });

        sundayEndTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = false;
                selectedDayOfWeek = "sunday";
                textViewToSet = sundayEndTimeTv;
                launchTimePickerDialog();
            }
        });

        sundaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sundaySwitch.isChecked()){
                    mPrefs.setShowQuizOnSunday(false, getApplicationContext());
                    sundayTv2.setTextColor(getResources().getColor(R.color.bgColor));
                    sundayTimeSwitch.setEnabled(true);
                    enableSundayTimes();
                    showSundayTimeLayout();
                }else{
                    mPrefs.setShowQuizOnSunday(true, getApplicationContext());
                    sundayTv2.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
                    sundayTimeSwitch.setEnabled(false);
                    disableSundayTimes();
                    hideSundayTimeLayout();
                }
            }
        });

        sundayTimeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sundayTimeSwitch.isChecked()){
                    mPrefs.setShowQuizTimeOnSundayChecked(true, getApplicationContext());
                    enableSundayTimes();
                }else{
                    mPrefs.setShowQuizTimeOnSundayChecked(false, getApplicationContext());
                    disableSundayTimes();
                }
            }
        });
        /**
         * end sunday listeners
         */
    }

    private void hideMondayTimeLayout(){
        mondayTv2.setVisibility(View.GONE);
        mondayTv3.setVisibility(View.GONE);
        mondayTv4.setVisibility(View.GONE);
        mondayStartTimeTv.setVisibility(View.GONE);
        mondayEndTimeTv.setVisibility(View.GONE);
        mondayTimeSwitch.setVisibility(View.GONE);
    }

    private void showMondayTimeLayout(){
        mondayTv2.setVisibility(View.VISIBLE);
        mondayTv3.setVisibility(View.VISIBLE);
        mondayTv4.setVisibility(View.VISIBLE);
        mondayStartTimeTv.setVisibility(View.VISIBLE);
        mondayEndTimeTv.setVisibility(View.VISIBLE);
        mondayTimeSwitch.setVisibility(View.VISIBLE);
    }

    private void hideTuesdayTimeLayout(){
        tuesdayTv2.setVisibility(View.GONE);
        tuesdayTv3.setVisibility(View.GONE);
        tuesdayTv4.setVisibility(View.GONE);
        tuesdayStartTimeTv.setVisibility(View.GONE);
        tuesdayEndTimeTv.setVisibility(View.GONE);
        tuesdayTimeSwitch.setVisibility(View.GONE);
    }

    private void showTuesdayTimeLayout(){
        tuesdayTv2.setVisibility(View.VISIBLE);
        tuesdayTv3.setVisibility(View.VISIBLE);
        tuesdayTv4.setVisibility(View.VISIBLE);
        tuesdayStartTimeTv.setVisibility(View.VISIBLE);
        tuesdayEndTimeTv.setVisibility(View.VISIBLE);
        tuesdayTimeSwitch.setVisibility(View.VISIBLE);
    }

    private void hideWednesdayTimeLayout(){
        wednesdayTv2.setVisibility(View.GONE);
        wednesdayTv3.setVisibility(View.GONE);
        wednesdayTv4.setVisibility(View.GONE);
        wednesdayStartTimeTv.setVisibility(View.GONE);
        wednesdayEndTimeTv.setVisibility(View.GONE);
        wednesdayTimeSwitch.setVisibility(View.GONE);
    }

    private void showWednesdayTimeLayout(){
        wednesdayTv2.setVisibility(View.VISIBLE);
        wednesdayTv3.setVisibility(View.VISIBLE);
        wednesdayTv4.setVisibility(View.VISIBLE);
        wednesdayStartTimeTv.setVisibility(View.VISIBLE);
        wednesdayEndTimeTv.setVisibility(View.VISIBLE);
        wednesdayTimeSwitch.setVisibility(View.VISIBLE);
    }

    private void hideThursdayTimeLayout(){
        thursdayTv2.setVisibility(View.GONE);
        thursdayTv3.setVisibility(View.GONE);
        thursdayTv4.setVisibility(View.GONE);
        thursdayStartTimeTv.setVisibility(View.GONE);
        thursdayEndTimeTv.setVisibility(View.GONE);
        thursdayTimeSwitch.setVisibility(View.GONE);
    }

    private void showThursdayTimeLayout(){
        thursdayTv2.setVisibility(View.VISIBLE);
        thursdayTv3.setVisibility(View.VISIBLE);
        thursdayTv4.setVisibility(View.VISIBLE);
        thursdayStartTimeTv.setVisibility(View.VISIBLE);
        thursdayEndTimeTv.setVisibility(View.VISIBLE);
        thursdayTimeSwitch.setVisibility(View.VISIBLE);
    }

    private void hideFridayTimeLayout(){
        fridayTv2.setVisibility(View.GONE);
        fridayTv3.setVisibility(View.GONE);
        fridayTv4.setVisibility(View.GONE);
        fridayStartTimeTv.setVisibility(View.GONE);
        fridayEndTimeTv.setVisibility(View.GONE);
        fridayTimeSwitch.setVisibility(View.GONE);
    }

    private void showFridayTimeLayout(){
        fridayTv2.setVisibility(View.VISIBLE);
        fridayTv3.setVisibility(View.VISIBLE);
        fridayTv4.setVisibility(View.VISIBLE);
        fridayStartTimeTv.setVisibility(View.VISIBLE);
        fridayEndTimeTv.setVisibility(View.VISIBLE);
        fridayTimeSwitch.setVisibility(View.VISIBLE);
    }

    private void hideSaturdayTimeLayout(){
        saturdayTv2.setVisibility(View.GONE);
        saturdayTv3.setVisibility(View.GONE);
        saturdayTv4.setVisibility(View.GONE);
        saturdayStartTimeTv.setVisibility(View.GONE);
        saturdayEndTimeTv.setVisibility(View.GONE);
        saturdayTimeSwitch.setVisibility(View.GONE);
    }

    private void showSaturdayTimeLayout(){
        saturdayTv2.setVisibility(View.VISIBLE);
        saturdayTv3.setVisibility(View.VISIBLE);
        saturdayTv4.setVisibility(View.VISIBLE);
        saturdayStartTimeTv.setVisibility(View.VISIBLE);
        saturdayEndTimeTv.setVisibility(View.VISIBLE);
        saturdayTimeSwitch.setVisibility(View.VISIBLE);
    }

    private void hideSundayTimeLayout(){
        sundayTv2.setVisibility(View.GONE);
        sundayTv3.setVisibility(View.GONE);
        sundayTv4.setVisibility(View.GONE);
        sundayStartTimeTv.setVisibility(View.GONE);
        sundayEndTimeTv.setVisibility(View.GONE);
        sundayTimeSwitch.setVisibility(View.GONE);
    }

    private void showSundayTimeLayout(){
        sundayTv2.setVisibility(View.VISIBLE);
        sundayTv3.setVisibility(View.VISIBLE);
        sundayTv4.setVisibility(View.VISIBLE);
        sundayStartTimeTv.setVisibility(View.VISIBLE);
        sundayEndTimeTv.setVisibility(View.VISIBLE);
        sundayTimeSwitch.setVisibility(View.VISIBLE);
    }


    private void enableMondayTimes(){
        mondayTv3.setTextColor(getResources().getColor(R.color.bgColor));
        mondayTv4.setTextColor(getResources().getColor(R.color.bgColor));
        mondayStartTimeTv.setEnabled(true);
        mondayEndTimeTv.setEnabled(true);
        mondayStartTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
        mondayEndTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
    }

    private void disableMondayTimes(){
        mondayTv3.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        mondayTv4.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        mondayStartTimeTv.setEnabled(false);
        mondayEndTimeTv.setEnabled(false);
        mondayStartTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        mondayEndTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
    }

    private void enableTuesdayTimes(){
        tuesdayTv3.setTextColor(getResources().getColor(R.color.bgColor));
        tuesdayTv4.setTextColor(getResources().getColor(R.color.bgColor));
        tuesdayStartTimeTv.setEnabled(true);
        tuesdayEndTimeTv.setEnabled(true);
        tuesdayStartTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
        tuesdayEndTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
    }

    private void disableTuesdayTimes(){
        tuesdayTv3.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        tuesdayTv4.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        tuesdayStartTimeTv.setEnabled(false);
        tuesdayEndTimeTv.setEnabled(false);
        tuesdayStartTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        tuesdayEndTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
    }

    private void enableWednesdayTimes(){
        wednesdayTv3.setTextColor(getResources().getColor(R.color.bgColor));
        wednesdayTv4.setTextColor(getResources().getColor(R.color.bgColor));
        wednesdayStartTimeTv.setEnabled(true);
        wednesdayEndTimeTv.setEnabled(true);
        wednesdayStartTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
        wednesdayEndTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
    }

    private void disableWednesdayTimes(){
        wednesdayTv3.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        wednesdayTv4.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        wednesdayStartTimeTv.setEnabled(false);
        wednesdayEndTimeTv.setEnabled(false);
        wednesdayStartTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        wednesdayEndTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
    }

    private void enableThursdayTimes(){
        thursdayTv3.setTextColor(getResources().getColor(R.color.bgColor));
        thursdayTv4.setTextColor(getResources().getColor(R.color.bgColor));
        thursdayStartTimeTv.setEnabled(true);
        thursdayEndTimeTv.setEnabled(true);
        thursdayStartTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
        thursdayEndTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
    }

    private void disableThursdayTimes(){
        thursdayTv3.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        thursdayTv4.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        thursdayStartTimeTv.setEnabled(false);
        thursdayEndTimeTv.setEnabled(false);
        thursdayStartTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        thursdayEndTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
    }

    private void enableFridayTimes(){
        fridayTv3.setTextColor(getResources().getColor(R.color.bgColor));
        fridayTv4.setTextColor(getResources().getColor(R.color.bgColor));
        fridayStartTimeTv.setEnabled(true);
        fridayEndTimeTv.setEnabled(true);
        fridayStartTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
        fridayEndTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
    }

    private void disableFridayTimes(){
        fridayTv3.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        fridayTv4.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        fridayStartTimeTv.setEnabled(false);
        fridayEndTimeTv.setEnabled(false);
        fridayStartTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        fridayEndTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
    }

    private void enableSaturdayTimes(){
        saturdayTv3.setTextColor(getResources().getColor(R.color.bgColor));
        saturdayTv4.setTextColor(getResources().getColor(R.color.bgColor));
        saturdayStartTimeTv.setEnabled(true);
        saturdayEndTimeTv.setEnabled(true);
        saturdayStartTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
        saturdayEndTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
    }

    private void disableSaturdayTimes(){
        saturdayTv3.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        saturdayTv4.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        saturdayStartTimeTv.setEnabled(false);
        saturdayEndTimeTv.setEnabled(false);
        saturdayStartTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        saturdayEndTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
    }

    private void enableSundayTimes(){
        sundayTv3.setTextColor(getResources().getColor(R.color.bgColor));
        sundayTv4.setTextColor(getResources().getColor(R.color.bgColor));
        sundayStartTimeTv.setEnabled(true);
        sundayEndTimeTv.setEnabled(true);
        sundayStartTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
        sundayEndTimeTv.setTextColor(getResources().getColor(R.color.colorHighlight));
    }

    private void disableSundayTimes(){
        sundayTv3.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        sundayTv4.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        sundayStartTimeTv.setEnabled(false);
        sundayEndTimeTv.setEnabled(false);
        sundayStartTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
        sundayEndTimeTv.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_dark_disabled));
    }

    @Override
    public void onTimeSelected(String time, Date date) {
        if(startDateSelected){
            switch(selectedDayOfWeek){
                case "monday":
                    startDateMonday = date;
                    break;
                case "tuesday":
                    startDateTuesday = date;
                    break;
                case "wednesday":
                    startDateWednesday = date;
                    break;
                case "thursday":
                    startDateThursday = date;
                    break;
                case "friday":
                    startDateFriday = date;
                    break;
                case "saturday":
                    startDateSaturday = date;
                    break;
                case "sunday":
                    startDateSunday = date;
                    break;
            }
        }else{
            switch(selectedDayOfWeek){
                case "monday":
                    endDateMonday = date;
                    break;
                case "tuesday":
                    endDateTuesday = date;
                    break;
                case "wednesday":
                    endDateWednesday = date;
                    break;
                case "thursday":
                    endDateThursday = date;
                    break;
                case "friday":
                    endDateFriday = date;
                    break;
                case "saturday":
                    endDateSaturday = date;
                    break;
                case "sunday":
                    endDateSunday = date;
                    break;
            }
        }

            switch(selectedDayOfWeek) {
                case "monday":
                    if(startDateMonday != null && endDateMonday != null && startDateMonday.getHours() < endDateMonday.getHours()) {
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeMonday(startDateMonday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeMonday(endDateMonday.getTime(), getApplicationContext());
                    }else if(startDateMonday.getHours() == endDateMonday.getHours() && startDateMonday.getMinutes() < endDateMonday.getMinutes()){
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeMonday(startDateMonday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeMonday(endDateMonday.getTime(), getApplicationContext());
                    }else{
                        Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "tuesday":
                    if(startDateTuesday != null && endDateTuesday != null && startDateTuesday.getHours() < endDateTuesday.getHours()) {
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeTuesday(startDateTuesday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeTuesday(endDateTuesday.getTime(), getApplicationContext());
                    }else if(startDateTuesday.getHours() == endDateTuesday.getHours() && startDateTuesday.getMinutes() < endDateTuesday.getMinutes()){
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeTuesday(startDateTuesday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeTuesday(endDateTuesday.getTime(), getApplicationContext());
                    }else{
                        Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "wednesday":
                    if(startDateWednesday != null && endDateWednesday != null && startDateWednesday.getHours() < endDateWednesday.getHours()) {
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeWednesday(startDateWednesday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeWednesday(endDateWednesday.getTime(), getApplicationContext());
                    }else if(startDateWednesday.getHours() == endDateWednesday.getHours() && startDateWednesday.getMinutes() < endDateWednesday.getMinutes()){
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeWednesday(startDateWednesday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeWednesday(endDateWednesday.getTime(), getApplicationContext());
                    }else{
                        Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "thursday":
                    if(startDateThursday != null && endDateThursday != null && startDateThursday.getHours() < endDateThursday.getHours()) {
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeThursday(startDateThursday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeThursday(endDateThursday.getTime(), getApplicationContext());
                    }else if(startDateThursday.getHours() == endDateThursday.getHours() && startDateThursday.getMinutes() < endDateThursday.getMinutes()){
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeThursday(startDateThursday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeThursday(endDateThursday.getTime(), getApplicationContext());
                    }else{
                        Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "friday":
                    if(startDateFriday != null && endDateFriday != null && startDateFriday.getHours() < endDateFriday.getHours()) {
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeFriday(startDateFriday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeFriday(endDateFriday.getTime(), getApplicationContext());
                    }else if(startDateFriday.getHours() == endDateFriday.getHours() && startDateFriday.getMinutes() < endDateFriday.getMinutes()){
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeFriday(startDateFriday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeFriday(endDateFriday.getTime(), getApplicationContext());
                    }else{
                        Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "saturday":
                    if(startDateSaturday != null && endDateSaturday != null && startDateSaturday.getHours() < endDateSaturday.getHours()) {
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeSaturday(startDateSaturday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeSaturday(endDateSaturday.getTime(), getApplicationContext());
                    }else if(startDateSaturday.getHours() == endDateSaturday.getHours() && startDateSaturday.getMinutes() < endDateSaturday.getMinutes()){
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeSaturday(startDateSaturday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeSaturday(endDateSaturday.getTime(), getApplicationContext());
                    }else{
                        Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "sunday":
                    if(startDateSunday != null && endDateSunday != null && startDateSunday.getHours() < endDateSunday.getHours()) {
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeSunday(startDateSunday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeSunday(endDateSunday.getTime(), getApplicationContext());
                    }else if(startDateSunday.getHours() == endDateSunday.getHours() && startDateSunday.getMinutes() < endDateSunday.getMinutes()){
                        textViewToSet.setText(time);
                        mPrefs.setSettingsStartTimeSunday(startDateSunday.getTime(), getApplicationContext());
                        mPrefs.setSettingsEndTimeSunday(endDateSunday.getTime(), getApplicationContext());
                    }else{
                        Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
    }

    private void launchTimePickerDialog(){
        FragmentManager fm = getSupportFragmentManager();
        TimeSelectionDialogFragment verseSelectedDialog = new TimeSelectionDialogFragment();
        verseSelectedDialog.show(fm, "TimePickerFragment");
    }
}
