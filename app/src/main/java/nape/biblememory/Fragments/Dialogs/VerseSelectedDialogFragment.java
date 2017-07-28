package nape.biblememory.Fragments.Dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.faithcomesbyhearing.dbt.model.Verse;

import nape.biblememory.Activities.BaseCallback;
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
    private Button confirm;
    private Button cancel;
    private UserPreferences mPrefs;
    private VerseOperations verseOperations;
    private CheckBox addVerseToInProgress;

    private String initialSelectedVerseNum;
    private long currentSelectedVerse;
    private String previousVerseNum;
    private String verseText;
    private String verseLocation;
    private long numOfVersesInChapter;
    private addVerseDialogActions dialogActionsListener;
    private BaseCallback<Verse> includeNextVerseCallback;
    private VerseOperations vOperations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verse_selected_dialog, container);
        verse = (TextView) view.findViewById(R.id.verseTextView);
        verseLocationTv = (TextView) view.findViewById(R.id.verseLocationTextView);
        includeNextVerseTv = (TextView) view.findViewById(R.id.include_next_verse);
        confirm = (Button) view.findViewById(R.id.addVerseButton);
        cancel = (Button) view.findViewById(R.id.cancelButton);
        addVerseToInProgress = (CheckBox) view.findViewById(R.id.addVerseToInProgress);
        mPrefs = new UserPreferences();
        vOperations = new VerseOperations(getActivity().getApplicationContext());
        if(vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME).size() > 2){
            addVerseToInProgress.setVisibility(View.GONE);
        }
        initialSelectedVerseNum = getArguments().getString("num");
        previousVerseNum = initialSelectedVerseNum;
        verseText = getArguments().getString("verseText");
        verseLocation = getArguments().getString("verseLocation");
        numOfVersesInChapter = getArguments().getLong("numOfVersesInChapter");
        verseOperations = new VerseOperations(getActivity().getApplicationContext());
        verse.setText(verseText);
        verseLocationTv.setText(verseLocation);
        dialogActionsListener = (addVerseDialogActions) getActivity();
        setOnclickListeners();
        currentSelectedVerse = Long.valueOf(initialSelectedVerseNum);
        if(currentSelectedVerse == numOfVersesInChapter){
            includeNextVerseTv.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private void setOnclickListeners() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScriptureData verse = new ScriptureData();
                verse.setVerse(verseText);
                verse.setVerseLocation(verseLocation);
                if(addVerseToInProgress.isChecked()) {
                    vOperations.addVerse(verse, MemoryListContract.LearningSetEntry.TABLE_NAME);
                }else{
                    verseOperations.addVerse(verse, MemoryListContract.CurrentSetEntry.TABLE_NAME);
                }
                dialogActionsListener.onVerseAdded();
                VerseSelectedDialogFragment.this.getDialog().cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerseSelectedDialogFragment.this.getDialog().cancel();
            }
        });

        includeNextVerseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogActionsListener.includeNextVerseSelected(verseLocation, includeNextVerseCallback, previousVerseNum);
            }
        });

        includeNextVerseCallback = new BaseCallback<Verse>() {
            @Override
            public void onResponse(Verse response) {
                verseText = removeExtrasSpaces(verseText + response.getVerseText());
                verseLocation = generateCombinedVerseLocations(response.getVerseId());
                verse.setText(verseText);
                verseLocationTv.setText(verseLocation);
                previousVerseNum = response.getVerseId();
                currentSelectedVerse = Long.valueOf(response.getVerseId());
                if(currentSelectedVerse == numOfVersesInChapter){
                    includeNextVerseTv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    private String removeExtrasSpaces(String s) {
        return s.replaceAll("\\s{2,}"," ");
    }

    private String generateCombinedVerseLocations(String verseId) {
        return mPrefs.getSelectedBook(getActivity().getApplicationContext()) + " " +
                mPrefs.getSelectedChapter(getActivity().getApplicationContext()) + ":" + initialSelectedVerseNum + "-" + verseId;
    }

    public interface addVerseDialogActions {
        void onVerseAdded();
        void includeNextVerseSelected(String verseLocation, BaseCallback<Verse> callback, String selectedVerseNum);
    }
}
