package nape.biblememory.view_layer.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;

import nape.biblememory.R;

/**
 * Created by Jonathan on 5/3/2016.
 */
public class FirstTimeUnlockDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("First Time Info");
        builder.setMessage(R.string.first_time_unlock_toast)
                .setPositiveButton("Okay, Got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        return builder.create();
    }
}
