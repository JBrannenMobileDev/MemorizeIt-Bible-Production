package nape.biblememory.view_layer.fragments.interfaces;


import nape.biblememory.models.MemorizedVerse;

/**
 * Created by jbrannen on 9/12/17.
 */

public interface MemorizedVerseReviewFragmentInterface {
    void onCorrect(String word);
    void onIncorrect(String word, int numOfRedLights);
    void hideKeyboard();
    void updateCorrectCount(int correctCount);
    void onDataReceived(String wordCount);
    void onReviewComplete(int correctCount, int wordCount);
    void onUpdateReMemorizedVerse(MemorizedVerse verse);
    void updateMemorizedVerse(MemorizedVerse verse, String date);
    void updateMemorizedVerseToForgotten(MemorizedVerse verse);
}
