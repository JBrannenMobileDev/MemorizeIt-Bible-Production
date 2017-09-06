package nape.biblememory.view_layer.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.view_layer.Activities.AddFriendActivity;
import nape.biblememory.view_layer.Activities.BaseCallback;
import nape.biblememory.view_layer.Activities.FriendDetailsActivity;
import nape.biblememory.Adapters.RecyclerViewAdapterFriends;
import nape.biblememory.Models.User;
import nape.biblememory.R;
import nape.biblememory.data_store.DataStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    @BindView(R.id.friends_recycler_view)RecyclerView recyclerView;
    @BindView(R.id.add_friend_fab)FloatingActionButton addFriendBt;
    @BindView(R.id.empty_state_friends_fragment_tv)TextView emptyStateTv;
    private RecyclerView.LayoutManager layoutManager;
    private BaseCallback<Integer> friendSelectedCallback;
    private BaseCallback<List<User>> friendsCallback;
    private RecyclerViewAdapterFriends mAdapter;
    private List<User> friendsList;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, v);
        hideSoftKeyboard();
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        friendSelectedCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                Intent intent = new Intent(getActivity(), FriendDetailsActivity.class);
                intent.putExtra("uid", friendsList.get(response).getUID());
                intent.putExtra("name", friendsList.get(response).getName());
                startActivityForResult(intent, 1);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        friendsCallback = new BaseCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                if(response != null && response.size() > 0) {
                    friendsList = response;
                    emptyStateTv.setVisibility(View.GONE);
                    setRecyclerViewItems(response);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        addFriendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        DataStore.getInstance().getFriends(friendsCallback, getActivity().getApplicationContext());
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {
            for(int i = 0; i < friendsList.size(); i++){
                if(friendsList.get(i).getUID().equalsIgnoreCase(data.getStringExtra("uid"))){
                    friendsList.remove(i);
                }
            }
        }
        Collections.sort(friendsList);
        setRecyclerViewItems(friendsList);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void setRecyclerViewItems(List<User> usersToDisplay){
        if(recyclerView != null) {
            mAdapter = new RecyclerViewAdapterFriends(usersToDisplay, friendSelectedCallback);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private void hideSoftKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
