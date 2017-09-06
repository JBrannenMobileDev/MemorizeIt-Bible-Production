package nape.biblememory.Models;

import android.content.Context;

import nape.biblememory.utils.UserPreferences;

/**
 * Created by jbrannen on 8/8/17.
 */

public class UserPreferencesModel {
    private boolean startQuizWhenPhoneUnlocks;
    private boolean showQuizMonday;
    private boolean showQuizTuesday;
    private boolean showQuizWednesday;
    private boolean showQuizThursday;
    private boolean showQuizFriday;
    private boolean showQuizSaturday;
    private boolean showQuizSunday;
    private boolean showQuizTimeMonday;
    private boolean showQuizTimeTuesday;
    private boolean showQuizTimeWednesday;
    private boolean showQuizTimeThursday;
    private boolean showQuizTimeFriday;
    private boolean showQuizTimeSaturday;
    private boolean showQuizTimeSunday;
    private boolean migratedToFirebase;
    private boolean rebuildError;
    private boolean tourStep1Complete;
    private boolean tourStep2Complete;
    private long mondayStartTime;
    private long mondayEndTime;
    private long tuesdayStartTime;
    private long tuesdayEndTime;
    private long wednesdayStartTime;
    private long wednesdayEndTime;
    private long thursdayStartTime;
    private long thursdayEndTime;
    private long fridayStartTime;
    private long fridayEndTime;
    private long saturdayStartTime;
    private long saturdayEndTime;
    private long sundayStartTime;
    private long sundayEndTime;
    private String selectedBibleVersion;
    private int quizViewCount;
    private int quizReviewIndex;

    public UserPreferencesModel() {
    }

    public void initAllData(Context context, UserPreferences mPrefs){
        tourStep1Complete = mPrefs.isTourStep1Complete(context);
        tourStep2Complete = mPrefs.isTourStep2Complete(context);
        quizViewCount = mPrefs.getQuizViewCount(context);
        quizReviewIndex = mPrefs.getQuizReviewIndex(context);
        selectedBibleVersion = mPrefs.getSelectedVersion(context);
        startQuizWhenPhoneUnlocks = mPrefs.isStartQuizWhenPhoneUnlock(context);
        showQuizMonday = mPrefs.showQuizOnMonday(context);
        showQuizTuesday = mPrefs.showQuizOnTuesday(context);
        showQuizWednesday = mPrefs.showQuizOnWednesday(context);
        showQuizThursday = mPrefs.showQuizOnThursday(context);
        showQuizFriday = mPrefs.showQuizOnFriday(context);
        showQuizSaturday = mPrefs.showQuizOnSaturday(context);
        showQuizSunday = mPrefs.showQuizOnSunday(context);
        showQuizTimeMonday = mPrefs.showQuizTimeOnMondayChecked(context);
        showQuizTimeTuesday = mPrefs.showQuizTimeOnTuesdayChecked(context);
        showQuizTimeWednesday = mPrefs.showQuizTimeOnWednesdayChecked(context);
        showQuizTimeThursday = mPrefs.showQuizTimeOnThursdayChecked(context);
        showQuizTimeFriday = mPrefs.showQuizTimeOnFridayChecked(context);
        showQuizTimeSaturday = mPrefs.showQuizTimeOnSaturdayChecked(context);
        showQuizTimeSunday = mPrefs.showQuizTimeOnSundayChecked(context);
        migratedToFirebase = mPrefs.isMigratedToFirebaseDb(context);
        rebuildError = mPrefs.isRebuildError(context);
        mondayStartTime = mPrefs.getSettingsStartTimeMonday(context);
        mondayEndTime = mPrefs.getSettingsEndTimeMonday(context);
        tuesdayStartTime = mPrefs.getSettingsStartTimeTuesday(context);
        tuesdayEndTime = mPrefs.getSettingsEndTimeTuesday(context);
        wednesdayStartTime = mPrefs.getSettingsStartTimeWednesday(context);
        wednesdayEndTime = mPrefs.getSettingsEndTimeWednesday(context);
        thursdayStartTime = mPrefs.getSettingsStartTimeThursday(context);
        thursdayEndTime = mPrefs.getSettingsEndTimeThursday(context);
        fridayStartTime = mPrefs.getSettingsStartTimeFriday(context);
        fridayEndTime = mPrefs.getSettingsEndTimeFriday(context);
        saturdayStartTime = mPrefs.getSettingsStartTimeSaturday(context);
        saturdayEndTime = mPrefs.getSettingsEndTimeSaturday(context);
        sundayStartTime = mPrefs.getSettingsStartTimeSunday(context);
        sundayEndTime = mPrefs.getSettingsEndTimeSunday(context);
    }

