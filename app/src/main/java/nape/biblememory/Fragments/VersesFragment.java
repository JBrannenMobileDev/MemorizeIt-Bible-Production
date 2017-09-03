package nape.biblememory.Fragments;


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
import nape.biblememory.Fragments.Dialogs.StarAlertDialog;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.R;
import nape.biblememory.Views.OnStartDragListener;
import nape.biblememory.Views.RecyclerListAdapter;
import nape.biblememory.Views.SimpleItemTouchHelperCallback;
import nape.biblememory.data_store.DataStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class VersesFragment extends Fragment implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;
    private FirebaseAnalytics mFirebaseAnalytics;

    public VersesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "MyVerses view", null);
        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final List<ScriptureData> myVerses = new ArrayList<>();
        final BaseCallback<List<ScriptureData>> orderChangedCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                for(int i = 0; i < response.size(); i++){
                    if(i < 3){
//                        DataStore.getInstance().updateQuizVerse(response.get(i), getActivity().getApplicationContext());
                    }else{
//                        DataStore.getInstance().updateUpcomingVerse(response.get(i), getActivity().getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        BaseCallback<List<ScriptureData>> QuizVersesCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                if(response != null){
                    myVerses.addAll(response);
                }
                BaseCallback<List<ScriptureData>> upcomingVersesCallback = new BaseCallback<List<ScriptureData>>() {
                    @Override
                    public void onResponse(List<ScriptureData> response) {
                        if(response != null){
                            myVerses.addAll(response);
                        }
                        initRecyclerView(myVerses, view, orderChangedCallback);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        initRecyclerView(myVerses, view, orderChangedCallback);
                    }
                };
                DataStore.getInstance().getLocalUpcomingVerses(upcomingVersesCallback, getActivity().getApplicationContext());
            }

            @Override
            public void onFailure(Exception e) {
                initRecyclerView(myVerses, view, orderChangedCallback);
            }
        };
        DataStore.getInstance().getLocalQuizVerses(QuizVersesCallback, getActivity().getApplicationContext());
    }

    private void initRecyclerView(List<ScriptureData> myVerses, View view, BaseCallback<List<ScriptureData>> dataChangedCallback){
        RecyclerListAdapter adapter = new RecyclerListAdapter(myVerses, getActivity(), this, this, dataChangedCallback);

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public void showStarAlert(){
        new StarAlertDialog().show(getFragmentManager(), null);
    }
}
