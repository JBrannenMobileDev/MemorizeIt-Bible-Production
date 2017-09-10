package nape.biblememory.models;

/**
 * Created by Jonathan on 9/30/2016.
 */
public class BookGroup {
    private String bookName;
    private int numOfVersesMemorized;

    public BookGroup(String bookName, int numOfVersesMemorized) {
        this.bookName = bookName;
        this.numOfVersesMemorized = numOfVersesMemorized;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getNumOfVersesMemorized() {
        return numOfVersesMemorized;
    }

    public void setNumOfVersesMemorized(int numOfVersesMemorized) {
        this.numOfVersesMemorized = numOfVersesMemorized;
    }
}
