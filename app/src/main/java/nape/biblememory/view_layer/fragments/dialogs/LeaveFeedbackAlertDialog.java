package nape.biblememory.view_layer.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.UserPreferencesModel;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.activities.SendFeedbackActivity;

/**
 * Created by jbrannen on 6/14/17.
 */

public class LeaveFeedbackAlertDialog extends DialogFragment {

    public LeaveFeedbackAlertDialog(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Would you like to leave us feedback so that we can improve our usefulness?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateUserPreference();
                Intent intent = new Intent(getActivity(), SendFeedbackActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateUserPreference();
                dismiss();
            }
        });
        return builder.create();
    }

    private void updateUserPreference(){
        UserPreferences mPrefs = new UserPreferences();
        mPrefs.setHowAreWeDoingDialogShown(true, getActivity().getApplicationContext());
        UserPreferencesModel model = new UserPreferencesModel();
        model.initAllData(getActivity().getApplicationContext(), mPrefs);
        DataStore.getInstance().saveUserPrefs(model, getActivity().getApplicationContext());
    }
}
