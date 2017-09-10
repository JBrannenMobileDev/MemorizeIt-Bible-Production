package nape.biblememory.view_layer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.view_layer.adapters.RecyclerViewAdapterPendingRequests;
import nape.biblememory.models.User;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    @BindView(R.id.pending_requests_recycler_view)RecyclerView recyclerViewPending;
    @BindView(R.id.empty_state_requests_tv)TextView emptyStateTv;


    private BaseCallback<Integer> deleteRequestCallback;
    private BaseCallback<Integer> confirmRequestCallback;
    private BaseCallback<List<User>> pendingRequestsCallback;
    private RecyclerViewAdapterPendingRequests mAdapterPending;
    private RecyclerView.LayoutManager layoutManagerPending;

    private List<User> pendingUsersToDisplay;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_requests, container, false);
        ButterKnife.bind(this, v);

        initializeCallbacks();

        layoutManagerPending = new LinearLayoutManager(this.getContext());
        recyclerViewPending.setLayoutManager(layoutManagerPending);
        DataStore.getInstance().registerForFriendRequests(pendingRequestsCallback);
        DataStore.getInstance().getFriendRequests(getActivity().getApplicationContext());
        return v;
    }

    private void initializeCallbacks() {

        confirmRequestCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                DataStore.getInstance().confirmFriendRequest(pendingUsersToDisplay.get(response).getUID(), getActivity().getApplicationContext());
                DataStore.getInstance().getFriendRequests(getActivity().getApplicationContext());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        deleteRequestCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                DataStore.getInstance().deleteFriendRequest(pendingUsersToDisplay.get(response).getUID(), getActivity().getApplicationContext());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        pendingRequestsCallback = new BaseCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                if(response != null) {
                    emptyStateTv.setVisibility(View.GONE);
                    pendingUsersToDisplay = response;
                    Collections.sort(pendingUsersToDisplay);
                    setPendingRecyclerViewItems(pendingUsersToDisplay);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    @Override
    public void onStop(){
        super.onStop();
        DataStore.getInstance().unregisterForFriendRequests(pendingRequestsCallback);
    }



    public void setPendingRecyclerViewItems(List<User> PendingUsersToDisplay){
        if(recyclerViewPending != null) {
            mAdapterPending = new RecyclerViewAdapterPendingRequests(PendingUsersToDisplay, confirmRequestCallback, deleteRequestCallback);
            recyclerViewPending.setAdapter(mAdapterPending);
        }
    }
}
