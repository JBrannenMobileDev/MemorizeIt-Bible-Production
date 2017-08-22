package nape.biblememory;

import android.content.Context;
import android.content.SharedPreferences;

import nape.biblememory.Models.UserPreferencesModel;
import nape.biblememory.data_store.DataStore;

public class UserPreferences{
    private static final String APP_SETTINGS = "APP_SETTINGS";


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public UserPreferences() {
    }

    public void nukeUserPrefs(Context context){
        getSharedPreferences(context).edit().clear().commit();
    }

    public void setPrefs(UserPreferencesModel response, Context context) {
        if(response != null) {
            setQuizViewCount(response.getQuizViewCount(), context);
            setQuizReviewIndex(response.getQuizReviewIndex(), context);
            setSelectedVersion(response.getSelectedBibleVersion(), context);
            setStartQuizWhenPhoneUnlocks(response.isStartQuizWhenPhoneUnlocks(), context);
            setShowQuizOnMonday(response.isShowQuizMonday(), context);
            setShowQuizOnTuesday(response.isShowQuizTuesday(), context);
            setShowQuizOnWednesday(response.isShowQuizWednesday(), context);
            setShowQuizOnThursday(response.isShowQuizThursday(), context);
            setShowQuizOnFriday(response.isShowQuizFriday(), context);
            setShowQuizOnSaturday(response.isShowQuizSaturday(), context);
            setShowQuizOnSunday(response.isShowQuizSunday(), context);
            setShowQuizTimeOnMondayChecked(response.isShowQuizTimeMonday(), context);
            setShowQuizTimeOnTuesdayChecked(response.isShowQuizTimeTuesday(), context);
            setShowQuizTimeOnWednesdayChecked(response.isShowQuizTimeWednesday(), context);
            setShowQuizTimeOnThursdayChecked(response.isShowQuizTimeThursday(), context);
            setShowQuizTimeOnFridayChecked(response.isShowQuizTimeFriday(), context);
            setShowQuizTimeOnSaturdayChecked(response.isShowQuizTimeSaturday(), context);
            setShowQuizTimeOnSundayChecked(response.isShowQuizTimeSunday(), context);
            setRebuildError(response.isRebuildError(), context);
            setMigratedToFirebaseDb(response.isMigratedToFirebase(), context);
            setSettingsStartTimeMonday(response.getMondayStartTime(), context);
            setSettingsStartTimeTuesday(response.getTuesdayStartTime(), context);
            setSettingsStartTimeWednesday(response.getWednesdayStartTime(), context);
            setSettingsStartTimeThursday(response.getThursdayStartTime(), context);
            setSettingsStartTimeFriday(response.getFridayStartTime(), context);
            setSettingsStartTimeSaturday(response.getSaturdayStartTime(), context);
            setSettingsStartTimeSunday(response.getSundayStartTime(), context);
            setSettingsEndTimeMonday(response.getMondayEndTime(), context);
            setSettingsEndTimeTuesday(response.getTuesdayEndTime(), context);
            setSettingsEndTimeWednesday(response.getWednesdayEndTime(), context);
            setSettingsEndTimeThursday(response.getThursdayEndTime(), context);
            setSettingsEndTimeFriday(response.getFridayEndTime(), context);
            setSettingsEndTimeSaturday(response.getSaturdayEndTime(), context);
            setSettingsEndTimeSunday(response.getSundayEndTime(), context);
        }else{
            UserPreferencesModel prefsModel = new UserPreferencesModel();
            prefsModel.initAllData(context, new UserPreferences());
            DataStore.getInstance().saveUserPrefs(prefsModel, context);
            setPrefs(prefsModel, context);
        }
    }

    public static void setFirstTimeUnlock(boolean firstTime, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(UserPreferenceConstants.FIRST_TIME_UNLOCK, firstTime);
        editor.commit();
    }

