package nape.biblememory.view_layer.fragments.presenters;

import android.content.Context;
import android.os.Vibrator;
import android.text.SpannableStringBuilder;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.managers.ModifyVerseText;
import nape.biblememory.managers.VerseStageManager;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.use_cases.UsecaseCallback;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.fragments.interfaces.PhoneUnlockPresenter;
import nape.biblememory.view_layer.fragments.interfaces.PhoneUnlockView;
import nape.biblememory.R;

public class PhoneUnlockPresenterImp implements PhoneUnlockPresenter, UsecaseCallback {

    private PhoneUnlockView view;
    private Context context;
    private UserPreferences mPrefs;
    private ModifyVerseText stringModifier;
    private String TAG = PhoneUnlockPresenterImp.class.getSimpleName();

    private MyVerse myVerse;
    private MemorizedVerse myMemorizedVerse;
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
    private boolean memorizedAndLearningListIsEmpty;

    private SimpleDateFormat dateFormat;
    private Calendar c;

    private boolean reviewVerse;
    private boolean forgottenVerse;
    private boolean isReviewMode;
    private int reviewIndex;
    private List<MemorizedVerse> reviewVersesList;
    private MemorizedVerse reviewForgottenVerses;
    private int quizViewCount;
    private BaseCallback<MyVerse> quizVerseCallback;

    private RealmResults<MyVerse> myVerses;
    private Realm realm;

    public PhoneUnlockPresenterImp(PhoneUnlockView view, Context context, MyVerse myVerse, boolean moreVerses){
        this.moreVerses = moreVerses;
        stageManager = new VerseStageManager();
        mPrefs = new UserPreferences();
        stringModifier = new ModifyVerseText();
        this.view = view;
        this.context = context;
        this.initialStage = false;
        this.hintClicked = false;
        reviewVerse = false;
        forgottenVerse = false;
        dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        c = Calendar.getInstance();
        isReviewMode = false;
        reviewIndex = mPrefs.getQuizReviewIndex(context);
        realm = Realm.getDefaultInstance();
        myVerses = realm.where(MyVerse.class).findAll();
    }

    @Override
    public void onRequestData() {
        quizViewCount = mPrefs.getQuizViewCount(context);
        quizVerseCallback = new BaseCallback<MyVerse>() {
            @Override
            public void onResponse(MyVerse response) {
                onSuccess(response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        if(quizViewCount >= 10){
            mPrefs.setQuizViewCount(0, context);
            onRequestReviewData();
        }else {
            mPrefs.setQuizViewCount(quizViewCount + 1, context);
            DataStore.getInstance().getRandomQuizVerse(quizVerseCallback);
        }
    }

    @Override
    public void onRequestReviewData() {
        isReviewMode = true;

        Realm realm = Realm.getDefaultInstance();
        List<MemorizedVerse> reviewVerses = realm.where(MemorizedVerse.class).findAll();
        if(reviewVerses != null && reviewVerses.size() > 0){
            MemorizedVerse forgottenVerseReview = null;
            for(MemorizedVerse verse : reviewVerses){
                if(verse.isForgotten()){
                    forgottenVerseReview = verse;
                    break;
                }
            }
            if(forgottenVerseReview != null){
                forgottenVerse = true;
                view.setReviewTitleVisibility(View.VISIBLE);
                view.setReviewTitleText("Review forgotten verse");
                view.setReviewTitleColor(R.color.colorNoText);
                reviewForgottenVerses = forgottenVerseReview;
                onSuccess(forgottenVerseReview);
            }else{
                reviewVerse = true;
                view.setReviewTitleVisibility(View.VISIBLE);
                view.setReviewTitleText("Review memorized verse");
                view.setReviewTitleColor(R.color.colorGreenText);
                reviewVersesList = reviewVerses;
                reviewIndex++;
                if(reviewIndex == reviewVerses.size()){
                    reviewIndex = 0;
                }
                mPrefs.setQuizReviewIndex(reviewIndex, context);
                onSuccess(reviewVerses.get(reviewIndex));
            }
        }else{
            isReviewMode = false;
            mPrefs.setQuizViewCount(quizViewCount + 1, context);
            DataStore.getInstance().getRandomQuizVerse(quizVerseCallback);
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
        view.setHintButtonVisibility(View.GONE, scripture.toMyVerse());
    }

    @Override
    public void onSuccess(MyVerse myVerse) {
        this.myVerse = myVerse;
        this.scripture = myVerse.toScriptureData();
        hintClicked = false;
        if(scripture.getMemoryStage() == 0 && scripture.getMemorySubStage() == 0){
            initialStage = true;
            view.setTitlebarText(R.string.read_this_verse_once);
        }else {
            view.setTitlebarText(R.string.what_is_the_verse);
        }
        initializeUnlockView();
    }

    public void onSuccess(MemorizedVerse myVerse) {
        this.myMemorizedVerse = myVerse;
        this.scripture = myVerse.toScriptureData();
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
            DataStore.getInstance().updateQuizVerse(scripture.toMyVerse(), context);
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
        boolean verseMemorized = false;

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
                        MemorizedVerse temp = scripture.toMemorizedVerseData();
                        temp.setForgotten(myMemorizedVerse.isForgotten());
                        String formattedDate = dateFormat.format(c.getTime());
                        temp.setLastSeenDate(formattedDate);
                        DataStore.getInstance().updateMemorizedVerse(temp, context);
                    }else if(!isReviewMode){
                        DataStore.getInstance().updateQuizVerse(scripture.toMyVerse(), context);
                    }
                } else if (subStage >= 0 && stage == 6) {
                    scripture.setMemorySubStage(0);
                    scripture.setMemoryStage(stage + 1);
                    if(forgottenVerse){
                        MemorizedVerse temp = scripture.toMemorizedVerseData();
                        temp.setForgotten(myMemorizedVerse.isForgotten());
                        String formattedDate = dateFormat.format(c.getTime());
                        temp.setLastSeenDate(formattedDate);
                        DataStore.getInstance().updateMemorizedVerse(temp, context);
                    }else if(!isReviewMode){
                        DataStore.getInstance().updateQuizVerse(scripture.toMyVerse(), context);
                    }
                } else if (stage == 7) {
                    if(forgottenVerse){
                        moveForgottenVerseToMemorizedList();
                    }else if(!isReviewMode){
                        moveVerseToRememberedList();
                    }
                    if (myVerses.size() < 1) {
                        memorizedAndLearningListIsEmpty = true;
                    }
                    view.showMemorizedAlert(myVerses.size() < 1 && true);
                } else {
                    scripture.setMemorySubStage(subStage + 1);
                    if(forgottenVerse || reviewVerse){
                        MemorizedVerse temp = scripture.toMemorizedVerseData();
                        temp.setForgotten(myMemorizedVerse.isForgotten());
                        String formattedDate = dateFormat.format(c.getTime());
                        temp.setLastSeenDate(formattedDate);
                        DataStore.getInstance().updateMemorizedVerse(temp, context);
                    }else{
                        DataStore.getInstance().updateQuizVerse(scripture.toMyVerse(), context);
                    }
                }
            }
        }

        if(moreVerses){
            if(!isReviewMode) {
                if (myVerses.size() > 0) {
                    resetVerseView();
                }
            }else{
                if (myVerses.size() > 0) {
                    resetVerseView();
                } else {
                    view.onFinishActivity();
                }
            }
        }else{
            if(!isReviewMode){
                if(stage != 7)
                    view.onFinishActivity();
            }else{
                view.onFinishActivity();
            }
        }
    }