    public boolean isTourStep1Complete() {
        return tourStep1Complete;
    }

    public void setTourStep1Complete(boolean tourStep1Complete) {
        this.tourStep1Complete = tourStep1Complete;
    }

    public boolean isTourStep2Complete() {
        return tourStep2Complete;
    }

    public void setTourStep2Complete(boolean tourStep2Complete) {
        this.tourStep2Complete = tourStep2Complete;
    }

    public String getSelectedBibleVersion() {
        return selectedBibleVersion;
    }

    public void setSelectedBibleVersion(String selectedBibleVersion) {
        this.selectedBibleVersion = selectedBibleVersion;
    }

    public int getQuizViewCount() {
        return quizViewCount;
    }

    public void setQuizViewCount(int quizViewCount) {
        this.quizViewCount = quizViewCount;
    }

    public int getQuizReviewIndex() {
        return quizReviewIndex;
    }

    public void setQuizReviewIndex(int quizReviewIndex) {
        this.quizReviewIndex = quizReviewIndex;
    }

    public boolean isStartQuizWhenPhoneUnlocks() {
        return startQuizWhenPhoneUnlocks;
    }

    public void setStartQuizWhenPhoneUnlocks(boolean startQuizWhenPhoneUnlocks) {
        this.startQuizWhenPhoneUnlocks = startQuizWhenPhoneUnlocks;
    }

    public boolean isShowQuizMonday() {
        return showQuizMonday;
    }

    public void setShowQuizMonday(boolean showQuizMonday) {
        this.showQuizMonday = showQuizMonday;
    }

    public boolean isShowQuizTuesday() {
        return showQuizTuesday;
    }

    public void setShowQuizTuesday(boolean showQuizTuesday) {
        this.showQuizTuesday = showQuizTuesday;
    }

    public boolean isShowQuizWednesday() {
        return showQuizWednesday;
    }

    public void setShowQuizWednesday(boolean showQuizWednesday) {
        this.showQuizWednesday = showQuizWednesday;
    }

    public boolean isShowQuizThursday() {
        return showQuizThursday;
    }

    public void setShowQuizThursday(boolean showQuizThursday) {
        this.showQuizThursday = showQuizThursday;
    }

    public boolean isShowQuizFriday() {
        return showQuizFriday;
    }

    public void setShowQuizFriday(boolean showQuizFriday) {
        this.showQuizFriday = showQuizFriday;
    }

    public boolean isShowQuizSaturday() {
        return showQuizSaturday;
    }

    public void setShowQuizSaturday(boolean showQuizSaturday) {
        this.showQuizSaturday = showQuizSaturday;
    }

    public boolean isShowQuizSunday() {
        return showQuizSunday;
    }

    public void setShowQuizSunday(boolean showQuizSunday) {
        this.showQuizSunday = showQuizSunday;
    }

    public boolean isShowQuizTimeMonday() {
        return showQuizTimeMonday;
    }

    public void setShowQuizTimeMonday(boolean showQuizTimeMonday) {
        this.showQuizTimeMonday = showQuizTimeMonday;
    }

    public boolean isShowQuizTimeTuesday() {
        return showQuizTimeTuesday;
    }

    public void setShowQuizTimeTuesday(boolean showQuizTimeTuesday) {
        this.showQuizTimeTuesday = showQuizTimeTuesday;
    }

    public boolean isShowQuizTimeWednesday() {
        return showQuizTimeWednesday;
    }

    public void setShowQuizTimeWednesday(boolean showQuizTimeWednesday) {
        this.showQuizTimeWednesday = showQuizTimeWednesday;
    }

