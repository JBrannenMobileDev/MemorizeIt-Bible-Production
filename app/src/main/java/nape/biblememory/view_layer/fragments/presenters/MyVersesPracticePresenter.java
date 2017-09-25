package nape.biblememory.view_layer.fragments.presenters;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.RemovedWord;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPracticeFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPracticeInterface;

/**
 * Created by jbrannen on 9/12/17.
 */

public class MyVersesPracticePresenter implements MyVersesPracticeInterface {
    private static final int STAGE_1_PERCENTAGE = 20;
    private static final int STAGE_2_PERCENTAGE = 35;
    private static final int STAGE_3_PERCENTAGE = 60;
    private static final int STAGE_4_PERCENTAGE = 80;
    private static final int STAGE_5_PERCENTAGE = 100;
    private MyVersesPracticeFragmentInterface fragment;
    private MyVerse verse;
    private String[] words;
    private String[] locationWords;
    private List<RemovedWord> removedWordsList;
    private List<RemovedWord> locationRemovedWordsList;
    private List<String> correctWords;
    private List<String> wrongWords;
    private List<Integer> spanStartsWrong;
    private List<Integer> locationSpanStartsWrong;
    private List<Integer> locationSpanAllStarts;
    private List<Integer> locationSpanAllEnds;
    private List<Integer> spanEndsWrong;
    private List<Integer> locationSpanEndsWrong;
    private List<Integer> spanStartsCorrect;
    private List<Integer> locationSpanStartsCorrect;
    private List<Integer> spanEndsCorrect;
    private List<Integer> locationSpanEndsCorrect;
    private int wordIndex;
    private int locationWordIndex;
    private int correctCount;
    private int wrongCount;
    private Realm realm;
    private String currentVerseText;
    private SimpleDateFormat dateFormat;
    private String verseLocation;
    private String modifiedVerseLocation;
    private int sizeToSubtractFromTotal;

    public MyVersesPracticePresenter(MyVersesPracticeFragmentInterface fragment, String verseLocation){
        this.fragment = fragment;
        wordIndex = 0;
        correctCount = 0;
        spanStartsCorrect = new ArrayList<>();
        spanStartsWrong = new ArrayList<>();
        spanEndsCorrect = new ArrayList<>();
        spanEndsWrong = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        this.verseLocation = verseLocation;
        removedWordsList = new ArrayList<>();
        correctWords = new ArrayList<>();
        wrongWords = new ArrayList<>();
        locationSpanEndsCorrect = new ArrayList<>();
        locationSpanStartsCorrect = new ArrayList<>();
        locationSpanEndsWrong = new ArrayList<>();
        locationSpanStartsWrong = new ArrayList<>();
        locationSpanAllStarts = new ArrayList<>();
        locationSpanAllEnds = new ArrayList<>();
        locationRemovedWordsList = new ArrayList<>();
        sizeToSubtractFromTotal = 0;
    }

    @Override
    public void fetchData(String verseLocation) {
        verse = realm.where(MyVerse.class).equalTo("verseLocation", verseLocation).findFirst();
        words = countWordsUsingSplit(verse.getVerse());
        locationWords = getLocationWords(verseLocation);
        initLocationSpans(verseLocation);
        switch (verse.getMemoryStage()) {
            case 0:
                fragment.resetTextColorForStage0();
                fragment.setVerseLocationText(verseLocation);
                fragment.onDataReceived(String.valueOf(words.length + locationWords.length - sizeToSubtractFromTotal));
                fragment.setVerseText(getVerseTextForStage0(words));
                break;
            case 1:
                fragment.setVerseLocationText(modifyVerseLocationForStage(verseLocation, verse.getMemoryStage(), verse.getMemorySubStage()));
                fragment.setVerseTextColor();
                fragment.setVerseLocationTextColor();
                fragment.setVerseText(getVerseTextForStage1(words));
                fragment.onDataReceived(String.valueOf(removedWordsList.size() + locationRemovedWordsList.size()));
                break;
            case 2:
                fragment.setVerseLocationText(modifyVerseLocationForStage(verseLocation, verse.getMemoryStage(), verse.getMemorySubStage()));
                fragment.setVerseTextColor();
                fragment.setVerseLocationTextColor();
                fragment.setVerseText(getVerseTextForStage2(words));
                fragment.onDataReceived(String.valueOf(removedWordsList.size() + locationRemovedWordsList.size()));
                break;
            case 3:
                fragment.setVerseLocationText(modifyVerseLocationForStage(verseLocation, verse.getMemoryStage(), verse.getMemorySubStage()));
                fragment.setVerseTextColor();
                fragment.setVerseLocationTextColor();
                fragment.setVerseText(getVerseTextForStage3(words));
                fragment.onDataReceived(String.valueOf(removedWordsList.size() + locationRemovedWordsList.size()));
                break;
            case 4:
                fragment.setVerseLocationText(modifyVerseLocationForStage(verseLocation, verse.getMemoryStage(), verse.getMemorySubStage()));
                fragment.setVerseTextColor();
                fragment.setVerseLocationTextColor();
                fragment.setVerseText(getVerseTextForStage4(words));
                fragment.onDataReceived(String.valueOf(removedWordsList.size() + locationRemovedWordsList.size()));
                break;
            case 5:
                fragment.setVerseLocationTextColor();
                fragment.setVerseTextColor();
                fragment.setVerseLocationTextColor();
                fragment.setVerseLocationText(modifyVerseLocationForStage(verseLocation, verse.getMemoryStage(), verse.getMemorySubStage()));
                if(verse.getMemorySubStage() == 0){
                    fragment.onDataReceived(String.valueOf(locationRemovedWordsList.size()));
                    fragment.setVerseText(verse.getVerse());
                }else {
                    fragment.setVerseText(getVerseTextForStage5(words));
                    fragment.onDataReceived(String.valueOf(removedWordsList.size()));
                }
                break;
            case 6:
                fragment.setVerseLocationTextColor();
                fragment.setVerseTextColor();
                fragment.setVerseLocationTextColor();
                fragment.setVerseLocationText(verseLocation);
                fragment.setVerseText(getVerseTextForStage6(words));
                fragment.onDataReceived(String.valueOf(removedWordsList.size()));
                break;
            case 7:
                fragment.setVerseLocationTextColor();
                fragment.setVerseTextColor();
                fragment.setVerseLocationTextColor();
                fragment.setVerseLocationText(verseLocation);
                fragment.setVerseText(verse.getVerse());
                break;
        }
    }

