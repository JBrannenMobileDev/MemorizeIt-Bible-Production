package nape.biblememory.view_layer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nape.biblememory.R;

public class VerseSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_selection);
        ButterKnife.bind(this);
        setTitle("Verse selection");
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @OnClick(R.id.verse_selection_search)
    public void onSearchClicked(){

    }

    @OnClick(R.id.verse_selection_popular)
    public void onPopularClicked(){

    }

    @OnClick(R.id.verse_selection_categories)
    public void onCategoriesClicked(){

    }
}
