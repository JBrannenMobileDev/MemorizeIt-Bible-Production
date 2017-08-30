package nape.biblememory.Fragments;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import nape.biblememory.Adapters.RecyclerViewAdapterMyVerses;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Fragments.Dialogs.RemoveVerseFromInProgressAlertDialog;
import nape.biblememory.Fragments.Dialogs.RemoveVerseFromNewVersesAlertDialog;
import nape.biblememory.Managers.ScriptureManager;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Presenters.LearningSetFragmentPresenter;
import nape.biblememory.Presenters.LearningSetFragmentPresenterImp;
import nape.biblememory.UserPreferences;
import nape.biblememory.data_store.DataStore;
import nape.biblememory.data_store.FirebaseDb.FirebaseDb;
import nape.biblememory.data_store.Sqlite.MemoryListContract;
import nape.biblememory.Views.SlidingTabLayout;
import nape.biblememory.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class LearningSetFragment extends Fragment implements LearningSetFragmentView{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ScriptureData> dataSet;
    private BaseCallback removeCallback;
    private LearningSetFragmentPresenter mPresenter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Context appContext;
    private FloatingActionButton addVerseFab;
    private UserPreferences mPrefs;
    private TextView emptyStateTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Quiz verses view", null);
        appContext = getActivity().getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshRecyclerView();
        mPrefs = new UserPreferences();
        if(mPrefs.isSnackbarVisible(getActivity().getApplicationContext())) {
            float distance = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 40,
                    getResources().getDisplayMetrics()
            );
            moveNewVerseFab(-distance);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_learning_set,container,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.learning_set_recycler_view);

        addVerseFab = (FloatingActionButton) v.findViewById(R.id.add_verse_Quiz_verses_fab);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        emptyStateTv = (TextView) v.findViewById(R.id.empty_state_learning_tv);

        mPresenter = new LearningSetFragmentPresenterImp(this, getActivity().getApplicationContext());

        removeCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                Bundle bundle = new Bundle();
                bundle.putString("verse_location", (String)response);
                RemoveVerseFromNewVersesAlertDialog removeAlert = new RemoveVerseFromNewVersesAlertDialog();
                removeAlert.setArguments(bundle);
                removeAlert.show(getChildFragmentManager(), null);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        RefreshRecyclerView();
        addVerseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ((LearningSetFragment.OnAddVerseSelectedListener) getActivity()).addVerseSelected();
                    mFirebaseAnalytics.logEvent("add_verse_from_QuizVers_selected", null);
                }catch (ClassCastException cce){

                }
            }
        });

        mPrefs = new UserPreferences();
        if(mPrefs.isSnackbarVisible(getActivity().getApplicationContext())) {
            float distance = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 40,
                    getResources().getDisplayMetrics()
            );
            moveNewVerseFab(-distance);
        }
        return v;
    }

    public void RefreshRecyclerView(){

        BaseCallback<List<ScriptureData>> learningCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                dataSet = response;
                if(response != null) {
                    if(response.size() > 0) {
                        emptyStateTv.setVisibility(View.GONE);
                    }
                    mAdapter = new RecyclerViewAdapterMyVerses(dataSet, SlidingTabLayout.POSITION_1, removeCallback, null, null);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getLocalQuizVerses(learningCallback, appContext);
    }

    @Override
    public void onRemoveClicked() {
        RefreshRecyclerView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            RefreshRecyclerView();
        }
    }

    public void moveNewVerseFab(float distance) {
        addVerseFab.animate().translationY(distance);
    }

    public interface OnAddVerseSelectedListener{
        void addVerseSelected();
    }
}