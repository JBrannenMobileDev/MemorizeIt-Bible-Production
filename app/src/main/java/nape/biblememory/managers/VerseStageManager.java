package nape.biblememory.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nape.biblememory.models.ScriptureData;

/**
 * Created by Jonathan on 6/26/2016.
 */
public class VerseStageManager {
    private static final int STAGE_1_PERCENTAGE = 20;
    private static final int STAGE_2_PERCENTAGE = 35;
    private static final int STAGE_3_PERCENTAGE = 60;
    private static final int STAGE_4_PERCENTAGE = 80;
    private static final int STAGE_5_PERCENTAGE = 100;
    public VerseStageManager(){}

    public String createModifiedVerseLocation(ScriptureData scripture){
        String verseLocation = "";
        int verseStage;
        int verseSubStage;
        if(scripture != null && scripture.getVerse() != null && scripture.getMemoryStage() >= 0 && scripture.getMemorySubStage() >= 0) {
            verseStage = scripture.getMemoryStage();
            verseSubStage = scripture.getMemorySubStage();
            switch(verseStage){
                case 0:
                    verseLocation = scripture.getVerseLocation();
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    verseLocation = modifyVerseLocationForStage(scripture.getVerseLocation(), verseStage, verseSubStage);
                    break;
                default:
                    verseLocation = scripture.getVerse();
            }
        }
        return verseLocation;
    }

    public String createModifiedVerse(ScriptureData scripture) {
        String verse = scripture.getVerse();
        for (int i = 0 ; i<verse.length() ; i++) {
            if (verse.charAt(i) == '\n' || verse.charAt(i) == '\t') {
                verse = scripture.getVerse().substring(0, i-1);
            }
        }
        scripture.setVerse(verse);
        String verseContent = "";
        int verseStage;
        if(scripture != null && scripture.getVerse() != null && scripture.getMemoryStage() >= 0) {
            verseStage = scripture.getMemoryStage();
            switch (verseStage) {
                case 0:
                    verseContent = scripture.getVerse();
                    break;
                case 1:
                    verseContent = modifyVerseForStage(STAGE_1_PERCENTAGE, scripture.getVerse(), verseStage);
                    break;
                case 2:
                    verseContent = modifyVerseForStage(STAGE_2_PERCENTAGE, scripture.getVerse(), verseStage);
                    break;
                case 3:
                    verseContent = modifyVerseForStage(STAGE_3_PERCENTAGE, scripture.getVerse(), verseStage);
                    break;
                case 4:
                    verseContent = modifyVerseForStage(STAGE_4_PERCENTAGE, scripture.getVerse(), verseStage);
                    break;
                case 5:
                    if(scripture.getMemorySubStage() == 0){
                        verseContent = scripture.getVerse();
                    }else {
                        verseContent = modifyVerseForStage(STAGE_5_PERCENTAGE, scripture.getVerse(), verseStage);
                    }
                    break;
                case 6:
                    verseContent = modifyVerseForStage(0, scripture.getVerse(), verseStage);
                    break;
                case 7:
                    verseContent = getPlaceholderVerse(scripture.getVerse());
                    break;
                default:
                    verseContent = scripture.getVerse();
            }
        }
        return verseContent;
    }

    private String modifyVerseForStage(int percentageMissing, String verseContent, int verseStage) {
        int verseWordCount = countWords(verseContent);
        if(verseStage == 6){
            return getPlaceholderVerse(verseContent);
        }else {
            int numberOfWordsToRemove = (int) (verseWordCount * (percentageMissing * .01));
            return getVerseWithRemovedWords(verseContent, numberOfWordsToRemove, verseWordCount, verseStage);
        }
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
                result = bookName + " " + removeChars(1, chp_verse_num);
                break;
            case 2:
                result = bookName + " " + removeChars(1, chp_verse_num);
                break;
            case 3:
                result = bookName + " " + removeChars(2, chp_verse_num);
                break;
            case 4:
                result = getBookNamePlaceholder(bookName) + " " + chp_verse_num;
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
        return result;
    }

    private String removeChars(int removeCount, String chp_verse_num) {
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
        int length = verseLocation.length();
        StringBuilder sb = new StringBuilder(verseLocation);
        String result = "";
        for(int i = 0; i < length; i++){
            if(sb.charAt(i) == ' '){
                result = sb.toString().substring(0, (i));
                break;
            }
        }
        return result;
    }

    private String getVerseAndChapter(String verseLocation) {
        int length = verseLocation.length();
        StringBuilder sb = new StringBuilder(verseLocation);
        String result = "";
        for(int i = 0; i < length; i++){
            if(sb.charAt(i) == ' '){
                result = sb.toString().substring(i+1);
                break;
            }
        }
        return result;
    }