    private void initLocationSpans(String verseLocation) {
        int wordIndex = 0;
        int charInWordIndex = 0;
        for(int i = 0; i < verseLocation.length(); i ++){
            if(charInWordIndex < locationWords[wordIndex].length() && locationWords[wordIndex].charAt(charInWordIndex) == verseLocation.charAt(i)){
                if(charInWordIndex == 0) {
                    locationSpanAllStarts.add(i);
                }
                charInWordIndex++;
            }else{
                locationSpanAllEnds.add(i);
                charInWordIndex = 0;
                wordIndex++;
                if(charInWordIndex < locationWords[wordIndex].length() && locationWords[wordIndex].charAt(charInWordIndex) == verseLocation.charAt(i)){
                    locationSpanAllStarts.add(i);
                    charInWordIndex++;
                }
            }
        }
        if(locationSpanAllStarts != locationSpanAllEnds){
            locationSpanAllEnds.add(verseLocation.length());
        }
    }

    private String[] getLocationWords(String verseLocation) {
        String[] wordsBySpace = countWordsUsingSplit(verseLocation);
        String[] wordsByColon = wordsBySpace[wordsBySpace.length - 1].split(":");
        String[] wordsByDash = wordsByColon[wordsByColon.length - 1].split("-");
        List<String> result = new ArrayList<>();

        for(int i = 0; i < wordsBySpace.length - 1; i++){
            result.add(wordsBySpace[i]);
        }
        for(int i = 0; i < wordsByColon.length - 1; i++){
            result.add(wordsByColon[i]);
            if(i == 0){
                result.add(":");
                sizeToSubtractFromTotal++;
            }
        }
        for(int i = 0; i < wordsByDash.length; i++) {
            result.add(wordsByDash[i]);
            if(i == 0 && wordsByDash.length > 1){
                result.add("-");
                sizeToSubtractFromTotal++;
            }
        }

        List<String> finalWordSplitList = new ArrayList<>();
        for(String word : result){
            if(Character.isDigit(word.charAt(0)) && word.length() > 1){
                for(char digit : word.toCharArray()){
                    String newWord = String.valueOf(digit);
                    finalWordSplitList.add(newWord);
                }
            }else{
                finalWordSplitList.add(word);
            }
        }

        String[] response = new String[finalWordSplitList.size()];
        for(int i = 0; i < finalWordSplitList.size(); i++){
            response[i] = finalWordSplitList.get(i);
        }
        return response;
    }

