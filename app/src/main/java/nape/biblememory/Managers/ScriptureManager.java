package nape.biblememory.Managers;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Sqlite.MemoryListContract;


public class ScriptureManager {

    private VerseOperations vOperations;
    private int currentSize;
    private int learningSize;
    private int rememberedSize;
    private int memorizedSize;

    public ScriptureManager(Context context){
        vOperations = new VerseOperations(context);
        List<ScriptureData> scriptureListCurrent = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
        List<ScriptureData> scriptureListLearning = vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME);
        List<ScriptureData> scriptureListRemembered = vOperations.getVerseSet(MemoryListContract.RememberedSetEntry.TABLE_NAME);
        List<ScriptureData> scriptureListMemorized = vOperations.getVerseSet(MemoryListContract.MemorizedSetEntry.TABLE_NAME);

        currentSize = scriptureListCurrent.size();
        learningSize = scriptureListLearning.size();
        rememberedSize = scriptureListRemembered.size();
        memorizedSize = scriptureListMemorized.size();


        ScriptureData verseData;

        if(currentSize > 2) {
            /**
             * If the learningSet size is less than 3 then add enough verses to the learning set to
             * make the size three again.  This ensures that the user always has three verses in the
             * learning rotation.
             */
            if(currentSize > 1 && learningSize < 3){
                int loopSize = 3 - learningSize;
                while(loopSize > 0){
                    if(currentSize > 0) {
                        scriptureListCurrent = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
                        verseData = initializeNewVerse(scriptureListCurrent.get(0));
                        if(!verseData.getVerseLocation().equalsIgnoreCase("")) {
                            vOperations.addVerse(verseData, MemoryListContract.LearningSetEntry.TABLE_NAME);
                            vOperations.removeVerse(verseData.getVerseLocation(), MemoryListContract.CurrentSetEntry.TABLE_NAME);
                        }
                        loopSize--;
                    }else{
                        break;
                    }
                }
            }
        }else if(currentSize == 1){
            scriptureListCurrent = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
            verseData = initializeNewVerse(scriptureListCurrent.get(0));
            if(!verseData.getVerseLocation().equalsIgnoreCase("")) {
                vOperations.addVerse(verseData, MemoryListContract.LearningSetEntry.TABLE_NAME);
                vOperations.removeVerse(verseData.getVerseLocation(), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            }
        }else if(currentSize == 2){
            int loopSize = 2 - learningSize;
            while(loopSize > 0){
                if(currentSize > 0) {
                    scriptureListCurrent = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
                    verseData = initializeNewVerse(scriptureListCurrent.get(0));
                    if(!verseData.getVerseLocation().equalsIgnoreCase("")) {
                        vOperations.addVerse(verseData, MemoryListContract.LearningSetEntry.TABLE_NAME);
                        vOperations.removeVerse(verseData.getVerseLocation(), MemoryListContract.CurrentSetEntry.TABLE_NAME);
                    }
                    loopSize--;
                }else{
                    break;
                }
            }
        }



    }

    public ScriptureData getLearningScripture(){
        int caseNumber;
        List<ScriptureData> scriptureListLearning = vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME);

        ScriptureData temp = getCurrentSetScripture();
        if(scriptureListLearning.size() < 3 && temp != null){
            vOperations.addVerse(temp, MemoryListContract.LearningSetEntry.TABLE_NAME);
            vOperations.removeVerse(temp.getVerseLocation(), MemoryListContract.CurrentSetEntry.TABLE_NAME);
        }
        if(scriptureListLearning != null && scriptureListLearning.size() > 2) {
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
        }
        return new ScriptureData();
    }

    public ScriptureData getCurrentSetScripture(){
        int caseNumber;
        List<ScriptureData> scriptureListLearning = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
        if(scriptureListLearning != null && scriptureListLearning.size() > 2) {
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
        }
        return new ScriptureData();
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
