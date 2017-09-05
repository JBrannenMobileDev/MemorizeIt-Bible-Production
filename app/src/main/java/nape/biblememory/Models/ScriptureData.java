package nape.biblememory.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class ScriptureData implements Parcelable{
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
    private String bookName;
    private String chapter;
    private long numOfVersesInChapter;
    private String verseNumber;
    private String versionCode;

    protected ScriptureData(Parcel in) {
        verse = in.readString();
        verseLocation = in.readString();
        startDate = in.readString();
        remeberedDate = in.readString();
        memorizedDate = in.readString();
        lastSeenDate = in.readString();
        primary_key_id = in.readInt();
        correctCount = in.readInt();
        viewedCount = in.readInt();
        memoryStage = in.readInt();
        memorySubStage = in.readInt();
        bookName = in.readString();
        chapter = in.readString();
        numOfVersesInChapter = in.readLong();
        verseNumber = in.readString();
        versionCode = in.readString();
    }

    public ScriptureData(){
    }

    public static final Creator<ScriptureData> CREATOR = new Creator<ScriptureData>() {
        @Override
        public ScriptureData createFromParcel(Parcel in) {
            return new ScriptureData(in);
        }

        @Override
        public ScriptureData[] newArray(int size) {
            return new ScriptureData[size];
        }
    };

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public long getNumOfVersesInChapter() {
        return numOfVersesInChapter;
    }

    public void setNumOfVersesInChapter(long numOfVersesInChapter) {
        this.numOfVersesInChapter = numOfVersesInChapter;
    }

    public ScriptureData(String verse, String verseLocation) {
        setVerse(verse);
        setVerseLocation(verseLocation);
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
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

    public void setVerseNumber(String verseNumber) {
        this.verseNumber = verseNumber;
    }

    public String getVerseNumber() {
        return verseNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(verse);
        dest.writeString(verseLocation);
        dest.writeString(startDate);
        dest.writeString(remeberedDate);
        dest.writeString(memorizedDate);
        dest.writeString(lastSeenDate);
        dest.writeInt(primary_key_id);
        dest.writeInt(correctCount);
        dest.writeInt(viewedCount);
        dest.writeInt(memoryStage);
        dest.writeInt(memorySubStage);
        dest.writeString(bookName);
        dest.writeString(chapter);
        dest.writeLong(numOfVersesInChapter);
        dest.writeString(verseNumber);
        dest.writeString(versionCode);
    }
}
