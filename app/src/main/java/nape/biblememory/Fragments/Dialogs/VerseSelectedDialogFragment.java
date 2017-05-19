package nape.biblememory.Fragments.Dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Sqlite.MemoryListContract;
import nape.biblememory.UserPreferences;
import nape.biblememory.R;

/**
 * Created by OWNER-PC on 12/19/2016.
 */

public class VerseSelectedDialogFragment extends DialogFragment {

    private TextView verse;
    private TextView verseLocationTv;
    private TextView includeNextVerseTv;
    private TextView removeVerse;
    private Button add;
    private Button cancel;
    private UserPreferences mPrefs;
    private VerseOperations verseOperations;

    private String selectedVerseNum;
    private String verseText;
    private String verseLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verse_selected_dialog, container);
        verse = (TextView) view.findViewById(R.id.verseTextView);
        verseLocationTv = (TextView) view.findViewById(R.id.verseLocationTextView);
        includeNextVerseTv = (TextView) view.findViewById(R.id.include_next_verse);
        removeVerse = (TextView) view.findViewById(R.id.remove_verse);
        add = (Button) view.findViewById(R.id.addVerseButton);
        cancel = (Button) view.findViewById(R.id.cancelButton);
        mPrefs = new UserPreferences();
        selectedVerseNum = getArguments().getString("num");
        verseText = getArguments().getString("verseText");
        verseLocation = getArguments().getString("verseLocation");
        verseOperations = new VerseOperations(getActivity().getApplicationContext());

        verse.setText(verseText);
        verseLocationTv.setText(verseLocation);

        setOnclickListeners();
        return view;
    }

    private void setOnclickListeners() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScriptureData verse = new ScriptureData();
                verse.setVerse(verseText);
                verse.setVerseLocation(verseLocation);
                verseOperations.addVerse(verse, MemoryListContract.CurrentSetEntry.TABLE_NAME);
                VerseSelectedDialogFragment.this.getDialog().cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerseSelectedDialogFragment.this.getDialog().cancel();
            }
        });
    }

    public interface OnVerseAdded{
        void onVerseAdded();
    }
}
