package nape.biblememory.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

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
                        Log.d(TAG, "UnlockReceiver - Unlock activity has been launched.");
                        context.startActivity(s);
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
