package nape.biblememory.view_layer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import nape.biblememory.custom_views.BackAwareEditText;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.data_layer.firebase_db.FirebaseDb;
import nape.biblememory.models.MyVerse;
import nape.biblememory.utils.MyVerseCopyer;
import nape.biblememory.view_layer.fragments.dialogs.DeleteVerseAlertDialog;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.R;
import nape.biblememory.view_layer.fragments.dialogs.MemorizedReviewInfoAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.MyVersesLearningInfoAlertDialog;
import nape.biblememory.view_layer.fragments.dialogs.VerseMemorizedAlertDialog;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPracticeFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPracticeInterface;
import nape.biblememory.view_layer.fragments.presenters.MyVersesPracticePresenter;

public class VerseTrainingActivity extends AppCompatActivity implements DeleteVerseAlertDialog.YesSelected,
        VerseMemorizedAlertDialog.YesSelected, MyVersesPracticeFragmentInterface, BackAwareEditText.BackPressedListener {

    @BindView(R.id.training_scroll_view)ScrollView scrollView;
    @BindView(R.id.training_frame_layout)FrameLayout frameLayout;
    @BindView(R.id.my_verses_review_user_input)BackAwareEditText userInput;
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
    @BindView(R.id.training_show_keyboard)ImageView keyboardBt;
    private MyVersesPracticeInterface presenter;
    private boolean reviewComplete;
    private MyVerse verse;
    private String verseLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_training);
        ButterKnife.bind(this);
        verseLocation = getIntent().getStringExtra("verseLocation");
        setTitle("Training");
        Realm realm = Realm.getDefaultInstance();
        verse = realm.where(MyVerse.class).equalTo("verseLocation", verseLocation).findFirst();
        presenter = new MyVersesPracticePresenter(this, verseLocation);
        initListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.verse_details_menu, menu);
        return true;
    }

    private void initListeners() {
        keyboardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard();
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

        userInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    keyboardBt.setVisibility(View.VISIBLE);
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
        presenter.onResume();
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
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userInput, InputMethodManager.SHOW_IMPLICIT);
        verseTextTv.setText("");
        presenter.resetReview();
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
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
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
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    wellDoneImage.setImageDrawable(getDrawable(R.drawable.well_done));
                    break;
                case 2:
                    wellDoneImage.setImageDrawable(getDrawable(R.drawable.well_done_2));
                    break;
                case 3:
                    wellDoneImage.setImageDrawable(getDrawable(R.drawable.well_done_3));
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
            DataStore.getInstance().saveMemorizedVerse(scripture.toMemorizedVerseData(), getApplicationContext());
            DataStore.getInstance().deleteQuizVerse(scripture.toMyVerse(), getApplicationContext());
            if(scripture.isGoldStar() == 1) {
                DataStore.getInstance().starNextVerse(getApplicationContext());
            }
            VerseMemorizedAlertDialog alert = new VerseMemorizedAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putBoolean("callOnFinish", true);
            bundle.putString("verseLocation", verse.getVerseLocation());
            bundle.putString("verse", verse.getVerse());
            alert.setArguments(bundle);
            alert.show(getSupportFragmentManager(), null);
        }else {
            FirebaseDb.getInstance().updateQuizVerse(temp, getApplicationContext());
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
        DataStore.getInstance().saveMemorizedVerse(scripture.toMemorizedVerseData(), getApplicationContext());
        DataStore.getInstance().deleteQuizVerse(scripture.toMyVerse(), getApplicationContext());
        DataStore.getInstance().starNextVerse(getApplicationContext());
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
        new MemorizedReviewInfoAlertDialog().show(getSupportFragmentManager(), null);
    }

    @Override
    public void showStageInfoDialog() {
        new MyVersesLearningInfoAlertDialog().show(getSupportFragmentManager(), null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_share) {
            sendShareIntent();
            return true;
        }
        if (id == R.id.action_reset) {
            MyVerse temp = MyVerseCopyer.getCopy(verse);
            temp.setMemoryStage(0);
            temp.setMemorySubStage(0);
            DataStore.getInstance().updateQuizVerse(temp, getApplicationContext());
            return true;
        }
        if (id == R.id.action_delete_verse) {
            DeleteVerseAlertDialog deleteDialog = new DeleteVerseAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putString("verse_location", verse.getVerseLocation());
            deleteDialog.setArguments(bundle);
            deleteDialog.show(getSupportFragmentManager(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendShareIntent(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, verse.getVerseLocation() + "  " + "Memorize It - Bible");
            String sAux = "\n" +verse.getVerseLocation() + "  " + verse.getVerse() + "\n\n";
            sAux = sAux + "Hey! I just started memorizing " + verse.getVerseLocation() +  " using this app. You should check it out. It gives me a quiz every time I open my phone.\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }

    private void hideSoftKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showSoftKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @Override
    public void onDeleteVerse(String verseLocation) {
        DataStore.getInstance().deleteQuizVerse(new MyVerse("", verseLocation), getApplicationContext());
        finish();
    }

    @Override
    public void callOnFinished() {
        finish();
    }

    @Override
    public void onHideUIControls() {
        hideSoftKeyboard();
    }

    @Override
    public void onImeBack(BackAwareEditText editText) {
        keyboardBt.setVisibility(View.VISIBLE);
    }
}
