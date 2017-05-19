package nape.biblememory.Presenters;

import android.content.Context;

import java.util.List;

import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Sqlite.MemoryListContract;
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
        vManager = new VerseOperations(context);
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
            vManager.addVerse(scripture, MemoryListContract.CurrentSetEntry.TABLE_NAME);
        }

        mView.onRemoveClicked(vManager.removeVerse(verseLocation, MemoryListContract.LearningSetEntry.TABLE_NAME));
    }
}
