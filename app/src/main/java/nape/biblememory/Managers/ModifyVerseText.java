package nape.biblememory.Managers;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.Models.ScriptureData;

/**
 * Created by Jonathan on 8/22/2016.
 */
public class ModifyVerseText {


    public SpannableStringBuilder createCheckAnswerText(String modifiedVerse, String unmodifiedVerse, ScriptureData scripture) {
        int verseSize = unmodifiedVerse.length();
        final SpannableStringBuilder sb = new SpannableStringBuilder(unmodifiedVerse);
        int R = 42;
        int G = 252;
        int B = 116;

        if(scripture.getMemoryStage() < 7) {
            for (int i = 0; i < verseSize; i++) {
                if (modifiedVerse.charAt(i) == '_' && unmodifiedVerse.charAt(i) != '_') {
                    sb.setSpan(new ForegroundColorSpan(Color.rgb(R, G, B)), i, i + 1, 0);
                }
            }
            return sb;
        }else{
            for (int i = 0; i < verseSize; i++) {
                if (unmodifiedVerse.charAt(i) != ' ') {
                    sb.setSpan(new ForegroundColorSpan(Color.rgb(R, G, B)), i, i + 1, 0);
                }
            }
            return sb;
        }
    }

    public SpannableStringBuilder createNoClickedAnswerVerse(String verseText){
        int verseSize = verseText.length();
        final SpannableStringBuilder sb = new SpannableStringBuilder(verseText);
        int R = 255;
        int G = 100;
        int B = 44;

        for(int i = 0; i < verseSize; i++){
            sb.setSpan(new ForegroundColorSpan(Color.rgb(R, G, B)), i, i+1, 0);
        }
        return sb;
    }
    public SpannableStringBuilder createYesClickedAnswerVerse(String verseText){
        int verseSize = verseText.length();
        final SpannableStringBuilder sb = new SpannableStringBuilder(verseText);
        int R = 42;
        int G = 252;
        int B = 116;

        for(int i = 0; i < verseSize; i++){
            sb.setSpan(new ForegroundColorSpan(Color.rgb(R, G, B)), i, i+1, 0);
        }
        return sb;
    }

    public SpannableStringBuilder createFinalStageTip(String verseText){
        int verseSize = verseText.length();
        final SpannableStringBuilder sb = new SpannableStringBuilder(verseText);
        int R = 0;
        int G = 63;
        int B = 84;

        for(int i = 0; i < verseSize; i++){
            sb.setSpan(new ForegroundColorSpan(Color.rgb(R, G, B)), i, i+1, 0);
        }
        return sb;
    }

    public SpannableStringBuilder createHintText(String modifiedVerseText, ScriptureData scripture) {
        int stage = scripture.getMemoryStage();
        int subStage = scripture.getMemorySubStage();
        SpannableStringBuilder sb = new SpannableStringBuilder(modifiedVerseText);

        switch(stage){
            case 1:
                sb = includeFirstLetter(modifiedVerseText, scripture.getVerse());
                break;
            case 2:
                sb = includeFirstLetter(modifiedVerseText, scripture.getVerse());
                break;
            case 3:
                sb = includeFirstLetter(modifiedVerseText, scripture.getVerse());
                break;
            case 4:
                sb = includeFirstLetter(modifiedVerseText, scripture.getVerse());
                break;
            case 5:
                if(subStage == 0){
                    sb = includeFirstWord(modifiedVerseText, scripture.getVerseLocation());
                }else{
                    sb = includeSecondWord(modifiedVerseText, scripture.getVerse());
                }
                break;
            case 6:
                sb = includeFirstWord(modifiedVerseText, scripture.getVerse());
                break;
            case 7:
                break;
        }

        return sb;
    }


    private SpannableStringBuilder includeFirstLetter(String modifiedVerseText, String verse) {
        int size = verse.length();
        int positionListSize;
        boolean sameWord = false;
        List<Integer> positionList = new ArrayList<>();
        List<Integer> resultPositionList = new ArrayList<>();
        StringBuilder resultString = new StringBuilder(modifiedVerseText);
        SpannableStringBuilder result;

        int R = 42;
        int G = 252;
        int B = 116;

        for(int i = 0; i < size; i++){
            if (modifiedVerseText.charAt(i) == '_' && sameWord != true) {
                resultString.setCharAt(i, verse.charAt(i));
                positionList.add(i);
                sameWord = true;
            }else if(modifiedVerseText.charAt(i) == ' '){
                sameWord = false;
            }
        }
        positionListSize = positionList.size();
        result = new SpannableStringBuilder(resultString);

        int newLetterPosition;
        for(int i = 0; i < positionListSize; i++){
            newLetterPosition = positionList.get(i);
            while(newLetterPosition >= 0 && resultString.charAt(newLetterPosition) != ' '){
                resultPositionList.add(newLetterPosition);
                newLetterPosition--;
            }
        }


        for(int i = 0; i < size; i++){
            if(resultPositionList.contains(i)){
                result.setSpan(new ForegroundColorSpan(Color.rgb(R, G, B)), i, i + 1, 0);
            }
        }
        return result;
    }


    private SpannableStringBuilder includeFirstWord(String modifiedVerseText, String verse) {
        int size = verse.length();
        StringBuilder resultString = new StringBuilder(modifiedVerseText);
        SpannableStringBuilder result;

        for(int i = 0; i < size; i++){
            if(resultString.charAt(i) == ' '){
                break;
            }
            resultString.setCharAt(i, verse.charAt(i));
        }

        result = new SpannableStringBuilder(resultString);
        return result;
    }


    private SpannableStringBuilder includeSecondWord(String modifiedVerseText, String verse) {
        int size = verse.length();
        StringBuilder resultString = new StringBuilder(modifiedVerseText);
        SpannableStringBuilder result;

        for(int i = 0; i < size; i++){
            if(resultString.charAt(i) == '_'){
                while(resultString.charAt(i) == '_'){
                    resultString.setCharAt(i, verse.charAt(i));
                    i++;
                }
                break;
            }
        }

        result = new SpannableStringBuilder(resultString);
        return result;
    }
}
