package nape.biblememory.Activities.Views;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.faithcomesbyhearing.dbt.model.Verse;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Activities.DBTApi.DBTApi;
import nape.biblememory.Activities.UserPreferences;
import nape.biblememory.R;

/**
 * Created by OWNER-PC on 12/19/2016.
 */

public class VerseSelectedDialogFragment extends DialogFragment {

    private TextView verse;
    private Button add;
    private Button cancel;
    private UserPreferences mPrefs;
    private DBTApi REST;
    private BaseCallback<Verse> selectedVerseCallback;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verse_selected_dialog, container);
        verse = (TextView) view.findViewById(R.id.verseTextView);
        add = (Button) view.findViewById(R.id.addVerseButton);
        cancel = (Button) view.findViewById(R.id.cancelButton);
        REST = new DBTApi(getActivity().getApplicationContext());
        context = getActivity().getApplicationContext();
        mPrefs = new UserPreferences();

        selectedVerseCallback = new BaseCallback<Verse>() {
            @Override
            public void onResponse(Verse response) {
                verse.setText(response.getVerseText());
            }

            @Override
            public void onFailure(Exception e) {
                Exception t = e;
            }
        };

        REST.getVerse(selectedVerseCallback, mPrefs.getDamId(context)+"OT", mPrefs.getSelectedBookId(context), mPrefs.getSelectedVerseNum(context), mPrefs.getSelectedChapter(context));

        setOnclickListeners();
        getDialog().setTitle("Add To My Verses List");

        return view;
    }

    private void setOnclickListeners() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
