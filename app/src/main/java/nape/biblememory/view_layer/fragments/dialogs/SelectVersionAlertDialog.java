package nape.biblememory.view_layer.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import nape.biblememory.utils.UserPreferences;

/**
 * Created by jbrannen on 6/14/17.
 */

public class SelectVersionAlertDialog extends DialogFragment {

    private VersionSelected mListener;
    private String verseNum;

    public SelectVersionAlertDialog(){}

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (VersionSelected) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments().getString("verse_num") != null){
            verseNum = getArguments().getString("verse_num");
        }
        final ArrayList<String> displayValues=new ArrayList<>();
        displayValues.add("English Standard Version (ESV)");
        displayValues.add("World English Bible (WEB)");
        displayValues.add("King James Version (KJV)");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,displayValues);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a version");
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserPreferences mPrefs = new UserPreferences();
                String versionCode = "ESV";
                switch(which){
                    case 0:
                        versionCode = "ESV";
                        break;
                    case 1:
                        versionCode = "WEB";
                        break;
                    case 2:
                        versionCode = "KJV";
                }
                mPrefs.setTempSelectedVersion(versionCode, getActivity().getApplicationContext());
                mListener.onVersionSelectedFromDialog(verseNum);
                dismiss();
            }
        });
        return builder.create();
    }

    public interface VersionSelected {
        void onVersionSelectedFromDialog(String verseNum);
    }
}
