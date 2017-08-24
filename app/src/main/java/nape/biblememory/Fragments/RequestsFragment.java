package nape.biblememory.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Adapters.RecyclerViewAdapterPendingRequests;
import nape.biblememory.Adapters.RecyclerViewAdapterRequests;
import nape.biblememory.Fragments.Dialogs.FriendAddedAlertDialog;
import nape.biblememory.Models.User;
import nape.biblememory.R;
import nape.biblememory.UserPreferences;
import nape.biblememory.custom_views.BackAwareEditText;
import nape.biblememory.data_store.DataStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    @BindView(R.id.request_edit_text)BackAwareEditText requestSearchBar;
    @BindView(R.id.searchEditTextLayout)RelativeLayout searchBarLayout;
    @BindView(R.id.merchants_serchbar_slide_layout)RelativeLayout searchBarSlideLayout;
    @BindView(R.id.merchant_search_clear_bt)FrameLayout merchantSearchClearBt;
    @BindView(R.id.requests_recycler_view)RecyclerView recyclerView;
    @BindView(R.id.pending_requests_recycler_view)RecyclerView recyclerViewPending;
    @BindView(R.id.request_loading_circle)ProgressBar requestLoadingCircle;
    @BindView(R.id.pending_requests_layout)LinearLayout pendingRequestsLayout;
    @BindView(R.id.friend_request_tv)TextView friendRequestsTv;
    @BindView(R.id.friend_request_count_tv)TextView friendRequestCountTv;

    private BaseCallback<Integer> friendSelectedCallback;
    private RecyclerViewAdapterRequests mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private BaseCallback<Integer> deleteRequestCallback;
    private BaseCallback<Integer> confirmRequestCallback;
    private BaseCallback<List<User>> pendingRequestsCallback;
    private RecyclerViewAdapterPendingRequests mAdapterPending;
    private RecyclerView.LayoutManager layoutManagerPending;
    private List<User> allUsers;
    private boolean dataReceived;
    private UserPreferences mPrefs;
    private List<User> usersToDisplay;
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
        mPrefs = new UserPreferences();
        allUsers = new ArrayList<>();
        dataReceived = false;
        initializeCallbacks();
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManagerPending = new LinearLayoutManager(this.getContext());
        recyclerViewPending.setLayoutManager(layoutManagerPending);
        initializeListeners();
        refreshRecyclerView();
        DataStore.getInstance().registerForFriendRequests(pendingRequestsCallback);
        DataStore.getInstance().getFriendRequests(getActivity().getApplicationContext());
        return v;
    }

    private void initializeCallbacks() {
        friendSelectedCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                verifyFriend(usersToDisplay.get(response).getUID(), usersToDisplay.get(response).getName());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        confirmRequestCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                DataStore.getInstance().confirmFriendRequest(pendingUsersToDisplay.get(response).getUID(), getActivity().getApplicationContext());
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
                    pendingUsersToDisplay = response;
                    friendRequestCountTv.setText(String.valueOf(response.size()));
                    if (response.size() > 1 || response.size() == 0) {
                        friendRequestsTv.setText("Friend requests");
                    } else {
                        friendRequestsTv.setText("Friend request");
                    }
                    if (response.size() > 0) {
                        friendRequestCountTv.setTextColor(getResources().getColor(R.color.colorGreenText));
                    } else {
                        friendRequestCountTv.setTextColor(getResources().getColor(R.color.colorWhite));
                    }

                    if(response.size() == 0){
                        recyclerViewPending.setVisibility(View.GONE);
                    }
                    setPendingRecyclerViewItems(response);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    private void verifyFriend(final String uid, final String selectedUserName) {
        BaseCallback<List<User>> friendsCallback = new BaseCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                if(uid.equals(mPrefs.getUserId(getActivity().getApplicationContext()))){
                    Toast.makeText(getActivity().getApplicationContext(), "You cannot add yourself as a friend.", Toast.LENGTH_SHORT).show();
                }else if(response.size() == 0){
                    addFriend(uid);
                    showFriendAddedDialog(selectedUserName);
                }else {
                    for (User user : response) {
                        if (user.getUID().equals(uid)) {
                            Toast.makeText(getActivity().getApplicationContext(), user.getName() + " is already your friend.", Toast.LENGTH_SHORT).show();
                        } else {
                            addFriend(uid);
                            showFriendAddedDialog(selectedUserName);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Try adding the friend again.", Toast.LENGTH_LONG).show();
            }
        };
        DataStore.getInstance().getFriends(friendsCallback, getActivity().getApplicationContext());
    }

    private void addFriend(String uid){
        DataStore.getInstance().sendFriendRequest(uid, getActivity().getApplicationContext());
    }

    private void showFriendAddedDialog(String userName){
        FriendAddedAlertDialog friendAddedDialog = new FriendAddedAlertDialog();
        Bundle bundle = new Bundle();
        if(userName != null && userName.length() > 0) {
            bundle.putString("user_name", userName);
        }else{
            bundle.putString("user_name", "Friend");
        }
        friendAddedDialog.setArguments(bundle);
        friendAddedDialog.show(getFragmentManager(), null);
    }


    private void initializeListeners() {
        requestSearchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchBarSlideLayout.animate().translationX(-((searchBarLayout.getWidth() - searchBarSlideLayout.getWidth())/2));
                return false;
            }
        });

        requestSearchBar.setBackPressedListener(new BackAwareEditText.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareEditText editText) {
                requestSearchBar.clearFocus();
                if(requestSearchBar.getText().length() == 0) {
                    searchBarSlideLayout.setVisibility(View.VISIBLE);
                    searchBarSlideLayout.animate().translationX(0);
                }
            }
        });

        requestSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!dataReceived && charSequence.length() > 0){
                    requestLoadingCircle.setVisibility(View.VISIBLE);
                }
                filterUserList(charSequence.toString());
                if(requestSearchBar.getText().length() == 0) {
                    searchBarSlideLayout.setVisibility(View.VISIBLE);
                    merchantSearchClearBt.setVisibility(View.GONE);
                }else {
                    searchBarSlideLayout.setVisibility(View.GONE);
                    merchantSearchClearBt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        requestSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(requestSearchBar.getText().length() == 0) {
                        searchBarSlideLayout.animate().translationX(0);
                    }
                    requestSearchBar.clearFocus();
                    hideSoftKeyboard();
                }
                return false;
            }
        });

        merchantSearchClearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSearchBar.setText("");
                setRecyclerViewItems(new ArrayList<User>());
                if(!requestSearchBar.hasFocus()){
                    searchBarSlideLayout.animate().translationX(0);
                }
            }
        });

        pendingRequestsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pendingUsersToDisplay.size() > 0) {
                    recyclerViewPending.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "There are no requests to view.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void filterUserList(String userEnteredText) {
        usersToDisplay = new ArrayList<>();
        if(userEnteredText != null && userEnteredText.length() > 0) {
            for (User user : allUsers) {
                if (user.getEmail().toLowerCase().contains(userEnteredText.toLowerCase()) || user.getName().toLowerCase().contains(userEnteredText.toLowerCase())) {
                    usersToDisplay.add(user);
                }
            }
        }
        setRecyclerViewItems(usersToDisplay);
    }

    private void hideSoftKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void fetchAllUsers() {
        BaseCallback<List<User>> usersCallback = new BaseCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                if (response != null) {
                    allUsers = response;
                    dataReceived = true;
                    requestLoadingCircle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getUsers(usersCallback);
    }

    @Override
    public void onResume(){
        super.onResume();
        fetchAllUsers();
        requestSearchBar.setText("");
        requestSearchBar.clearFocus();
    }

    @Override
    public void onStop(){
        super.onStop();
        DataStore.getInstance().unregisterForFriendRequests(pendingRequestsCallback);
    }

    public void refreshRecyclerView(){
        if(recyclerView != null) {
            mAdapter = new RecyclerViewAdapterRequests(new ArrayList<User>(), friendSelectedCallback);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void setRecyclerViewItems(List<User> usersToDisplay){
        if(recyclerView != null) {
            mAdapter = new RecyclerViewAdapterRequests(usersToDisplay, friendSelectedCallback);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void setPendingRecyclerViewItems(List<User> PendingUsersToDisplay){
        if(recyclerViewPending != null) {
            mAdapterPending = new RecyclerViewAdapterPendingRequests(PendingUsersToDisplay, confirmRequestCallback, deleteRequestCallback);
            recyclerViewPending.setAdapter(mAdapterPending);
        }
    }
}
