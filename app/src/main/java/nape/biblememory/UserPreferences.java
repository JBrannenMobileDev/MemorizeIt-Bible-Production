package nape.biblememory;

import android.content.Context;
import android.content.SharedPreferences;

import nape.biblememory.UserPreferenceConstants;

public class UserPreferences{
    private static final String APP_SETTINGS = "APP_SETTINGS";


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public UserPreferences() {
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
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_BIBLE_LANGUAGE, "");
    }

    public void setSelectedBibleLanguage(String bibleLanguage, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_BIBLE_LANGUAGE, bibleLanguage);
        editor.commit();
    }

    public void setSelectedVersion(String version, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_BIBLE_VERSION, version);
    }

    public String getSelectedVersion(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.SELECTED_BIBLE_VERSION, "ESV");
    }

    public void setDamIdNewTestament(String damId, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.NEW_TEST_DAM_ID, damId);
    }

    public String getDamIdNewTestament(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.NEW_TEST_DAM_ID, "ENGESVN2ET");
    }

    public void setDamIdOldTestament(String damId, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.OLD_TEST_DAM_ID, damId);
    }

    public String getDamIdOldTestament(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.OLD_TEST_DAM_ID, "ENGESVO2ET");
    }

    public void setDamId(String damId, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.DAM_ID, damId);
    }

    public String getDamId(Context context) {
        return getSharedPreferences(context).getString(UserPreferenceConstants.DAM_ID, "ENGESV");
    }

    public void setSelectedVerseNum(String verseNum, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.SELECTED_VERSE_NUM, verseNum);
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
}
