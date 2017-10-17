package nape.biblememory.view_layer.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.UserPreferencesModel;
import nape.biblememory.utils.UserPreferences;

/**
 * Created by jbrannen on 6/14/17.
 */

public class QuestionUserAlertDialog extends DialogFragment {

    public QuestionUserAlertDialog(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Have we been helpful with memorizing verses?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new RateUsAlertDialog().show(getFragmentManager(), null);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new LeaveFeedbackAlertDialog().show(getFragmentManager(), null);
            }
        });
        return builder.create();
    }
}
