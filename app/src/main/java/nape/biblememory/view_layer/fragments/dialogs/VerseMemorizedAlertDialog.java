package nape.biblememory.view_layer.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import nape.biblememory.R;
import nape.biblememory.view_layer.activities.MainActivity;

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
        builder.setTitle("Verse Memorized!").
        setItems(R.array.verse_memorized_options, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                        mainIntent.putExtra("comingFromUnlockQuiz", true);
                        getActivity().startActivity(mainIntent);
                        mListener.callOnFinished();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
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
