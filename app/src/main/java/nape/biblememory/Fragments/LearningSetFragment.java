package nape.biblememory.Fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nape.biblememory.Adapters.RecyclerViewAdapterMyVerses;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Fragments.Dialogs.RemoveVerseFromInProgressAlertDialog;
import nape.biblememory.Managers.ScriptureManager;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Presenters.LearningSetFragmentPresenter;
import nape.biblememory.Presenters.LearningSetFragmentPresenterImp;
import nape.biblememory.Sqlite.MemoryListContract;
import nape.biblememory.Views.SlidingTabLayout;
import nape.biblememory.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class LearningSetFragment extends Fragment implements LearningSetFragmentView{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ScriptureManager scriptureManager;
    private List<ScriptureData> dataSet;
    private BaseCallback removeCallback;
    private LearningSetFragmentPresenter mPresenter;

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
        View v =inflater.inflate(R.layout.fragment_learning_set,container,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.learning_set_recycler_view);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPresenter = new LearningSetFragmentPresenterImp(this, getContext());

        removeCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                Bundle bundle = new Bundle();
                bundle.putString("verse_location", (String)response);
                RemoveVerseFromInProgressAlertDialog removeAlert = new RemoveVerseFromInProgressAlertDialog();
                removeAlert.setArguments(bundle);
                removeAlert.show(getChildFragmentManager(), null);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        dataSet = scriptureManager.getScriptureSet(MemoryListContract.LearningSetEntry.TABLE_NAME);
        mAdapter = new RecyclerViewAdapterMyVerses(dataSet, SlidingTabLayout.POSITION_1, removeCallback, null);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public void RefreshRecyclerView(){
        dataSet = scriptureManager.getScriptureSet(MemoryListContract.LearningSetEntry.TABLE_NAME);
        mAdapter = new RecyclerViewAdapterMyVerses(dataSet, SlidingTabLayout.POSITION_1, removeCallback, null);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRemoveClicked(Integer responseCode) {
        RefreshRecyclerView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            scriptureManager = new ScriptureManager(getContext());
            RefreshRecyclerView();
        }
    }

    public void onYesSelected(String verseLocation) {
        mPresenter.onRemoveClicked(verseLocation);
    }
}