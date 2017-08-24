package nape.biblememory.Fragments.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by jbrannen on 6/14/17.
 */

public class FriendAddedAlertDialog extends DialogFragment {

    public FriendAddedAlertDialog(){

    }

    private requestTabActions mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mListener = (requestTabActions)getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String userName = getArguments().getString("user_name");
        builder.setTitle("Friend request has been sent to " + userName);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onFriendAdded();
                dismiss();
            }
        });
        return builder.create();
    }

    public interface requestTabActions{
        void onFriendAdded();
    }
}
