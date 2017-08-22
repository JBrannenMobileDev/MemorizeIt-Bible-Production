package nape.biblememory.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Models.BookGroup;
import nape.biblememory.Models.Friend;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.R;
import nape.biblememory.Singletons.RecyclerViewSingleton;

public class RecyclerViewAdapterFriends extends RecyclerView.Adapter<RecyclerViewAdapterFriends.ViewHolder> {

    private List<Friend> mDataSet;
    private BaseCallback friendSelectedCallback;


    private View selectedView;
    private boolean isViewSelected;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        //items for bookName group cardView
        private LinearLayout friendItemLayout;
        private TextView name;
        private TextView email;
        private TextView upcomingCount;
        private TextView quizCount;
        private TextView memorizedCount;

        public ViewHolder(View v, final BaseCallback<Integer> selectedCallback) {
            super(v);
            friendItemLayout = (LinearLayout) v.findViewById(R.id.friend_item_layout);
            name = (TextView) v.findViewById(R.id.friends_name_tv);
            email = (TextView) v.findViewById(R.id.friends_email_tv);
            upcomingCount = (TextView) v.findViewById(R.id.friends_upcoming_count_tv);
            quizCount = (TextView) v.findViewById(R.id.friends_quiz_count_tv);
            memorizedCount = (TextView) v.findViewById(R.id.friends_memorized_count_tv);

            friendItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCallback.onResponse(getAdapterPosition());
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
    public RecyclerViewAdapterFriends(List<Friend> dataset, BaseCallback<Integer> friendSelectedCallback) {
        mDataSet = dataset;
        this.friendSelectedCallback = friendSelectedCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterFriends.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        // create a new view
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v, friendSelectedCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mDataSet.get(position).getName());
        holder.email.setText(mDataSet.get(position).getEmail());
        holder.upcomingCount.setText(mDataSet.get(position).getUpcomingVerses().size());
        holder.quizCount.setText(mDataSet.get(position).getQuizVerses().size());
        holder.memorizedCount.setText(mDataSet.get(position).getMemorizedVerses().size());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}