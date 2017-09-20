package nape.biblememory.utils;

import nape.biblememory.models.MyVerse;

/**
 * Created by jbrannen on 9/16/17.
 */

public class MyVerseCopyer {
    public static MyVerse getCopy(MyVerse verseToCopy) {
        MyVerse myVerse = new MyVerse();
        myVerse.setVerse(verseToCopy.getVerse());
        myVerse.setVersionCode(verseToCopy.getVersionCode());
        myVerse.setVerseLocation(verseToCopy.getVerseLocation());
        myVerse.setMemoryStage(verseToCopy.getMemoryStage());
        myVerse.setBookName(verseToCopy.getBookName());
        myVerse.setChapter(verseToCopy.getChapter());
        myVerse.setCorrectCount(verseToCopy.getCorrectCount());
        myVerse.setLastSeenDate(verseToCopy.getLastSeenDate());
        myVerse.setMemorizedDate(verseToCopy.getMemorizedDate());
        myVerse.setMemorySubStage(verseToCopy.getMemorySubStage());
        myVerse.setNumOfVersesInChapter(verseToCopy.getNumOfVersesInChapter());
        myVerse.setPrimary_key_id(verseToCopy.getPrimary_key_id());
        myVerse.setRemeberedDate(verseToCopy.getRemeberedDate());
        myVerse.setStartDate(verseToCopy.getStartDate());
        myVerse.setViewedCount(verseToCopy.getViewedCount());
        myVerse.setListPosition(verseToCopy.getListPosition());
        myVerse.setGoldStar(verseToCopy.getGoldStar());
        return myVerse;
    }
}