    @Override
    public void onNewCharInput(char c) {
        switch(verse.getMemoryStage()) {
            case 0:
                if(locationWordIndex < locationWords.length){
                    if(Character.toLowerCase(c) == Character.toLowerCase(locationWords[locationWordIndex].charAt(0))) {
                        fragment.setVerseLocationText(getVerselocationTextForStage0(locationWords, locationWordIndex));
                        locationWordIndex++;
                        correctCount++;
                        fragment.updateCorrectCount(correctCount);
                        fragment.turnLightGreen();
                    }else{
                        fragment.turnLightRed(1);
                    }
                }else if (wordIndex < words.length) {
                    if (Character.toLowerCase(c) == Character.toLowerCase(getFirstLetterOfWord(words[wordIndex]))) {
                        fragment.setVerseText(getVerseTextForStage0(words, true, wordIndex));
                        wordIndex++;
                        correctCount++;
                        fragment.updateCorrectCount(correctCount);
                        fragment.turnLightGreen();
                    } else{
                        fragment.turnLightRed(1);
                    }
                    if(wordIndex == words.length){
                        fragment.onReviewComplete(correctCount, words.length + locationWords.length - sizeToSubtractFromTotal);
                        fragment.hideKeyboard();
                    }
                }
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                if(locationWordIndex < locationRemovedWordsList.size()){
                    if(Character.toLowerCase(c) == Character.toLowerCase(locationRemovedWordsList.get(locationWordIndex).getWord().charAt(0))) {
                        fragment.setVerseLocationText(getVerselocationTextModified(true, locationWordIndex));
                        locationWordIndex++;
                        correctCount++;
                        wrongCount = 0;
                        fragment.updateCorrectCount(correctCount);
                        fragment.turnLightGreen();
                    }else{
                        wrongCount++;
                        fragment.turnLightRed(wrongCount);
                        if(wrongCount == 3){
                            wrongCount = 0;
                            fragment.setVerseLocationText(getVerselocationTextModified(false, locationWordIndex));
                            locationWordIndex++;
                        }
                    }
                    if(locationWordIndex == locationRemovedWordsList.size()){
                        wrongCount = 0;
                    }
                }else if(wordIndex < removedWordsList.size()){
                    if (Character.toLowerCase(c) == Character.toLowerCase(getFirstLetterOfWord(removedWordsList.get(wordIndex).getWord()))) {
                        fragment.setVerseText(getVerseTextModified(words, true, wordIndex));
                        wordIndex++;
                        correctCount++;
                        wrongCount = 0;
                        fragment.updateCorrectCount(correctCount);
                        fragment.turnLightGreen();
                    }else{
                        wrongCount++;
                        fragment.turnLightRed(wrongCount);
                        if(wrongCount == 3){
                            wrongCount = 0;
                            fragment.setVerseText(getVerseTextModified(words, false, wordIndex));
                            wordIndex++;
                        }
                    }
                    if(wordIndex == removedWordsList.size()){
                        fragment.onReviewComplete(correctCount, removedWordsList.size() + locationRemovedWordsList.size());
                        fragment.hideKeyboard();
                    }
                }
                break;
            case 5:
                if(verse.getMemorySubStage() == 0){
                    if(locationWordIndex < locationRemovedWordsList.size()){
                        if(Character.toLowerCase(c) == Character.toLowerCase(locationRemovedWordsList.get(locationWordIndex).getWord().charAt(0))) {
                            fragment.setVerseLocationText(getVerselocationTextModified(true, locationWordIndex));
                            locationWordIndex++;
                            correctCount++;
                            wrongCount = 0;
                            fragment.updateCorrectCount(correctCount);
                            fragment.turnLightGreen();
                        }else{
                            wrongCount++;
                            fragment.turnLightRed(wrongCount);
                            if(wrongCount == 3){
                                wrongCount = 0;
                                fragment.setVerseLocationText(getVerselocationTextModified(false, locationWordIndex));
                                locationWordIndex++;
                            }
                        }
                        if(locationWordIndex == locationRemovedWordsList.size()){
                            wrongCount = 0;
                        }
                    }
                    if(locationWordIndex == locationRemovedWordsList.size()){
                        fragment.onReviewComplete(correctCount, locationRemovedWordsList.size());
                        fragment.hideKeyboard();
                    }
                }else if(wordIndex < removedWordsList.size()){
                    if (Character.toLowerCase(c) == Character.toLowerCase(getFirstLetterOfWord(removedWordsList.get(wordIndex).getWord()))) {
                        fragment.setVerseText(getVerseTextModified(words, true, wordIndex));
                        wordIndex++;
                        correctCount++;
                        wrongCount = 0;
                        fragment.updateCorrectCount(correctCount);
                        fragment.turnLightGreen();
                    }else{
                        wrongCount++;
                        fragment.turnLightRed(wrongCount);
                        if(wrongCount == 3){
                            wrongCount = 0;
                            fragment.setVerseText(getVerseTextModified(words, false, wordIndex));
                            wordIndex++;
                        }
                    }
                    if(wordIndex == removedWordsList.size()){
                        fragment.onReviewComplete(correctCount, removedWordsList.size());
                        fragment.hideKeyboard();
                    }
                }
                break;
            case 6:
                if(wordIndex < removedWordsList.size()){
                    if (Character.toLowerCase(c) == Character.toLowerCase(getFirstLetterOfWord(removedWordsList.get(wordIndex).getWord()))) {
                        fragment.setVerseText(getVerseTextModified(words, true, wordIndex));
                        wordIndex++;
                        correctCount++;
                        wrongCount = 0;
                        fragment.updateCorrectCount(correctCount);
                        fragment.turnLightGreen();
                    }else{
                        wrongCount++;
                        fragment.turnLightRed(wrongCount);
                        if(wrongCount == 3){
                            wrongCount = 0;
                            fragment.setVerseText(getVerseTextModified(words, false, wordIndex));
                            wordIndex++;
                        }
                    }
                    if(wordIndex == removedWordsList.size()){
                        fragment.onReviewComplete(correctCount, words.length);
                        fragment.hideKeyboard();
                    }
                }
                break;
        }
    }

