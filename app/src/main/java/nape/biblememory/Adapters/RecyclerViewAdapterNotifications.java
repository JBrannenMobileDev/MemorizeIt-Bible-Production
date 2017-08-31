package nape.biblememory.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Models.User;
import nape.biblememory.R;

import static nape.biblememory.R.id.confirm_request_tv;

public class RecyclerViewAdapterNotifications extends RecyclerView.Adapter<RecyclerViewAdapterNotifications.ViewHolder> {

    private List<User> blessings;
    private BaseCallback<Integer> friendSelectedCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView message;



        public ViewHolder(View v, final BaseCallback<Integer> friendSelectedCallback) {
            super(v);
            name = (TextView) v.findViewById(R.id.notification_friends_name_tv);
            message = (TextView) v.findViewById(R.id.notification_message_tv);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friendSelectedCallback.onResponse(getLayoutPosition());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterNotifications(List<User> blessings, BaseCallback<Integer> friendPositionSelectedCallback) {
        this.blessings = blessings;
        this.friendSelectedCallback = friendPositionSelectedCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterNotifications.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v, friendSelectedCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(blessings.get(position).getName());
        holder.message.setText("sent you a blessing!");
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return blessings.size();
    }
}