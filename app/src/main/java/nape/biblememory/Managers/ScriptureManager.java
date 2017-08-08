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
        vOperations = new VerseOperations(context);
    }



    public ScriptureData getCurrentSetScripture(){
        List<ScriptureData> scriptureListCurrentSet = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
        if(scriptureListCurrentSet != null && scriptureListCurrentSet.size() > 0) {
            return scriptureListCurrentSet.get(0);
        }
        return null;
    }

    private int calculateCaseNumber(int random) {
        if(random > 66){
            return 1;
        }else if(random > 33){
            return 2;
        }else {
            return 3;
        }
    }

    private int generateRandomNumber(int rangeSize){
        Random generate = new Random();
        return (generate.nextInt(rangeSize) + 1) - 1;
    }

    private ScriptureData initializeNewVerse(ScriptureData verseData){
        verseData.setStartDate(String.valueOf(Calendar.DATE));
        verseData.setMemoryStage(0);
        verseData.setMemorySubStage(0);
        verseData.setCorrectCount(0);
        verseData.setViewedCount(0);
        return verseData;
    }

    public void updateScriptureStatus(ScriptureData scripture) {
        vOperations.updateVerse(scripture);
    }

    public void addVerse(ScriptureData scripture, String tableName){
        vOperations.addVerse(scripture, tableName);
    }

    public void removeVerse(String verseLocation, String tableName){
        vOperations.removeVerse(verseLocation, tableName);
    }

    public void updateLearningList() {
        List<ScriptureData> scriptureListCurrent = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
        if(scriptureListCurrent.size() > 0) {
            vOperations.addVerse(scriptureListCurrent.get(0), MemoryListContract.LearningSetEntry.TABLE_NAME);
            vOperations.removeVerse(scriptureListCurrent.get(0).getVerseLocation(), MemoryListContract.LearningSetEntry.TABLE_NAME);
        }
    }

    public List<ScriptureData> getScriptureSet(String tableName){
        return vOperations.getVerseSet(tableName);
    }
}
