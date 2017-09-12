package nape.biblememory.view_layer.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.R;

public class RecyclerViewAdapterMemorized extends RecyclerView.Adapter<RecyclerViewAdapterMemorized.ViewHolder> {

    private List<MemorizedVerse> mDataSet;
    private BaseCallback<MemorizedVerse> itemSelectedCallback;
    private BaseCallback<MemorizedVerse> shareSelectedCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView verseLocation;
        public TextView verse;
        public ImageView shareIcon;
        private FrameLayout itemLayout;

        public ViewHolder(View v) {
            super(v);
            verseLocation = (TextView) v.findViewById(R.id.memorized_verse_location);
            verse = (TextView) v.findViewById(R.id.memorized_verse);
            shareIcon = (ImageView) v.findViewById(R.id.share_verse_icon);
            itemLayout = (FrameLayout) v.findViewById(R.id.memorized_verses_layout);

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemSelectedCallback.onResponse(mDataSet.get(getLayoutPosition()));
                }
            });

            shareIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareSelectedCallback.onResponse(mDataSet.get(getLayoutPosition()));
                }
            });
        }
    }

    public RecyclerViewAdapterMemorized(List<MemorizedVerse> dataset, BaseCallback<MemorizedVerse> itemSelectedCallback, BaseCallback<MemorizedVerse> shareSelectedCallback) {
        mDataSet = dataset;
        this.itemSelectedCallback = itemSelectedCallback;
        this.shareSelectedCallback = shareSelectedCallback;
    }

    @Override
    public RecyclerViewAdapterMemorized.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memorized_verse_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mDataSet.get(position).isForgotten()){
            holder.verseLocation.setText(mDataSet.get(position).getVerseLocation());
            holder.verse.setText(mDataSet.get(position).getVerse());
            holder.verseLocation.setTextColor(Color.argb(255, 218, 0, 0));
            holder.verse.setTextColor(Color.argb(255, 218, 0, 0));
            holder.shareIcon.setVisibility(View.GONE);
        }else {
            holder.verseLocation.setText(mDataSet.get(position).getVerseLocation());
            holder.verse.setText(mDataSet.get(position).getVerse());
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}