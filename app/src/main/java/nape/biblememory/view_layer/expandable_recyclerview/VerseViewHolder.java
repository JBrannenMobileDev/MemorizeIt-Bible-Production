package nape.biblememory.view_layer.expandable_recyclerview;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.view_layer.activities.CategoriesActivity;

/**
 * Created by jbrannen on 9/27/17.
 */

public class VerseViewHolder extends ChildViewHolder {
    @BindView(R.id.verse_tv) TextView verseTv;
    @BindView(R.id.verse_location_tv) TextView verseLocationTv;
    @BindView(R.id.verse_plus_image) ImageView addBt;
    private BaseCallback<MyVerse> addVerseSelected;
    private BaseCallback<MyVerse> verseSelected;
    private MyVerse verse;

    public VerseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.verse_plus_image)
    public void onAddVerseClicked(){
        if(verse == null){
            addVerseSelected.onFailure(new Exception("verse from Category verse added is null."));
        }else {
            addVerseSelected.onResponse(verse);
        }
    }

    @OnClick(R.id.verse_item_layout)
    public void onItemClicked(){
        if(verse == null){
            verseSelected.onFailure(new Exception("verse is null, cannot access verse details."));
        }else {
            verseSelected.onResponse(verse);
        }
    }

    public void onBind(MyVerse verse, BaseCallback<MyVerse> addVerseSelected, BaseCallback<MyVerse> verseSelected) {
        this.addVerseSelected = addVerseSelected;
        this.verseSelected = verseSelected;
        this.verse = verse;

        verseTv.setText(verse.getVerse());
        verseLocationTv.setText(verse.getVerseLocation());

        if(doesUserHaveVerse(verse)){
            verseTv.setTextColor(Color.argb(255,0,114,152));
            verseLocationTv.setTextColor(Color.argb(255,0,114,152));
            addBt.setVisibility(View.GONE);
        }else{
            verseTv.setTextColor(Color.argb(255,187,186,186));
            verseLocationTv.setTextColor(Color.argb(255,187,186,186));
            addBt.setVisibility(View.VISIBLE);
        }
    }

    private boolean doesUserHaveVerse(MyVerse verse){
        boolean alreadyHasVerse = false;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MyVerse> myVerses = realm.where(MyVerse.class).findAll();
        RealmResults<MemorizedVerse> memorizedVerses = realm.where(MemorizedVerse.class).findAll();

        //Checks users verse list to see if they already have the verse.
        for(MyVerse verseLocal : myVerses){
            if(verse.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                alreadyHasVerse = true;
            }
        }

        //Checks users memorized verse list to see if they already have the verse.
        for(MemorizedVerse verseLocal : memorizedVerses){
            if(verse.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                alreadyHasVerse = true;
            }
        }
        realm.close();
        return alreadyHasVerse;
    }
}
