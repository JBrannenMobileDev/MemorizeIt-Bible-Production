package nape.biblememory.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nape.biblememory.Activities.PhoneUnlockActivity;
import nape.biblememory.Adapters.RecyclerViewAdapterMemorized;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Models.BookGroup;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Singletons.RecyclerViewSingleton;
import nape.biblememory.data_store.DataStore;
import nape.biblememory.UserPreferences;
import nape.biblememory.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class MemorizedSetFragment extends Fragment {

    private RecyclerView verseListRecyclerView;
    private RecyclerView.Adapter verseListAdapter;
    private RecyclerView.LayoutManager verseListLayoutManager;
    private List<ScriptureData> dataSet;
    private List<String> bookDataSet;
    private BaseCallback bookSelectedCallback;
    private BaseCallback bookDeselectedCallback;
    private MaterialSpinner spinner;
    private UserPreferences mPrefs;
    private List<Object> combinedList;
    private TextView reviewBt;
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<Object> allMemorizedVerses;

    private CardView forgottenVerseCardView;
    private TextView verseLocation;
    private TextView verse;
    private TextView memorizedDateTitle;
    private TextView lastSeenDateTitle;
    private TextView memorizedDateText;
    private TextView lastSeenDateText;
    private TextView emptyState;
    private ImageView forgottenArrow;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Memorized verses view", null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllMemorizedVerses();
        RefreshRecyclerView();
        initializeForgottenCardView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_memorized_set,container,false);
        verseListRecyclerView = (RecyclerView) v.findViewById(R.id.memorized_set_recycler_view);
        spinner = (MaterialSpinner) v.findViewById(R.id.memorized_sort_spinner);
        reviewBt = (TextView) v.findViewById(R.id.memorized_review_tv);
        mPrefs = new UserPreferences();

        verseLocation = (TextView) v.findViewById(R.id.verse_location_forgotten);
        forgottenVerseCardView = (CardView) v.findViewById(R.id.card_view_forgotten);
        verse = (TextView) v.findViewById(R.id.verse_forgotten);
        memorizedDateTitle = (TextView) v.findViewById(R.id.memorized_date_title_forgotten);
        lastSeenDateTitle = (TextView) v.findViewById(R.id.last_seen_title_forgotten);
        memorizedDateText = (TextView) v.findViewById(R.id.memorized_date_textview_forgotten);
        lastSeenDateText = (TextView) v.findViewById(R.id.last_seen_textview_forgotten);
        emptyState = (TextView) v.findViewById(R.id.empty_state_memorized_tv);
        forgottenArrow = (ImageView) v.findViewById(R.id.expand_arrow_memorized);

        allMemorizedVerses = new ArrayList<>();
        getAllMemorizedVerses();
        createBookSelectedCallback();
        initializeSpinner(v);
        initializeRecyclerView(v);
        initializeForgottenCardView();

        reviewBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "This feature is in progress. The next app version will include this.", Toast.LENGTH_LONG).show();
//                Intent reviewIntent = new Intent(getActivity(), PhoneUnlockActivity.class);
//                reviewIntent.putExtra("from_review", "from_review");
//                startActivity(reviewIntent);
            }
        });

        forgottenVerseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verse.getVisibility() == View.VISIBLE){
                    collopaseCardView();
                    forgottenArrow.animate().rotation(0);
                }else{
                    expandCardView();
                    forgottenArrow.animate().rotation(180f);
                }
            }
        });

        return v;
    }

    private void initializeForgottenCardView() {
        BaseCallback<ScriptureData> forgottenVerseCallback = new BaseCallback<ScriptureData>() {
            @Override
            public void onResponse(ScriptureData response) {
                if(response != null) {
                    emptyState.setVisibility(View.GONE);
                    populateCardView(response);
                }else{
                    hideForgotenCardView();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getLocalForgottenVerse(forgottenVerseCallback, getActivity().getApplicationContext());
    }

    private void hideForgotenCardView() {
        forgottenVerseCardView.setVisibility(View.GONE);
    }

    private void populateCardView(ScriptureData verse){
        forgottenVerseCardView.setVisibility(View.VISIBLE);
        this.verse.setText(verse.getVerse());
        memorizedDateText.setText(verse.getRemeberedDate());
        lastSeenDateText.setText(verse.getLastSeenDate());
        verseLocation.setText(verse.getVerseLocation());
    }

    private void expandCardView() {
        lastSeenDateTitle.setVisibility(View.VISIBLE);
        lastSeenDateText.setVisibility(View.VISIBLE);
        verse.setVisibility(View.VISIBLE);
    }

    private void collopaseCardView() {
        lastSeenDateTitle.setVisibility(View.GONE);
        lastSeenDateText.setVisibility(View.GONE);
        verse.setVisibility(View.GONE);
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
        BaseCallback<List<ScriptureData>> memorizedCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                dataSet = response;
                if(response != null) {
                    if(response.size() > 0) {
                        emptyState.setVisibility(View.GONE);
                    }
                    setAdapterForRecyclerView();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getLocalMemorizedVerses(memorizedCallback, getActivity().getApplicationContext());
    }

    private void initializeSpinner(View v) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.sort_options_array, R.layout.spinner_item_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setItems("Newest", "Oldest", "Book Name");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mFirebaseAnalytics.logEvent("memorized_view_by_selected", null);
                mPrefs.setMemorizedSpinnerPosition(position, getActivity().getApplicationContext());
                setAdapterForRecyclerView();
            }
        });

        spinner.setSelectedIndex(mPrefs.getMemorizedSpinnerPosition(getActivity().getApplicationContext()));
    }


    private void updateAdapterForCollapseBook(String bookName){
        List<Object> tempVerseList;
        if(combinedList != null){
            tempVerseList = removeVersesFromBook(bookName, combinedList);
            combinedList = tempVerseList;
        }else{
            tempVerseList = getBookGroupList(allMemorizedVerses);
        }
        verseListAdapter = new RecyclerViewAdapterMemorized(tempVerseList, bookSelectedCallback, bookDeselectedCallback);
        verseListRecyclerView.setAdapter(verseListAdapter);
    }

    private void updateAdapterForExpandBook(String bookName){
        List<Object> bookList;
        List<Object> verseList;
        verseList = allMemorizedVerses;
        bookList = getBookGroupList(verseList);
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
        getAllMemorizedVerses();
        RecyclerViewSingleton.getInstance().listOfSelectedBooks = new ArrayList<>();
        List<Object> tempVerseList = new ArrayList<>();
        switch(mPrefs.getMemorizedSpinnerPosition(getContext())){
            //Newest
            case 0:
                tempVerseList = allMemorizedVerses;
                Collections.reverse(tempVerseList);
                break;

            //Oldest
            case 1:
                tempVerseList = allMemorizedVerses;
                break;
            //Book Name

            case 2:
                tempVerseList = getBookGroupList(allMemorizedVerses);
                break;
        }
        verseListRecyclerView.setAdapter(null);
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
        for (int i = 0; i < verseLocation.length(); i++){
            char c = verseLocation.charAt(i);
            if(Character.isSpaceChar(c)){
                if(Character.isDigit(verseLocation.charAt(i+1))){
                    bookName = verseLocation.substring(0, i);
                }
            }
        }
        return bookName;
    }

    public void getAllMemorizedVerses() {
        allMemorizedVerses = new ArrayList<>();

        BaseCallback<List<ScriptureData>> memorizedCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                allMemorizedVerses.addAll(response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getLocalMemorizedVerses(memorizedCallback, getActivity().getApplicationContext());
    }
}