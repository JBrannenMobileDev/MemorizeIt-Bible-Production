package nape.biblememory.Activities.DBTApi;

import android.content.Context;

import com.faithcomesbyhearing.dbt.Dbt;
import com.faithcomesbyhearing.dbt.callback.BookCallback;
import com.faithcomesbyhearing.dbt.callback.ChapterCallback;
import com.faithcomesbyhearing.dbt.callback.VerseCallback;
import com.faithcomesbyhearing.dbt.callback.VolumeCallback;
import com.faithcomesbyhearing.dbt.model.Book;
import com.faithcomesbyhearing.dbt.model.Chapter;
import com.faithcomesbyhearing.dbt.model.Verse;
import com.faithcomesbyhearing.dbt.model.Volume;

import java.util.List;

import nape.biblememory.Activities.BaseCallback;
import nape.biblememory.R;

/**
 * Created by Jonathan on 12/4/2016.
 */

public class DBTApi {

    public DBTApi(Context context) {
        Dbt.setApiKey(context.getResources().getString(R.string.dbt_api_key));
    }

    public void getVolumeList(final BaseCallback volumeCallback, String languageCode){
        Dbt.getLibraryVolume(null, "text", null, languageCode, new VolumeCallback() {
            @Override
            public void success(List<Volume> volumes) {
                volumeCallback.onResponse(volumes);
            }

            @Override
            public void failure(Exception e) {
                volumeCallback.onFailure(e);
            }
        });
    }

    public void getVolume(final BaseCallback volumeCallback, String damId){
        Dbt.getLibraryVolume(damId, "text", null, null, new VolumeCallback() {
            @Override
            public void success(List<Volume> volumes) {
                volumeCallback.onResponse(volumes);
            }

            @Override
            public void failure(Exception e) {
                volumeCallback.onFailure(e);
            }
        });
    }

    public void getBooksList(final BaseCallback booksCallback, String damId){
        Dbt.getLibraryBook(damId, new BookCallback() {
            @Override
            public void success(List<Book> books) {
                booksCallback.onResponse(books);
            }

            @Override
            public void failure(Exception e) {
                booksCallback.onFailure(e);
            }
        });
    }

    public void getChapterList(final BaseCallback chaptersCallback, String damId, String bookId){
        Dbt.getLibraryChapter(damId, bookId, new ChapterCallback() {
            @Override
            public void success(List<Chapter> chapters) {
                chaptersCallback.onResponse(chapters);
            }

            @Override
            public void failure(Exception e) {
                chaptersCallback.onFailure(e);
            }
        });
    }

    private void getVfromDBT(final BaseCallback verseCallback, String damId, String bookId,
                          String verseStart, String verseEnd, String chapterId){
        Dbt.getTextVerse(damId, bookId, chapterId, verseStart, verseEnd, new VerseCallback() {
            @Override
            public void success(List<Verse> verses) {
                verseCallback.onResponse(verses);
            }

            @Override
            public void failure(Exception e) {
                verseCallback.onFailure(e);
            }
        });
    }

    public void getVerseList(final BaseCallback verseCallback, String damId, String bookId,
                             String verseStart, String verseEnd, String chapterId){
        getVfromDBT(verseCallback, damId, bookId, verseStart, verseEnd, chapterId);
    }

    public void getVerse(final BaseCallback verseCallback, String damId, String bookId,
                             String verseNum, String chapterId){
        getVfromDBT(verseCallback, damId, bookId, verseNum, null, chapterId);
    }
}
