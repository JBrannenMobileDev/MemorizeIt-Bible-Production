package nape.biblememory.Activities.Managers;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import nape.biblememory.Activities.Models.ScriptureData;
import nape.biblememory.Activities.Sqlite.MemoryListContract;


public class ScriptureManager {
    private String verse1 = "A man's heart plans his way, but the Lord directs his steps.";
    private String verse2 = "But the fruit of the Spirit is love, joy, peace, longsuffering, " +
                            "kindness, goodness, faithfulness, gentleness, self-control. Against such there " +
                            "is no law.";
    private String verse3 = "But God demonstrates His own love towards us, in that while we were " +
                            "still sinners, Christ died for us.";
    private String verse4 = "for all have sinned and fallen short of the glory of God,";
    private String verse5 = "For the wages of sin is death, but the gift of God is eternal life in " +
                            "Christ Jesus our Lord";
    private String verse6 = "that if you confess with your mouth the Lord Jesus and believe in your" +
                            " heart that God has raised Him from the dead, you will be saved.";
    private String verse7 = "For with the heart one believes unto righteousness, and with the mouth" +
                            " confession is made unto salvation.";
    private String verse8 = "Behold, I stand at the door and knock. If anyone hears My voice and opens" +
                            " the door, I will come in to him and dine with him, and he with Me.";
    private String verse9 = "For by grace you have been saved through faith, and that not of yourselves;" +
                            " it is the gift of God, 9 not of works, lest anyone should boast.";
    private String verse10 = "Jesus Christ is the same yesterday, today, and forever.";
    private String verse11 = "while we do not look at the things which are seen, but at the things which" +
                             " are not seen. For the things which are seen are temporary, but the things which are not seen are eternal.";
    private String verse12 = "Therefore submit to God. Resist the devil and he will flee from you.";
    private String verse13 = "in everything give thanks; for this is the will of God in Christ Jesus for you.";
    private String verse14 = "Your word I have hidden in my heart, that I might not sin against You!";
    private String verse1Location = "Proverbs 16:9";
    private String verse2Location = "Galatians 5:22-23";
    private String verse3Location = "Romans 5:8";
    private String verse4Location = "Romans 3:23";
    private String verse5Location = "Romans 6:23";
    private String verse6Location = "Romans 10:9";
    private String verse7Location = "Romans 10:10";
    private String verse8Location = "Revelation 3:20";
    private String verse9Location = "Ephesians 2:8-9";
    private String verse10Location = "Hebrews 13:8";
    private String verse11Location = "2Corinthians 4:18";
    private String verse12Location = "James 4:7";
    private String verse13Location = "1Thessalonians 5:18";
    private String verse14Location = "Psalm 119:11";

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

        if(currentSize < 1) {
            vOperations.addVerse(new ScriptureData(verse1, verse1Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse2, verse2Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse3, verse3Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse4, verse4Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse5, verse5Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse6, verse6Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse7, verse7Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse8, verse8Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse9, verse9Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse10, verse10Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse11, verse11Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse12, verse12Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse13, verse13Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            vOperations.addVerse(new ScriptureData(verse14, verse14Location), MemoryListContract.CurrentSetEntry.TABLE_NAME);
            scriptureListCurrent = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
            currentSize = scriptureListCurrent.size();
        }


        /**
         * If the learningSet size is less than 3 then add enough verses to the learning set to
         * make the size three again.  This ensures that the user always has three verses in the
         * learning rotation.
         */
        if(currentSize > 1 && learningSize < 3){
            int loopSize = 3 - learningSize;
            ScriptureData verseData;
            while(loopSize > 0){
                if(currentSize > 0) {
                    scriptureListCurrent = vOperations.getVerseSet(MemoryListContract.CurrentSetEntry.TABLE_NAME);
                    verseData = initializeNewVerse(scriptureListCurrent.get(0));
                    vOperations.addVerse(verseData, MemoryListContract.LearningSetEntry.TABLE_NAME);
                    vOperations.removeVerse(verseData.getVerseLocation(), MemoryListContract.CurrentSetEntry.TABLE_NAME);
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
