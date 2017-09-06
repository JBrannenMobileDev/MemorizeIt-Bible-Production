package nape.biblememory.view_layer.fragments.presenters;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.view_layer.Activities.BaseCallback;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.data_store.DataStore;
import nape.biblememory.view_layer.fragments.interfaces.VersesFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.VersesPresenterInterface;

/**
 * Created by jbrannen on 9/3/17.
 */

public class VersesPresenter implements VersesPresenterInterface{

    private VersesFragmentInterface fragment;
    private Context context;
    private final List<ScriptureData> myVerses = new ArrayList<>();

    public VersesPresenter(VersesFragmentInterface fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public void fetchData() {
        BaseCallback<List<ScriptureData>> QuizVersesCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                if(response != null){
                    myVerses.addAll(response);
                }
                fragment.onReceivedRecyclerData(myVerses);
            }

            @Override
            public void onFailure(Exception e) {
                fragment.onReceivedRecyclerData(myVerses);
            }
        };
        DataStore.getInstance().getQuizVerses(QuizVersesCallback, context.getApplicationContext().getApplicationContext());
    }

    @Override
    public void onDatasetChanged(List<ScriptureData> response) {
        if(response.size() == myVerses.size()){
            for(int i = 0; i < response.size(); i++){
                DataStore.getInstance().updateQuizVerse(myVerses.get(i), response.get(i), context.getApplicationContext());
            }
        }else {
            ScriptureData verseToRemove = getDeletedVerse(response);
            DataStore.getInstance().deleteQuizVerse(verseToRemove, context);
        }
    }

    private ScriptureData getDeletedVerse(List<ScriptureData> response){
        for(int i = 0; i < myVerses.size(); i++){
            boolean contains = false;
            for(int j = 0; j < response.size(); j++){
                if(myVerses.get(i).getVerseLocation().equalsIgnoreCase(response.get(j).getVerseLocation())){
                    contains = true;
                }
            }
            if(!contains){
                return myVerses.get(i);
            }
        }
        return new ScriptureData();
    }
}
