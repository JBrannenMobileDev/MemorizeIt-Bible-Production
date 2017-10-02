package nape.biblememory.view_layer.expandable_recyclerview;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

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
    private TextView verseTv;
    private TextView verseLocationTv;
    private FrameLayout verseLayout;
    private ImageView addBt;
    private BaseCallback<MyVerse> addVerseSelected;
    private BaseCallback<MyVerse> verseSelected;
    private MyVerse verse;

    public VerseViewHolder(View itemView) {
        super(itemView);
        verseTv = (TextView)itemView.findViewById(R.id.verse_tv);
        verseLocationTv = (TextView)itemView.findViewById(R.id.verse_location_tv);
        addBt = (ImageView)itemView.findViewById(R.id.verse_plus_image);
        verseLayout = (FrameLayout)itemView.findViewById(R.id.verse_item_layout);
        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVerseSelected.onResponse(verse);
            }
        });
        verseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verseSelected.onResponse(verse);
            }
        });
    }

    public void onBind(MyVerse verse, BaseCallback<MyVerse> addVerseSelected, BaseCallback<MyVerse> verseSelected) {
        this.addVerseSelected = addVerseSelected;
        this.verseSelected = verseSelected;
        this.verse = verse;
        verseTv.setText(verse.getVerse());
        verseLocationTv.setText(verse.getVerseLocation());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MyVerse> myVerses = realm.where(MyVerse.class).findAll();
        RealmResults<MemorizedVerse> memorizedVerses = realm.where(MemorizedVerse.class).findAll();
        boolean alreadyHas = false;
        for(MyVerse verseLocal : myVerses){
            if(verse.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                alreadyHas = true;
            }
        }

        for(MemorizedVerse verseLocal : memorizedVerses){
            if(verse.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                alreadyHas = true;
            }
        }
        if(alreadyHas){
           verseTv.setTextColor(Color.argb(255,0,114,152));
           verseLocationTv.setTextColor(Color.argb(255,0,114,152));
           addBt.setVisibility(View.GONE);
        }else{
            verseTv.setTextColor(Color.argb(255,187,186,186));
            verseLocationTv.setTextColor(Color.argb(255,187,186,186));
            addBt.setVisibility(View.VISIBLE);
        }
        realm.close();

    }
}
