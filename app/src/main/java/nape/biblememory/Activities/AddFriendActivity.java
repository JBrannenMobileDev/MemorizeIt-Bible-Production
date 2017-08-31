package nape.biblememory.Activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Adapters.RecyclerViewAdapterRequests;
import nape.biblememory.Models.User;
import nape.biblememory.R;
import nape.biblememory.UserPreferences;
import nape.biblememory.custom_views.BackAwareEditText;
import nape.biblememory.data_store.DataStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendActivity extends AppCompatActivity {

    @BindView(R.id.request_edit_text)BackAwareEditText requestSearchBar;
    @BindView(R.id.searchEditTextLayout)RelativeLayout searchBarLayout;
    @BindView(R.id.merchants_serchbar_slide_layout)RelativeLayout searchBarSlideLayout;
    @BindView(R.id.merchant_search_clear_bt)FrameLayout merchantSearchClearBt;
    @BindView(R.id.requests_recycler_view)RecyclerView recyclerView;
    @BindView(R.id.request_loading_circle)ProgressBar requestLoadingCircle;


    private RecyclerView.LayoutManager layoutManager;
    private List<User> usersToDisplay;
    private List<String> usersPendingRequests;
    private BaseCallback<Integer> friendSelectedCallback;
    private UserPreferences mPrefs;
    private List<User> allUsers;
    private boolean dataReceived;
    private RecyclerViewAdapterRequests mAdapter;
    private List<String> friendsList;

    public AddFriendActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        setTitle("Find a friend");
        mPrefs = new UserPreferences();
        initCallbacks();
        initializeListeners();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        refreshRecyclerView();
    }

    @Override
    public void onResume(){
        super.onResume();
        fetchAllUsers();
        requestSearchBar.setText("");
        requestSearchBar.clearFocus();
    }

    private void initCallbacks() {
        friendSelectedCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                verifyFriend(usersToDisplay.get(response).getUID(), usersToDisplay.get(response).getName());
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
                if(uid.equals(mPrefs.getUserId(getApplicationContext()))){
                    Toast.makeText(getApplicationContext(), "You cannot add yourself as a friend.", Toast.LENGTH_SHORT).show();
                }else if(usersPendingRequests.size() != 0){
                    if(!usersPendingRequests.contains(uid)){
                        if(response.size() == 0) {
                            addFriend(uid);
                        }else{
                            for (User user : response) {
                                if (user.getUID().equals(uid)) {
                                    Toast.makeText(getApplicationContext(), user.getName() + " is already your friend.", Toast.LENGTH_SHORT).show();
                                } else if(usersPendingRequests.contains(user.getUID())) {
                                    Toast.makeText(getApplicationContext(), "Friend request already sent.", Toast.LENGTH_SHORT).show();
                                } else{
                                    addFriend(uid);
                                }
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Friend request pending.", Toast.LENGTH_SHORT).show();
                    }
                }else if(response.size() == 0) {
                    addFriend(uid);
                }else{
                    for (User user : response) {
                        if (user.getUID().equals(uid)) {
                            Toast.makeText(getApplicationContext(), user.getName() + " is already your friend.", Toast.LENGTH_SHORT).show();
                        } else if(usersPendingRequests.contains(user.getUID())) {
                            Toast.makeText(getApplicationContext(), "Friend request already sent.", Toast.LENGTH_SHORT).show();
                        } else{
                            addFriend(uid);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong. Try adding the friend again.", Toast.LENGTH_LONG).show();
            }
        };
        DataStore.getInstance().getFriends(friendsCallback, getApplicationContext());
    }

    private void addFriend(String uid){
        DataStore.getInstance().sendFriendRequest(uid, getApplicationContext());
        DataStore.getInstance().addPendingRequest(uid, getApplicationContext());

        BaseCallback<List<String>> pendingRequestsCallback = new BaseCallback<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                if(response != null && response.size() > 0){
                    usersPendingRequests = response;
                }
                setRecyclerViewItems(usersToDisplay);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getPendingRequests(pendingRequestsCallback, getApplicationContext());
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
    }

    public void refreshRecyclerView(){
        if(recyclerView != null) {
            mAdapter = new RecyclerViewAdapterRequests(new ArrayList<User>(), friendSelectedCallback, usersPendingRequests, getApplicationContext(), friendsList);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void setRecyclerViewItems(List<User> usersToDisplay){
        if(recyclerView != null) {
            mAdapter = new RecyclerViewAdapterRequests(usersToDisplay, friendSelectedCallback, usersPendingRequests, getApplicationContext(), friendsList);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private void fetchAllUsers() {
        BaseCallback<List<String>> friendsCallback = new BaseCallback<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                if(response != null){
                    friendsList = response;
                }
                BaseCallback<List<String>> pendingRequestsCallback = new BaseCallback<List<String>>() {
                    @Override
                    public void onResponse(List<String> response) {
                        usersPendingRequests = new ArrayList<>();
                        if(response != null){
                            usersPendingRequests = response;
                        }
                        BaseCallback<List<User>> usersCallback = new BaseCallback<List<User>>() {
                            @Override
                            public void onResponse(List<User> response) {
                                if (response != null) {
                                    allUsers = response;
                                    Collections.sort(allUsers);
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
                    public void onFailure(Exception e) {

                    }
                };
                DataStore.getInstance().getPendingRequests(pendingRequestsCallback, getApplicationContext());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getFriendsString(friendsCallback, getApplicationContext());
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
        Collections.sort(usersToDisplay);
        setRecyclerViewItems(usersToDisplay);
    }

    private void hideSoftKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
