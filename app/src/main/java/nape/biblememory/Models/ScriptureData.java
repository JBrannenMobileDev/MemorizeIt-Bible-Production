package nape.biblememory.Models;


public class ScriptureData {
    private String verse;
    private String verseLocation;
    private String startDate;
    private String remeberedDate;
    private String memorizedDate;
    private String lastSeenDate;
    private int primary_key_id;
    private int correctCount;
    private int viewedCount;
    private int memoryStage;
    private int memorySubStage;

    public ScriptureData(String verse, String verseLocation) {
        setVerse(verse);
        setVerseLocation(verseLocation);
    }

    public ScriptureData() {
    }


    public String getVerse() {
        return verse;
    }

    public String getVerseLocation() {
        return verseLocation;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public void setVerseLocation(String verseLocation) {
        this.verseLocation = verseLocation;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getViewedCount() {
        return viewedCount;
    }

    public void setViewedCount(int viewedCount) {
        this.viewedCount = viewedCount;
    }

    public int getMemoryStage() {
        return memoryStage;
    }

    public void setMemoryStage(int memoryStage) {
        this.memoryStage = memoryStage;
    }

    public int getMemorySubStage() {
        return memorySubStage;
    }

    public void setMemorySubStage(int memorySubStage) {
        this.memorySubStage = memorySubStage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getRemeberedDate() {
        return remeberedDate;
    }

    public void setRemeberedDate(String remeberedDate) {
        this.remeberedDate = remeberedDate;
    }

    public String getMemorizedDate() {
        return memorizedDate;
    }

    public void setMemorizedDate(String memorizedDate) {
        this.memorizedDate = memorizedDate;
    }

    public String getLastSeenDate() {
        return lastSeenDate;
    }

    public void setLastSeenDate(String lastSeenDate) {
        this.lastSeenDate = lastSeenDate;
    }

    public int getPrimary_key_id() {
        return primary_key_id;
    }

    public void setPrimary_key_id(int primary_key_id) {
        this.primary_key_id = primary_key_id;
    }
}