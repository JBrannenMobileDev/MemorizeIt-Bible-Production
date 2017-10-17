package nape.biblememory.view_layer.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.R;

public class RecyclerViewAdapterMemorized extends RecyclerView.Adapter<RecyclerViewAdapterMemorized.ViewHolder> {

    private List<MemorizedVerse> mDataSet;
    private BaseCallback<MemorizedVerse> itemSelectedCallback;
    private BaseCallback<MemorizedVerse> shareSelectedCallback;
    private BaseCallback<MemorizedVerse> reviewNowSelectedCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView verseLocation;
        public TextView verse;
        public TextView alertMsg;
        public ImageView alertIcon;
        public ImageView shareIcon;
        private FrameLayout itemLayout;
        private FrameLayout reviewNowLayout;

        public ViewHolder(View v) {
            super(v);
            verseLocation = (TextView) v.findViewById(R.id.memorized_verse_location);
            verse = (TextView) v.findViewById(R.id.memorized_verse);
            shareIcon = (ImageView) v.findViewById(R.id.share_verse_icon);
            itemLayout = (FrameLayout) v.findViewById(R.id.memorized_verses_layout);
            reviewNowLayout = (FrameLayout) v.findViewById(R.id.review_now_layout);
            alertIcon = (ImageView) v.findViewById(R.id.review_alert_image);
            alertMsg = (TextView) v.findViewById(R.id.review_alert_text);

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

            reviewNowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reviewNowSelectedCallback.onResponse(mDataSet.get(getLayoutPosition()));
                }
            });
        }
    }

    public RecyclerViewAdapterMemorized(List<MemorizedVerse> dataset, BaseCallback<MemorizedVerse> itemSelectedCallback,
                                        BaseCallback<MemorizedVerse> shareSelectedCallback, BaseCallback<MemorizedVerse> reviewNowSelectedCallback) {
        mDataSet = dataset;
        this.itemSelectedCallback = itemSelectedCallback;
        this.shareSelectedCallback = shareSelectedCallback;
        this.reviewNowSelectedCallback = reviewNowSelectedCallback;
    }

    @Override
    public RecyclerViewAdapterMemorized.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memorized_verse_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Calendar cal = Calendar.getInstance();
        Calendar reviewDatePlusOneMonth = Calendar.getInstance();
        Calendar reviewDatePlusOneWeek = Calendar.getInstance();
        Date lastReviewed = null;
        try {
            lastReviewed = formatter.parse(mDataSet.get(position).getLastSeenDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(lastReviewed != null) {
            reviewDatePlusOneMonth.setTime(lastReviewed);
            reviewDatePlusOneMonth.add(Calendar.DATE, 30);
            reviewDatePlusOneWeek.setTime(lastReviewed);
            reviewDatePlusOneWeek.add(Calendar.DATE, 7);
        }
        if(mDataSet.get(position).isForgotten()){
            holder.verseLocation.setText(mDataSet.get(position).getVerseLocation());
            holder.verse.setText("Forgotten!");
            holder.verseLocation.setTextColor(Color.argb(255, 255, 68, 0));
            holder.verse.setTextColor(Color.argb(255, 255, 68, 0));
            holder.shareIcon.setVisibility(View.GONE);
            holder.reviewNowLayout.setVisibility(View.VISIBLE);
        }else if(lastReviewed != null && reviewDatePlusOneMonth.before(cal)) {
            holder.verseLocation.setText(mDataSet.get(position).getVerseLocation());
            holder.verse.setText("Review past due!");
            holder.verseLocation.setTextColor(Color.argb(255, 255, 153, 0));
            holder.verse.setTextColor(Color.argb(255, 255, 153, 0));
            holder.shareIcon.setVisibility(View.GONE);
            holder.reviewNowLayout.setVisibility(View.VISIBLE);
            holder.alertIcon.setColorFilter(Color.argb(255, 255, 153, 0));
            holder.alertMsg.setTextColor(Color.argb(255, 255, 153, 0));
        }else if(lastReviewed != null && reviewDatePlusOneWeek.before(cal)) {
            holder.verseLocation.setText(mDataSet.get(position).getVerseLocation());
            holder.verse.setText("Review due!");
            holder.verseLocation.setTextColor(Color.argb(255, 255, 213, 1));
            holder.verse.setTextColor(Color.argb(255, 255, 213, 1));
            holder.shareIcon.setVisibility(View.GONE);
            holder.reviewNowLayout.setVisibility(View.VISIBLE);
            holder.alertIcon.setColorFilter(Color.argb(255, 255, 213, 1));
            holder.alertMsg.setTextColor(Color.argb(255, 255, 213, 1));
        }else{
            holder.verseLocation.setText(mDataSet.get(position).getVerseLocation());
            holder.verse.setText(mDataSet.get(position).getVerse());
            holder.verseLocation.setTextColor(Color.argb(255, 1, 81, 107));
            holder.verse.setTextColor(Color.argb(255, 1, 81, 107));
            holder.shareIcon.setVisibility(View.VISIBLE);
            holder.reviewNowLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}