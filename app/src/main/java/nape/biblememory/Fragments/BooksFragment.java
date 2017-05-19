package nape.biblememory.Fragments;


import android.content.Context;
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
import java.util.List;

import nape.biblememory.Adapters.RecyclerViewAdapterBooks;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.DBTApi.DBTApi;
import nape.biblememory.UserPreferences;
import nape.biblememory.Views.SlidingTabLayout;
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
    private Context context;

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

        context = getActivity().getApplicationContext();

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        oldTestament = new ArrayList<>();
        newTestament = new ArrayList<>();

        bookSelectedCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                mPrefs.setSelectedBook((String) response, getContext());
                mPrefs.setSelectedBookId(getBookId((String)response), context);
                mPrefs.setNumberOfChapters(getNumberOfChapters((String)response), context);
                setBookLocation((String)response);
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

    private void setBookLocation(String response) {
        boolean isOT = false;
        for(int i = 0; i < oldTestament.size(); i++){
            if(oldTestament.get(i).getBookName().equalsIgnoreCase(response)){
                isOT = true;
                break;
            }
        }
        mPrefs.setBookLocationOT(isOT, context);
    }

    private String getBookId(String response) {
        for(Book book : newTestament){
            if(response.equalsIgnoreCase(book.getBookName())){
                return book.getBookId();
            }
        }

        for(Book book : oldTestament){
            if(response.equalsIgnoreCase(book.getBookName())){
                return book.getBookId();
            }
        }
        return "";
    }

    private long getNumberOfChapters(String response) {
        for(Book book : newTestament){
            if(response.equalsIgnoreCase(book.getBookName())){
                return book.getNumberOfChapters();
            }
        }

        for(Book book : oldTestament){
            if(response.equalsIgnoreCase(book.getBookName())){
                return book.getNumberOfChapters();
            }
        }

        return 0;
    }

    public interface BooksFragmentListener{
        void onBookSelected(String bookName);
    }

    private void getBookList(final BaseCallback bookSelectedCallback){
        final DBTApi REST = new DBTApi(context);
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
                mPrefs.setDamId(volumeList.get(0).getDamId().substring(0,6), context);
                mPrefs.setDamIdNewTestament(volumeList.get(0).getDamId(), context);
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
                mPrefs.setDamIdNewTestament(volumeList.get(1).getDamId(), context);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        REST.getVolumeList(volumesCallbackOldTest, "eng");
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
