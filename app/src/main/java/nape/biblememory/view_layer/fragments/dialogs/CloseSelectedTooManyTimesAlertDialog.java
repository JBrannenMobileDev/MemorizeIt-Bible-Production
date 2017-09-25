package nape.biblememory.view_layer.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import nape.biblememory.R;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.activities.MainActivity;
import nape.biblememory.view_layer.activities.QuizSettingsActivity;

/**
 * Created by jbrannen on 6/14/17.
 */

public class CloseSelectedTooManyTimesAlertDialog extends DialogFragment {

    private YesSelected mListener;

    public CloseSelectedTooManyTimesAlertDialog(){

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CloseSelectedTooManyTimesAlertDialog.YesSelected) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        mListener.callOnFinished();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("We noticed you selected Close multiple times!").setMessage("Would you like to change how often the quiz appears?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().startActivity(new Intent(getActivity().getApplicationContext(), QuizSettingsActivity.class));
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }

    public interface YesSelected {
        void callOnFinished();
    }
}
