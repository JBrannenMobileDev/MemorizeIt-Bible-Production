package nape.biblememory.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Adapters.RecyclerViewAdapterFriends;
import nape.biblememory.Models.User;
import nape.biblememory.R;
import nape.biblememory.data_store.DataStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    @BindView(R.id.friends_recycler_view)RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BaseCallback<Integer> friendSelectedCallback;
    private BaseCallback<List<User>> friendsCallback;
    private RecyclerViewAdapterFriends mAdapter;

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

            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        friendsCallback = new BaseCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                if(response != null && response.size() > 0)
                    setRecyclerViewItems(response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getFriends(friendsCallback, getActivity().getApplicationContext());
        return v;
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
