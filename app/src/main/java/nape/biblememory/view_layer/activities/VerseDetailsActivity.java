package nape.biblememory.view_layer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.fragments.dialogs.DeleteVerseAlertDialog;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.R;

public class VerseDetailsActivity extends AppCompatActivity implements DeleteVerseAlertDialog.YesSelected{

    @BindView(R.id.verse_details_verse)TextView verseText;
    @BindView(R.id.verse_details_last_seen_tv)TextView lastSeen;
    @BindView(R.id.verse_details_progress_tv)TextView progressTv;

    private ScriptureData verse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_details);
        ButterKnife.bind(this);
        initView((ScriptureData)getIntent().getParcelableExtra("verse"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.verse_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_share) {
            sendShareIntent();
            return true;
        }
        if (id == R.id.action_reset) {
            verse.setMemoryStage(0);
            verse.setMemorySubStage(0);
            DataStore.getInstance().updateQuizVerse(verse.toMyVerse(), getApplicationContext());
            progressTv.setText("0%");
            return true;
        }
        if (id == R.id.action_delete_verse) {
            DeleteVerseAlertDialog deleteDialog = new DeleteVerseAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putString("verse_location", verse.getVerseLocation());
            deleteDialog.setArguments(bundle);
            deleteDialog.show(getSupportFragmentManager(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendShareIntent(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, verse.getVerseLocation() + "  " + "Memorize It - Bible");
            String sAux = "\n" +verse.getVerseLocation() + "  " + verse.getVerse() + "\n\n";
            sAux = sAux + "Hey! I just memorized " + verse.getVerseLocation() +  " using this app.  It gives me a quiz every time I open my phone.\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    private void initView(ScriptureData verse) {
        this.verse = verse;
        verseText.setText(verse.getVerse());
        lastSeen.setText(verse.getLastSeenDate());
        progressTv.setText(calculateProgress(verse.getMemoryStage(), verse.getMemorySubStage()) + "%");
        setTitle(Html.fromHtml("<h2>" +verse.getVerseLocation()+ "</h2>"));
    }

    private int calculateProgress(int memoryStage, int memorySubStage) {
        double progress;
        switch (memoryStage) {
            case 0:
                progress = 0;
                break;
            case 1:
                progress = 1 + memorySubStage;
                break;
            case 2:
                progress = 4 + memorySubStage;
                break;
            case 3:
                progress = 7 + memorySubStage;
                break;
            case 4:
                progress = 10 + memorySubStage;
                break;
            case 5:
                progress = 13 + memorySubStage;
                break;
            case 6:
                progress = 16 + memorySubStage;
                break;
            case 7:
                progress = 19;
                break;
            default:
                progress = 0;
        }
        progress = (progress/20)*100;
        return (int)progress;
    }

    @Override
    public void onDeleteVerse(String verseLocation) {
        DataStore.getInstance().deleteQuizVerse(new MyVerse("", verseLocation), getApplicationContext());
        finish();
    }
}
