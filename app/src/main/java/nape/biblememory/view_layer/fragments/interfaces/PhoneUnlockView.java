package nape.biblememory.view_layer.fragments.interfaces;

import android.text.SpannableStringBuilder;

import nape.biblememory.models.MyVerse;

/**
 * Created by Jonathan on 5/1/2016.
 */
public interface PhoneUnlockView{
    void setTitlebarText(int text);
    void setBasebarText(String text);
    void setVerificationLayoutVisibility(int visibility);
    void setCheckAnswerButtonVisibility(int visibility);
    void setVerseText(String verse);
    void setSpannableVerseText(SpannableStringBuilder verse);
    void setSpannableVerseLocationText(SpannableStringBuilder location);
    void setVerseLocationText(String verseLocation);
    void onFinishActivity();
    void setCheckAnswerButtonText(int done);
    void setDoneButtonFont();
    void setCheckAnswerButtonFont();
    void setHintButtonVisibility(int value, MyVerse scripture);
    void showMemorizedAlert(boolean memorizedAndLearningListIsEmpty);
    void setReviewTitleVisibility(int visible);
    void setReviewTitleText(String s);
    void setReviewTitleColor(int colorNoText);
    void setTitlebarTextColor(int color);
    void showCloseSelectedTooManyTimesDialog();
}
