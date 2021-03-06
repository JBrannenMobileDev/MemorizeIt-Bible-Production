package nape.biblememory.view_layer.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        mListener.callOnFinished();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String verseLocation = getArguments().getString("verseLocation");
        final String verse = getArguments().getString("verse");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Verse Memorized!").
        setItems(R.array.verse_memorized_options, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                switch(which){
                    case 0:
                        mainIntent.putExtra("launch_add_verse", true);
                        break;
                    case 1:
                        mainIntent.putExtra("launch_share", true);
                        mainIntent.putExtra("verseLocation", verseLocation);
                        mainIntent.putExtra("verse", verse);
                        break;
                    case 2:
                        mainIntent.putExtra("launch_rate", true);
                        break;
                    default:
                        mainIntent = null;
                }
                if(mainIntent != null)
                    getActivity().startActivity(mainIntent);
            }
        });
        return builder.create();
    }

    public interface YesSelected {
        void callOnFinished();
        void onHideUIControls();
    }
}
