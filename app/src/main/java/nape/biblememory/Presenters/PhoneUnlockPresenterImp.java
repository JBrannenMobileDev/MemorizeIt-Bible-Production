package nape.biblememory.Presenters;

import android.content.Context;
import android.os.Vibrator;
import android.text.SpannableStringBuilder;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nape.biblememory.Fragments.Dialogs.VerseMemorizedAlertDialog;
import nape.biblememory.Managers.ModifyVerseText;
import nape.biblememory.Managers.ScriptureManager;
import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Managers.VerseStageManager;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Sqlite.MemoryListContract;
import nape.biblememory.UseCases.UsecaseCallback;
import nape.biblememory.UserPreferences;
import nape.biblememory.Fragments.PhoneUnlockView;
import nape.biblememory.R;

public class PhoneUnlockPresenterImp implements PhoneUnlockPresenter, UsecaseCallback {

    private PhoneUnlockView view;
    private Context context;
    private UserPreferences mPrefs;
    private ModifyVerseText stringModifier;
    private String TAG = PhoneUnlockPresenterImp.class.getSimpleName();

    private ScriptureData scripture;
    private boolean moreVerses;
    private boolean initialStage;
    private boolean hintClicked;
    private String modifiedVerseText;
    private String modifiedVerseLocationText;

    private static final String MORE_VERSES = "More Verses";
    private static final String EMPTY_STRING = "";
    private static final String CLOSE = "Close";

    private ScriptureManager scriptureManager;
    private VerseStageManager stageManager;
    private VerseOperations vOperations;
    private boolean memorizedAndLearningListIsEmpty;

    private SimpleDateFormat dateFormat;
    private Calendar c;

    public PhoneUnlockPresenterImp(PhoneUnlockView view, Context context){
        scriptureManager = new ScriptureManager(context);
        stageManager = new VerseStageManager();
        mPrefs = new UserPreferences();
        vOperations = new VerseOperations(context);
        stringModifier = new ModifyVerseText();
        this.view = view;
        this.context = context;
        this.moreVerses = false;
        this.initialStage = false;
        this.hintClicked = false;
        setMoreVersesSwitch(false);
        dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        c = Calendar.getInstance();
    }

    @Override
    public void onRequestData() {
        ScriptureData tempData = scriptureManager.getLearningScripture();
        onSuccess(tempData);

        if(mPrefs.isFirstTimeUnlock(context)) {
            mPrefs.setFirstTimeUnlock(false, context);
        }
    }

    @Override
    public void onMoreSwitchStateChanged(boolean isChecked) {
        if(isChecked) {
            view.setMoreVersesLayoutColor(R.color.colorProgressBg);
            moreVerses = true;
        }else{
            view.setMoreVersesLayoutColor(R.color.colorAccent);
            moreVerses = false;
        }
    }

    @Override
    public void onHintClicked() {
        hintClicked = true;
        if(scripture.getMemoryStage() == 5 && scripture.getMemorySubStage() == 0){
            setHintLocationText();
        }else{
            setHintVerseText();
        }
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(110);
        view.setHintButtonVisibility(View.GONE);
    }


    @Override
    public void onSuccess(ScriptureData scripture) {
        this.scripture = scripture;
        hintClicked = false;
        if(scripture.getMemoryStage() == 0 && scripture.getMemorySubStage() == 0){
            initialStage = true;
            view.setTitlebarText(R.string.read_this_verse_once);
        }else {
            view.setTitlebarText(R.string.what_is_the_verse);
        }
        initializeUnlockView();
    }

    @Override
    public void onFailure(Exception error) {

    }

    @Override
    public void onCloseClicked() {
        view.onFinishActivity();
    }

    @Override
    public void onCheckAnswerClicked() {
        if(initialStage){
            initialStage = false;
            scripture.setMemoryStage(1);
            scriptureManager.updateScriptureStatus(scripture);
            if(moreVerses){
                resetVerseView();
            }else {
                view.onFinishActivity();
            }
        }else {
            if(scripture.getMemoryStage() < 7) {
                setCheckAnswerVerseText();
                setCheckAnswerLocationText();
            }else{
                setCheckAnswerVerseText();
            }
            setVerificationView();
        }
    }

