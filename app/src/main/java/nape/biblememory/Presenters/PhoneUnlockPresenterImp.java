package nape.biblememory.Presenters;

import android.content.Context;
import android.os.Vibrator;
import android.text.SpannableStringBuilder;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.Managers.ModifyVerseText;
import nape.biblememory.Managers.VerseOperations;
import nape.biblememory.Managers.VerseStageManager;
import nape.biblememory.Models.ScriptureData;
import nape.biblememory.data_store.DataStore;
import nape.biblememory.data_store.Sqlite.MemoryListContract;
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

    private VerseStageManager stageManager;
    private VerseOperations vOperations;
    private boolean memorizedAndLearningListIsEmpty;

    private SimpleDateFormat dateFormat;
    private Calendar c;

    private boolean reviewVerse;
    private boolean forgottenVerse;
    private boolean isReviewMode;
    private int reviewIndex;
    private List<ScriptureData> reviewVersesList;
    private List<ScriptureData> reviewForgottenVersesList;

    public PhoneUnlockPresenterImp(PhoneUnlockView view, Context context){
        stageManager = new VerseStageManager();
        mPrefs = new UserPreferences();
        vOperations = VerseOperations.getInstance(context);
        stringModifier = new ModifyVerseText();
        this.view = view;
        this.context = context;
        this.moreVerses = false;
        this.initialStage = false;
        this.hintClicked = false;
        reviewVerse = false;
        forgottenVerse = false;
        setMoreVersesSwitch(false);
        dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        c = Calendar.getInstance();
        isReviewMode = false;
        reviewIndex = 1;
    }

    @Override
    public void onRequestData() {
        int quizViewCount = mPrefs.getQuizViewCount(context);
        final BaseCallback<ScriptureData> quizVerseCallback = new BaseCallback<ScriptureData>() {
            @Override
            public void onResponse(ScriptureData response) {
                onSuccess(response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        if(quizViewCount >= 15){
            mPrefs.setQuizViewCount(0, context);
            BaseCallback<ScriptureData> forgottenVerseCallback = new BaseCallback<ScriptureData>() {
                @Override
                public void onResponse(ScriptureData response) {
                    BaseCallback<ScriptureData> memroizedVerseAtCallback = new BaseCallback<ScriptureData>() {
                        @Override
                        public void onResponse(ScriptureData response) {
                            if(response != null){
                                mPrefs.setQuizReviewIndex(mPrefs.getQuizReviewIndex(context)+1, context);
                                view.setReviewTitleVisibility(View.VISIBLE);
                                view.setReviewTitleText("Review memorized verse");
                                view.setReviewTitleColor(R.color.colorGreenText);
                                reviewVerse = true;
                                quizVerseCallback.onResponse(response);
                            }else{
                                DataStore.getInstance().getRandomQuizVerse(context, quizVerseCallback);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    };
                    if(response != null){
                        view.setReviewTitleVisibility(View.VISIBLE);
                        view.setReviewTitleText("Review forgotten verse");
                        view.setReviewTitleColor(R.color.colorNoText);
                        forgottenVerse = true;
                        quizVerseCallback.onResponse(response);
                    }else{
                        DataStore.getInstance().getLocalMeorizedVerseAt(mPrefs.getQuizReviewIndex(context), memroizedVerseAtCallback, context);
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            };
            DataStore.getInstance().getLocalForgottenVerse(forgottenVerseCallback, context);
        }else {
            mPrefs.setQuizViewCount(quizViewCount + 1, context);
            DataStore.getInstance().getRandomQuizVerse(context, quizVerseCallback);
        }
    }

    @Override
    public void onRequestReviewData() {
        isReviewMode = true;
        BaseCallback<List<ScriptureData>> reviewForgottenVersesCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                if(response != null && response.size() > 0) {
                    forgottenVerse = true;
                    view.setReviewTitleVisibility(View.VISIBLE);
                    view.setReviewTitleText("Review forgotten verse");
                    view.setReviewTitleColor(R.color.colorNoText);
                    reviewForgottenVersesList = response;
                    onSuccess(response.get(0));
                }else{
                    BaseCallback<List<ScriptureData>> reviewVersesListCallback = new BaseCallback<List<ScriptureData>>() {
                        @Override
                        public void onResponse(List<ScriptureData> response) {
                            if(response != null && response.size() > 0) {
                                reviewVerse = true;
                                view.setReviewTitleVisibility(View.VISIBLE);
                                view.setReviewTitleText("Review memorized verse");
                                view.setReviewTitleColor(R.color.colorGreenText);
                                reviewVersesList = response;
                                onSuccess(response.get(reviewIndex));
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    };
                    DataStore.getInstance().getMemorizedVerses(reviewVersesListCallback, context);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getForgottenVerses(reviewForgottenVersesCallback, context);
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
            DataStore.getInstance().updateQuizVerse(scripture, context);
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
            if(reviewVerse) {
                String formattedDate = dateFormat.format(c.getTime());
                scripture.setLastSeenDate(formattedDate);
            }else {
                if (stage == 0) {
                    scripture.setMemoryStage(stage + 1);
                } else {
                    scripture.setMemorySubStage(subStage + 1);
                }
                if (subStage > 2 && stage > 0 && stage < 6) {
                    scripture.setMemorySubStage(0);
                    scripture.setMemoryStage(stage + 1);
                    if(forgottenVerse){
                        DataStore.getInstance().updateForgottenVerse(scripture, context);
                    }else if(!isReviewMode){
                        DataStore.getInstance().updateQuizVerse(scripture, context);
                    }
                } else if (subStage > 0 && stage == 6) {
                    scripture.setMemorySubStage(0);
                    scripture.setMemoryStage(stage + 1);
                    if(forgottenVerse){
                        DataStore.getInstance().updateForgottenVerse(scripture, context);
                    }else if(!isReviewMode){
                        DataStore.getInstance().updateQuizVerse(scripture, context);
                    }
                } else if (stage == 7) {
                    if(forgottenVerse){
                        moveForgottenVerseToMemorizedList();
                    }else if(!isReviewMode){
                        moveVerseToRememberedList();
                        DataStore.getInstance().moveUpcomingVerseToQuiz(context);
                    }
                    if (vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME).size() < 1) {
                        memorizedAndLearningListIsEmpty = true;
                    }
                    view.showMemorizedAlert(memorizedAndLearningListIsEmpty);
                } else {
                    scripture.setMemorySubStage(subStage + 1);
                    DataStore.getInstance().updateQuizVerse(scripture, context);
                }
            }
        }

        if(moreVerses){
            if(!isReviewMode) {
                if (vOperations.getVerseSet(MemoryListContract.LearningSetEntry.TABLE_NAME).size() > 0) {
                    resetVerseView();
                } else {
                    if (!memorizedAndLearningListIsEmpty)
                        view.onFinishActivity();
                }
            }else{
                resetVerseView();
            }
        }else{
            if(!memorizedAndLearningListIsEmpty)
                view.onFinishActivity();
        }
    }

    private void moveForgottenVerseToMemorizedList() {
        reviewForgottenVersesList = null;
        forgottenVerse = false;
        String formattedDate = dateFormat.format(c.getTime());
        scripture.setMemorizedDate(formattedDate);
        DataStore.getInstance().saveMemorizedVerse(scripture, context);
        DataStore.getInstance().deleteForgottenVerse(scripture, context);
    }


    private void moveVerseToRememberedList() {
        String formattedDate = dateFormat.format(c.getTime());
        scripture.setRemeberedDate(formattedDate);
        DataStore.getInstance().saveMemorizedVerse(scripture, context);
        DataStore.getInstance().deleteQuizVerse(scripture, context);

        BaseCallback<List<ScriptureData>> upcomingCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                if(response.size() > 0) {
                    if (response != null && response.get(0).getVerseLocation() != null && response.get(0).getVerse() != null) {
                        DataStore.getInstance().saveQuizVerse(response.get(0), context);
                        DataStore.getInstance().deleteUpcomingVerse(response.get(0), context);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getLocalUpcomingVerses(upcomingCallback, context);
    }


    @Override
    public void onNoClicked() {
        setNoClickedVerseText();
        String formattedDate = dateFormat.format(c.getTime());
        int stage = scripture.getMemoryStage();
        int subStage = scripture.getMemorySubStage();
        if (subStage == 0) {
            if (stage == 1) {
                scripture.setMemoryStage(0);
                scripture.setMemorySubStage(0);
            } else if (stage > 1) {
                scripture.setMemorySubStage(2);
                scripture.setMemoryStage(stage - 1);
            }
        } else {
            scripture.setMemorySubStage(subStage - 1);
        }
        if(forgottenVerse){
            scripture.setLastSeenDate(formattedDate);
            DataStore.getInstance().updateForgottenVerse(scripture, context);
        }else if(reviewVerse){
            scripture.setLastSeenDate(formattedDate);
            DataStore.getInstance().saveForgottenVerse(scripture, context);
            DataStore.getInstance().deleteMemorizedVerse(scripture, context);
        }else {
            DataStore.getInstance().updateQuizVerse(scripture, context);
        }

        if (moreVerses) {
            resetVerseView();
        } else {
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
        forgottenVerse = false;
        reviewVerse = false;
        view.setReviewTitleVisibility(View.GONE);
        view.setMoreSwitchVisibility(false);
        view.setVerseText(EMPTY_STRING);
        view.setVerseLocationText(EMPTY_STRING);
        view.setCheckAnswerButtonFont();
        view.setBasebarText(CLOSE);
        view.setVerificationLayoutVisibility(View.GONE);
        if(isReviewMode){
            if(reviewForgottenVersesList != null && reviewForgottenVersesList.size() > 0){
                onRequestReviewData();
            }else {
                if(reviewVersesList == null){
                    onRequestReviewData();
                }else if(reviewIndex < reviewVersesList.size()) {
                    view.setReviewTitleVisibility(View.VISIBLE);
                    view.setReviewTitleText("Review memorized verse");
                    view.setReviewTitleColor(R.color.colorGreenText);
                    onSuccess(reviewVersesList.get(reviewIndex));
                    reviewIndex++;
                }else{
                    view.setReviewTitleVisibility(View.VISIBLE);
                    view.setReviewTitleText("Review memorized verse");
                    view.setReviewTitleColor(R.color.colorGreenText);
                    onSuccess(reviewVersesList.get(0));
                    reviewIndex = 1;
                }
            }
        }else {
            onRequestData();
        }
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
