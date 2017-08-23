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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Adapters.RecyclerViewAdapterRequests;
import nape.biblememory.Models.User;
import nape.biblememory.R;
import nape.biblememory.custom_views.BackAwareEditText;
import nape.biblememory.data_store.DataStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    @BindView(R.id.request_edit_text)BackAwareEditText merchantSearchBar;
    @BindView(R.id.searchEditTextLayout)RelativeLayout searchBarLayout;
    @BindView(R.id.merchants_serchbar_slide_layout)RelativeLayout searchBarSlideLayout;
    @BindView(R.id.merchant_search_clear_bt)FrameLayout merchantSearchClearBt;
    @BindView(R.id.requests_recycler_view)RecyclerView recyclerView;
    @BindView(R.id.request_loading_circle)ProgressBar requestLoadingCircle;

    private BaseCallback<Integer> friendSelectedCallback;
    private RecyclerViewAdapterRequests mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> allUsers;
    private boolean dataReceived;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_requests, container, false);
        ButterKnife.bind(this, v);
        allUsers = new ArrayList<>();
        dataReceived = false;

        friendSelectedCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                DataStore.getInstance().addFriend(allUsers.get(response).getUID(), getActivity().getApplicationContext());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        initializeListeners();
        refreshRecyclerView();
        return v;
    }


    private void initializeListeners() {
        merchantSearchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchBarSlideLayout.animate().translationX(-((searchBarLayout.getWidth() - searchBarSlideLayout.getWidth())/2));
                return false;
            }
        });

        merchantSearchBar.setBackPressedListener(new BackAwareEditText.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareEditText editText) {
                merchantSearchBar.clearFocus();
                if(merchantSearchBar.getText().length() == 0) {
                    searchBarSlideLayout.setVisibility(View.VISIBLE);
                    searchBarSlideLayout.animate().translationX(0);
                }
            }
        });

        merchantSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!dataReceived && charSequence.length() > 0){
                    requestLoadingCircle.setVisibility(View.VISIBLE);
                }
                filterMerchantList(charSequence.toString());
                if(merchantSearchBar.getText().length() == 0) {
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

        merchantSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(merchantSearchBar.getText().length() == 0) {
                        searchBarSlideLayout.animate().translationX(0);
                    }
                    merchantSearchBar.clearFocus();
                    hideSoftKeyboard();
                }
                return false;
            }
        });

        merchantSearchClearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merchantSearchBar.setText("");
                setRecyclerViewItems(new ArrayList<User>());
                if(!merchantSearchBar.hasFocus()){
                    searchBarSlideLayout.animate().translationX(0);
                }
            }
        });
    }

    public void filterMerchantList(String userEnteredText) {
        List<User> usersToDisplay = new ArrayList<>();
        for(User user : allUsers){
            if(user.getEmail().toLowerCase().contains(userEnteredText.toLowerCase()) || user.getName().toLowerCase().contains(userEnteredText.toLowerCase())){
                usersToDisplay.add(user);
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
}
