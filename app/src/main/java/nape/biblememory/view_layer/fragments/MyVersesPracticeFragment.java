package nape.biblememory.view_layer.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.data_layer.firebase_db.FirebaseDb;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.utils.MyVerseCopyer;
import nape.biblememory.view_layer.fragments.dialogs.MemorizedReviewInfoAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.MyVersesLearningInfoAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.VerseMemorizedAlertDialog;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPracticeFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPracticeInterface;
import nape.biblememory.view_layer.fragments.presenters.MyVersesPracticePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyVersesPracticeFragment extends Fragment implements MyVersesPracticeFragmentInterface , VerseMemorizedAlertDialog.YesSelected{
    @BindView(R.id.my_verses_review_user_input)EditText userInput;
    @BindView(R.id.memorized_review_verse_text)TextView verseTextTv;
    @BindView(R.id.memorized_review_correct_count)TextView correctCountTv;
    @BindView(R.id.memorized_word_count)TextView wordCountTv;
    @BindView(R.id.my_verse_review_verse_loaction)TextView verseLocationTv;
    @BindView(R.id.memorized_green_dot1)ImageView dot1;
    @BindView(R.id.memorized_green_dot2)ImageView dot2;
    @BindView(R.id.memorized_green_dot3)ImageView dot3;
    @BindView(R.id.memorized_percent_complete)TextView progressText;
    @BindView(R.id.memorized_review_replay)ImageView replayIcon;
    @BindView(R.id.memorized_review_info)ImageView infoButton;
    @BindView(R.id.well_done1_image)ImageView wellDoneImage;
    private MyVersesPracticeInterface presenter;
    private String verseLocation;
    private boolean reviewComplete;

    public MyVersesPracticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_verses_practice, container, false);
        ButterKnife.bind(this, v);
        verseLocation = getArguments().getString("verseLocation");
        presenter = new MyVersesPracticePresenter(this, verseLocation);
        initListeners(v);
        return v;
    }

    private void initListeners(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!reviewComplete) {
                    requestEditTextFocusFromPresenter();
                }
            }
        });

        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    char input = s.charAt(s.length() - 1);
                    presenter.onNewCharInput(input);
                }
            }
        });

        replayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verseTextTv.setText("");
                requestEditTextFocus();
                replayIcon.setVisibility(View.GONE);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.infoPressed();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
        presenter.onStop();
    }

    public void requestEditTextFocus(){
        replayIcon.setVisibility(View.GONE);
        wellDoneImage.setVisibility(View.GONE);
        dot1.setVisibility(View.GONE);
        dot2.setVisibility(View.GONE);
        dot3.setVisibility(View.GONE);
        correctCountTv.setText("0");
        userInput.setText("");
        userInput.requestFocus();
        progressText.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userInput, InputMethodManager.SHOW_IMPLICIT);
        verseTextTv.setText("");
        presenter.resetReview();
    }

    public void requestEditTextFocusFromPresenter(){
        dot1.setVisibility(View.GONE);
        dot2.setVisibility(View.GONE);
        dot3.setVisibility(View.GONE);
        wellDoneImage.setVisibility(View.GONE);
        replayIcon.setVisibility(View.GONE);
        userInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userInput, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void turnLightGreen() {
        dot2.setVisibility(View.GONE);
        dot3.setVisibility(View.GONE);
        dot1.setVisibility(View.VISIBLE);
        dot1.setAlpha(1f);
        dot1.setColorFilter(getResources().getColor(R.color.colorGreenText));
        dot1.animate().alpha(0).setDuration(1000);
        correctCountTv.setTextColor(getResources().getColor(R.color.colorGreenText));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                correctCountTv.setTextColor(getResources().getColor(R.color.White));
            }
        }, 300);
    }

    @Override
    public void updateCorrectCount(int correctCount) {
        correctCountTv.setText(String.valueOf(correctCount));
    }

    @Override
    public void onDataReceived(String wordCount) {
        wordCountTv.setText(wordCount);
    }

    @Override
    public void turnLightRed(int i) {
        Vibrator v = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        switch(i){
            case 1:
                v.vibrate(100);
                dot1.animate().alpha(1f).setDuration(0);
                dot1.setAlpha(1f);
                dot1.setColorFilter(getResources().getColor(R.color.colorNoText));
                dot1.setVisibility(View.VISIBLE);
                break;
            case 2:
                v.vibrate(300);
                dot2.setAlpha(1f);
                dot2.setColorFilter(getResources().getColor(R.color.colorNoText));
                dot2.setVisibility(View.VISIBLE);
                break;
            case 3:
                v.vibrate(900);
                dot3.setAlpha(1f);
                dot3.setColorFilter(getResources().getColor(R.color.colorNoText));
                dot3.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dot1.setVisibility(View.GONE);
                        dot2.setVisibility(View.GONE);
                        dot3.setVisibility(View.GONE);
                    }
                }, 100);
                break;
        }
    }

    @Override
    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            reviewComplete = true;
        }
    }

    @Override
    public void onReviewComplete(int correctCount, int wordCount) {
        SimpleDateFormat dateFormat;
        Calendar c;
        dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        c = Calendar.getInstance();
        int percentComplete = (int)((((float)correctCount)/((float)wordCount))*100f);
        if(percentComplete == 100){
            presenter.updateMyVerseCorrect(dateFormat.format(c.getTime()));
            Random rand = new Random();
            int n = rand.nextInt(3) + 1;
            switch(n){
                case 1:
                    wellDoneImage.setImageDrawable(getActivity().getDrawable(R.drawable.well_done));
                    break;
                case 2:
                    wellDoneImage.setImageDrawable(getActivity().getDrawable(R.drawable.well_done_2));
                    break;
                case 3:
                    wellDoneImage.setImageDrawable(getActivity().getDrawable(R.drawable.well_done_3));
                    break;
            }
            wellDoneImage.setVisibility(View.VISIBLE);
            progressText.setTextColor(getResources().getColor(R.color.colorGreenText));
        }else if(percentComplete >= 75){
            progressText.setTextColor(getResources().getColor(R.color.colorWhite));
        }else{
            presenter.updateMyVerseWrong(dateFormat.format(c.getTime()));
            progressText.setTextColor(getResources().getColor(R.color.colorNoText));
        }
        progressText.setText(String.valueOf(percentComplete) + "%");
        progressText.setVisibility(View.VISIBLE);
        replayIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateMyVerseVerse(MyVerse verse, String date) {
        MyVerse temp = MyVerseCopyer.getCopy(verse);
        temp.setLastSeenDate(date);
        if(verse.getMemoryStage() == 7){
            ScriptureData scripture = verse.toScriptureData();
            String formattedDate = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime());
            scripture.setRemeberedDate(formattedDate);
            DataStore.getInstance().addVerseMemorized(scripture);
            DataStore.getInstance().saveMemorizedVerse(scripture.toMemorizedVerseData(), getActivity().getApplicationContext());
            DataStore.getInstance().deleteQuizVerse(scripture.toMyVerse(), getActivity().getApplicationContext());
            if(scripture.isGoldStar() == 1) {
                DataStore.getInstance().starNextVerse(getActivity().getApplicationContext());
            }
            VerseMemorizedAlertDialog alert = new VerseMemorizedAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putBoolean("callOnFinish", true);
            bundle.putString("verseLocation", verse.getVerseLocation());
            bundle.putString("verse", verse.getVerse());
            alert.setArguments(bundle);
            alert.show(getFragmentManager(), null);
        }else {
            FirebaseDb.getInstance().updateQuizVerse(temp, getActivity().getApplicationContext());
        }
    }

    @Override
    public void setVerseText(SpannableStringBuilder span) {
        verseTextTv.setText(span);
    }

    @Override
    public void setVerseText(String verse) {
        verseTextTv.setText(verse);
    }

    @Override
    public void setVerseLocationText(String verseLocation){
        verseLocationTv.setText(verseLocation);
    }

    @Override
    public void setVerseLocationText(SpannableStringBuilder verseLocation){
        verseLocationTv.setText(verseLocation);
    }

    @Override
    public void moveVerseToMemorized(ScriptureData scripture) {
        DataStore.getInstance().addVerseMemorized(scripture);
        DataStore.getInstance().saveMemorizedVerse(scripture.toMemorizedVerseData(), getActivity().getApplicationContext());
        DataStore.getInstance().deleteQuizVerse(scripture.toMyVerse(), getActivity().getApplicationContext());
        DataStore.getInstance().starNextVerse(getActivity().getApplicationContext());
    }

    @Override
    public void setVerseTextColor() {
        verseTextTv.setTextColor(getResources().getColor(R.color.bgColor));
    }

    @Override
    public void resetTextColorForStage0(){
        verseTextTv.setTextColor(getResources().getColor(R.color.greyBgDark));
        verseLocationTv.setTextColor(getResources().getColor(R.color.greyBgDark));
    }

    @Override
    public void setVerseLocationTextColor() {
        verseLocationTv.setTextColor(getResources().getColor(R.color.bgColor));
    }

    @Override
    public void showStage1InfoDialog() {
        new MemorizedReviewInfoAlertDialog().show(getFragmentManager(), null);
    }

    @Override
    public void showStageInfoDialog() {
        new MyVersesLearningInfoAlertDialog().show(getFragmentManager(), null);
    }

    @Override
    public void callOnFinished() {
        getActivity().finish();
    }

    @Override
    public void onHideUIControls() {

    }
}
