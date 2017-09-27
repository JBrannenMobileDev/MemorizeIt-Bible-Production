package nape.biblememory.view_layer.expandable_recyclerview;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import nape.biblememory.R;
import nape.biblememory.models.MyVerse;

/**
 * Created by jbrannen on 9/27/17.
 */

public class VerseViewHolder extends ChildViewHolder {
    private TextView verseTv;
    private TextView verseLocationTv;

    public VerseViewHolder(View itemView) {
        super(itemView);
        verseTv = (TextView)itemView.findViewById(R.id.verse_tv);
        verseLocationTv = (TextView)itemView.findViewById(R.id.verse_location_tv);
    }

    public void onBind(MyVerse verse) {
        verseTv.setText(verse.getVerse());
        verseLocationTv.setText(verse.getVerseLocation());
    }
}