    public static boolean isFirstTimeUnlock(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.FIRST_TIME_UNLOCK , true);
    }

    public void setUnlockViewCount(int count, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(UserPreferenceConstants.UNLOCK_VIEW_COUNT, count);
        editor.commit();
    }

    public int getUnlockViewCount(Context context){
        return getSharedPreferences(context).getInt(UserPreferenceConstants.UNLOCK_VIEW_COUNT, 0);
    }

    public boolean getIsIntentFromPhoneCall(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.INTENT_INCOMING_CALL, false);
    }

    public void setIsIntentFromPhoneCall(boolean val, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(UserPreferenceConstants.INTENT_INCOMING_CALL, val);
        editor.commit();
    }

    public int getScreenWidth(Context context){
        return getSharedPreferences(context).getInt(UserPreferenceConstants.SCREEN_WIDTH, 0);
    }

    public void setScreenWidth(int width, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(UserPreferenceConstants.SCREEN_WIDTH, width);
        editor.commit();
    }

    public int getMemorizedSpinnerPosition(Context context){
        return getSharedPreferences(context).getInt(UserPreferenceConstants.MEMORIZED_SPINNER_POSITION, 0);
    }

    public void setMemorizedSpinnerPosition(int position, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(UserPreferenceConstants.MEMORIZED_SPINNER_POSITION, position);
        editor.commit();
    }

    public void setPreviouslySelectedBookGroup(String bookName, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.PREVIOUS_BOOK_GROUP, bookName);
        editor.commit();
    }

    public String getPreviouslySelectedBookGroup(Context context){
        return getSharedPreferences(context).getString(UserPreferenceConstants.PREVIOUS_BOOK_GROUP, "");
    }

    public String getSelectedBook(Context context){
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_BOOK, "");
    }

    public void setSelectedBook(String bookName, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_BOOK, bookName);
        editor.commit();
    }

    public String getSelectedBookId(Context context){
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_BOOK_ID, "");
    }

    public void setSelectedBookId(String bookId, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_BOOK_ID, bookId);
        editor.commit();
    }

    public String getSelectedChapter(Context context){
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_CHAPTER, "");
    }

    public void setSelectedChapter(String chapterNum, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_CHAPTER, chapterNum);
        editor.commit();
    }

    public void setNumberOfChapters(long numOfChapters, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(UserPreferenceConstants.NUMBER_OF_CHAPTERS, numOfChapters);
        editor.commit();
    }

    public long getNumberOfChapters(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.NUMBER_OF_CHAPTERS, 0);
    }

    public void setNumberOfVerses(long numOfVerses, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.NUMBER_OF_VERSES, numOfVerses);
        editor.commit();
    }

    public long getNumberOfVerses(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.NUMBER_OF_VERSES, 0);
    }

    public String getSelectedBibleLanguage(Context context){
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_BIBLE_LANGUAGE, "English");
    }

    public void setSelectedBibleLanguage(String bibleLanguage, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_BIBLE_LANGUAGE, bibleLanguage);
        editor.commit();
    }

    public void setSelectedVersion(String version, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_BIBLE_VERSION, version);
        editor.commit();
    }

    public void setTempSelectedVersion(String versionCode, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putString(UserPreferenceConstants.SELECTED_BIBLE_VERSION_TEMP, versionCode);
        editor.commit();
    }

    public String getSelectedVersion(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_BIBLE_VERSION, "ESV");
    }

    public String getTempSelectedVersion(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_BIBLE_VERSION_TEMP, "");
    }

    public void setDamIdNewTestament(String damId, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.NEW_TEST_DAM_ID, damId);
        editor.commit();
    }

    public String getDamIdNewTestament(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.NEW_TEST_DAM_ID, "ENGESVN2ET");
    }

    public void setDamIdOldTestament(String damId, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.OLD_TEST_DAM_ID, damId);
        editor.commit();
    }

    public String getDamIdOldTestament(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.OLD_TEST_DAM_ID, "ENGESVO2ET");
    }

    public void setDamId(String damId, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.DAM_ID, damId);
        editor.commit();
    }

    public String getDamId(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.DAM_ID, "ENGESV");
    }

    public void setSelectedVerseNum(String verseNum, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_VERSE_NUM, verseNum);
        editor.commit();
    }

    public String getSelectedVerseNum(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_VERSE_NUM, "");
    }

    public void setBookLocationOT(boolean isOT, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(UserPreferenceConstants.BOOK_LOCATION_OT, isOT);
        editor.commit();
    }

    public boolean isBookLocationOT(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.BOOK_LOCATION_OT , false);
    }

    public void setChapterId(String chapterId, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.CHAPTER_ID, chapterId);
        editor.commit();
    }

    public String getChapterId(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.CHAPTER_ID, "");
    }

    public void setStartQuizWhenPhoneUnlocks(boolean start, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(UserPreferenceConstants.START_QUIZ_WHEN_PHONE_UNLOCKS, start);
        editor.commit();
    }

    public boolean isStartQuizWhenPhoneUnlock(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.START_QUIZ_WHEN_PHONE_UNLOCKS , true);
    }

    public void setShowQuizOnMonday(boolean show, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(UserPreferenceConstants.SHOW_QUIZ_MONDAY, show);
        editor.commit();
    }

    public boolean showQuizOnMonday(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SHOW_QUIZ_MONDAY , true);
    }

    public void setShowQuizOnTuesday(boolean pressed, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SHOW_QUIZ_TUESDAY, pressed);
        editor.commit();
    }

    public boolean showQuizOnTuesday(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SHOW_QUIZ_TUESDAY , true);
    }

    public void setShowQuizOnWednesday(boolean pressed, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SHOW_QUIZ_WEDNESDAY, pressed);
        editor.commit();
    }

    public boolean showQuizOnWednesday(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SHOW_QUIZ_WEDNESDAY , true);
    }

    public void setShowQuizOnThursday(boolean pressed, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SHOW_QUIZ_THURSDAY, pressed);
        editor.commit();
    }

    public boolean showQuizOnThursday(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SHOW_QUIZ_THURSDAY , true);
    }

    public void setShowQuizOnFriday(boolean pressed, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SHOW_QUIZ_FRIDAY, pressed);
        editor.commit();
    }

    public boolean showQuizOnFriday(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SHOW_QUIZ_FRIDAY , true);
    }

    public void setShowQuizOnSaturday(boolean pressed, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SHOW_QUIZ_SATURDAY, pressed);
        editor.commit();
    }

    public boolean showQuizOnSaturday(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SHOW_QUIZ_SATURDAY , true);
    }

    public void setShowQuizOnSunday(boolean pressed, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SHOW_QUIZ_SUNDAY, pressed);
        editor.commit();
    }

    public boolean showQuizOnSunday(Context context){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SHOW_QUIZ_SUNDAY , true);
    }

    public void setSettingsStartTimeMonday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_START_TIME_MONDAY, time);
        editor.commit();
    }

    public long getSettingsStartTimeMonday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_START_TIME_MONDAY, 0);
    }

    public void setSettingsEndTimeMonday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_END_TIME_MONDAY, time);
        editor.commit();
    }

    public long getSettingsEndTimeMonday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_END_TIME_MONDAY, 0);
    }

    public void setSettingsStartTimeTuesday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_START_TIME_TUESDAY, time);
        editor.commit();
    }

    public long getSettingsStartTimeTuesday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_START_TIME_TUESDAY, 0);
    }

    public void setSettingsEndTimeTuesday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_END_TIME_TUESDAY, time);
        editor.commit();
    }

    public long getSettingsEndTimeTuesday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_END_TIME_TUESDAY, 0);
    }
    public void setSettingsStartTimeWednesday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_START_TIME_WEDNESDAY, time);
        editor.commit();
    }

    public long getSettingsStartTimeWednesday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_START_TIME_WEDNESDAY, 0);
    }

    public void setSettingsEndTimeWednesday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_END_TIME_WEDNESDAY, time);
        editor.commit();
    }

    public long getSettingsEndTimeWednesday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_END_TIME_WEDNESDAY, 0);
    }
    public void setSettingsStartTimeThursday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_START_TIME_THURSDAY, time);
        editor.commit();
    }

    public long getSettingsStartTimeThursday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_START_TIME_THURSDAY, 0);
    }

    public void setSettingsEndTimeThursday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_END_TIME_THURSDAY, time);
        editor.commit();
    }

    public long getSettingsEndTimeThursday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_END_TIME_THURSDAY, 0);
    }
    public void setSettingsStartTimeFriday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_START_TIME_FRIDAY, time);
        editor.commit();
    }

    public long getSettingsStartTimeFriday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_START_TIME_FRIDAY, 0);
    }

    public void setSettingsEndTimeFriday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_END_TIME_FRIDAY, time);
        editor.commit();
    }

    public long getSettingsEndTimeFriday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_END_TIME_FRIDAY, 0);
    }
    public void setSettingsStartTimeSaturday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_START_TIME_SATURDAY, time);
        editor.commit();
    }

    public long getSettingsStartTimeSaturday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_START_TIME_SATURDAY, 0);
    }

    public void setSettingsEndTimeSaturday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_END_TIME_SATURDAY, time);
        editor.commit();
    }

    public long getSettingsEndTimeSaturday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_END_TIME_SATURDAY, 0);
    }
    public void setSettingsStartTimeSunday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_START_TIME_SUNDAY, time);
        editor.commit();
    }

    public long getSettingsStartTimeSunday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_START_TIME_SUNDAY, 0);
    }

    public void setSettingsEndTimeSunday(long time, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putLong(UserPreferenceConstants.SETTINGS_END_TIME_SUNDAY, time);
        editor.commit();
    }

    public long getSettingsEndTimeSunday(Context context){
        return getSharedPreferences(context).getLong(UserPreferenceConstants.SETTINGS_END_TIME_SUNDAY, 0);
    }

    public boolean isStartQuizBasedOffDayOfWeek(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SHOW_QUIZ_THIS_DAY , true);
    }

    public void setShowQuizBasedOffDayOfWeek(boolean pressed, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SHOW_QUIZ_THIS_DAY, pressed);
        editor.commit();
    }

    public void setShowQuizTimeOnMondayChecked(boolean b, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.MONDAY_TIME_SWITCH_CHECKED, b);
        editor.commit();
    }

    public boolean showQuizTimeOnMondayChecked(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.MONDAY_TIME_SWITCH_CHECKED , false);
    }

    public void setShowQuizTimeOnTuesdayChecked(boolean b, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.TUESDAY_TIME_SWITCH_CHECKED, b);
        editor.commit();
    }

    public boolean showQuizTimeOnTuesdayChecked(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.TUESDAY_TIME_SWITCH_CHECKED , false);
    }

    public void setShowQuizTimeOnWednesdayChecked(boolean b, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.WEDNESDAY_TIME_SWITCH_CHECKED, b);
        editor.commit();
    }

    public boolean showQuizTimeOnWednesdayChecked(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.WEDNESDAY_TIME_SWITCH_CHECKED , false);
    }

    public void setShowQuizTimeOnThursdayChecked(boolean b, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.THURSDAY_TIME_SWITCH_CHECKED, b);
        editor.commit();
    }

    public boolean showQuizTimeOnThursdayChecked(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.THURSDAY_TIME_SWITCH_CHECKED , false);
    }

    public void setShowQuizTimeOnFridayChecked(boolean b, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.FRIDAY_TIME_SWITCH_CHECKED, b);
        editor.commit();
    }

    public boolean showQuizTimeOnFridayChecked(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.FRIDAY_TIME_SWITCH_CHECKED , false);
    }

    public void setShowQuizTimeOnSaturdayChecked(boolean b, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SATURDAY_TIME_SWITCH_CHECKED, b);
        editor.commit();
    }

    public boolean showQuizTimeOnSaturdayChecked(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SATURDAY_TIME_SWITCH_CHECKED , false);
    }

    public void setShowQuizTimeOnSundayChecked(boolean b, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.SUNDAY_TIME_SWITCH_CHECKED, b);
        editor.commit();
    }

    public boolean showQuizTimeOnSundayChecked(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.SUNDAY_TIME_SWITCH_CHECKED , false);
    }

    public void setFirstTimeSignIn(boolean b, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putBoolean(UserPreferenceConstants.FIRST_TIME_LOGIN, b);
        editor.commit();
    }

    public boolean isFirstTimeLogind(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.FIRST_TIME_LOGIN , true);
    }

    public String getUserId(Context context){
        return getSharedPreferences(context).getString(UserPreferenceConstants.USER_ID, "");
    }

    public void setUserId(String id, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.USER_ID, id);
        editor.commit();
    }

    public void setUserEmail(String email, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putString(UserPreferenceConstants.USER_EMAIL, email);
        editor.commit();
    }

    public String getUserEmail(Context context){
        return getSharedPreferences(context).getString(UserPreferenceConstants.USER_EMAIL, "");
    }

    public void setRebuildError(boolean b, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(UserPreferenceConstants.REBUILD_ERROR, b);
        editor.commit();
    }

    public boolean isRebuildError(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.REBUILD_ERROR , false);
    }

    public void setMigratedToFirebaseDb(boolean b, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(UserPreferenceConstants.MIGRATED_FB, b);
        editor.commit();
    }

    public boolean isMigratedToFirebaseDb(Context context) {
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.MIGRATED_FB , false);
    }

    public int getQuizViewCount(Context context) {
        return getSharedPreferences(context).getInt(UserPreferenceConstants.QUIZ_VIEW_COUNT, 0);
    }

    public void setQuizViewCount(int count, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(UserPreferenceConstants.QUIZ_VIEW_COUNT, count);
        editor.commit();
    }

    public void setQuizReviewIndex(int i, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(UserPreferenceConstants.REVIEW_VERSE_INDEX, i);
        editor.commit();
    }

    public int getQuizReviewIndex(Context context) {
        return getSharedPreferences(context).getInt(UserPreferenceConstants.REVIEW_VERSE_INDEX, 0);
    }
}
