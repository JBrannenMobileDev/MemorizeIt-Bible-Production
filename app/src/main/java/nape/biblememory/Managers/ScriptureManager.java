package nape.biblememory.Managers;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import nape.biblememory.Models.ScriptureData;
import nape.biblememory.data_store.Sqlite.MemoryListContract;


public class ScriptureManager {

    private VerseOperations vOperations;

    public ScriptureManager(Context context){
        vOperations = VerseOperations.getInstance(context);
    }

    public void updateScriptureStatus(ScriptureData scripture) {
        vOperations.updateVerse(scripture);
    }

    public void updateScriptureStatus(ScriptureData locationToUpdate, ScriptureData valueToSave) {
        vOperations.updateVerse(locationToUpdate, valueToSave);
    }

    public void updateForgottenScriptureStatus(ScriptureData scripture) {
        vOperations.updateForgottenVerse(scripture);
    }

    public void addVerse(ScriptureData scripture, String tableName){
        vOperations.addVerse(scripture, tableName);
    }

    public List<ScriptureData> getScriptureSet(String tableName){
        return vOperations.getVerseSet(tableName);
    }
}
