package nape.biblememory.Fragments;

import android.text.SpannableStringBuilder;

import nape.biblememory.Models.ScriptureData;

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
    void showFirstTimeDialog();
    void setCheckAnswerButtonText(int done);
    void setDoneButtonFont();
    void setCheckAnswerButtonFont();
    void setMoreSwitchVisibility(boolean visible);
    void setMoreVerseSwitchState(boolean state);
    void setHintButtonVisibility(int value, ScriptureData scripture);
    void showMemorizedAlert(boolean memorizedAndLearningListIsEmpty);
    void setMoreVersesLayoutColor(int color);
    void setReviewTitleVisibility(int visible);
    void setReviewTitleText(String s);
    void setReviewTitleColor(int colorNoText);
}
