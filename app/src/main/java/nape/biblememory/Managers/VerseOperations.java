package nape.biblememory.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nape.biblememory.Models.ScriptureData;
import nape.biblememory.Sqlite.BibleMemoryDbHelper;
import nape.biblememory.Sqlite.MemoryListContract;

/**
 * Created by Jonathan on 6/15/2016.
 */
public class VerseOperations {
    private BibleMemoryDbHelper dbHelper;
    private SQLiteDatabase scriptureDb_writable;
    private SQLiteDatabase scriptureDb_readable;
    private static final String TAG = "VersoOperations";

    public VerseOperations(Context context) {
        dbHelper = new BibleMemoryDbHelper(context);
        scriptureDb_readable = dbHelper.getReadableDatabase();
        scriptureDb_writable = dbHelper.getWritableDatabase();
    }

    public List<ScriptureData> getVerseSet(String tableName) {
        List<ScriptureData> results = new ArrayList<>();
        ScriptureData verseObject;
        Cursor cursor;
        
        switch(tableName){
            case MemoryListContract.CurrentSetEntry.TABLE_NAME:
                cursor = scriptureDb_readable.query(tableName,
                        new String[] { MemoryListContract.CurrentSetEntry.PRIMARY_KEY_ID,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_ENTRY_ID,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_VERSE_CONTENT,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORY_STAGE,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_START_DATE,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_REMEMBERED_DATE,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORIZE_DATE,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_DATE_LAST_SEEN,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_COUNT_CORRECT,
                        MemoryListContract.CurrentSetEntry.COLUMN_NAME_COUNT_VIEWED},
                        null, null, null, null, null);

                if (cursor != null) {
                    int idIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_ENTRY_ID);
                    int vContentIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_VERSE_CONTENT);
                    int stageIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORY_STAGE);
                    int subStageIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE);
                    int startDateIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_START_DATE);
                    int rememberedDateIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_REMEMBERED_DATE);
                    int memorizedDateIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORIZE_DATE);
                    int lastSeenDateIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_DATE_LAST_SEEN);
                    int countCorrectDateIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_COUNT_CORRECT);
                    int countViewedDateIndex = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.COLUMN_NAME_COUNT_VIEWED);
                    int primaryKeyId = cursor.getColumnIndex(MemoryListContract.CurrentSetEntry.PRIMARY_KEY_ID);

                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        verseObject = new ScriptureData();
                        verseObject.setPrimary_key_id(cursor.getInt(primaryKeyId));
                        verseObject.setVerseLocation(cursor.getString(idIndex));
                        verseObject.setVerse(cursor.getString(vContentIndex));
                        verseObject.setMemoryStage(cursor.getInt(stageIndex));
                        verseObject.setMemorySubStage(cursor.getInt(subStageIndex));
                        verseObject.setStartDate(cursor.getString(startDateIndex));
                        verseObject.setRemeberedDate(cursor.getString(rememberedDateIndex));
                        verseObject.setMemorizedDate(cursor.getString(memorizedDateIndex));
                        verseObject.setLastSeenDate(cursor.getString(lastSeenDateIndex));
                        verseObject.setCorrectCount(cursor.getInt(countCorrectDateIndex));
                        verseObject.setViewedCount(cursor.getInt(countViewedDateIndex));
                        results.add(verseObject);
                        cursor.moveToNext();
                    }
                }
                break;


            case MemoryListContract.LearningSetEntry.TABLE_NAME:
                cursor = scriptureDb_readable.query(tableName,
                        new String[] { MemoryListContract.CurrentSetEntry.PRIMARY_KEY_ID,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_ENTRY_ID,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_VERSE_CONTENT,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_STAGE,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_START_DATE,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_REMEMBERED_DATE,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORIZE_DATE,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_DATE_LAST_SEEN,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_COUNT_CORRECT,
                                MemoryListContract.LearningSetEntry.COLUMN_NAME_COUNT_VIEWED},
                        null, null, null, null, null);

                if (cursor != null) {
                    int idIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_ENTRY_ID);
                    int vContentIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_VERSE_CONTENT);
                    int stageIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_STAGE);
                    int subStageIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE);
                    int startDateIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_START_DATE);
                    int rememberedDateIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_REMEMBERED_DATE);
                    int memorizedDateIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORIZE_DATE);
                    int lastSeenDateIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_DATE_LAST_SEEN);
                    int countCorrectDateIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_COUNT_CORRECT);
                    int countViewedDateIndex = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.COLUMN_NAME_COUNT_VIEWED);
                    int primaryKeyId = cursor.getColumnIndex(MemoryListContract.LearningSetEntry.PRIMARY_KEY_ID);

                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        verseObject = new ScriptureData();
                        verseObject.setPrimary_key_id(cursor.getInt(primaryKeyId));
                        verseObject.setVerseLocation(cursor.getString(idIndex));
                        verseObject.setVerse(cursor.getString(vContentIndex));
                        verseObject.setMemoryStage(cursor.getInt(stageIndex));
                        verseObject.setMemorySubStage(cursor.getInt(subStageIndex));
                        verseObject.setStartDate(cursor.getString(startDateIndex));
                        verseObject.setRemeberedDate(cursor.getString(rememberedDateIndex));
                        verseObject.setMemorizedDate(cursor.getString(memorizedDateIndex));
                        verseObject.setLastSeenDate(cursor.getString(lastSeenDateIndex));
                        verseObject.setCorrectCount(cursor.getInt(countCorrectDateIndex));
                        verseObject.setViewedCount(cursor.getInt(countViewedDateIndex));
                        results.add(verseObject);
                        cursor.moveToNext();
                    }
                }
                break;


            case MemoryListContract.MemorizedSetEntry.TABLE_NAME:
                cursor = scriptureDb_readable.query(tableName,
                        new String[] { MemoryListContract.CurrentSetEntry.PRIMARY_KEY_ID,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_ENTRY_ID,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_VERSE_CONTENT,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORY_STAGE,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_START_DATE,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_REMEMBERED_DATE,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORIZE_DATE,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_DATE_LAST_SEEN,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_COUNT_CORRECT,
                                MemoryListContract.MemorizedSetEntry.COLUMN_NAME_COUNT_VIEWED},
                        null, null, null, null, null);

                if (cursor != null) {
                    int idIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_ENTRY_ID);
                    int vContentIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_VERSE_CONTENT);
                    int stageIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORY_STAGE);
                    int subStageIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE);
                    int startDateIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_START_DATE);
                    int rememberedDateIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_REMEMBERED_DATE);
                    int memorizedDateIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORIZE_DATE);
                    int lastSeenDateIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_DATE_LAST_SEEN);
                    int countCorrectDateIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_COUNT_CORRECT);
                    int countViewedDateIndex = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_COUNT_VIEWED);
                    int primaryKeyId = cursor.getColumnIndex(MemoryListContract.MemorizedSetEntry.PRIMARY_KEY_ID);

                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        verseObject = new ScriptureData();
                        verseObject.setPrimary_key_id(cursor.getInt(primaryKeyId));
                        verseObject.setVerseLocation(cursor.getString(idIndex));
                        verseObject.setVerse(cursor.getString(vContentIndex));
                        verseObject.setMemoryStage(cursor.getInt(stageIndex));
                        verseObject.setMemorySubStage(cursor.getInt(subStageIndex));
                        verseObject.setStartDate(cursor.getString(startDateIndex));
                        verseObject.setRemeberedDate(cursor.getString(rememberedDateIndex));
                        verseObject.setMemorizedDate(cursor.getString(memorizedDateIndex));
                        verseObject.setLastSeenDate(cursor.getString(lastSeenDateIndex));
                        verseObject.setCorrectCount(cursor.getInt(countCorrectDateIndex));
                        verseObject.setViewedCount(cursor.getInt(countViewedDateIndex));
                        results.add(verseObject);
                        cursor.moveToNext();
                    }
                }
                break;


            case MemoryListContract.RememberedSetEntry.TABLE_NAME:
                cursor = scriptureDb_readable.query(tableName,
                        new String[] { MemoryListContract.CurrentSetEntry.PRIMARY_KEY_ID,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_ENTRY_ID,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_VERSE_CONTENT,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORY_STAGE,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_START_DATE,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_REMEMBERED_DATE,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORIZE_DATE,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_DATE_LAST_SEEN,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_COUNT_CORRECT,
                                MemoryListContract.RememberedSetEntry.COLUMN_NAME_COUNT_VIEWED},
                        null, null, null, null, null);

                if (cursor != null) {
                    int idIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_ENTRY_ID);
                    int vContentIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_VERSE_CONTENT);
                    int stageIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORY_STAGE);
                    int subStageIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE);
                    int startDateIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_START_DATE);
                    int rememberedDateIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_REMEMBERED_DATE);
                    int memorizedDateIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORIZE_DATE);
                    int lastSeenDateIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_DATE_LAST_SEEN);
                    int countCorrectDateIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_COUNT_CORRECT);
                    int countViewedDateIndex = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.COLUMN_NAME_COUNT_VIEWED);
                    int primaryKeyId = cursor.getColumnIndex(MemoryListContract.RememberedSetEntry.PRIMARY_KEY_ID);

                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        verseObject = new ScriptureData();
                        verseObject.setPrimary_key_id(cursor.getInt(primaryKeyId));
                        verseObject.setVerseLocation(cursor.getString(idIndex));
                        verseObject.setVerse(cursor.getString(vContentIndex));
                        verseObject.setMemoryStage(cursor.getInt(stageIndex));
                        verseObject.setMemorySubStage(cursor.getInt(subStageIndex));
                        verseObject.setStartDate(cursor.getString(startDateIndex));
                        verseObject.setRemeberedDate(cursor.getString(rememberedDateIndex));
                        verseObject.setMemorizedDate(cursor.getString(memorizedDateIndex));
                        verseObject.setLastSeenDate(cursor.getString(lastSeenDateIndex));
                        verseObject.setCorrectCount(cursor.getInt(countCorrectDateIndex));
                        verseObject.setViewedCount(cursor.getInt(countViewedDateIndex));
                        results.add(verseObject);
                        cursor.moveToNext();
                    }
                }
                break;
        }


        return results;
    }

    public Long addVerse(ScriptureData scriptureData, String tableName) {
        ContentValues values = new ContentValues();
        switch(tableName) {
            case MemoryListContract.CurrentSetEntry.TABLE_NAME:
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_ENTRY_ID, scriptureData.getVerseLocation());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_VERSE_CONTENT, scriptureData.getVerse());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORY_STAGE, scriptureData.getMemoryStage());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE, scriptureData.getMemorySubStage());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_COUNT_VIEWED, scriptureData.getViewedCount());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_COUNT_CORRECT, scriptureData.getCorrectCount());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_START_DATE, scriptureData.getStartDate());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_DATE_LAST_SEEN, scriptureData.getLastSeenDate());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_REMEMBERED_DATE, scriptureData.getRemeberedDate());
                values.put(MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORIZE_DATE, scriptureData.getMemorizedDate());
                break;

            case MemoryListContract.LearningSetEntry.TABLE_NAME:
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_ENTRY_ID, scriptureData.getVerseLocation());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_VERSE_CONTENT, scriptureData.getVerse());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_STAGE, scriptureData.getMemoryStage());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE, scriptureData.getMemorySubStage());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_COUNT_VIEWED, scriptureData.getViewedCount());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_COUNT_CORRECT, scriptureData.getCorrectCount());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_START_DATE, scriptureData.getStartDate());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_DATE_LAST_SEEN, scriptureData.getLastSeenDate());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_REMEMBERED_DATE, scriptureData.getRemeberedDate());
                values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORIZE_DATE, scriptureData.getMemorizedDate());
                break;

            case MemoryListContract.RememberedSetEntry.TABLE_NAME:
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_ENTRY_ID, scriptureData.getVerseLocation());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_VERSE_CONTENT, scriptureData.getVerse());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORY_STAGE, scriptureData.getMemoryStage());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE, scriptureData.getMemorySubStage());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_COUNT_VIEWED, scriptureData.getViewedCount());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_COUNT_CORRECT, scriptureData.getCorrectCount());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_START_DATE, scriptureData.getStartDate());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_DATE_LAST_SEEN, scriptureData.getLastSeenDate());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_REMEMBERED_DATE, scriptureData.getRemeberedDate());
                values.put(MemoryListContract.RememberedSetEntry.COLUMN_NAME_MEMORIZE_DATE, scriptureData.getMemorizedDate());
                break;

            case MemoryListContract.MemorizedSetEntry.TABLE_NAME:
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_ENTRY_ID, scriptureData.getVerseLocation());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_VERSE_CONTENT, scriptureData.getVerse());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORY_STAGE, scriptureData.getMemoryStage());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE, scriptureData.getMemorySubStage());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_COUNT_VIEWED, scriptureData.getViewedCount());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_COUNT_CORRECT, scriptureData.getCorrectCount());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_START_DATE, scriptureData.getStartDate());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_DATE_LAST_SEEN, scriptureData.getLastSeenDate());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_REMEMBERED_DATE, scriptureData.getRemeberedDate());
                values.put(MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORIZE_DATE, scriptureData.getMemorizedDate());
                break;

        }
        return scriptureDb_writable.insert(tableName, null, values);
    }

    public Integer removeVerse(String verseLocation, String tableName) {
        String whereClause = MemoryListContract.CurrentSetEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] whereArgs = new String[] {verseLocation};
        return scriptureDb_writable.delete(tableName, whereClause, whereArgs);
    }

    public Integer updateVerse(ScriptureData scripture) {
        ContentValues values = new ContentValues();
        values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_STAGE, scripture.getMemoryStage());
        values.put(MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE, scripture.getMemorySubStage());

        // updating row
        return scriptureDb_writable.update(MemoryListContract.LearningSetEntry.TABLE_NAME, values,
                MemoryListContract.LearningSetEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[] { String.valueOf(scripture.getVerseLocation()) });
    }
}
