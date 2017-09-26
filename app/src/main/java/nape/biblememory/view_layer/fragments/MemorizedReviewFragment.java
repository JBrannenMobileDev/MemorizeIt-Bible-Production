package nape.biblememory.view_layer.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.data_layer.firebase_db.FirebaseDb;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.utils.MemorizedVerseCopyer;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.fragments.dialogs.MemorizedReviewInfoAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.ReMemorizedAlertDialog;
import nape.biblememory.view_layer.fragments.interfaces.MemorizedVerseReviewFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MemorizedVerseReviewInterface;
import nape.biblememory.view_layer.fragments.presenters.MemorizedVerseReviewPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemorizedReviewFragment extends Fragment implements MemorizedVerseReviewFragmentInterface {
    @BindView(R.id.memorized_review_user_input)EditText userInput;
    @BindView(R.id.memorized_review_verse_text)TextView verseTextTv;
    @BindView(R.id.memorized_review_correct_count)TextView correctCountTv;
    @BindView(R.id.memorized_word_count)TextView wordCountTv;
    @BindView(R.id.memorized_green_dot1)ImageView dot1;
    @BindView(R.id.memorized_green_dot2)ImageView dot2;
    @BindView(R.id.memorized_green_dot3)ImageView dot3;
    @BindView(R.id.memorized_percent_complete)TextView progressText;
    @BindView(R.id.memorized_review_replay)ImageView replayIcon;
    @BindView(R.id.memorized_review_info)ImageView infoButton;
    @BindView(R.id.well_done1_image)ImageView wellDoneImage;
    private MemorizedVerseReviewInterface presenter;
    private String verseLocation;
    private String verse;
    private boolean reviewComplete;
    private List<Integer> spanStarts;
    private List<Integer> spanends;
    private boolean reviewNow;


    public MemorizedReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_memorized_review, container, false);
        ButterKnife.bind(this, v);
        verse = "";
        presenter = new MemorizedVerseReviewPresenter(this);
        verseLocation = getArguments().getString("verseLocation");
        initListeners(v);
        spanStarts = new ArrayList<>();
        spanends = new ArrayList<>();
        UserPreferences mPrefs = new UserPreferences();
        mPrefs.setComingFromMemorizedDetails(true, getActivity().getApplicationContext());
        reviewNow = getArguments().getBoolean("reviewNow", false);
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
                if(s.length() > 0) {
                    char input = s.charAt(s.length() - 1);
                    if (Character.isLetter(input)) {
                        presenter.onNewCharInput(input);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        replayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEditTextFocus();
                replayIcon.setVisibility(View.GONE);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MemorizedReviewInfoAlertDialog().show(getFragmentManager(), null);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.fetchData(verseLocation);
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
        presenter.resetReview();
        correctCountTv.setText("0");
        verse = "";
        userInput.setText("");
        userInput.requestFocus();
        verseTextTv.setText("");
        spanStarts.clear();
        spanends.clear();
        progressText.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userInput, InputMethodManager.SHOW_IMPLICIT);
    }

    public void requestEditTextFocusFromPresenter(){
        dot1.setVisibility(View.GONE);
        dot2.setVisibility(View.GONE);
        dot3.setVisibility(View.GONE);
        wellDoneImage.setVisibility(View.GONE);
        replayIcon.setVisibility(View.GONE);
        userInput.requestFocus();
        verseTextTv.setText("");
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userInput, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onDataReceived(String wordCount) {
        wordCountTv.setText(wordCount);
    }

    @Override
    public void onCorrect(String word) {
        turnLightsGreen();
        if(verse.equalsIgnoreCase("")){
            verse = verse + word;
        }else{
            verse = verse + " " + word;
        }
        final SpannableStringBuilder sb = new SpannableStringBuilder(verse);
        for(int i = 0; i < spanStarts.size(); i++) {
            sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 0, 0)), spanStarts.get(i), spanends.get(i), 0);
        }
        verse = sb.toString();
        verseTextTv.setText(sb);
    }

    private void turnLightsGreen() {
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
    public void onIncorrect(String word, int numOfRedLights) {
        Vibrator v = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        switch(numOfRedLights){
            case 1:
                v.vibrate(100);
                dot1.animate().alpha(1f).setDuration(0);
                dot1.setAlpha(1f);
                dot1.setColorFilter(getResources().getColor(R.color.red));
                dot1.setVisibility(View.VISIBLE);
                break;
            case 2:
                v.vibrate(300);
                dot2.setAlpha(1f);
                dot2.setColorFilter(getResources().getColor(R.color.red));
                dot2.setVisibility(View.VISIBLE);
                break;
            case 3:
                v.vibrate(900);
                dot3.setAlpha(1f);
                dot3.setColorFilter(getResources().getColor(R.color.red));
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

                if (verse.equalsIgnoreCase("")) {
                    verseTextTv.setText(word);
                    verse = verse + word;
                    spanStarts.add(verse.length() - word.length());
                    spanends.add(verse.length());
                    final SpannableStringBuilder sb = new SpannableStringBuilder(verse);
                    for(int i = 0; i < spanStarts.size(); i++) {
                        sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 0, 0)), spanStarts.get(i), spanends.get(i), 0);
                    }
                    verse = sb.toString();
                    verseTextTv.setText(sb);
                } else {
                    verseTextTv.setText(verse + " " + word);
                    verse = verse + " " + word;
                    final SpannableStringBuilder sb = new SpannableStringBuilder(verse);
                    spanStarts.add(verse.length() - word.length());
                    spanends.add(verse.length());
                    for(int i = 0; i < spanStarts.size(); i++) {
                        sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 0, 0)), spanStarts.get(i), spanends.get(i), 0);
                    }
                    verse = sb.toString();
                    verseTextTv.setText(sb);
                }
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
    public void updateCorrectCount(int correctCount) {
        correctCountTv.setText(String.valueOf(correctCount));
    }

    @Override
    public void onReviewComplete(int correctCount, int wordCount) {
        SimpleDateFormat dateFormat;
        Calendar c;
        dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        c = Calendar.getInstance();
        presenter.updateMemorizedVerse(dateFormat.format(c.getTime()));
        int percentComplete = (int)((((float)correctCount)/((float)wordCount))*100f);
        if(percentComplete == 100){
            if(reviewNow) {
                new ReMemorizedAlertDialog().show(getFragmentManager(), null);
                presenter.onReMemorized();
                reviewNow = false;
            }
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
        }else{
            presenter.updateMemorizedVerseToFalse();
            progressText.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        progressText.setText(String.valueOf(percentComplete) + "%");
        progressText.setVisibility(View.VISIBLE);
        replayIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpdateReMemorizedVerse(MemorizedVerse verse) {
        DataStore.getInstance().updateReMemorizedVerse(verse, getActivity().getApplicationContext());
    }

    @Override
    public void updateMemorizedVerse(MemorizedVerse verse, String date) {
        MemorizedVerse temp = MemorizedVerseCopyer.getCopy(verse);
        temp.setLastSeenDate(date);
        FirebaseDb.getInstance().updateMemorizedVerse(temp, getActivity().getApplicationContext());
    }

    @Override
    public void updateMemorizedVerseToForgotten(MemorizedVerse verse) {
        MemorizedVerse temp = MemorizedVerseCopyer.getCopy(verse);
        temp.setForgotten(true);
        FirebaseDb.getInstance().updateMemorizedVerse(temp, getActivity().getApplicationContext());
    }
}
