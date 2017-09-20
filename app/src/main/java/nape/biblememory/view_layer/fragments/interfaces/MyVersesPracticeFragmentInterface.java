package nape.biblememory.view_layer.fragments.interfaces;


import android.text.SpannableStringBuilder;

import nape.biblememory.models.MyVerse;
import nape.biblememory.models.ScriptureData;

/**
 * Created by jbrannen on 9/12/17.
 */

public interface MyVersesPracticeFragmentInterface {
    void hideKeyboard();
    void onReviewComplete(int correctCount, int wordCount);
    void updateMyVerseVerse(MyVerse verse, String date);
    void setVerseText(SpannableStringBuilder currentVerseText);
    void updateCorrectCount(int correctCount);
    void onDataReceived(String wordCount);
    void turnLightRed(int i);
    void turnLightGreen();
    void setVerseText(String verse);
    void moveVerseToMemorized(ScriptureData scripture);
    void setVerseTextColor();
    void setVerseLocationText(String verseLocation);
    void setVerseLocationText(SpannableStringBuilder verseLocation);
    void resetTextColorForStage0();
    void setVerseLocationTextColor();
}
