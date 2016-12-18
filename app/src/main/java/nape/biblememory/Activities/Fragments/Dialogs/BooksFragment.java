package nape.biblememory.Activities.Fragments.Dialogs;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faithcomesbyhearing.dbt.model.Book;
import com.faithcomesbyhearing.dbt.model.Volume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nape.biblememory.Activities.Adapters.RecyclerViewAdapterBooks;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Activities.DBTApi.DBTApi;
import nape.biblememory.Activities.UserPreferences;
import nape.biblememory.Activities.Views.SlidingTabLayout;
import nape.biblememory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {

    private static final String TAG = "BooksFragment: ";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BaseCallback bookSelectedCallback;
    private UserPreferences mPrefs;
    private List<Book> newTestament;
    private List<Book> oldTestament;

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

        oldTestament = new ArrayList<>();
        newTestament = new ArrayList<>();

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
        String selectedBibleLanguage;

        final BaseCallback<List<Book>> booksCallbackNewTest = new BaseCallback<List<Book>>() {
            @Override
            public void onResponse(List<Book> response) {
                newTestament = response;
                List<String> bookList;
                bookList = getBookNameList(oldTestament);
                bookList.addAll(getBookNameList(newTestament));
                handleRESTResponse(bookSelectedCallback, bookList);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        final BaseCallback volumesCallbackNewTest = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                volumeList = (List<Volume>) response;
                //TODO filter out desired volume from volumesList.
                REST.getBooksList(booksCallbackNewTest, volumeList.get(0).getDamId());
            }

            @Override
            public void onFailure(Exception e) {
                String t = e.getMessage();
            }
        };

        final BaseCallback<List<Book>> booksCallbackOldTest = new BaseCallback<List<Book>>() {
            @Override
            public void onResponse(List<Book> response) {
                oldTestament = response;
                REST.getVolumeList(volumesCallbackNewTest, "eng");
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        final BaseCallback volumesCallbackOldTest = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                volumeList = (List<Volume>) response;
                //TODO filter out desired volume from volumesList.

                REST.getBooksList(booksCallbackOldTest, volumeList.get(1).getDamId());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        selectedBibleLanguage = mPrefs.getSelectedVersion(getActivity().getApplicationContext());

        if("ESV".equals(selectedBibleLanguage)){
            Resources res = getResources();
            List<String> books = Arrays.asList(res.getStringArray(R.array.books_of_the_bible));
            handleRESTResponse(bookSelectedCallback, books);
        }else {
            REST.getVolumeList(volumesCallbackOldTest, "eng");
        }
    }

    private List<String> getBookNameList(List<Book> bookList) {
        List<String> bookNameList = new ArrayList<>();
        for(Book book : bookList){
            bookNameList.add(book.getBookName());
        }
        return bookNameList;
    }

    private void handleRESTResponse(BaseCallback bookSelectedCallback, List<String> response){
        mAdapter = new RecyclerViewAdapterBooks(response, SlidingTabLayout.POSITION_1, bookSelectedCallback);
        mRecyclerView.setAdapter(mAdapter);
    }
}
