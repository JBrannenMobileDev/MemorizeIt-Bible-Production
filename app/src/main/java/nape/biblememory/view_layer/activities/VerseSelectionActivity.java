package nape.biblememory.view_layer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nape.biblememory.R;
import nape.biblememory.managers.NetworkManager;
import nape.biblememory.view_layer.fragments.dialogs.NoInternetAlertDialog;

public class VerseSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_selection);
        ButterKnife.bind(this);
        setTitle("Verse selection");
        Intent returnIntent = new Intent();
        setResult(0,returnIntent);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @OnClick(R.id.verse_selection_search)
    public void onSearchClicked(){
        if(NetworkManager.getInstance().isInternet(getApplicationContext())) {
            startActivity(new Intent(getApplicationContext(), FindAVerseActivity.class));
        }else{
            new NoInternetAlertDialog().show(getSupportFragmentManager(), null);
        }
    }

    @OnClick(R.id.verse_selection_popular)
    public void onPopularClicked(){
        Toast.makeText(this, "This feature is under construction.", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.verse_selection_categories)
    public void onCategoriesClicked(){
        startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
    }
}
