package nape.biblememory.view_layer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.models.UserPreferencesModel;
import nape.biblememory.utils.UserPreferences;

public class ManualEntryActivity extends AppCompatActivity {

    @BindView(R.id.manual_entry_verse_location)EditText verseLocation;
    @BindView(R.id.manual_entry_verse)EditText verse;
    @BindView(R.id.manual_entry_bible_verseion)EditText bibleVerseion;
    @BindView(R.id.manual_entry_clear)TextView clearTv;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        ButterKnife.bind(this);
        setTitle("Manual entry");
        clearTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verseLocation.setText("");
                verse.setText("");
                bibleVerseion.setText("");
            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(this, "Manual entry activity", null);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @OnClick(R.id.manual_entry_add)
    public void onAddVerseClicked(){
        if(verseLocation.getText().length() > 0 && verse.getText().length() > 0 && bibleVerseion.getText().length() > 0){
            if(verseLocation.getText().toString().contains(":")) {
                addVerse(verseLocation.getText().toString(), verse.getText().toString(), bibleVerseion.getText().toString());
                Toast.makeText(this, "Verse added!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Verse reference is missing a : between the book number and verse number. ", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Form is incomplete!", Toast.LENGTH_LONG).show();
        }
    }

    private void addVerse(String verseLocation, String verse, String verseion) {
        UserPreferences mPrefs = new UserPreferences();
        ScriptureData newVerse = new ScriptureData();
        newVerse.setVerseLocation(sanitizeUserInput(verseLocation));
        newVerse.setVerse(sanitizeUserInput(verse));
        newVerse.setVersionCode(sanitizeUserInput(verseion));
        newVerse.setMemoryStage(0);
        newVerse.setMemorySubStage(0);
        RealmResults<MyVerse> quizVerses = Realm.getDefaultInstance().where(MyVerse.class).findAll();
        newVerse.setListPosition(quizVerses.size());
        if(quizVerses.size() < 3){
            newVerse.setGoldStar(1);
        }
        Bundle bundle = new Bundle();
        bundle.putString("verse_added_manual", newVerse.getVerseLocation());
        mFirebaseAnalytics.logEvent("verse_added_manual", bundle);
        DataStore.getInstance().saveQuizVerse(newVerse, getApplicationContext());
        mPrefs.setTourStep1Complete(true, getApplicationContext());
        UserPreferencesModel model = new UserPreferencesModel();
        model.initAllData(getApplicationContext(), mPrefs);
        DataStore.getInstance().saveUserPrefs(model, getApplicationContext());
    }

    private String sanitizeUserInput(String userInput){
        StringBuilder temp = new StringBuilder(userInput);
        while(temp.charAt(0) == ' '){
            temp = new StringBuilder(temp.substring(1));
        }
        while(temp.charAt(temp.length() - 1) == ' '){
            temp = new StringBuilder(temp.substring(0, temp.length() - 1));
        }
        for (int i = 0 ; i< temp.length() ; i++) {
            if (temp.charAt(i) == '\n' || temp.charAt(i) == '\t') {
                temp.deleteCharAt(i);
            }
        }
        return temp.toString();
    }
}
