package nape.biblememory.models;

import android.support.annotation.NonNull;

/**
 * Created by jbrannen on 9/19/17.
 */

public class RemovedWord implements Comparable<RemovedWord>{
    private String word;
    private int index;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(@NonNull RemovedWord wordToCompare) {
        return this.index - wordToCompare.getIndex();
    }
}
