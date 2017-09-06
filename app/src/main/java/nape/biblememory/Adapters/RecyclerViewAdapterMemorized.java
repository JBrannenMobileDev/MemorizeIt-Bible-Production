package nape.biblememory.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import nape.biblememory.view_layer.Activities.BaseCallback;
import nape.biblememory.Models.BookGroup;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Singletons.RecyclerViewSingleton;
import nape.biblememory.R;

public class RecyclerViewAdapterMemorized extends RecyclerView.Adapter<RecyclerViewAdapterMemorized.ViewHolder> {
    private static final int BOOK_GROUP_NAME = 0;
    private static final int SCRIPTURE = 1;
    private static final String PLURAL_FORM = "verses memorized";
    private static final String SINGULAR_FORM = "verse memorized";

    private List<Object> mDataSet;
    private BaseCallback expandCallback;
    private BaseCallback collapseCallback;

    private View selectedView;
    private boolean isViewSelected;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        //items for bookName group cardView
        public TextView bookName;
        public TextView numberOfVersesMemorized;
        public TextView versesTextView;

        // items for memorized cardView
        public TextView verseLocation;
        public TextView verse;
        public TextView memorizedDateTextView;
        public TextView lastSeenDateTextView;
        public LinearLayout lastSeenLayout;
        public ImageView expandArrow;

        public ViewHolder(View v, final BaseCallback eCallback, final BaseCallback cCallback) {
            super(v);
            bookName = (TextView) v.findViewById(R.id.book_name);
            numberOfVersesMemorized = (TextView) v.findViewById(R.id.number_memorized_textview);

            verseLocation = (TextView) v.findViewById(R.id.verse_location);
            verse = (TextView) v.findViewById(R.id.verse);
            lastSeenLayout = (LinearLayout) v.findViewById(R.id.last_seen_layout);
            versesTextView = (TextView) v.findViewById(R.id.verses_memorized_textview);
            memorizedDateTextView = (TextView) v.findViewById(R.id.memorized_date_textview);
            lastSeenDateTextView = (TextView) v.findViewById(R.id.last_seen_textview);
            expandArrow = (ImageView) v.findViewById(R.id.expand_arrow_mem);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(v.findViewById(R.id.card_view_book_name) != null){
                        RecyclerViewSingleton selectedMemory = RecyclerViewSingleton.getInstance();
                        List<String> selectedBooksList = selectedMemory.listOfSelectedBooks;
                        TextView selectedBookNameTextView = (TextView) v.findViewById(R.id.book_name);
                        String selectedBookName = selectedBookNameTextView.getText().toString();
                        if(selectedBooksList.contains(selectedBookName)){
                            selectedBooksList.remove(selectedBookName);
                            selectedMemory.listOfSelectedBooks = selectedBooksList;
                            cCallback.onResponse(selectedBookName);
                        } else {
                            selectedBooksList.add(selectedBookName);
                            selectedMemory.listOfSelectedBooks = selectedBooksList;
                            eCallback.onResponse(selectedBookName);
                        }
                    }else {
                        if (v.equals(selectedView)) {
                            collapseCardView(selectedView);
                            if(expandArrow != null){
                                expandArrow.animate().rotation(0);
                            }
                            selectedView = null;
                            isViewSelected = false;
                        } else {
                            if (isViewSelected) {
                                collapseCardView(selectedView);
                                if(expandArrow != null){
                                    expandArrow.animate().rotation(0);
                                }
                            }
                            isViewSelected = true;
                            expandCardView(v);
                            if(expandArrow != null){
                                expandArrow.animate().rotation(180f);
                            }
                        }

                        if (isViewSelected) {
                            selectedView = v;
                        }
                    }
                }
            });
        }

        private void collapseCardView(View v){
            LinearLayout lastSeen =(LinearLayout) v.findViewById(R.id.last_seen_layout);
            TextView vrs = (TextView) v.findViewById(R.id.verse);
            TextView memorizedText = (TextView) v.findViewById(R.id.memorized_date_title);
            TextView lastSeenText = (TextView) v.findViewById(R.id.last_seen_title);
            memorizedText.setTypeface(Typeface.DEFAULT);
            lastSeenText.setTypeface(Typeface.DEFAULT);
            lastSeen.setVisibility(View.GONE);
            vrs.setVisibility(View.GONE);
        }

        private void expandCardView(View v){
            LinearLayout lastSeen =(LinearLayout) v.findViewById(R.id.last_seen_layout);
            TextView vrs = (TextView) v.findViewById(R.id.verse);
            TextView memorizedText = (TextView) v.findViewById(R.id.memorized_date_title);
            TextView lastSeenText = (TextView) v.findViewById(R.id.last_seen_title);
            memorizedText.setTypeface(Typeface.DEFAULT_BOLD);
            lastSeenText.setTypeface(Typeface.DEFAULT_BOLD);
            lastSeen.setVisibility(View.VISIBLE);
            vrs.setVisibility(View.VISIBLE);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterMemorized(List<Object> dataset, BaseCallback expandCallback, BaseCallback collapseCallback) {
        mDataSet = dataset;
        this.expandCallback = expandCallback;
        this.collapseCallback = collapseCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterMemorized.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        // create a new view
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_memorized, parent, false);
        ViewHolder vh = new ViewHolder(v, expandCallback, collapseCallback);

        switch (viewType) {
            case BOOK_GROUP_NAME:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_book_name, parent, false);
                vh = new ViewHolder(v1, expandCallback, collapseCallback);
                break;
            case SCRIPTURE:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_memorized, parent, false);
                vh = new ViewHolder(v2, expandCallback, collapseCallback);
                break;
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mDataSet.get(position) instanceof BookGroup){
            int numMemorized = ((BookGroup) mDataSet.get(position)).getNumOfVersesMemorized();
            holder.bookName.setText(((BookGroup) mDataSet.get(position)).getBookName());
            holder.numberOfVersesMemorized.setText(Integer.toString(numMemorized));
            if(numMemorized == 1){
                holder.versesTextView.setText(SINGULAR_FORM);
            }else{
                holder.versesTextView.setText(PLURAL_FORM);
            }
        }else {
            ScriptureData verse = (ScriptureData) mDataSet.get(position);
            holder.verseLocation.setText(verse.getVerseLocation());
            holder.verse.setText(verse.getVerse());
            holder.memorizedDateTextView.setText(verse.getRemeberedDate());
            holder.lastSeenDateTextView.setText(verse.getLastSeenDate());
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.get(position) instanceof ScriptureData) {
            return SCRIPTURE;
        } else if (mDataSet.get(position) instanceof BookGroup) {
            return BOOK_GROUP_NAME;
        }
        return -1;
    }
}