package nape.biblememory.view_layer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.ScriptureData;

public class ManualEntryActivity extends AppCompatActivity {

    @BindView(R.id.manual_entry_verse_location)EditText verseLocation;
    @BindView(R.id.manual_entry_verse)EditText verse;
    @BindView(R.id.manual_entry_bible_verseion)EditText bibleVerseion;
    @BindView(R.id.manual_entry_clear)TextView clearTv;

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
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @OnClick(R.id.manual_entry_add)
    public void onAddVerseClicked(){
        if(verseLocation.getText().length() > 0 && verse.getText().length() > 0 && bibleVerseion.getText().length() > 0){
            addVerse(verseLocation.getText().toString(), verse.getText().toString(), bibleVerseion.getText().toString());
            Toast.makeText(this, "Verse added!", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "Form is incomplete!", Toast.LENGTH_LONG).show();
        }
    }

    private void addVerse(String verseLocation, String verse, String verseion) {
        ScriptureData newVerse = new ScriptureData();
        newVerse.setVerseLocation(verseLocation);
        newVerse.setVerse(verse);
        newVerse.setVersionCode(verseion);
        newVerse.setMemoryStage(0);
        newVerse.setMemorySubStage(0);
        DataStore.getInstance().saveQuizVerse(newVerse, getApplicationContext());
    }
}
