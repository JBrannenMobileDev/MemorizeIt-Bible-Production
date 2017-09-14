package nape.biblememory.models;

/**
 * Created by jbrannen on 9/14/17.
 */

public class RemoveVerse {
    private int position;
    private ScriptureData verse;


    public RemoveVerse(int position, ScriptureData verse) {
        this.position = position;
        this.verse = verse;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ScriptureData getVerse() {
        return verse;
    }

    public void setVerse(ScriptureData verse) {
        this.verse = verse;
    }
}
