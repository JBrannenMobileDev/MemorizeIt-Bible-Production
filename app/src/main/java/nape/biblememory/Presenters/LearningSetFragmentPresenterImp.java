package nape.biblememory.Presenters;

import android.content.Context;

import java.util.List;

import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.data_store.DataStore;
import nape.biblememory.data_store.Sqlite.MemoryListContract;
import nape.biblememory.Fragments.LearningSetFragmentView;

/**
 * Created by Jonathan on 9/20/2016.
 */
public class LearningSetFragmentPresenterImp implements LearningSetFragmentPresenter {
    private LearningSetFragmentView mView;
    private VerseOperations vManager;
    private Context context;

    public LearningSetFragmentPresenterImp(LearningSetFragmentView view, Context context){
        mView = view;
        this.context = context;
        vManager = VerseOperations.getInstance(context);
    }

    @Override
    public void onRemoveClicked(String verseLocation) {
        List<ScriptureData> scriptureList = vManager.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME);
        ScriptureData scripture = null;

        for(ScriptureData verse : scriptureList){
            if(verse.getVerseLocation() != null) {
                if (verse.getVerseLocation().equalsIgnoreCase(verseLocation)) {
                    scripture = verse;
                }
            }
        }

        if(scripture != null) {
            DataStore.getInstance().saveUpcomingVerse(scripture, context);
            DataStore.getInstance().deleteQuizVerse(scripture, context);
            mView.onRemoveClicked();
        }
    }
}
