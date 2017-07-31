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
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

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
    private String previousVerseLocation;
    private long numOfVersesInChapter;
    private addVerseDialogActions dialogActionsListener;
    private BaseCallback<Verse> includeNextVerseCallback;
    private String bookName;
    private String chapter;
    private boolean comingFromNewVerses;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        initialSelectedVerseNum = getArguments().getString("num");
        previousVerseNum = initialSelectedVerseNum;
        verseText = getArguments().getString("verseText");
        verseLocation = getArguments().getString("verseLocation");
        previousVerseLocation = verseLocation;
        numOfVersesInChapter = getArguments().getLong("numOfVersesInChapter");
        bookName = getArguments().getString("book_name");
        chapter = getArguments().getString("chapter");
        comingFromNewVerses = getArguments().getBoolean("comingFromNew");
        verseOperations = new VerseOperations(getActivity().getApplicationContext());
        if(verseOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME).size() > 2){
            addVerseToInProgress.setVisibility(View.GONE);
        }
        verse.setText(verseText);
        verseLocationTv.setText(verseLocation);
        dialogActionsListener = (addVerseDialogActions) getActivity();
        setOnclickListeners();
        currentSelectedVerse = Long.valueOf(initialSelectedVerseNum);
        if(currentSelectedVerse == numOfVersesInChapter){
            includeNextVerseTv.setVisibility(View.INVISIBLE);
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Verse selection dialog", null);
        return view;
    }

    private void setOnclickListeners() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScriptureData verse = new ScriptureData();
                boolean verseAlreadyExists = false;
                verse.setVerse(verseText);
                verse.setVerseLocation(verseLocation);
                verse.setVerseNumber(initialSelectedVerseNum);
                verse.setBookName(mPrefs.getSelectedBook(getActivity().getApplicationContext()));
                verse.setChapter(mPrefs.getSelectedChapter(getActivity().getApplicationContext()));
                Bundle bundle = new Bundle();
                bundle.putString("verse_added", verse.getVerseLocation());
                mFirebaseAnalytics.logEvent("verse_added", bundle);
                List<ScriptureData> currentList = verseOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
                List<ScriptureData> learningList = verseOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME);
                List<ScriptureData> rememberedList = verseOperations.getVerseSet(MemoryListContract.RememberedSetEntry.TABLE_NAME);
                for(ScriptureData verseCurrent : currentList){
                    if(verseCurrent.getVerseLocation().equalsIgnoreCase(verse.getVerseLocation())){
                        verseAlreadyExists = true;
                    }
                }
                for(ScriptureData verseLearning : learningList){
                    if(verseLearning.getVerseLocation().equalsIgnoreCase(verse.getVerseLocation())){
                        verseAlreadyExists = true;
                    }
                }
                for(ScriptureData verseRemembered : rememberedList){
                    if(verseRemembered.getVerseLocation().equalsIgnoreCase(verse.getVerseLocation())){
                        verseAlreadyExists = true;
                    }
                }

                if(!verseAlreadyExists) {
                    if (addVerseToInProgress.isChecked()) {
                        mFirebaseAnalytics.logEvent("verse_added_to_learning_list", bundle);
                        verseOperations.addVerse(verse, MemoryListContract.LearningSetEntry.TABLE_NAME);
                    } else {
                        verseOperations.addVerse(verse, MemoryListContract.CurrentSetEntry.TABLE_NAME);
                    }
                    if (comingFromNewVerses) {
                        verseOperations.removeVerse(previousVerseLocation, MemoryListContract.CurrentSetEntry.TABLE_NAME);
                    }
                    dialogActionsListener.onVerseAdded(comingFromNewVerses);
                    VerseSelectedDialogFragment.this.getDialog().cancel();
                }else{
                    new VerseAlreadyExistsAlertDialog().show(getChildFragmentManager(), null);
                }
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
                Bundle bundle = new Bundle();
                bundle.putString("include_next_verse", verseLocation);
                mFirebaseAnalytics.logEvent("include_next_verse", bundle);
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
        if(!comingFromNewVerses) {
            if (bookName != null && chapter != null) {
                return bookName + " " + chapter + ":" + initialSelectedVerseNum + "-" + verseId;
            }
            return mPrefs.getSelectedBook(getActivity().getApplicationContext()) + " " +
                    mPrefs.getSelectedChapter(getActivity().getApplicationContext()) + ":" + initialSelectedVerseNum + "-" + verseId;
        }else{
            return mPrefs.getSelectedBook(getActivity().getApplicationContext()) + " " +
                    mPrefs.getSelectedChapter(getActivity().getApplicationContext()) + ":" + getStartVerseNum(initialSelectedVerseNum) + "-" + verseId;
        }
    }

    private String getStartVerseNum(String initialSelectedVerseNum) {
        String result = null;
        if(!initialSelectedVerseNum.contains("-")){
            return initialSelectedVerseNum;
        }
        return result;
    }

    public interface addVerseDialogActions {
        void onVerseAdded(boolean comingFromNewVerses);
        void includeNextVerseSelected(String verseLocation, BaseCallback<Verse> callback, String selectedVerseNum);
    }
}
