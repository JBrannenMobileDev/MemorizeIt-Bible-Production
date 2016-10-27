package nape.biblememory.Activities;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void setNumberOfChapters(int numOfChapters, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(UserPreferenceConstants.NUMBER_OF_CHAPTERS, numOfChapters);
        editor.commit();
    }

    public int getNumberOfChapters(Context context){
        return getSharedPreferences(context).getInt(UserPreferenceConstants.NUMBER_OF_CHAPTERS, 0);
    }

    public void setNumberOfVerses(int numOfVerses, Context applicationContext) {
        SharedPreferences.Editor editor = getSharedPreferences(applicationContext).edit();
        editor.putInt(UserPreferenceConstants.NUMBER_OF_VERSES, numOfVerses);
        editor.commit();
    }

    public int getNumberOfVerses(Context context){
        return getSharedPreferences(context).getInt(UserPreferenceConstants.NUMBER_OF_VERSES, 0);
    }
}
