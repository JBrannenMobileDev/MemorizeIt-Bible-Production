package nape.biblememory.Fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nape.biblememory.Adapters.RecyclerViewAdapterMemorized;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Managers.ScriptureManager;
import nape.biblememory.Models.BookGroup;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Singletons.RecyclerViewSingleton;
import nape.biblememory.Sqlite.MemoryListContract;
import nape.biblememory.UserPreferences;
import nape.biblememory.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class MemorizedSetFragment extends Fragment {

    private RecyclerView verseListRecyclerView;
    private RecyclerView.Adapter verseListAdapter;
    private RecyclerView.LayoutManager verseListLayoutManager;
    private ScriptureManager scriptureManager;
    private List<ScriptureData> dataSet;
    private List<String> bookDataSet;
    private BaseCallback bookSelectedCallback;
    private BaseCallback bookDeselectedCallback;
    private Spinner spinner;
    private UserPreferences mPrefs;
    List<Object> combinedList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scriptureManager = new ScriptureManager(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_memorized_set,container,false);
        verseListRecyclerView = (RecyclerView) v.findViewById(R.id.memorized_set_recycler_view);
        spinner = (Spinner) v.findViewById(R.id.memorized_sort_spinner);
        mPrefs = new UserPreferences();

        createBookSelectedCallback();
        initializeSpinner(v);
        initializeRecyclerView(v);

        return v;
    }

    public void RefreshRecyclerView(){
        mPrefs.setPreviouslySelectedBookGroup("", getContext());
        setAdapterForRecyclerView();
    }

    private void createBookSelectedCallback() {
        bookSelectedCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                updateAdapterForExpandBook((String) response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        bookDeselectedCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                updateAdapterForCollapseBook((String) response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    private void initializeRecyclerView(View v) {
        verseListLayoutManager = new LinearLayoutManager(v.getContext());
        verseListRecyclerView.setLayoutManager(verseListLayoutManager);
        dataSet = scriptureManager.getScriptureSet(MemoryListContract.RememberedSetEntry.TABLE_NAME);
        dataSet.addAll(scriptureManager.getScriptureSet(MemoryListContract.MemorizedSetEntry.TABLE_NAME));
        setAdapterForRecyclerView();
    }

    private void initializeSpinner(View v) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.sort_options_array, R.layout.spinner_item_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(mPrefs.getMemorizedSpinnerPosition(getContext()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPrefs.setMemorizedSpinnerPosition(position, getContext());
                setAdapterForRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateAdapterForCollapseBook(String bookName){
        List<Object> tempVerseList;
        if(combinedList != null){
            tempVerseList = removeVersesFromBook(bookName, combinedList);
            combinedList = tempVerseList;
        }else{
            tempVerseList = getBookGroupList(getAllMemorizedVerses());
        }
        verseListAdapter = new RecyclerViewAdapterMemorized(tempVerseList, bookSelectedCallback, bookDeselectedCallback);
        verseListRecyclerView.setAdapter(verseListAdapter);
    }

    private void updateAdapterForExpandBook(String bookName){
        List<Object> bookList;
        List<Object> verseList;
        if(combinedList != null) {
            bookList = combinedList;
        }else{
            bookList = getBookGroupList(getAllMemorizedVerses());
        }
        verseList = getAllMemorizedVerses();
        combinedList = createCombinedList(bookName, bookList, verseList);

        verseListAdapter = new RecyclerViewAdapterMemorized(combinedList, bookSelectedCallback, bookDeselectedCallback);
        verseListRecyclerView.setAdapter(verseListAdapter);
    }

    private List<Object> createCombinedList(String bookName, List<Object> bookList, List<Object> allVersesList) {
        int bookListSize = bookList.size();
        List<Object> filteredVersesList = getFilteredVerses(bookName, allVersesList);

        if(filteredVersesList != null && filteredVersesList.size() > 0){
            List<Object> resultList = new ArrayList<>();
            List<Object> half1;
            List<Object> half2;
            int count = 0;
            for(Object book : bookList){
                if(bookListSize == 1){
                    resultList.addAll(bookList);
                    resultList.addAll(filteredVersesList);
                    return resultList;
                }else if(book instanceof BookGroup){
                    if(((BookGroup) book).getBookName().equals(bookName)){
                        half1 = bookList.subList(0,count+1);
                        half2 = bookList.subList(count+1, bookListSize);
                        resultList.addAll(half1);
                        resultList.addAll(filteredVersesList);
                        resultList.addAll(half2);
                        return resultList;
                    }
                }
                count++;
            }
            return null;
        }else {
            return null;
        }
    }

    private List<Object> getFilteredVerses(String bookName, List<Object> allVersesList) {
        List<Object> filteredVersesList = new ArrayList<>();
        ScriptureData sDataVerse;
        for(Object verse : allVersesList){
            sDataVerse = (ScriptureData) verse;
            if(bookName.equals(parseBookName(sDataVerse.getVerseLocation()))){
                filteredVersesList.add(verse);
            }
        }
        return filteredVersesList;
    }

    private List<Object> removeVersesFromBook(String bookName, List<Object> objList){
        List<Object> resultList = new ArrayList<>();

        for(Object obj : objList){
            if(obj instanceof ScriptureData){
                if(!parseBookName(((ScriptureData) obj).getVerseLocation()).equals(bookName)){
                    resultList.add(obj);
                }
            }else{
                resultList.add(obj);
            }
        }
        return resultList;
    }

    private void setAdapterForRecyclerView() {
        RecyclerViewSingleton.getInstance().listOfSelectedBooks = new ArrayList<>();
        List<Object> tempVerseList = new ArrayList<>();
        switch(mPrefs.getMemorizedSpinnerPosition(getContext())){
            //Book Name
            case 0:
                tempVerseList = getBookGroupList(getAllMemorizedVerses());
                break;

            //Verse Group
            case 1:
                break;

            //Newest
            case 2:
                tempVerseList = getAllMemorizedVerses();
                break;

            //Oldest
            case 3:
                tempVerseList = getAllMemorizedVerses();
                Collections.reverse(tempVerseList);
                break;
        }
        verseListAdapter = new RecyclerViewAdapterMemorized(tempVerseList, bookSelectedCallback, bookDeselectedCallback);
        verseListRecyclerView.setAdapter(verseListAdapter);
    }

    private List<Object> getBookGroupList(List<Object> allVerses) {
        List<Object> bookGroupList = new ArrayList<>();
        int bookPosition;
        for(Object verse : allVerses){
            ScriptureData scripture = (ScriptureData)verse;
            String bookName = parseBookName(scripture.getVerseLocation());
            BookGroup bg;
            bookPosition = getBookPosition(bookGroupList, bookName);
            if(bookPosition == -1){
                bg = new BookGroup(bookName, 1);
                bookGroupList.add(bg);
            }else {
                bg = (BookGroup)bookGroupList.get(bookPosition);
                int numMemorized = bg.getNumOfVersesMemorized() + 1;
                bg.setNumOfVersesMemorized(numMemorized);
            }
        }
        return bookGroupList;
    }

    private int getBookPosition(List<Object> bookGroupList, String bookName) {
        int counter = 0;
        for(Object bookGroup : bookGroupList){
            if(((BookGroup) bookGroup).getBookName().equalsIgnoreCase(bookName)){
                return counter;
            }
            counter++;
        }
        return -1;
    }


    public List<Object> getAllVerseBooks(List<Object> dataSet) {
        List<Object> bookList = new ArrayList<>();
        for(Object verse : dataSet){
            ScriptureData tempVerse = (ScriptureData)verse;
            if(tempVerse.getVerseLocation() != null){
                String verseLocation = tempVerse.getVerseLocation();
                boolean parsedBookName = false;

                int index = 0;
                while(!parsedBookName){
                    if((verseLocation.charAt(index) == 1) || (verseLocation.charAt(index) == 2) || (verseLocation.charAt(index) == 3)){
                        parsedBookName = false;
                    }else if(verseLocation.charAt(index) == ' '){
                                String parsedBookNameString = verseLocation.substring(0,index);
                                if(!bookList.contains(parsedBookNameString)){
                                    bookList.add(parsedBookNameString);
                                }
                        parsedBookName = true;
                    }
                    index++;
                }
            }
        }
        return bookList;
    }

    private String parseBookName(String verseLocation){
        String bookName = null;
        if(verseLocation != null){
            boolean parsedBookName = false;

            int index = 0;
            while(!parsedBookName){
                if((verseLocation.charAt(index) == 1) || (verseLocation.charAt(index) == 2) || (verseLocation.charAt(index) == 3)){
                    parsedBookName = false;
                }else if(verseLocation.charAt(index) == ' '){
                    bookName = verseLocation.substring(0,index);
                    parsedBookName = true;
                }
                index++;
            }
        }
        return bookName;
    }

    public List<Object> getAllMemorizedVerses() {
        List<Object> versesList = new ArrayList<>();
        versesList.addAll(scriptureManager.getScriptureSet(MemoryListContract.RememberedSetEntry.TABLE_NAME));
        versesList.addAll(scriptureManager.getScriptureSet(MemoryListContract.MemorizedSetEntry.TABLE_NAME));
        return versesList;
    }
}