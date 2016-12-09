package nape.biblememory.Activities.Fragments.Dialogs;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faithcomesbyhearing.dbt.model.Volume;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.Activities.Adapters.RecyclerViewAdapterBooks;
import nape.biblememory.Activities.Adapters.RecyclerViewAdapterMyVerses;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Activities.DBTApi.DBTApi;

import nape.biblememory.Activities.UserPreferences;
import nape.biblememory.Activities.Views.SlidingTabLayout;
import nape.biblememory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BaseCallback bookSelectedCallback;
    private UserPreferences mPrefs;

    List<Volume> volumeList;


    public BooksFragment() {
        volumeList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_books, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.books_recycler_view);

        mPrefs = new UserPreferences();

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        bookSelectedCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                mPrefs.setSelectedBook((String) response, getContext());
                ((BooksFragment.BooksFragmentListener) getActivity()).onBookSelected((String) response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        getBookList(bookSelectedCallback);

        // Inflate the layout for this fragment
        return v;
    }

    public interface BooksFragmentListener{
        void onBookSelected(String bookName);
    }

    private void getBookList(final BaseCallback bookSelectedCallback){
        final DBTApi REST = new DBTApi(getActivity().getApplicationContext());

        final BaseCallback booksCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                handleRESTResponse(bookSelectedCallback, response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        BaseCallback volumesCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                volumeList = (List<Volume>) response;
                //TODO filter out desired volume from volumesList.
                REST.getBooksList(booksCallback, volumeList.get(0).getDamId());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        REST.getVolumeList(volumesCallback, mPrefs.getSelectedBibleLanguage(getActivity().getApplicationContext()));
    }

    private void handleRESTResponse(BaseCallback bookSelectedCallback, Object response){
        mAdapter = new RecyclerViewAdapterBooks((List<String>)response, SlidingTabLayout.POSITION_1, bookSelectedCallback);
        mRecyclerView.setAdapter(mAdapter);
    }
}
