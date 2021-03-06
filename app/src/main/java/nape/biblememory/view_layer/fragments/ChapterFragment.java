package nape.biblememory.view_layer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.view_layer.adapters.ChapterGridviewAdapter;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChapterFragment extends Fragment {
    private GridView gridView;
    private long numOfChapters;
    private BaseCallback refreshDataCallback;
    private BaseCallback chapterSelectedCallback;
    private UserPreferences mPrefs;
    private String chaptNumSelected;
    private String bookNameForSavedView;
    private FirebaseAnalytics mFirebaseAnalytics;

    public ChapterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_chapter, container, false);
        chaptNumSelected = "0";

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Books_fragment", null);

        chapterSelectedCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                chaptNumSelected = (String)response;
                mPrefs.setSelectedChapter(chaptNumSelected, getContext());
                ((ChapterFragment.ChaptersFragmentListener) getActivity()).onChapterSelected((String) response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        List<String> dataList = new ArrayList<>();
        gridView = (GridView) v.findViewById(R.id.chapter_gridview);
        gridView.setAdapter(new ChapterGridviewAdapter(v.getContext(), dataList, 0, chapterSelectedCallback));
        mPrefs = new UserPreferences();

        bookNameForSavedView = mPrefs.getSelectedBook(getContext());

        refreshDataCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {
                List<String> chaptersList = new ArrayList<>();
                numOfChapters = (long) response;
                for(int i = 1; i <= numOfChapters; i++){
                    chaptersList.add(String.valueOf(i));
                }
                gridView = (GridView) v.findViewById(R.id.chapter_gridview);
                int tempNum = Integer.valueOf(chaptNumSelected);
                if(tempNum != 0){
                    tempNum = tempNum - 1;
                }
                if(mPrefs.getSelectedBook(getContext()).equalsIgnoreCase(bookNameForSavedView)) {
                    gridView.setAdapter(new ChapterGridviewAdapter(v.getContext(), chaptersList, tempNum, chapterSelectedCallback));
                }else{
                    bookNameForSavedView = mPrefs.getSelectedBook(getContext());
                    gridView.setAdapter(new ChapterGridviewAdapter(v.getContext(), chaptersList, 0, chapterSelectedCallback));
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        gridView = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if(refreshDataCallback != null){
                refreshDataCallback.onResponse(getNumOfChaptersToDisplay());
            }
        }
    }

    private Long getNumOfChaptersToDisplay() {
        Long result = mPrefs.getNumberOfChapters(getContext());
        return result;
    }

    public interface ChaptersFragmentListener{
        void onChapterSelected(String chapterNum);
    }

}
