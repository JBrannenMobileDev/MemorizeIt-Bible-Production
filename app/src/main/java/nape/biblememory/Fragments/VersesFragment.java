package nape.biblememory.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Activities.VerseDetailsActivity;
import nape.biblememory.Fragments.Dialogs.StarAlertDialog;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.R;
import nape.biblememory.Views.OnStartDragListener;
import nape.biblememory.Views.RecyclerListAdapter;
import nape.biblememory.Views.SimpleItemTouchHelperCallback;
import nape.biblememory.data_store.DataStore;
import nape.biblememory.view_layer.fragments.interfaces.VersesFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.VersesPresenterInterface;
import nape.biblememory.view_layer.fragments.presenters.VersesPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class VersesFragment extends Fragment implements OnStartDragListener, VersesFragmentInterface {

    private ItemTouchHelper mItemTouchHelper;
    private FirebaseAnalytics mFirebaseAnalytics;
    private VersesPresenterInterface presenter;
    private BaseCallback<List<ScriptureData>> orderChangedCallback;
    private BaseCallback<ScriptureData> itemSelectedCallback;
    private View view;
    private RecyclerListAdapter adapter;

    public VersesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "MyVerses view", null);
        presenter = new VersesPresenter(this, getActivity().getApplicationContext());
        initCallbacks();
        return new RecyclerView(container.getContext());
    }

    private void initCallbacks() {
        orderChangedCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                if(response != null)
                    presenter.onDatasetChanged(response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        itemSelectedCallback = new BaseCallback<ScriptureData>() {
            @Override
            public void onResponse(ScriptureData verse) {
                Intent intent = new Intent(getActivity(), VerseDetailsActivity.class);
                intent.putExtra("verse", verse);
                startActivityForResult(intent, 2);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    public void onDeleteVerse(ScriptureData verse){
        adapter.removeItem(verse);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        presenter.fetchData();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public void showStarAlert(){
        new StarAlertDialog().show(getFragmentManager(), null);
    }

    @Override
    public void onReceivedRecyclerData(List<ScriptureData> myVerses) {
        adapter = new RecyclerListAdapter(myVerses, getActivity(), this, this,
                orderChangedCallback, itemSelectedCallback);
        RecyclerView recyclerView = (RecyclerView)view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
