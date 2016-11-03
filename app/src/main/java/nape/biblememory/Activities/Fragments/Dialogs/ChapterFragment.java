package nape.biblememory.Activities.Fragments.Dialogs;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.Activities.Adapters.ChapterGridviewAdapter;
import nape.biblememory.Activities.Interfaces.BaseCallback;
import nape.biblememory.Activities.Managers.ScriptureManager;
import nape.biblememory.Activities.UserPreferences;
import nape.biblememory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChapterFragment extends Fragment {
    private GridView gridView;
    private int numOfChapters;
    private BaseCallback refreshDataCallback;
    private BaseCallback chapterSelectedCallback;
    private UserPreferences mPrefs;
    private String chaptNumSelected;

    public ChapterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_chapter, container, false);
        chaptNumSelected = "0";

        chapterSelectedCallback = new BaseCallback() {
            @Override
            public void OnResponse(Object obj) {
                chaptNumSelected = (String)obj;
                ((ChapterFragment.ChaptersFragmentListener) getActivity()).onChapterSelected((String) obj);
            }
        };

        List<String> dataList = new ArrayList<>();
        gridView = (GridView) v.findViewById(R.id.chapter_gridview);
        gridView.setAdapter(new ChapterGridviewAdapter(v.getContext(), dataList, 0, chapterSelectedCallback));
        mPrefs = new UserPreferences();

        refreshDataCallback = new BaseCallback() {
            @Override
            public void OnResponse(Object obj) {
                List<String> chaptersList = new ArrayList<>();
                numOfChapters = (int) obj;
                for(int i = 1; i <= numOfChapters; i++){
                    chaptersList.add(String.valueOf(i));
                }
                gridView = (GridView) v.findViewById(R.id.chapter_gridview);
                int tempNum = Integer.valueOf(chaptNumSelected);
                if(tempNum != 0){
                    tempNum = tempNum - 1;
                }
                gridView.setAdapter(new ChapterGridviewAdapter(v.getContext(), chaptersList, tempNum, chapterSelectedCallback));
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
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(refreshDataCallback != null){
                refreshDataCallback.OnResponse(getNumOfChaptersToDisplay());
            }
        }
    }

    private Object getNumOfChaptersToDisplay() {
        Object result = mPrefs.getNumberOfChapters(getContext());
        return result;
    }

    public interface ChaptersFragmentListener{
        void onChapterSelected(String chapterNum);
    }

}
