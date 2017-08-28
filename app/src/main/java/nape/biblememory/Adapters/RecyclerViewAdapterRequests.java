package nape.biblememory.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Models.User;
import nape.biblememory.R;

import static nape.biblememory.R.id.add_friend_tv;

public class RecyclerViewAdapterRequests extends RecyclerView.Adapter<RecyclerViewAdapterRequests.ViewHolder> {

    private List<User> mDataSet;
    private List<String> usersPendingRequests;
    private List<String> friendList;
    private BaseCallback friendSelectedCallback;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView email;
        private TextView addFriend;


        public ViewHolder(View v, final BaseCallback<Integer> selectedCallback) {
            super(v);
            addFriend = (TextView) v.findViewById(add_friend_tv);
            name = (TextView) v.findViewById(R.id.friends_name_tv);
            email = (TextView) v.findViewById(R.id.friends_email_tv);

            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCallback.onResponse(getLayoutPosition());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterRequests(List<User> dataset, BaseCallback<Integer> friendSelectedCallback, List<String> usersPendingRequests, Context context, List<String> friendsList) {
        mDataSet = dataset;
        this.friendSelectedCallback = friendSelectedCallback;
        this.usersPendingRequests = usersPendingRequests;
        this.context = context;
        this.friendList = friendsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterRequests.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v, friendSelectedCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(usersPendingRequests.contains(mDataSet.get(position).getUID())){
            holder.addFriend.setText("Requested");
            holder.addFriend.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_grey_bg));
        }
        if(friendList.contains(mDataSet.get(position).getUID())){
            holder.addFriend.setText("Following");
            holder.addFriend.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_grey_bg));
        }
        holder.name.setText(mDataSet.get(position).getName());
        holder.email.setText(mDataSet.get(position).getEmail());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}