package nape.biblememory.view_layer.fragments.presenters;

import io.realm.Realm;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.view_layer.fragments.interfaces.MemorizedVerseReviewFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MemorizedVerseReviewInterface;

/**
 * Created by jbrannen on 9/12/17.
 */

public class MemorizedVerseReviewPresenter implements MemorizedVerseReviewInterface {

    private MemorizedVerseReviewFragmentInterface fragment;
    private MemorizedVerse verse;
    private String[] words;
    private int wordIndex;
    private int correctCount;
    private int incorrectCount;
    private Realm realm;

    public MemorizedVerseReviewPresenter(MemorizedVerseReviewFragmentInterface fragment){
        this.fragment = fragment;
        wordIndex = 0;
        correctCount = 0;
        incorrectCount = 0;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void fetchData(String verseLocation) {
        verse = realm.where(MemorizedVerse.class).equalTo("verseLocation", verseLocation).findFirst();
        words = countWordsUsingSplit(verse.getVerse());
        fragment.onDataReceived(String.valueOf(words.length));
    }

    @Override
    public void onNewCharInput(char c) {
        if(wordIndex < words.length) {
            if (Character.toLowerCase(c) == Character.toLowerCase(getFirstLetterOfWord(words[wordIndex]))) {
                fragment.onCorrect(words[wordIndex]);
                correctCount++;
                incorrectCount = 0;
                fragment.updateCorrectCount(correctCount);
                wordIndex++;
            } else {
                incorrectCount++;
                if(incorrectCount >= 4){
                    incorrectCount = 0;
                }
                fragment.onIncorrect(words[wordIndex], incorrectCount);
                if(incorrectCount == 3) {
                    wordIndex++;
                }
            }
            if(wordIndex == words.length){
                fragment.onReviewComplete(correctCount, words.length);
                fragment.hideKeyboard();
            }
        }
    }

    @Override
    public void resetReview() {
        wordIndex = 0;
        correctCount = 0;
    }

    @Override
    public void onReMemorized() {
        fragment.onUpdateReMemorizedVerse(verse);
    }

    @Override
    public void updateMemorizedVerse(final String date) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MemorizedVerse realmVerse = realm.where(MemorizedVerse.class).equalTo("verseLocation", verse.getVerseLocation()).findFirst();
                realmVerse.setLastSeenDate(date);
            }
        });
        fragment.updateMemorizedVerse(verse, date);
    }

    @Override
    public void onStop() {
        realm.close();
    }

    @Override
    public void updateMemorizedVerseToFalse() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MemorizedVerse realmVerse = realm.where(MemorizedVerse.class).equalTo("verseLocation", verse.getVerseLocation()).findFirst();
                realmVerse.setForgotten(true);
            }
        });
        fragment.updateMemorizedVerseToForgotten(verse);
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
}
