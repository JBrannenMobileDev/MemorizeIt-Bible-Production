package nape.biblememory.view_layer.fragments;


import android.app.Activity;
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
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.RemoveVerse;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.view_layer.activities.VerseTrainingActivity;
import nape.biblememory.view_layer.fragments.dialogs.StarAlertDialog;
import nape.biblememory.view_layer.material_recyclerview.OnStartDragListener;
import nape.biblememory.view_layer.material_recyclerview.RecyclerListAdapterMyVerses;
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
    private BaseCallback<List<ScriptureData>> dataChangedCallback;
    private BaseCallback<ScriptureData> itemSelectedCallback;
    private BaseCallback<RemoveVerse> onItemRemovedCallback;
    private View view;
    private RecyclerListAdapterMyVerses adapter;
    private myVersesListener mListener;

    public MyVersesFragment() {
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.fetchData();
    }


    @Override
    public void onPause(){
        super.onPause();
        presenter.onStop();
    }
    @Override
    public void onStop(){
        super.onStop();
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
        dataChangedCallback = new BaseCallback<List<ScriptureData>>() {
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
                Intent intent = new Intent(getActivity(), VerseTrainingActivity.class);
                intent.putExtra("verseLocation", verse.getVerseLocation());
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        onItemRemovedCallback = new BaseCallback<RemoveVerse>() {
            @Override
            public void onResponse(RemoveVerse verse) {
                mListener.onVerseDeleted(verse.getVerse(), verse.getPosition());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (myVersesListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
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
        adapter = new RecyclerListAdapterMyVerses(scripList, getActivity(), this, this,
                dataChangedCallback, itemSelectedCallback, onItemRemovedCallback);
        RecyclerView recyclerView = (RecyclerView)view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void undoDelete(final ScriptureData verse, final int position) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(verse.toMyVerse());
            }
        });
        realm.close();
        adapter.addItem(verse, position);
    }

    public interface myVersesListener {
        void onVerseDeleted(ScriptureData verse, int position);
    }
}