    public boolean isShowQuizTimeThursday() {
        return showQuizTimeThursday;
    }

    public void setShowQuizTimeThursday(boolean showQuizTimeThursday) {
        this.showQuizTimeThursday = showQuizTimeThursday;
    }

    public boolean isShowQuizTimeFriday() {
        return showQuizTimeFriday;
    }

    public void setShowQuizTimeFriday(boolean showQuizTimeFriday) {
        this.showQuizTimeFriday = showQuizTimeFriday;
    }

    public boolean isShowQuizTimeSaturday() {
        return showQuizTimeSaturday;
    }

    public void setShowQuizTimeSaturday(boolean showQuizTimeSaturday) {
        this.showQuizTimeSaturday = showQuizTimeSaturday;
    }

    public boolean isShowQuizTimeSunday() {
        return showQuizTimeSunday;
    }

    public void setShowQuizTimeSunday(boolean showQuizTimeSunday) {
        this.showQuizTimeSunday = showQuizTimeSunday;
    }

    public boolean isMigratedToFirebase() {
        return migratedToFirebase;
    }

    public void setMigratedToFirebase(boolean migratedToFirebase) {
        this.migratedToFirebase = migratedToFirebase;
    }

    public boolean isRebuildError() {
        return rebuildError;
    }

    public void setRebuildError(boolean rebuildError) {
        this.rebuildError = rebuildError;
    }

    public long getMondayStartTime() {
        return mondayStartTime;
    }

    public void setMondayStartTime(long mondayStartTime) {
        this.mondayStartTime = mondayStartTime;
    }

    public long getMondayEndTime() {
        return mondayEndTime;
    }

    public void setMondayEndTime(long mondayEndTime) {
        this.mondayEndTime = mondayEndTime;
    }

    public long getTuesdayStartTime() {
        return tuesdayStartTime;
    }

    public void setTuesdayStartTime(long tuesdayStartTime) {
        this.tuesdayStartTime = tuesdayStartTime;
    }

    public long getTuesdayEndTime() {
        return tuesdayEndTime;
    }

    public void setTuesdayEndTime(long tuesdayEndTime) {
        this.tuesdayEndTime = tuesdayEndTime;
    }

    public long getWednesdayStartTime() {
        return wednesdayStartTime;
    }

    public void setWednesdayStartTime(long wednesdayStartTime) {
        this.wednesdayStartTime = wednesdayStartTime;
    }

    public long getWednesdayEndTime() {
        return wednesdayEndTime;
    }

    public void setWednesdayEndTime(long wednesdayEndTime) {
        this.wednesdayEndTime = wednesdayEndTime;
    }

    public long getThursdayStartTime() {
        return thursdayStartTime;
    }

    public void setThursdayStartTime(long thursdayStartTime) {
        this.thursdayStartTime = thursdayStartTime;
    }

    public long getThursdayEndTime() {
        return thursdayEndTime;
    }

    public void setThursdayEndTime(long thursdayEndTime) {
        this.thursdayEndTime = thursdayEndTime;
    }

    public long getFridayStartTime() {
        return fridayStartTime;
    }

    public void setFridayStartTime(long fridayStartTime) {
        this.fridayStartTime = fridayStartTime;
    }

    public long getFridayEndTime() {
        return fridayEndTime;
    }

    public void setFridayEndTime(long fridayEndTime) {
        this.fridayEndTime = fridayEndTime;
    }

    public long getSaturdayStartTime() {
        return saturdayStartTime;
    }

    public void setSaturdayStartTime(long saturdayStartTime) {
        this.saturdayStartTime = saturdayStartTime;
    }

    public long getSaturdayEndTime() {
        return saturdayEndTime;
    }

    public void setSaturdayEndTime(long saturdayEndTime) {
        this.saturdayEndTime = saturdayEndTime;
    }

    public long getSundayStartTime() {
        return sundayStartTime;
    }

    public void setSundayStartTime(long sundayStartTime) {
        this.sundayStartTime = sundayStartTime;
    }

    public long getSundayEndTime() {
        return sundayEndTime;
    }

    public void setSundayEndTime(long sundayEndTime) {
        this.sundayEndTime = sundayEndTime;
    }
}
