package nape.biblememory.Fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nape.biblememory.Adapters.RecyclerViewAdapterMyVerses;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Managers.ScriptureManager;
import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Sqlite.MemoryListContract;
import nape.biblememory.Views.SlidingTabLayout;
import nape.biblememory.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class MyVersesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ScriptureManager scriptureManager;
    private List<ScriptureData> dataSet;
    private BaseCallback removeCallback;
    private FloatingActionButton addVerseButton;
    private VerseOperations vManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scriptureManager = new ScriptureManager(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =inflater.inflate(R.layout.fragment_my_verses,container,false);

        addVerseButton = (FloatingActionButton) v.findViewById(R.id.add_verse_button_fab);
        addVerseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ((OnAddVerseSelectedListener) getActivity()).addVerseSelected();
                }catch (ClassCastException cce){

                }
            }
        });


        mRecyclerView = (RecyclerView) v.findViewById(R.id.current_set_recycler_view);
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                rv.getChildLayoutPosition(v);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        removeCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                final String verseLocation = (String) response;
                vManager = new VerseOperations(getActivity().getApplicationContext());
                vManager.removeVerse(verseLocation, MemoryListContract.CurrentSetEntry.TABLE_NAME);
                RefreshRecyclerView();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        dataSet = scriptureManager.getScriptureSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
        mAdapter = new RecyclerViewAdapterMyVerses(dataSet, SlidingTabLayout.POSITION_0, removeCallback);
        mRecyclerView.setAdapter(mAdapter);
        RefreshRecyclerView();
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            RefreshRecyclerView();
        }
    }

    public void RefreshRecyclerView(){
        if(mRecyclerView != null) {
            scriptureManager = new ScriptureManager(getContext());
            dataSet = scriptureManager.getScriptureSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
            mAdapter = new RecyclerViewAdapterMyVerses(dataSet, SlidingTabLayout.POSITION_0, removeCallback);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public interface OnAddVerseSelectedListener{
        void addVerseSelected();
    }
}