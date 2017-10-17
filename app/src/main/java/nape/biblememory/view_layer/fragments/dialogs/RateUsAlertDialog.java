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
import nape.biblememory.view_layer.activities.MainActivity;

/**
 * Created by jbrannen on 6/14/17.
 */

public class RateUsAlertDialog extends DialogFragment {

    public RateUsAlertDialog(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Can you please give us a rating?");
        builder.setMessage("Your review lets potential new users know if MemorizeIt-Bible is worth using!");
        builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateUserPreference();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("launch_rate", true);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Not right now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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