    @Override
    public void resetReview() {
        wordIndex = 0;
        sizeToSubtractFromTotal = 0;
        locationWordIndex = 0;
        correctCount = 0;
        wrongCount = 0;
        modifiedVerseLocation = "";
        spanEndsWrong.clear();
        spanEndsCorrect.clear();
        spanStartsWrong.clear();
        spanStartsCorrect.clear();
        locationSpanEndsWrong.clear();
        locationSpanEndsCorrect.clear();
        locationSpanStartsWrong.clear();
        locationSpanStartsCorrect.clear();
        locationRemovedWordsList.clear();
        locationSpanAllEnds.clear();
        locationSpanAllStarts.clear();
        removedWordsList.clear();
        correctWords.clear();
        wrongWords.clear();
        currentVerseText = "";
        fetchData(verseLocation);
    }

    @Override
    public void resetVerseText() {
        fragment.setVerseText(currentVerseText);
    }

    @Override
    public void infoPressed() {
        if(verse.getMemoryStage() == 0){
            fragment.showStage1InfoDialog();
        }else{
            fragment.showStageInfoDialog();
        }
    }

    @Override
    public void updateMyVerseCorrect(final String date) {
        final ScriptureData updatedVerse = increaseMemoryStage(verse.getMemoryStage(), verse.getMemorySubStage(), verse.toScriptureData());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyVerse realmVerse = realm.where(MyVerse.class).equalTo("verseLocation", verse.getVerseLocation()).findFirst();
                realmVerse.setMemoryStage(updatedVerse.getMemoryStage());
                realmVerse.setMemorySubStage(updatedVerse.getMemorySubStage());
                realmVerse.setLastSeenDate(date);
            }
        });
        fragment.updateMyVerseVerse(updatedVerse.toMyVerse(), date);
    }

    @Override
    public void updateMyVerseWrong(final String date) {
        final ScriptureData updatedVerse = decreaseMemoryStage(verse.getMemoryStage(), verse.getMemorySubStage(), verse.toScriptureData());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyVerse realmVerse = realm.where(MyVerse.class).equalTo("verseLocation", verse.getVerseLocation()).findFirst();
                realmVerse.setMemoryStage(updatedVerse.getMemoryStage());
                realmVerse.setMemorySubStage(updatedVerse.getMemorySubStage());
                realmVerse.setLastSeenDate(date);
            }
        });
        fragment.updateMyVerseVerse(updatedVerse.toMyVerse(), date);
    }

    private ScriptureData increaseMemoryStage(int stage, int subStage, ScriptureData scripture) {
        if (stage == 0) {
            scripture.setMemoryStage(stage + 1);
        } else if (subStage > 2 && stage > 0 && stage < 6) {
            scripture.setMemorySubStage(0);
            scripture.setMemoryStage(stage + 1);
        }else if (subStage >= 0 && stage == 6) {
            scripture.setMemorySubStage(0);
            scripture.setMemoryStage(stage + 1);
        }else {
            if(scripture.getMemoryStage() == 5 && scripture.getMemorySubStage() == 1){
                scripture.setMemoryStage(6);
                scripture.setMemorySubStage(0);
            }else if(scripture.getMemoryStage() == 6 && scripture.getMemorySubStage() == 0){
                scripture.setMemoryStage(7);
                scripture.setMemorySubStage(0);
            }else{
                scripture.setMemorySubStage(subStage + 1);
            }
        }
        return scripture;
    }

    private ScriptureData decreaseMemoryStage(int stage, int subStage, ScriptureData scripture) {
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
        return scripture;
    }

    private void moveVerseToRememberedList() {
        Calendar c = Calendar.getInstance();
        String formattedDate = dateFormat.format(c.getTime());
        ScriptureData scripture = verse.toScriptureData();
        scripture.setRemeberedDate(formattedDate);
        fragment.moveVerseToMemorized(scripture);
    }

    @Override
    public void onStop() {
    }

    public static String[] countWordsUsingSplit(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        String[] words = input.split("\\s+");
        return words;
    }

    private char getFirstLetterOfWord(String word){
        char[] wordArray = word.toCharArray();
        for(char c : wordArray){
            if(Character.isLetter(c)){
                return c;
            }
        }
        return ' ';
    }

    private SpannableStringBuilder getVerseTextForStage0(String[] words){
        String result = "";
        for(String word : words){
            if(result.equalsIgnoreCase("")){
                result = result + word;
            }else {
                result = result + " " +  word;
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        return sb;
    }

    private SpannableStringBuilder getVerseTextForStage0(String[] words, boolean userInputCorrect, int wordIndex) {
        String result = "";
        for(int i = 0; i < words.length; i ++){
            if(result.equalsIgnoreCase("")){
                result = result + words[i];
                if(i == wordIndex) {
                    if (userInputCorrect) {
                        spanStartsCorrect.add(result.length() - words[i].length());
                        spanEndsCorrect.add(result.length());
                    }
                }
            }else {
                result = result + " " +  words[i];
                if(i == wordIndex) {
                    if (userInputCorrect) {
                        spanStartsCorrect.add(result.length() - words[i].length());
                        spanEndsCorrect.add(result.length());
                    }
                }
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        for(int i = 0; i < spanStartsCorrect.size(); i++) {
            sb.setSpan(new ForegroundColorSpan(Color.rgb(1, 81, 107)), spanStartsCorrect.get(i), spanEndsCorrect.get(i), 0);
        }
        return sb;
    }

    private SpannableStringBuilder getVerselocationTextForStage0(String[] locationWords, int locationWordIndex) {
        String result = "";
        for(int i = 0; i < locationWords.length; i ++){
            if(result.equalsIgnoreCase("")){
                result = result + locationWords[i];
                if(i == locationWordIndex) {
                    locationSpanStartsCorrect.add(result.length() - locationWords[i].length());
                    locationSpanEndsCorrect.add(result.length());
                }
            }else {
                if(locationWords[i].equalsIgnoreCase(":") || result.charAt(result.length()-1) == ':' || locationWords[i].equalsIgnoreCase("-") ||
                        result.charAt(result.length()-1) == '-'){
                    result = result + locationWords[i];
                }else {
                    if((Character.isDigit(locationWords[i].charAt(0)) && Character.isDigit(locationWords[i-1].charAt(0)))) {
                        result = result + locationWords[i];
                    }else{
                        result = result + " " + locationWords[i];
                    }
                }
                if(i == locationWordIndex) {
                    if(i+1 < locationWords.length && (locationWords[i+1].equalsIgnoreCase(":") || locationWords[i+1].equalsIgnoreCase("-"))){
                        locationSpanStartsCorrect.add(result.length() - locationWords[i].length());
                        locationSpanEndsCorrect.add(result.length() + 1);
                        result = result + locationWords[i+1];
                        i++;
                        this.locationWordIndex++;
                    }else {
                        locationSpanStartsCorrect.add(result.length() - locationWords[i].length());
                        locationSpanEndsCorrect.add(result.length());
                    }
                }
            }
        }
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        for(int i = 0; i < locationSpanStartsCorrect.size(); i++) {
            sb.setSpan(new ForegroundColorSpan(Color.rgb(1, 81, 107)), locationSpanStartsCorrect.get(i), locationSpanEndsCorrect.get(i), 0);
        }
        return sb;
    }

    private SpannableStringBuilder getVerseTextForStage1(String[] words){
        int numToRemove = (int)(words.length*((float)(STAGE_1_PERCENTAGE)/100f));
        String[] modifiedWordList = removeRandomWords(words, numToRemove);
        String result = "";
        for(String word : modifiedWordList){
            if(result.equalsIgnoreCase("")){
                result = result + word;
            }else {
                result = result + " " +  word;
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        return sb;
    }

    private SpannableStringBuilder getVerseTextForStage2(String[] words){
        int numToRemove = (int)(words.length*((float)(STAGE_2_PERCENTAGE)/100f));
        String[] modifiedWordList = removeRandomWords(words, numToRemove);
        String result = "";
        for(String word : modifiedWordList){
            if(result.equalsIgnoreCase("")){
                result = result + word;
            }else {
                result = result + " " +  word;
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        return sb;
    }

    private SpannableStringBuilder getVerseTextModified(String[] words, boolean userInputCorrect, int wordIndex) {
        String result = "";
        int wordIndexToUpdate = removedWordsList.get(wordIndex).getIndex();
        for (int i = 0; i < words.length; i++) {
            if (result.equalsIgnoreCase("")) {
                result = result + words[i];
                if (i == wordIndexToUpdate) {
                    if (userInputCorrect) {
                        correctWords.add(removedWordsList.get(wordIndex).getWord());
                        spanStartsCorrect.add(result.length() - words[i].length());
                        spanEndsCorrect.add(result.length());
                    } else {
                        wrongWords.add(removedWordsList.get(wordIndex).getWord());
                        spanStartsWrong.add(result.length() - words[i].length());
                        spanEndsWrong.add(result.length());
                    }
                }
            } else {
                result = result + " " + words[i];
                if (i == wordIndexToUpdate) {
                    if (userInputCorrect) {
                        correctWords.add(removedWordsList.get(wordIndex).getWord());
                        spanStartsCorrect.add(result.length() - words[i].length());
                        spanEndsCorrect.add(result.length());
                    } else {
                        wrongWords.add(removedWordsList.get(wordIndex).getWord());
                        spanStartsWrong.add(result.length() - words[i].length());
                        spanEndsWrong.add(result.length());
                    }
                }
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        for (int i = 0; i < spanStartsCorrect.size(); i++) {
            sb.replace(spanStartsCorrect.get(i), spanEndsCorrect.get(i), correctWords.get(i));
            sb.setSpan(new ForegroundColorSpan(Color.rgb(41, 222, 104)), spanStartsCorrect.get(i), spanEndsCorrect.get(i), 0);
        }
        for (int i = 0; i < spanStartsWrong.size(); i++) {
            sb.replace(spanStartsWrong.get(i), spanEndsWrong.get(i), wrongWords.get(i));
            sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 68, 0)), spanStartsWrong.get(i), spanEndsWrong.get(i), 0);
        }
        return sb;
    }

    public SpannableStringBuilder getVerselocationTextModified(boolean correct, int locationWordIndex) {
        if(correct){
            locationSpanStartsCorrect.add(locationRemovedWordsList.get(locationWordIndex).getIndex());
            locationSpanEndsCorrect.add(locationRemovedWordsList.get(locationWordIndex).getIndex() + locationRemovedWordsList.get(locationWordIndex).getWord().length());
        }else{
            locationSpanStartsWrong.add(locationRemovedWordsList.get(locationWordIndex).getIndex());
            locationSpanEndsWrong.add(locationRemovedWordsList.get(locationWordIndex).getIndex() + locationRemovedWordsList.get(locationWordIndex).getWord().length());
        }
        addCheckedWordBackIntoVerseLocation(locationWordIndex);
        final SpannableStringBuilder sb = new SpannableStringBuilder(modifiedVerseLocation);
        for (int i = 0; i < locationSpanStartsCorrect.size(); i++) {
            sb.setSpan(new ForegroundColorSpan(Color.rgb(41, 222, 104)), locationSpanStartsCorrect.get(i), locationSpanEndsCorrect.get(i), 0);
        }
        for (int i = 0; i < locationSpanStartsWrong.size(); i++) {
            sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 68, 0)), locationSpanStartsWrong.get(i), locationSpanEndsWrong.get(i), 0);
        }
        return sb;
    }

    private void addCheckedWordBackIntoVerseLocation(int locationWordIndex) {
        StringBuilder modifiedVerseLocationBuilder = new StringBuilder(modifiedVerseLocation);
        for (int i = 0; i < locationRemovedWordsList.get(locationWordIndex).getWord().length(); i++){
            modifiedVerseLocationBuilder.setCharAt(i + locationRemovedWordsList.get(locationWordIndex).getIndex(), locationRemovedWordsList.get(locationWordIndex).getWord().charAt(i));
        }
        modifiedVerseLocation = modifiedVerseLocationBuilder.toString();
    }

    private SpannableStringBuilder getVerseTextForStage3(String[] words){
        int numToRemove = (int)(words.length*((float)(STAGE_3_PERCENTAGE)/100f));
        String[] modifiedWordList = removeRandomWords(words, numToRemove);
        String result = "";
        for(String word : modifiedWordList){
            if(result.equalsIgnoreCase("")){
                result = result + word;
            }else {
                result = result + " " +  word;
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        return sb;
    }

    private SpannableStringBuilder getVerseTextForStage4(String[] words){
        int numToRemove = (int)(words.length*((float)(STAGE_4_PERCENTAGE)/100f));
        String[] modifiedWordList = removeRandomWords(words, numToRemove);
        String result = "";
        for(String word : modifiedWordList){
            if(result.equalsIgnoreCase("")){
                result = result + word;
            }else {
                result = result + " " +  word;
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        return sb;
    }

    private SpannableStringBuilder getVerseTextForStage5(String[] words){
        String[] modifiedWordList = removeAllWordsExceptFirst(words);
        String result = "";
        for(String word : modifiedWordList){
            if(result.equalsIgnoreCase("")){
                result = result + word;
            }else {
                result = result + " " +  word;
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        return sb;
    }

    private SpannableStringBuilder getVerseTextForStage6(String[] words){
        String[] modifiedWordList = removeAllWords(words);
        String result = "";
        for(String word : modifiedWordList){
            if(result.equalsIgnoreCase("")){
                result = result + word;
            }else {
                result = result + " " +  word;
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        return sb;
    }

    private SpannableStringBuilder getVerseTextForStage7(String[] words){
        int numToRemove = (int)(words.length*((float)(STAGE_2_PERCENTAGE)/100f));
        String[] modifiedWordList = removeRandomWords(words, numToRemove);
        String result = "";
        for(String word : modifiedWordList){
            if(result.equalsIgnoreCase("")){
                result = result + word;
            }else {
                result = result + " " +  word;
            }
        }
        currentVerseText = result;
        final SpannableStringBuilder sb = new SpannableStringBuilder(result);
        return sb;
    }

    private String[] removeRandomWords(String[] words, int numToRemove) {
        Random rand = new Random();
        List<Integer> wordsIndexAlreadyRemoved = new ArrayList<>();
        for(int i = 0; i < numToRemove; i++){
            int randomNum = rand.nextInt(words.length);
            if(verse.getMemoryStage() < 4 && notAllRemainingWordsAreOnIgnoreList(words)) {
                while (wordsIndexAlreadyRemoved.contains(randomNum) || isIgnoreListMatch(words[randomNum])) {
                    randomNum = rand.nextInt(words.length);
                }
            }else{
                while (wordsIndexAlreadyRemoved.contains(randomNum)) {
                    randomNum = rand.nextInt(words.length - 1) + 0;
                }
            }
            RemovedWord removedWord = new RemovedWord();
            removedWord.setIndex(randomNum);
            removedWord.setWord(words[randomNum]);
            removedWordsList.add(removedWord);
            words[randomNum] = replaceWithUnderscore(words[randomNum]);
            wordsIndexAlreadyRemoved.add(randomNum);
        }
        Collections.sort(removedWordsList);
        return words;
    }

    private String[] removeAllWordsExceptFirst(String[] words) {
        for(int i = 1; i < words.length; i++){
            RemovedWord removedWord = new RemovedWord();
            removedWord.setIndex(i);
            removedWord.setWord(words[i]);
            removedWordsList.add(removedWord);
            words[i] = replaceWithUnderscore(words[i]);
        }
        Collections.sort(removedWordsList);
        return words;
    }

    private String[] removeAllWords(String[] words) {
        for(int i = 0; i < words.length; i++){
            RemovedWord removedWord = new RemovedWord();
            removedWord.setIndex(i);
            removedWord.setWord(words[i]);
            removedWordsList.add(removedWord);
            words[i] = replaceWithUnderscore(words[i]);
        }
        Collections.sort(removedWordsList);
        return words;
    }

    private boolean notAllRemainingWordsAreOnIgnoreList(String[] words) {
        for(String word: words){
            if(!isIgnoreListMatch(word) && !word.contains("_")){
                return true;
            }
        }
        return false;
    }

    private boolean isIgnoreListMatch(String currentWord) {
        final String[] ignoreList = {"the", "The", "be" ,"and","And", "of", "too", "it", "i", "I", "that", "That", "for", "For",
                "you", "You", "with", "With", "on", "this", "they", "at", "but", "by", "or", "as", "what", "is",
                "if", "my", "will", "as", "A", "a"};
        int listSize = ignoreList.length;

        for(int i = 0; i < listSize; i++){
            if(ignoreList[i].toLowerCase().contains(currentWord.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    private String replaceWithUnderscore(String word) {
        char[] charList = word.toCharArray();
        for(int i = 0; i < charList.length; i++){
            if(Character.isLetter(charList[i])){
                charList[i] = '_';
            }
        }

        return new String(charList);
    }

    private String modifyVerseLocationForStage(String verseLocation, int verseStage, int verseSubStage){
        String result = "";
        String bookName = getBookName(verseLocation);
        String chp_verse_num = getVerseAndChapter(verseLocation);
        switch(verseStage){
            case 0:
                result = bookName + " " + chp_verse_num;
                break;
            case 1:
                result = removeRandomLocationItems(35);
                break;
            case 2:
                result = removeRandomLocationItems(60);
                break;
            case 3:
                result = removeRandomLocationItems(80);
                break;
            case 4:
                result = removeRandomLocationItems(80);
                break;
            case 5:
                if(verseSubStage == 0){
                    result = getVerseLocationPlaceholder(verseLocation);
                }else {
                    result = verseLocation;
                }
                break;
            case 6:
                result = verseLocation;
                break;
            case 7:
                result = verseLocation;
                break;

        }
        modifiedVerseLocation = result;
        return result;
    }

    private String removeRandomLocationItems(double percentOfWordsToRemove) {
        removeNonWordsFromLocationWords();
        int itemsToRemove = (int)((percentOfWordsToRemove/100)*locationWords.length);
        List<Integer> indexesRemoved = new ArrayList<>();
        for(int i = 0; i < itemsToRemove; i++ ){
            int rand = generateRandomNumber(locationWords.length);
            while(indexesRemoved.contains(rand)){
                rand = generateRandomNumber(locationWords.length);
            }
            indexesRemoved.add(rand);
        }
        for(Integer index : indexesRemoved){
            RemovedWord removedWord = new RemovedWord();
            removedWord.setWord(locationWords[index]);
            removedWord.setIndex(locationSpanAllStarts.get(index));
            locationRemovedWordsList.add(removedWord);
        }
        return buildModifiedVerseLocation();
    }

    private String buildModifiedVerseLocation() {
        StringBuilder result = new StringBuilder(verseLocation);
        Collections.sort(locationRemovedWordsList);
        for (RemovedWord removedWord : locationRemovedWordsList){
            int charIndexResult = removedWord.getIndex();
            for(int i = 0; i < removedWord.getWord().length(); i++){
                result.setCharAt(charIndexResult,'_');
                charIndexResult++;
            }
        }
        return result.toString();
    }

    private void removeNonWordsFromLocationWords() {
        List<String> tempWordList = new ArrayList<>();
        List<Integer> tempSpanStart = new ArrayList<>();
        List<Integer> tempSpanEnd = new ArrayList<>();
        for(int i = 0; i < locationWords.length; i++){
            if(!locationWords[i].equalsIgnoreCase(":") && !locationWords[i].equalsIgnoreCase("-")){
                tempWordList.add(locationWords[i]);
                tempSpanStart.add(locationSpanAllStarts.get(i));
                tempSpanEnd.add(locationSpanAllEnds.get(i));
            }
        }

        String[] result = new String[tempWordList.size()];
        for(int i = 0; i < tempWordList.size(); i++){
            result[i] = tempWordList.get(i);
        }
        locationWords = result;
        locationSpanAllStarts = tempSpanStart;
        locationSpanAllEnds = tempSpanEnd;
    }

    private int generateRandomNumber(int rangeSize){
        Random generate = new Random();
        return generate.nextInt(rangeSize);
    }

    private String removeChars(String chp_verse_num) {
        if(chp_verse_num != null) {
            String result;
            String[] chpAndVerse = chp_verse_num.split(":");
            int tempRandNum = generateRandomNumber(100);
            if(tempRandNum > 49){
                result = chpAndVerse[0] + ":" + replaceWithUnderscores(chpAndVerse[1]);
            }else{
                result = replaceWithUnderscores(chpAndVerse[0]) + ":" + chpAndVerse[1];
            }
            return result;
        }
        return chp_verse_num;
    }

    private String replaceWithUnderscores(String string){
        String temp = "";
        for(int i = 0; i < string.length() ; i++){
            temp = temp + "_";
        }
        return temp;
    }

    private String getBookName(String verseLocation) {
        String[] words = countWordsUsingSplit(verseLocation);
        StringBuilder result = new StringBuilder();
        result.append(words[0]);
        for(int i = 1; i < words.length; i++){
            if(!Character.isDigit(words[i].charAt(0))){
                result.append(" ");
                result.append(words[i]);
            }else{
                break;
            }
        }
        return result.toString();
    }

    private String getVerseAndChapter(String verseLocation) {
        String[] words = countWordsUsingSplit(verseLocation);
        StringBuilder result = new StringBuilder();
        for(int i = 1; i < words.length; i++){
            if(Character.isDigit(words[i].charAt(0))){
                result.append(" ");
                result.append(words[i]);
            }
        }
        return result.toString();
    }

    private String getBookNamePlaceholder(String bookName) {
        if(bookName != null && bookName.length() > 0) {
            int length = bookName.length();
            StringBuilder result = new StringBuilder(bookName);
            for (int i = 0; i < length; i++) {
                if(result.charAt(i) != ' ') {
                    result.setCharAt(i, '_');
                }
            }
            return result.toString();
        }
        return "";
    }

    private String getVerseLocationPlaceholder(String verseLocation) {
        int length = verseLocation.length();
        StringBuilder result = new StringBuilder(verseLocation);
        for(int i = 0;i < length; i++){
            if(result.charAt(i) != ' ' && result.charAt(i) != ':' && result.charAt(i) != '-') {
                result.setCharAt(i, '_');
            }
        }
        removeRandomLocationItems(100);
        return result.toString();
    }

    private String getFirstWordOnlyVerse(String verseContent) {
        int length = verseContent.length();
        boolean isFirstWordSkipped = false;
        StringBuilder result = new StringBuilder(verseContent);
        for(int i = 0; i < length; i++){
            if(isFirstWordSkipped) {
                if (verseContent.charAt(i) != ' ' && verseContent.charAt(i) != '-' &&
                        verseContent.charAt(i) != '.' && verseContent.charAt(i) != ',' &&
                        verseContent.charAt(i) != ':' && verseContent.charAt(i) != ';' &&
                        verseContent.charAt(i) != '!' && verseContent.charAt(i) != '?') {
                    result.setCharAt(i, '_');
                }
            }else{
                if(verseContent.charAt(i) == ' '){
                    isFirstWordSkipped = true;
                }
            }
        }
        return result.toString();
    }
}
