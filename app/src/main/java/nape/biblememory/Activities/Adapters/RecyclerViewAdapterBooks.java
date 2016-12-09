package nape.biblememory.Activities.Adapters;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;


import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.R;

public class RecyclerViewAdapterBooks extends RecyclerView.Adapter<RecyclerViewAdapterBooks.ViewHolder> {
    private List<String> mDataset;
    private int mTabPosition;
    private BaseCallback bookSelectedCallback;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Button bookNameTextView;
        public LinearLayout bookViewLayout;
        public ViewHolder(View v, final int mPosition, final BaseCallback rCallback) {
            super(v);
            bookNameTextView = (Button) v.findViewById(R.id.book_name_button);
            bookNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookSelectedCallback.onResponse(bookNameTextView.getText());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterBooks(List<String> dataset, int tabPosition, BaseCallback bookSelected) {
        mDataset = dataset;
        mTabPosition = tabPosition;
        this.bookSelectedCallback = bookSelected;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterBooks.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        // create a new view
        switch(mTabPosition){
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_view, parent, false);
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_view, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_view, parent, false);
                break;
        }

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, mTabPosition, bookSelectedCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bookNameTextView.setText(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}