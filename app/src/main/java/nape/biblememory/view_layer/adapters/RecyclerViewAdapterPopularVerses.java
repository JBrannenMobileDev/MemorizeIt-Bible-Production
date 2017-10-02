package nape.biblememory.view_layer.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import nape.biblememory.R;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.User;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.view_layer.activities.VerseDetailsActivity;

public class RecyclerViewAdapterPopularVerses extends RecyclerView.Adapter<RecyclerViewAdapterPopularVerses.ViewHolder> {

    private List<MyVerse> mDataSet;
    private BaseCallback<MyVerse> verseSelectedCallback;
    private BaseCallback<MyVerse> verseAddSelectedCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView verseLocation;
        private TextView verse;
        private TextView popularityNumber;
        private FrameLayout verseLayout;
        private ImageView verseAddBt;


        public ViewHolder(View v) {
            super(v);
            verseLayout = (FrameLayout) v.findViewById(R.id.verse_item_layout);
            verseLocation = (TextView) v.findViewById(R.id.verse_location_tv);
            verse = (TextView) v.findViewById(R.id.verse_tv);
            popularityNumber = (TextView) v.findViewById(R.id.verse_position_tv);
            verseAddBt = (ImageView) v.findViewById(R.id.verse_plus_image);

            verseLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verseSelectedCallback.onResponse(mDataSet.get(getLayoutPosition()));
                }
            });

            verseAddBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verseAddSelectedCallback.onResponse(mDataSet.get(getLayoutPosition()));
                }
            });
        }
    }

    public RecyclerViewAdapterPopularVerses(List<MyVerse> dataset, BaseCallback<MyVerse> verseSelectedCallback, BaseCallback<MyVerse> verseAddSelectedCallback) {
        mDataSet = dataset;
        this.verseSelectedCallback = verseSelectedCallback;
        this.verseAddSelectedCallback = verseAddSelectedCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterPopularVerses.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_verses_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MyVerse> myVerses = realm.where(MyVerse.class).findAll();
        RealmResults<MemorizedVerse> memorizedVerses = realm.where(MemorizedVerse.class).findAll();
        boolean alreadyHas = false;
        for(MyVerse verseLocal : myVerses){
            if(mDataSet.get(position).getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                alreadyHas = true;
            }
        }

        for(MemorizedVerse verseLocal : memorizedVerses){
            if(mDataSet.get(position).getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                alreadyHas = true;
            }
        }
        holder.verseLocation.setText(mDataSet.get(position).getVerseLocation());
        holder.verse.setText(mDataSet.get(position).getVerse());
        holder.popularityNumber.setText(String.valueOf(position));
        if(alreadyHas){
            holder.verse.setTextColor(Color.argb(255,0,114,152));
            holder.verseLocation.setTextColor(Color.argb(255,0,114,152));
            holder.verseAddBt.setVisibility(View.GONE);
        }else{
            holder.verse.setTextColor(Color.argb(255,187,186,186));
            holder.verseLocation.setTextColor(Color.argb(255,187,186,186));
            holder.verseAddBt.setVisibility(View.VISIBLE);
        }
        realm.close();
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}