package nape.biblememory.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.view_layer.Activities.BaseCallback;
import nape.biblememory.Models.User;
import nape.biblememory.R;

public class RecyclerViewAdapterFriends extends RecyclerView.Adapter<RecyclerViewAdapterFriends.ViewHolder> {

    private List<User> mDataSet;
    private BaseCallback friendSelectedCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView email;
        private TextView verseCount;
        private RelativeLayout friendLayout;


        public ViewHolder(View v, final BaseCallback<Integer> selectedCallback) {
            super(v);
            friendLayout = (RelativeLayout) v.findViewById(R.id.friend_item_layout);
            name = (TextView) v.findViewById(R.id.friends_name_tv);
            email = (TextView) v.findViewById(R.id.friends_email_tv);
            verseCount = (TextView) v.findViewById(R.id.friends_verse_count_tv);

            friendLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCallback.onResponse(getLayoutPosition());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterFriends(List<User> dataset, BaseCallback<Integer> friendSelectedCallback) {
        mDataSet = dataset;
        this.friendSelectedCallback = friendSelectedCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterFriends.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v, friendSelectedCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mDataSet.get(position).getName());
        holder.email.setText(mDataSet.get(position).getEmail());
        holder.verseCount.setText(String.valueOf(mDataSet.get(position).getTotalVerses()));
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}