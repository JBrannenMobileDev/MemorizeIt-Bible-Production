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

import static nape.biblememory.R.id.confirm_request_tv;

public class RecyclerViewAdapterPendingRequests extends RecyclerView.Adapter<RecyclerViewAdapterPendingRequests.ViewHolder> {

    private List<User> mDataSet;
    private BaseCallback<Integer> confirmCallback;
    private BaseCallback<Integer> deleteCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView confirm;
        private TextView delete;


        public ViewHolder(View v, final BaseCallback<Integer> confirmCallback, final BaseCallback<Integer> deleteCallback) {
            super(v);
            confirm = (TextView) v.findViewById(confirm_request_tv);
            delete = (TextView) v.findViewById(R.id.delete_request_tv);
            name = (TextView) v.findViewById(R.id.friends_name_tv);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmCallback.onResponse(getLayoutPosition());

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCallback.onResponse(getLayoutPosition());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapterPendingRequests(List<User> dataset, final BaseCallback<Integer> confirmCallback, final BaseCallback<Integer> deleteCallback) {
        mDataSet = dataset;
        this.confirmCallback = confirmCallback;
        this.deleteCallback = deleteCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapterPendingRequests.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_requests_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v, confirmCallback, deleteCallback);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mDataSet.get(position).getName());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}