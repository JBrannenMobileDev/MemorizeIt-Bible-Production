package nape.biblememory.Activities.Fragments.Dialogs;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nape.biblememory.Activities.Adapters.RecyclerViewAdapterBooks;
import nape.biblememory.Activities.Adapters.RecyclerViewAdapterMyVerses;
import nape.biblememory.Activities.Interfaces.BaseCallback;
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

    public BooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_books, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.books_recycler_view);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        bookSelectedCallback = new BaseCallback() {
            @Override
            public void OnResponse(Object obj) {
                ((BooksFragment.BooksFragmentListener) getActivity()).onBookSelected((String) obj);
            }
        };

        Resources res = getResources();
        String[] books = res.getStringArray(R.array.books_of_the_bible);

        mAdapter = new RecyclerViewAdapterBooks(books, SlidingTabLayout.POSITION_1, bookSelectedCallback);
        mRecyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        return v;
    }

    public interface BooksFragmentListener{
        void onBookSelected(String bookName);
    }

}
