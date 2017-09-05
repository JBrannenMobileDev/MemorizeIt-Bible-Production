package nape.biblememory.Views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Fragments.VersesFragment;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.R;

/**
 * Created by jbrannen on 9/2/17.
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<ScriptureData> dataset = new ArrayList<>();
    private BaseCallback<List<ScriptureData>> dataChangedCallback;
    private BaseCallback<ScriptureData> itemSelectedCallback;
    private final OnStartDragListener mDragStartListener;
    private static Context context;
    private VersesFragment fragment;

    public RecyclerListAdapter(List<ScriptureData> dataset, Context context, OnStartDragListener dragStartListener,
                               VersesFragment fragment, BaseCallback<List<ScriptureData>> dataChangedCallback,
                               BaseCallback<ScriptureData> itemSelectedCallback) {
        mDragStartListener = dragStartListener;
        this.dataset.addAll(dataset);
        this.context = context;
        this.fragment = fragment;
        this.dataChangedCallback = dataChangedCallback;
        this.itemSelectedCallback = itemSelectedCallback;
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

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_verses_verse_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.verse.setText(dataset.get(position).getVerse());
        holder.verseLocation.setText(dataset.get(position).getVerseLocation());
        if(position > 2){
            holder.quizIcon.setVisibility(View.GONE);
            holder.progressView.setVisibility(View.GONE);
            holder.itemLayout.setBackgroundResource(R.color.greyBgLight);
            holder.verseLocation.setTextColor(context.getResources().getColor(R.color.greyBgDark));
            holder.verse.setTextColor(context.getResources().getColor(R.color.greyBgDark));
        }else{
            holder.progressView.setVisibility(View.VISIBLE);
            holder.progressView.setText(String.valueOf(calculateProgress(dataset.get(position).getMemoryStage(), dataset.get(position).getMemorySubStage())) + "%");
            holder.quizIcon.setVisibility(View.VISIBLE);
            holder.quizIcon.setColorFilter(Color.argb(255, 255, 213, 1));
            holder.itemLayout.setBackgroundResource(R.color.colorWhite);
            holder.verseLocation.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.verse.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelectedCallback.onResponse(dataset.get(position));
            }
        });

        holder.quizIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.showStarAlert();
            }
        });

        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(dataset, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void removeItem(ScriptureData result) {
        for(int i = 0; i < dataset.size(); i++){
            if(dataset.get(i).getVerseLocation().equalsIgnoreCase(result.getVerseLocation())){
                dataset.remove(dataset.get(i));
                notifyItemRemoved(i);
                RecyclerListAdapter.this.dataChangedCallback.onResponse(dataset);
            }
        }
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public final TextView verseLocation;
        public final TextView verse;
        public final ImageView handleView;
        public final ImageView quizIcon;
        public final FrameLayout itemLayout;
        public int mostRecentActionState;
        public final TextView progressView;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            verseLocation = (TextView) itemView.findViewById(R.id.my_verses_verse_location);
            verse = (TextView) itemView.findViewById(R.id.my_verses_verse);
            handleView = (ImageView) itemView.findViewById(R.id.sampleImage);
            quizIcon = (ImageView) itemView.findViewById(R.id.quiz_icon);
            itemLayout = (FrameLayout) itemView.findViewById(R.id.my_verses_layout);
            progressView = (TextView) itemView.findViewById(R.id.progress_tv);
        }

        @Override
        public void onItemSelected(int actionState) {
            mostRecentActionState = actionState;
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                itemView.setBackgroundColor(Color.argb(255, 218, 0, 0));
            }else {
                itemView.setBackgroundColor(Color.argb(255, 13, 151, 197));
            }
            handleView.setColorFilter(Color.WHITE);
            verseLocation.setTextColor(Color.WHITE);
            verse.setTextColor(Color.WHITE);
        }

        @Override
        public void onItemClear() {

            if(getLayoutPosition() < 3){
                quizIcon.setColorFilter(Color.argb(255, 255, 213, 1));
                quizIcon.setVisibility(View.VISIBLE);
                itemView.setBackgroundColor(Color.WHITE);
                verseLocation.setTextColor(Color.argb(255,1,81,107));
                verse.setTextColor(Color.argb(255,1,81,107));
            }else{
                itemLayout.setBackgroundResource(R.color.greyBgLight);
                verseLocation.setTextColor(context.getResources().getColor(R.color.greyBgDark));
                verse.setTextColor(context.getResources().getColor(R.color.greyBgDark));
            }
            handleView.setColorFilter(context.getResources().getColor(R.color.greyBgDark));

            if (mostRecentActionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
                RecyclerListAdapter.this.notifyDataSetChanged();
            }
            RecyclerListAdapter.this.dataChangedCallback.onResponse(dataset);
        }
    }
}