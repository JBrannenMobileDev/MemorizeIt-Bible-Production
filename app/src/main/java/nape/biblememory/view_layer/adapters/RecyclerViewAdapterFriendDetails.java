package nape.biblememory.view_layer.adapters;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.R;

public class RecyclerViewAdapterFriendDetails extends RecyclerView.Adapter<RecyclerViewAdapterFriendDetails.ViewHolder> {
    private List<ScriptureData> mDataset;
    private List<ScriptureData> allMyVerses;
    private BaseCallback<Integer> copyVerseIndexCallback;
    public View selectedView;
    public boolean isViewSelected;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout copyLayout;
        public TextView verseLocation;
        public TextView verse;
        public TextView version;
        public ImageView expandArrow;

        public ViewHolder(View v, final BaseCallback<Integer> copyVerseIndexCallback) {
            super(v);
            verseLocation = (TextView) v.findViewById(R.id.verse_location);
            verse = (TextView) v.findViewById(R.id.verse);
            version = (TextView) v.findViewById(R.id.verse_version);
            copyLayout = (LinearLayout) v.findViewById(R.id.copy_verse_layout);
            expandArrow = (ImageView) v.findViewById(R.id.friend_details_expand_arrow);

            copyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyVerseIndexCallback.onResponse(getLayoutPosition());
                }
            });

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (v.equals(selectedView)) {
                        collapseCardView(selectedView);
                        expandArrow.animate().rotation(0);
                        selectedView = null;
                        isViewSelected = false;
                    }else{
                        if(isViewSelected) {
                            collapseCardView(selectedView);
                            expandArrow.animate().rotation(0);
                        }
                        isViewSelected = true;
                        expandCardView(v);
                        expandArrow.animate().rotation(180f);
                    }

                    if(isViewSelected){
                        selectedView = v;
                    }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterFriendDetails(List<ScriptureData> dataset, BaseCallback<Integer> copyVerseIndexCallback, List<ScriptureData> allMyVerses) {
        mDataset = dataset;
        this.allMyVerses = allMyVerses;
        this.copyVerseIndexCallback = copyVerseIndexCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterFriendDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_friend_verse, parent, false);
        ViewHolder vh = new ViewHolder(v, copyVerseIndexCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScriptureData verse = mDataset.get(position);
        holder.verseLocation.setText(verse.getVerseLocation());
        holder.verse.setText(verse.getVerse());
        if(verse.getVersionCode() == null){
            holder.version.setVisibility(View.INVISIBLE);
        }else {
            holder.version.setText("(" + verse.getVersionCode() + ")");
        }

        for(ScriptureData verseSingle : allMyVerses){
            if(verseSingle.getVerseLocation().equalsIgnoreCase(mDataset.get(position).getVerseLocation())){
                holder.copyLayout.setBackgroundResource(R.drawable.rounded_rectangle_grey_bg);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void expandCardView(View v){
        LinearLayout.LayoutParams CVLayoutParams;
        Resources r;
        int px7;
        int px4;

        CVLayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        r = v.getResources();
        px7 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                0,
                r.getDisplayMetrics()
        );
        px4 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1,
                r.getDisplayMetrics()
        );
        CVLayoutParams.setMargins(px7,px7,px7,px4);

        CardView card = (CardView) v.findViewById(R.id.card_view_friend);
        card.setLayoutParams(CVLayoutParams);
    }

    private void collapseCardView(View v){
        LinearLayout.LayoutParams CVLayoutParams;
        Resources r;
        int px7;
        int px4;

        r = v.getResources();
        CVLayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        85,
                        r.getDisplayMetrics()
                ));

        px7 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                0,
                r.getDisplayMetrics()
        );

        px4 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1,
                r.getDisplayMetrics()
        );

        CVLayoutParams.setMargins(px7,px7,px7,px4);

        CardView card = (CardView) v.findViewById(R.id.card_view_friend);
        card.setLayoutParams(CVLayoutParams);
    }
}