    private String getBookNamePlaceholder(String bookName) {
        if(bookName != null && bookName.length() > 0) {
            int length = bookName.length();
            StringBuilder result = new StringBuilder(bookName);
            for (int i = 0; i < length; i++) {
                result.setCharAt(i, '_');
            }
            return result.toString();
        }
        return "";
    }

    private String getVerseLocationPlaceholder(String verseLocation) {
        int length = verseLocation.length();
        StringBuilder result = new StringBuilder(verseLocation);
        for(int i = 0;i < length; i++){
            if(result.charAt(i) != ' ' && result.charAt(i) != ':') {
                result.setCharAt(i, '_');
            }
        }
        return result.toString();
    }

    private String getPlaceholderVerse(String verseContent) {
        int length = verseContent.length();
        StringBuilder result = new StringBuilder(verseContent);
        for(int i = 0; i < length; i++){
            if(verseContent.charAt(i) != ' ' && verseContent.charAt(i) != '-' &&
                    verseContent.charAt(i) != '.' && verseContent.charAt(i) != ',' &&
                    verseContent.charAt(i) != ':' && verseContent.charAt(i) != ';' &&
                    verseContent.charAt(i) != '!' && verseContent.charAt(i) != '?') {
                result.setCharAt(i, '_');
            }
        }
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

    private String getVerseWithRemovedWords(String verseContent, int numberOfWordsToRemove, int wordCount, int verseStage) {
        if(wordCount != numberOfWordsToRemove) {
            List<Integer> listOfRandomNumbers = new ArrayList<>();
            int tempRandNum;
            for (int i = 0; i < numberOfWordsToRemove; i++) {
                tempRandNum = generateRandomNumber(wordCount);
                if (listOfRandomNumbers.contains(tempRandNum)) {
                    i--;
                } else {
                    listOfRandomNumbers.add(tempRandNum);
                }
            }

            return removeRandomWords(verseContent, listOfRandomNumbers, verseStage);
        }else{
            return getFirstWordOnlyVerse(verseContent);
        }
    }

    private String removeRandomWords(String verseContent, List<Integer> listOfRandomNumbers, int verseStage) {
        String verse = verseContent;

        for(Integer num : listOfRandomNumbers){
            verse = removeWord(num, verse, verseContent, verseStage);
        }
        return verse;
    }

    private String removeWord(Integer wordNum, String verse, String verseContent, int verseStage) {
        String result = verse;

        String wordHolder = null;
        int wordCount = 0;
        int currentWordSize = 0;
        boolean word = false;
        int endOfLine = verseContent.length() - 1;
        for (int i = 0; i < verseContent.length(); i++) {
            // if the char is a letter, word = true.
            if ((Character.isLetter(verseContent.charAt(i))) && i != endOfLine) {
                currentWordSize++;
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if ((!Character.isLetter(verseContent.charAt(i))) && word) {
                wordCount++;
                if(wordCount == wordNum) {
                    wordHolder = verseContent.substring(i - currentWordSize, i);
                    if(isIgnoreListMatch(wordHolder) && verseStage < 4){
                        wordNum++;
                    }else{
                        result = getWordReplacement(currentWordSize, i, result);
                        return result;
                    }
                }
                word = false;
                currentWordSize = 0;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if ((verseContent.charAt(i) != ' ') && i == endOfLine) {
                wordCount++;
                currentWordSize = 0;
            }
        }
        return result;
    }

    private boolean isIgnoreListMatch(String currentWord) {
        final String[] ignoreList = {"the", "The", "be" ,"and", "of", "too", "it", "i", "I", "that", "That", "for", "For",
                "you", "You", "with", "With", "on", "this", "they", "at", "but", "by", "or", "as", "what", "is",
                "if", "my", "will", "as", "A", "a"};
        int listSize = ignoreList.length;

        for(int i = 0; i < listSize; i++){
            if(currentWord.equals(ignoreList[i])){
                return true;
            }
        }
        return false;
    }

    private String getWordReplacement(int length, int i, String verse) {
        StringBuilder result = new StringBuilder(verse);
        for(int j = 0; j < length; j++){
            i--;
            result.setCharAt(i, '_');
        }

        return result.toString();
    }

    public static int countWords(String s){
        int wordCount = 0;
        boolean word = false;
        int endOfLine = s.length();

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    private int generateRandomNumber(int rangeSize){
        Random generate = new Random();
        return (generate.nextInt(rangeSize) + 1) - 1;
    }
}
