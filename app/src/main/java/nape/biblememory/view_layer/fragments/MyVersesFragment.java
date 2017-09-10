package nape.biblememory.view_layer.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import nape.biblememory.models.MyVerse;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.view_layer.activities.VerseDetailsActivity;
import nape.biblememory.view_layer.fragments.dialogs.StarAlertDialog;
import nape.biblememory.view_layer.material_recyclerview.OnStartDragListener;
import nape.biblememory.view_layer.material_recyclerview.RecyclerListAdapter;
import nape.biblememory.view_layer.material_recyclerview.SimpleItemTouchHelperCallback;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPresenterInterface;
import nape.biblememory.view_layer.fragments.presenters.MyVersesPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyVersesFragment extends Fragment implements OnStartDragListener, MyVersesFragmentInterface {

    private ItemTouchHelper mItemTouchHelper;
    private FirebaseAnalytics mFirebaseAnalytics;
    private MyVersesPresenterInterface presenter;
    private BaseCallback<List<ScriptureData>> orderChangedCallback;
    private BaseCallback<ScriptureData> itemSelectedCallback;
    private View view;
    private RecyclerListAdapter adapter;

    public MyVersesFragment() {
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.fetchData();
    }

    @Override
    public void onStop(){
        super.onStop();
        presenter.onStop();
    }

    public void RefetchData() {
        presenter.fetchData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "MyVerses view", null);
        presenter = new MyVersesPresenter(this, getActivity().getApplicationContext());
        initCallbacks();
        return new RecyclerView(container.getContext());
    }

    private void initCallbacks() {
        orderChangedCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                List<MyVerse> verseList = new ArrayList<>();
                if(response != null) {
                    for (int i = 0; i < response.size(); i++) {
                        verseList.add(response.get(i).toMyVerse());
                        verseList.get(i).setListPosition(i);
                    }
                    presenter.onDatasetChanged(verseList);
                }

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

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public void showStarAlert(){
        new StarAlertDialog().show(getFragmentManager(), null);
    }

    @Override
    public void onReceivedRecyclerData(List<MyVerse> myVerses) {
        List<ScriptureData> scripList = new ArrayList<>();
        for(MyVerse verse : myVerses){
            scripList.add(verse.toScriptureData());
        }
        adapter = new RecyclerListAdapter(scripList, getActivity(), this, this,
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