    @Override
    public void onYesClicked() {
        setYesClickedVerseText();
        int stage = scripture.getMemoryStage();
        int subStage = scripture.getMemorySubStage();

        if(!hintClicked) {
            if (stage == 0) {
                scripture.setMemoryStage(stage + 1);
            } else {
                scripture.setMemorySubStage(subStage + 1);
            }
            if (subStage > 2 && stage > 0 && stage < 6) {
                scripture.setMemorySubStage(0);
                scripture.setMemoryStage(stage + 1);
                scriptureManager.updateScriptureStatus(scripture);
            } else if(subStage > 0 && stage == 6){
                scripture.setMemorySubStage(0);
                scripture.setMemoryStage(stage + 1);
                scriptureManager.updateScriptureStatus(scripture);
            } else if(stage == 7){
                moveVerseToRememberedList();
                addCurrentVerseToLearningList();
                if(vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME).size() < 1) {
                    memorizedAndLearningListIsEmpty = true;
                }
                view.showMemorizedAlert(memorizedAndLearningListIsEmpty);
            } else {
                scripture.setMemorySubStage(subStage + 1);
                scriptureManager.updateScriptureStatus(scripture);
            }
        }

        if(moreVerses){
            if(vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME).size() > 0) {
                resetVerseView();
            }else{
                if(!memorizedAndLearningListIsEmpty)
                    view.onFinishActivity();
            }
        }else{
            if(!memorizedAndLearningListIsEmpty)
                view.onFinishActivity();
        }
    }

    private void addCurrentVerseToLearningList() {
        scriptureManager.updateLearningList();
    }

    private void moveVerseToRememberedList() {
        String formattedDate = dateFormat.format(c.getTime());
        scripture.setRemeberedDate(formattedDate);
        ScriptureData newScripture = scriptureManager.getCurrentSetScripture();
        scripture.setMemorySubStage(0);
        scripture.setMemoryStage(0);
        scriptureManager.addVerse(scripture, MemoryListContract.RememberedSetEntry.TABLE_NAME);
        scriptureManager.removeVerse(scripture.getVerseLocation(), MemoryListContract.LearningSetEntry.TABLE_NAME);
        if(newScripture != null && newScripture.getVerseLocation() != null && newScripture.getVerse() != null){
            scriptureManager.addVerse(newScripture, MemoryListContract.LearningSetEntry.TABLE_NAME);
            scriptureManager.removeVerse(newScripture.getVerseLocation(), MemoryListContract.CurrentSetEntry.TABLE_NAME);
        }
    }


    @Override
    public void onNoClicked() {
        setNoClickedVerseText();
        int stage = scripture.getMemoryStage();
        int subStage = scripture.getMemorySubStage();
        if(subStage == 0){
            if(stage == 1){
                scripture.setMemoryStage(0);
                scripture.setMemorySubStage(0);
            }else if(stage > 1) {
                scripture.setMemorySubStage(2);
                scripture.setMemoryStage(stage - 1);
            }
        }else{
            scripture.setMemorySubStage(subStage - 1);
        }
        scriptureManager.updateScriptureStatus(scripture);

        if(moreVerses){
            resetVerseView();
        }else{
            view.onFinishActivity();
        }
    }

    private void setModifiedVerseText(){
        modifiedVerseText = stageManager.createModifiedVerse(scripture);
        if(modifiedVerseText.length() > 190){
            view.setVerseText(modifiedVerseText + "\n" + "\n" + "\n" + "\n" + "\n" + "\n");
        }else if(modifiedVerseText.length() > 175){
            view.setVerseText(modifiedVerseText + "\n" + "\n" + "\n" + "\n");
        }else if(modifiedVerseText.length() > 150){
            view.setVerseText(modifiedVerseText + "\n" + "\n" + "\n");
        }else{
            view.setVerseText(modifiedVerseText);
        }
    }

    private void setModifiedVerseLocationText(){
        modifiedVerseLocationText = stageManager.createModifiedVerseLocation(scripture);
        view.setVerseLocationText(modifiedVerseLocationText);
    }

    private void setCheckAnswerVerseText(){
        SpannableStringBuilder checkAnswerVerse = stringModifier.createCheckAnswerText(modifiedVerseText, scripture.getVerse(), scripture);
        view.setSpannableVerseText(checkAnswerVerse);
    }

    private void setCheckAnswerLocationText(){
        SpannableStringBuilder checkAnswerText = stringModifier.createCheckAnswerText(modifiedVerseLocationText, scripture.getVerseLocation(), scripture);
        view.setSpannableVerseLocationText(checkAnswerText);
    }

    private void setHintVerseText(){
        SpannableStringBuilder hintText = stringModifier.createHintText(modifiedVerseText, scripture);
        view.setSpannableVerseText(hintText);
    }

    private void setHintLocationText(){
        SpannableStringBuilder hintText = stringModifier.createHintText(modifiedVerseLocationText, scripture);
        view.setSpannableVerseLocationText(hintText);
    }

    private void setNoClickedVerseText(){
        String unmodifiedVerse = scripture.getVerse();
        String unmodifiedVerseLocation = scripture.getVerseLocation();

        SpannableStringBuilder checkAnswerVerse = stringModifier.createNoClickedAnswerVerse(unmodifiedVerse);
        SpannableStringBuilder checkAnswerVerseLocation = stringModifier.createNoClickedAnswerVerse(unmodifiedVerseLocation);

        view.setSpannableVerseLocationText(checkAnswerVerseLocation);
        view.setSpannableVerseText(checkAnswerVerse);
    }

    private void setYesClickedVerseText(){
        String unmodifiedVerse = scripture.getVerse();
        String unmodifiedVerseLocation = scripture.getVerseLocation();

        SpannableStringBuilder checkAnswerVerse = stringModifier.createYesClickedAnswerVerse(unmodifiedVerse);
        SpannableStringBuilder checkAnswerVerseLocation = stringModifier.createYesClickedAnswerVerse(unmodifiedVerseLocation);

        view.setSpannableVerseLocationText(checkAnswerVerseLocation);
        view.setSpannableVerseText(checkAnswerVerse);
    }



    private void setVerificationView() {
        view.setTitlebarText(R.string.did_you_get_it);
        view.setMoreSwitchVisibility(true);
        view.setCheckAnswerButtonVisibility(View.GONE);
        view.setHintButtonVisibility(View.GONE);
        view.setVerificationLayoutVisibility(View.VISIBLE);
    }

    private void resetVerseView() {
        view.setMoreSwitchVisibility(false);
        view.setVerseText(EMPTY_STRING);
        view.setVerseLocationText(EMPTY_STRING);
        view.setCheckAnswerButtonFont();
        view.setBasebarText(CLOSE);
        view.setVerificationLayoutVisibility(View.GONE);
        onRequestData();
    }


    private void initializeUnlockView(){
        String formattedDate = dateFormat.format(c.getTime());
        setModifiedVerseLocationText();
        setModifiedVerseText();
        scripture.setLastSeenDate(formattedDate);
        if(initialStage){
            view.setMoreSwitchVisibility(true);
            view.setDoneButtonFont();
            view.setCheckAnswerButtonText(R.string.done);
            view.setHintButtonVisibility(View.GONE);
        }else {
            view.setMoreSwitchVisibility(false);
            view.setCheckAnswerButtonText(R.string.check_answer);
            if (scripture.getMemoryStage() != 7) {
                view.setHintButtonVisibility(View.VISIBLE);
            }else{
                SpannableStringBuilder finalStageTip = stringModifier.createFinalStageTip("Final Stage!");
                view.setSpannableVerseText(finalStageTip);
            }
        }
        view.setCheckAnswerButtonVisibility(View.VISIBLE);
    }


    private void setMoreVersesSwitch(boolean state){
        this.moreVerses = state;
        view.setMoreVerseSwitchState(state);
    }
}