    private void moveForgottenVerseToMemorizedList() {
        reviewForgottenVerses = null;
        forgottenVerse = false;
        String formattedDate = dateFormat.format(c.getTime());
        scripture.setMemorizedDate(formattedDate);
        MemorizedVerse temp = scripture.toMemorizedVerseData();
        temp.setForgotten(false);
        DataStore.getInstance().updateMemorizedVerse(temp, context);
        if(myVerses.size() < 1) {
            view.onFinishActivity();
        }
    }

    private void moveVerseToRememberedList() {
        String formattedDate = dateFormat.format(c.getTime());
        scripture.setRemeberedDate(formattedDate);
        DataStore.getInstance().addVerseMemorized(scripture);
        DataStore.getInstance().saveMemorizedVerse(scripture.toMemorizedVerseData(), context);
        DataStore.getInstance().deleteQuizVerse(scripture.toMyVerse(), context);
        DataStore.getInstance().starNextVerse(context);
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
            MemorizedVerse temp = scripture.toMemorizedVerseData();
            temp.setForgotten(myMemorizedVerse.isForgotten());
            DataStore.getInstance().updateMemorizedVerse(temp, context);
        }else if(reviewVerse){
            scripture.setLastSeenDate(formattedDate);
            if(subStage == 0){
                scripture.setMemoryStage(stage - 1);
                scripture.setMemorySubStage(2);
            }else{
                scripture.setMemorySubStage(subStage - 1);
            }
            MemorizedVerse temp = scripture.toMemorizedVerseData();
            temp.setForgotten(true);
            DataStore.getInstance().updateMemorizedVerse(temp, context);
        }else {
            DataStore.getInstance().updateQuizVerse(scripture.toMyVerse(), context);
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
        view.setCheckAnswerButtonVisibility(View.GONE);
        view.setHintButtonVisibility(View.GONE, scripture.toMyVerse());
        view.setVerificationLayoutVisibility(View.VISIBLE);
        view.setTitlebarTextColor(context.getResources().getColor(R.color.colorGreenText));
    }

    private void resetVerseView() {
        forgottenVerse = false;
        reviewVerse = false;
        isReviewMode = false;
        view.setCheckAnswerButtonFont();
        view.setReviewTitleVisibility(View.GONE);
        view.setVerseText(EMPTY_STRING);
        view.setVerseLocationText(EMPTY_STRING);
        view.setBasebarText(CLOSE);
        view.setVerificationLayoutVisibility(View.GONE);
        view.setTitlebarTextColor(context.getResources().getColor(R.color.gold));
        onRequestData();
    }


    private void initializeUnlockView(){
        String formattedDate = dateFormat.format(c.getTime());
        setModifiedVerseLocationText();
        setModifiedVerseText();
        scripture.setLastSeenDate(formattedDate);
        if(initialStage){
            view.setCheckAnswerButtonText(R.string.done);
            view.setDoneButtonFont();
            view.setHintButtonVisibility(View.GONE, scripture.toMyVerse());
        }else {
            view.setCheckAnswerButtonText(R.string.check_answer);
            if (scripture.getMemoryStage() != 7) {
                view.setHintButtonVisibility(View.VISIBLE, scripture.toMyVerse());
            }
        }
        view.setCheckAnswerButtonVisibility(View.VISIBLE);
    }
}
