package nape.biblememory.Managers;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Sqlite.MemoryListContract;


public class ScriptureManager {

    private VerseOperations vOperations;

    public ScriptureManager(Context context){
        vOperations = new VerseOperations(context);
    }

    public ScriptureData getLearningScripture(){
        int caseNumber;
        List<ScriptureData> scriptureListLearning = vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME);

        ScriptureData temp = getCurrentSetScripture();
        if(scriptureListLearning.size() < 3 && temp != null){
            vOperations.addVerse(temp, MemoryListContract.LearningSetEntry.TABLE_NAME);
            vOperations.removeVerse(temp.getVerseLocation(), MemoryListContract.CurrentSetEntry.TABLE_NAME);
        }

        scriptureListLearning = vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME);
        if(scriptureListLearning != null && scriptureListLearning.size() > 2 && scriptureListLearning.get(0).getVerse() != null && scriptureListLearning.get(1).getVerse() != null && scriptureListLearning.get(2).getVerse() != null) {
            Random generate = new Random();
            final int random = generate.nextInt(100) + 1;
            caseNumber = calculateCaseNumber(random);
            switch (caseNumber) {
                case 1:
                    return scriptureListLearning.get(0);
                case 2:
                    return scriptureListLearning.get(1);
                case 3:
                    return scriptureListLearning.get(2);
                default:
                    return scriptureListLearning.get(0);
            }
        }else if(scriptureListLearning != null && scriptureListLearning.size() == 2 && scriptureListLearning.get(0).getVerse() != null && scriptureListLearning.get(1).getVerse() != null){
            Random generate = new Random();
            final int random = generate.nextInt(100) + 1;
            caseNumber = calculateCaseNumber(random);
            switch (caseNumber) {
                case 1:
                    return scriptureListLearning.get(0);
                case 2:
                case 3:
                    return scriptureListLearning.get(1);
                default:
                    return scriptureListLearning.get(0);
            }
        }else if(scriptureListLearning != null && scriptureListLearning.size() == 1 && scriptureListLearning.get(0).getVerse() != null){
            Random generate = new Random();
            final int random = generate.nextInt(100) + 1;
            caseNumber = calculateCaseNumber(random);
            switch (caseNumber) {
                case 1:
                case 2:
                case 3:
                    return scriptureListLearning.get(0);
                default:
                    return scriptureListLearning.get(0);
            }
        }
        return new ScriptureData();
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

    public void updateLerningList() {
        List<ScriptureData> scriptureListCurrent = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
        int size = scriptureListCurrent.size();
        int randNum = generateRandomNumber(size);
        vOperations.addVerse(scriptureListCurrent.get(randNum), MemoryListContract.LearningSetEntry.TABLE_NAME);
        vOperations.removeVerse(scriptureListCurrent.get(randNum).getVerseLocation(), MemoryListContract.LearningSetEntry.TABLE_NAME);
    }

    public List<ScriptureData> getScriptureSet(String tableName){
        return vOperations.getVerseSet(tableName);
    }
}
