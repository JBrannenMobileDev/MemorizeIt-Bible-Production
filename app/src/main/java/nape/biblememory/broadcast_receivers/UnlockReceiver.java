package nape.biblememory.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.view_layer.activities.PhoneUnlockActivity;
import nape.biblememory.utils.UserPreferences;


public class UnlockReceiver extends BroadcastReceiver {
    private static final String TAG = "UnlockReceiver";
    private UserPreferences mPrefs;

    @Override
    public void onReceive(final Context context, Intent intent) {
        mPrefs = new UserPreferences();
        Realm.init(context.getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm4.realm").schemaVersion(4).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                    Log.d(TAG, "UnlockReceiver - Intent ACTION_USER_PRESENT triggered");
                    if (mPrefs.getIsIntentFromPhoneCall(context.getApplicationContext())) {
                        mPrefs.setIsIntentFromPhoneCall(false, context.getApplicationContext());
                        Log.d(TAG, "UnlockReceiver - Intent from phone call set to false in mPrefs.");
                    } else {
                        BaseCallback<MyVerse> myVerseCallback = new BaseCallback<MyVerse>() {
                            @Override
                            public void onResponse(MyVerse response) {
                                Intent s = new Intent(context.getApplicationContext(), PhoneUnlockActivity.class);
                                s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (response != null && mPrefs.isStartQuizWhenPhoneUnlock(context.getApplicationContext())) {
                                    Calendar calendar = Calendar.getInstance();
                                    boolean startQuiz = true;
                                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                    if (dayOfWeek == Calendar.MONDAY) {
                                        if (!mPrefs.showQuizOnMonday(context.getApplicationContext()) && mPrefs.showQuizTimeOnMondayChecked(context.getApplicationContext())) {
                                            if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeMonday(context.getApplicationContext()))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeMonday(context.getApplicationContext())))) {
                                                startQuiz = false;
                                            } else {
                                                startQuiz = true;
                                            }
                                        } else if (!mPrefs.showQuizOnMonday(context) && !mPrefs.showQuizTimeOnMondayChecked(context.getApplicationContext())) {
                                            startQuiz = false;
                                        } else if (mPrefs.showQuizOnMonday(context.getApplicationContext())) {
                                            startQuiz = true;
                                        }
                                    } else if (dayOfWeek == Calendar.TUESDAY) {
                                        if (!mPrefs.showQuizOnTuesday(context) && mPrefs.showQuizTimeOnTuesdayChecked(context.getApplicationContext())) {
                                            if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeTuesday(context.getApplicationContext()))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeTuesday(context.getApplicationContext())))) {
                                                startQuiz = false;
                                            } else {
                                                startQuiz = true;
                                            }
                                        } else if (!mPrefs.showQuizOnTuesday(context.getApplicationContext()) && !mPrefs.showQuizTimeOnTuesdayChecked(context.getApplicationContext())) {
                                            startQuiz = false;
                                        } else if (mPrefs.showQuizOnTuesday(context.getApplicationContext())) {
                                            startQuiz = true;
                                        }
                                    } else if (dayOfWeek == Calendar.WEDNESDAY) {
                                        if (!mPrefs.showQuizOnWednesday(context.getApplicationContext()) && mPrefs.showQuizTimeOnWednesdayChecked(context.getApplicationContext())) {
                                            if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeWednesday(context.getApplicationContext()))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeWednesday(context.getApplicationContext())))) {
                                                startQuiz = false;
                                            } else {
                                                startQuiz = true;
                                            }
                                        } else if (!mPrefs.showQuizOnWednesday(context.getApplicationContext()) && !mPrefs.showQuizTimeOnWednesdayChecked(context.getApplicationContext())) {
                                            startQuiz = false;
                                        } else if (mPrefs.showQuizOnWednesday(context.getApplicationContext())) {
                                            startQuiz = true;
                                        }
                                    } else if (dayOfWeek == Calendar.THURSDAY) {
                                        if (!mPrefs.showQuizOnThursday(context.getApplicationContext()) && mPrefs.showQuizTimeOnThursdayChecked(context.getApplicationContext())) {
                                            if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeThursday(context.getApplicationContext()))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeThursday(context.getApplicationContext())))) {
                                                startQuiz = false;
                                            } else {
                                                startQuiz = true;
                                            }
                                        } else if (!mPrefs.showQuizOnThursday(context.getApplicationContext()) && !mPrefs.showQuizTimeOnThursdayChecked(context.getApplicationContext())) {
                                            startQuiz = false;
                                        } else if (mPrefs.showQuizOnThursday(context.getApplicationContext())) {
                                            startQuiz = true;
                                        }
                                    } else if (dayOfWeek == Calendar.FRIDAY) {
                                        if (!mPrefs.showQuizOnFriday(context.getApplicationContext()) && mPrefs.showQuizTimeOnFridayChecked(context.getApplicationContext())) {
                                            if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeFriday(context.getApplicationContext()))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeFriday(context.getApplicationContext())))) {
                                                startQuiz = false;
                                            } else {
                                                startQuiz = true;
                                            }
                                        } else if (!mPrefs.showQuizOnFriday(context.getApplicationContext()) && !mPrefs.showQuizTimeOnFridayChecked(context.getApplicationContext())) {
                                            startQuiz = false;
                                        } else if (mPrefs.showQuizOnFriday(context.getApplicationContext())) {
                                            startQuiz = true;
                                        }
                                    } else if (dayOfWeek == Calendar.SATURDAY) {
                                        if (!mPrefs.showQuizOnSaturday(context.getApplicationContext()) && mPrefs.showQuizTimeOnSaturdayChecked(context.getApplicationContext())) {
                                            if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeSaturday(context.getApplicationContext()))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeSaturday(context.getApplicationContext())))) {
                                                startQuiz = false;
                                            } else {
                                                startQuiz = true;
                                            }
                                        } else if (!mPrefs.showQuizOnSaturday(context.getApplicationContext()) && !mPrefs.showQuizTimeOnSaturdayChecked(context.getApplicationContext())) {
                                            startQuiz = false;
                                        } else if (mPrefs.showQuizOnSaturday(context.getApplicationContext())) {
                                            startQuiz = true;
                                        }
                                    } else if (dayOfWeek == Calendar.SUNDAY) {
                                        if (!mPrefs.showQuizOnSunday(context.getApplicationContext()) && mPrefs.showQuizTimeOnSundayChecked(context.getApplicationContext())) {
                                            if (calendar.getTime().before(new Date(mPrefs.getSettingsEndTimeSunday(context.getApplicationContext()))) && calendar.getTime().after(new Date(mPrefs.getSettingsStartTimeSunday(context.getApplicationContext())))) {
                                                startQuiz = false;
                                            } else {
                                                startQuiz = true;
                                            }
                                        } else if (!mPrefs.showQuizOnSunday(context.getApplicationContext()) && !mPrefs.showQuizTimeOnSundayChecked(context.getApplicationContext())) {
                                            startQuiz = false;
                                        } else if (mPrefs.showQuizOnSunday(context.getApplicationContext())) {
                                            startQuiz = true;
                                        }
                                    }

                                    if (startQuiz) {
                                        Log.d(TAG, "UnlockReceiver - Unlock activity has been launched.");
                                        try {
                                            context.getApplicationContext().startActivity(s);
                                        }catch(Exception e){

                                        }
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        };
                        DataStore.getInstance().getRandomQuizVerse(myVerseCallback);
                    }
                } else if (intent.getAction().equals(Intent.ACTION_ANSWER)) {
                    String stateStr = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    if (stateStr != null && stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        mPrefs.setIsIntentFromPhoneCall(true, context.getApplicationContext());
                        Log.d(TAG, "UnlockReceiver - Intent from phone call set to false in mPrefs.");
                    }
                }
            }
        }
    }
}
