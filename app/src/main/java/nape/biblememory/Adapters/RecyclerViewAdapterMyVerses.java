package nape.biblememory.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.R;

public class RecyclerViewAdapterMyVerses extends RecyclerView.Adapter<RecyclerViewAdapterMyVerses.ViewHolder> {
    private List<ScriptureData> mDataset;
    private int mTabPosition;
    private BaseCallback removeCallback;
    private BaseCallback moveCallback;
    private BaseCallback editCallback;
    public View selectedView;
    public boolean isViewSelected;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ProgressBar pb;
        public TextView progressPercent;
        public TextView verseLocation;
        public TextView verse;
        public TextView version;
        public RelativeLayout removeLayout;
        public LinearLayout moveLayout;
        public ImageView expandImage;

        public ViewHolder(View v, final int mPosition, final BaseCallback rCallback, final BaseCallback mCallback, final BaseCallback eCallback) {
            super(v);
            verseLocation = (TextView) v.findViewById(R.id.verse_location);
            verse = (TextView) v.findViewById(R.id.verse);
            pb = (ProgressBar) v.findViewById(R.id.progressBar);
            progressPercent = (TextView) v.findViewById(R.id.progress_percent);
            version = (TextView) v.findViewById(R.id.verse_version);
            expandImage = (ImageView) v.findViewById(R.id.expand_image);

            if(mPosition == 1){
                removeLayout = (RelativeLayout) v.findViewById(R.id.linearLayoutRemove);
            }else{
                removeLayout = (RelativeLayout) v.findViewById(R.id.removeLayout);
                moveLayout = (LinearLayout) v.findViewById(R.id.moveLayout);
            }

            if(removeLayout != null) {
                removeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rCallback.onResponse(verseLocation.getText());
                    }
                });
            }

            if(moveLayout != null ){
                moveLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScriptureData scriptureData = new ScriptureData();
                        scriptureData.setVerseLocation(verseLocation.getText().toString());
                        scriptureData.setVerse(verse.getText().toString());
                        mCallback.onResponse(scriptureData);
                    }
                });
            }

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (v.equals(selectedView)) {
                        collapseCardView(selectedView, mPosition);
                        if(expandImage != null) {
                            expandImage.animate().rotation(0f);
                        }
                        selectedView = null;
                        isViewSelected = false;
                    }else{
                        if(isViewSelected) {
                            collapseCardView(selectedView, mPosition);
                            if(expandImage != null) {
                                expandImage.animate().rotation(0f);
                            }
                        }
                        isViewSelected = true;
                        switch(mPosition){
                            case 0:
                                expandCardView(v,mPosition);
                                if(expandImage != null) {
                                    expandImage.animate().rotation(180f);
                                }
//                                editTv.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                expandCardView(v, mPosition);
                                break;
                        }
                    }

                    if(isViewSelected){
                        selectedView = v;
                    }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterMyVerses(List<ScriptureData> dataset, int tabPosition, BaseCallback removeCallback, BaseCallback<ScriptureData> moveCallback, BaseCallback editCallback) {
        mDataset = dataset;
        mTabPosition = tabPosition;
        this.removeCallback = removeCallback;
        this.moveCallback = moveCallback;
        this.editCallback = editCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterMyVerses.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        // create a new view
        switch(mTabPosition){
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_inprogress, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_memorized, parent, false);
                break;
        }

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, mTabPosition, removeCallback, moveCallback, editCallback);
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
        if(mTabPosition == 1) {
            int progress = calculateProgress(verse.getMemoryStage(), verse.getMemorySubStage());
            holder.pb.setProgress(progress);
            holder.progressPercent.setText(Integer.toString(progress));
        }
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void expandCardView(View v, int position){
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

        CardView card;
        switch(position){
            case 0:
                card = (CardView) v.findViewById(R.id.card_view);
                card.setLayoutParams(CVLayoutParams);
                break;
            case 2:
                card = (CardView) v.findViewById(R.id.card_view_memorized);
                card.setLayoutParams(CVLayoutParams);
                break;
        }
    }

    private void collapseCardView(View v, int position){
        LinearLayout.LayoutParams CVLayoutParams;
        Resources r;
        int px7;
        int px4;

        r = v.getResources();
        CVLayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        77,
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

        CardView card;
        switch(position){
            case 0:
                card = (CardView) v.findViewById(R.id.card_view);
                card.setLayoutParams(CVLayoutParams);
                break;
            case 2:
                card = (CardView) v.findViewById(R.id.card_view_memorized);
                card.setLayoutParams(CVLayoutParams);
                break;
        }
    }
}