package nape.biblememory.Activities.Views;

import android.text.SpannableStringBuilder;

import nape.biblememory.Activities.Models.ScriptureData;

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
    void setMoreSwitchTrackText(boolean state);
    void setSwitchTextInvisible();
    void setHintButtonVisibility(int value);
    void setVerseTextAlignmentCenter(boolean alighnment);
}
