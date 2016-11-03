package nape.biblememory.Activities.Fragments.Dialogs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.Activities.Adapters.ChapterGridviewAdapter;
import nape.biblememory.Activities.Adapters.VersesGridviewAdapter;
import nape.biblememory.Activities.Interfaces.BaseCallback;
import nape.biblememory.Activities.UserPreferences;
import nape.biblememory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerseFragment extends Fragment {

    private GridView gridView;
    private int numOfVerses;
    private BaseCallback refreshDataCallback;
    private BaseCallback verseSelectedCallback;
    private UserPreferences mPrefs;

    public VerseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_verse, container, false);
        List<String> dataList = new ArrayList<>();
        gridView = (GridView) v.findViewById(R.id.verses_gridview);
        gridView.setAdapter(new VersesGridviewAdapter(v.getContext(), dataList, 0, verseSelectedCallback));
        mPrefs = new UserPreferences();

        verseSelectedCallback = new BaseCallback() {
            @Override
            public void OnResponse(Object obj) {

            }
        };

        refreshDataCallback = new BaseCallback() {
            @Override
            public void OnResponse(Object obj) {
                List<String> chaptersList = new ArrayList<>();
                numOfVerses = (int) obj;
                for(int i = 1; i <= numOfVerses; i++){
                    chaptersList.add(String.valueOf(i));
                }
                gridView = (GridView) v.findViewById(R.id.verses_gridview);
                gridView.setAdapter(new VersesGridviewAdapter(v.getContext(), chaptersList, 0, verseSelectedCallback));
            }
        };
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(refreshDataCallback != null){
                refreshDataCallback.OnResponse(getNumOfVersesToDisplay());
            }
        }
    }

    private Object getNumOfVersesToDisplay() {
        Object result = mPrefs.getNumberOfChapters(getContext());
        return result;
    }

}
