package nape.biblememory.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Activities.PhoneUnlockActivity;
import nape.biblememory.Sqlite.MemoryListContract;
import nape.biblememory.UserPreferences;


public class UnlockReceiver extends BroadcastReceiver {
    private static final String TAG = "UnlockReceiver";
    private UserPreferences mPrefs;

    @Override
    public void onReceive(Context context, Intent intent) {

        mPrefs = new UserPreferences();

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                Log.d(TAG, "UnlockReceiver - Intent ACTION_USER_PRESENT triggered" );
                if(mPrefs.getIsIntentFromPhoneCall(context)){
                    mPrefs.setIsIntentFromPhoneCall(false, context);
                    Log.d(TAG, "UnlockReceiver - Intent from phone call set to false in mPrefs." );
                }else {
                    Intent s = new Intent(context, PhoneUnlockActivity.class);
                    s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    VerseOperations vOperations = new VerseOperations(context);
                    if(vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME).size() > 0 && mPrefs.isStartQuizWhenPhoneUnlock(context)) {
                        Calendar calendar = Calendar.getInstance();
                        boolean startQuiz = true;
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        if (dayOfWeek == Calendar.MONDAY) {
                            if(!mPrefs.showQuizOnMonday(context) && mPrefs.showQuizTimeOnMondayChecked(context)){
                                if(calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeMonday(context))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeMonday(context)))){
                                    startQuiz = false;
                                }else{
                                    startQuiz = true;
                                }
                            }else if (!mPrefs.showQuizOnMonday(context) && !mPrefs.showQuizTimeOnMondayChecked(context)) {
                                startQuiz = false;
                            }else if(mPrefs.showQuizOnMonday(context)) {
                                startQuiz = true;
                            }
                        } else if (dayOfWeek == Calendar.TUESDAY) {
                            if(!mPrefs.showQuizOnTuesday(context) && mPrefs.showQuizTimeOnTuesdayChecked(context)){
                                if(calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeTuesday(context))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeTuesday(context)))){
                                    startQuiz = false;
                                }else{
                                    startQuiz = true;
                                }
                            }else if (!mPrefs.showQuizOnTuesday(context) && !mPrefs.showQuizTimeOnTuesdayChecked(context)) {
                                startQuiz = false;
                            }else if(mPrefs.showQuizOnTuesday(context)) {
                                startQuiz = true;
                            }
                        } else if (dayOfWeek == Calendar.WEDNESDAY) {
                            if(!mPrefs.showQuizOnWednesday(context) && mPrefs.showQuizTimeOnWednesdayChecked(context)){
                                if(calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeWednesday(context))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeWednesday(context)))){
                                    startQuiz = false;
                                }else{
                                    startQuiz = true;
                                }
                            }else if (!mPrefs.showQuizOnWednesday(context) && !mPrefs.showQuizTimeOnWednesdayChecked(context)) {
                                startQuiz = false;
                            }else if(mPrefs.showQuizOnWednesday(context)) {
                                startQuiz = true;
                            }
                        } else if (dayOfWeek == Calendar.THURSDAY) {
                            if(!mPrefs.showQuizOnThursday(context) && mPrefs.showQuizTimeOnThursdayChecked(context)){
                                if(calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeThursday(context))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeThursday(context)))){
                                    startQuiz = false;
                                }else{
                                    startQuiz = true;
                                }
                            }else if (!mPrefs.showQuizOnThursday(context) && !mPrefs.showQuizTimeOnThursdayChecked(context)) {
                                startQuiz = false;
                            }else if(mPrefs.showQuizOnThursday(context)){
                                startQuiz = true;
                            }
                        } else if (dayOfWeek == Calendar.FRIDAY) {
                            if (!mPrefs.showQuizOnFriday(context) && mPrefs.showQuizTimeOnFridayChecked(context)) {
                                if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeFriday(context))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeFriday(context)))) {
                                    startQuiz = false;
                                } else {
                                    startQuiz = true;
                                }
                            } else if (!mPrefs.showQuizOnFriday(context) && !mPrefs.showQuizTimeOnFridayChecked(context)) {
                                startQuiz = false;
                            } else if (mPrefs.showQuizOnFriday(context)) {
                                startQuiz = true;
                            }
                        }else if (dayOfWeek == Calendar.SATURDAY) {
                            if (!mPrefs.showQuizOnSaturday(context) && mPrefs.showQuizTimeOnSaturdayChecked(context)) {
                                if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeSaturday(context))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeSaturday(context)))) {
                                    startQuiz = false;
                                } else {
                                    startQuiz = true;
                                }
                            } else if (!mPrefs.showQuizOnSaturday(context) && !mPrefs.showQuizTimeOnSaturdayChecked(context)) {
                                startQuiz = false;
                            } else if (mPrefs.showQuizOnSaturday(context)) {
                                startQuiz = true;
                            }
                        }else if (dayOfWeek == Calendar.SUNDAY) {
                            if (!mPrefs.showQuizOnSunday(context) && mPrefs.showQuizTimeOnSundayChecked(context)) {
                                if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeSunday(context))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeSunday(context)))) {
                                    startQuiz = false;
                                } else {
                                    startQuiz = true;
                                }
                            } else if (!mPrefs.showQuizOnSunday(context) && !mPrefs.showQuizTimeOnSundayChecked(context)) {
                                startQuiz = false;
                            } else if (mPrefs.showQuizOnSunday(context)) {
                                startQuiz = true;
                            }
                        }

                        if(startQuiz){
                            Log.d(TAG, "UnlockReceiver - Unlock activity has been launched.");
                            context.startActivity(s);
                        }

                    }
                }
            }else if(intent.getAction().equals(Intent.ACTION_ANSWER)){
                    String stateStr = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    if(stateStr != null && stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        mPrefs.setIsIntentFromPhoneCall(true, context);
                        Log.d(TAG, "UnlockReceiver - Intent from phone call set to false in mPrefs." );
                    }
            }
        }
    }
}
