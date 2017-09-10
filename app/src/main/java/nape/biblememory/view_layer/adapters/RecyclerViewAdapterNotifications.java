package nape.biblememory.view_layer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.models.User;
import nape.biblememory.R;

public class RecyclerViewAdapterNotifications extends RecyclerView.Adapter<RecyclerViewAdapterNotifications.ViewHolder> {

    private List<User> blessings;
    private BaseCallback<Integer> friendSelectedCallback;
    private BaseCallback<Integer> giveABlessingCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView message;
        private TextView giveBlessingBack;



        public ViewHolder(View v, final BaseCallback<Integer> friendSelectedCallback, final BaseCallback<Integer> giveABlessingCallback) {
            super(v);
            name = (TextView) v.findViewById(R.id.notification_friends_name_tv);
            message = (TextView) v.findViewById(R.id.notification_message_tv);
            giveBlessingBack = (TextView) v.findViewById(R.id.notification_give_a_blessing);
            giveBlessingBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    giveABlessingCallback.onResponse(getLayoutPosition());
                    removeItem(getLayoutPosition());
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friendSelectedCallback.onResponse(getLayoutPosition());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterNotifications(List<User> blessings, BaseCallback<Integer> friendPositionSelectedCallback, BaseCallback<Integer> giveABlessingCallback) {
        this.blessings = blessings;
        this.friendSelectedCallback = friendPositionSelectedCallback;
        this.giveABlessingCallback = giveABlessingCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterNotifications.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v, friendSelectedCallback, giveABlessingCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(blessings.get(position).getName());
        holder.message.setText("gave you a blessing!");
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return blessings.size();
    }

    private void removeItem(int position) {
        blessings.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, blessings.size());
    }
}