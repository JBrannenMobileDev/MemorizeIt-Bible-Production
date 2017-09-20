package nape.biblememory.view_layer.fragments.interfaces;

/**
 * Created by jbrannen on 9/12/17.
 */

public interface MyVersesPracticeInterface {
    void fetchData(String verseLocation);
    void onNewCharInput(char c);
    void resetReview();
    void updateMyVerseCorrect(String date);
    void updateMyVerseWrong(String date);
    void onStop();
    void resetVerseText();
}
