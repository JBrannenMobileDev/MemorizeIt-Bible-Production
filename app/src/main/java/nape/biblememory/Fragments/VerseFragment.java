package nape.biblememory.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.faithcomesbyhearing.dbt.model.Chapter;
import com.faithcomesbyhearing.dbt.model.Verse;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.Adapters.VersesGridviewAdapter;
import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.DBTApi.DBTApi;
import nape.biblememory.UserPreferences;
import nape.biblememory.R;
import nape.biblememory.Views.VerseFragmentCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerseFragment extends Fragment {

    private GridView gridView;
    private long numOfVerses;
    private BaseCallback refreshDataCallback;
    private VerseFragmentCallback verseSelectedCallback;
    private BaseCallback<List<Chapter>> chapterCallback;
    private UserPreferences mPrefs;
    private List<Chapter> chapterList;
    private String chapterId;
    private OnVerseSelected verseSelectedListener;
    private Context context;
    private int previousSelectedVersePosition;
    private ProgressBar verseSelectedLoadingBar;
    private FirebaseAnalytics mFirebaseAnalytics;

    public VerseFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            verseSelectedListener = (OnVerseSelected) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public void setGridViewVisible(){
        gridView.setVisibility(View.VISIBLE);
        verseSelectedLoadingBar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_verse, container, false);
        context = getActivity().getApplicationContext();
        verseSelectedLoadingBar = (ProgressBar) v.findViewById(R.id.verse_selected_loading_bar);
        List<String> dataList = new ArrayList<>();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Books_fragment", null);

        verseSelectedCallback = new VerseFragmentCallback() {
            @Override
            public void onResponse(Object response, int selectedPosition) {
                verseSelectedLoadingBar.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                mPrefs.setSelectedVerseNum((String) response, context);
                mPrefs.setTempSelectedVersion("", getActivity().getApplicationContext());
                verseSelectedListener.onVerseSelected((String) response);
                previousSelectedVersePosition = selectedPosition;
                setUserVisibleHint(true);
            }

            @Override
            public void onResponse(Object response) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };


        gridView = (GridView) v.findViewById(R.id.verses_gridview);
        gridView.setAdapter(new VersesGridviewAdapter(v.getContext(), dataList, 0, verseSelectedCallback));
        mPrefs = new UserPreferences();

        refreshDataCallback = new BaseCallback() {
            @Override
            public void onResponse(Object response) {

                chapterCallback = new BaseCallback<List<Chapter>>() {
                    @Override
                    public void onResponse(List<Chapter> response) {
                        chapterList = response;
                        chapterId = getChapterId();
                        final List<String> verseNumList = new ArrayList<>();

                        BaseCallback<Integer> verseNumCallback = new BaseCallback<Integer>() {
                            @Override
                            public void onResponse(Integer response) {
                                verseSelectedLoadingBar.setVisibility(View.GONE);
                                numOfVerses = response;
                                mPrefs.setNumberOfVerses(numOfVerses, context);
                                for (int i = 1; i <= numOfVerses; i++) {
                                    verseNumList.add(String.valueOf(i));
                                }
                                gridView = (GridView) v.findViewById(R.id.verses_gridview);
                                gridView.setAdapter(new VersesGridviewAdapter(v.getContext(), verseNumList, previousSelectedVersePosition, verseSelectedCallback));
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        };
                        getNumOfVersesToDisplay(verseNumCallback);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };
                getChapterList(chapterCallback);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(refreshDataCallback != null){
                refreshDataCallback.onResponse(null);
            }
        }
    }

    private void getNumOfVersesToDisplay(final BaseCallback<Integer> numOfVersesCallback) {
        final DBTApi REST = new DBTApi(getActivity().getApplicationContext());

        BaseCallback<List<Verse>> verseListCallback = new BaseCallback<List<Verse>>() {
            @Override
            public void onResponse(List<Verse> response) {
                numOfVersesCallback.onResponse(response.size());
            }

            @Override
            public void onFailure(Exception e) {
            }
        };

        if(mPrefs.isBookLocationOT(context)) {
            REST.getVerseRange(verseListCallback, mPrefs.getDamIdOldTestament(context), mPrefs.getSelectedBookId(context), null, null, chapterId);
        }else{
            REST.getVerseRange(verseListCallback, mPrefs.getDamIdNewTestament(context), mPrefs.getSelectedBookId(context), null, null, chapterId);
        }
    }

    private void getChapterList(final BaseCallback<List<Chapter>> chapterSelectedCallback){
        final DBTApi REST = new DBTApi(getActivity().getApplicationContext());

        final BaseCallback<List<Chapter>> chapterCallback = new BaseCallback<List<Chapter>>() {
            @Override
            public void onResponse(List<Chapter> response) {
                chapterList = response;
                chapterSelectedCallback.onResponse(response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        REST.getChapterList(chapterCallback, mPrefs.getDamId(context), mPrefs.getSelectedBookId(context));
    }

    private String getChapterId(){
        String selectedChapter = mPrefs.getSelectedChapter(context);
        String result = "";
        for(Chapter chapter : chapterList){
            if(selectedChapter.equalsIgnoreCase(chapter.getChapterId())){
                result = chapter.getChapterId();
                mPrefs.setChapterId(result, context);
            }
        }
        return result;
    }

    public interface OnVerseSelected{
        void onVerseSelected(String response);
    }

}
