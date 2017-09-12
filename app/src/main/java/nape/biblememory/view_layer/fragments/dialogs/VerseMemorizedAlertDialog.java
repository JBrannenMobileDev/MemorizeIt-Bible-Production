package nape.biblememory.view_layer.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by jbrannen on 6/14/17.
 */

public class VerseMemorizedAlertDialog extends DialogFragment {

    private YesSelected mListener;

    public VerseMemorizedAlertDialog(){

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (VerseMemorizedAlertDialog.YesSelected) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final boolean callFinish = getArguments().getBoolean("callOnFinish", true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Verse Memorized!");
        builder.setMessage("Good job! To view or review your memorized verses, please visit the Memorized page. ");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(callFinish) {
                    mListener.callOnFinished();
                }else{
                    mListener.onHideUIControls();
                }
                dismiss();
            }
        });
        return builder.create();
    }

    public interface YesSelected {
        void callOnFinished();
        void onHideUIControls();
    }
}
