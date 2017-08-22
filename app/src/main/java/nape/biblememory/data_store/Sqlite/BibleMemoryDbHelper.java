package nape.biblememory.data_store.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jonathan on 5/4/2016.
 */
public class BibleMemoryDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RememberIt_Bible.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CURRENT_SET_ENTRIES =
            "CREATE TABLE " + MemoryListContract.CurrentSetEntry.TABLE_NAME + " (" +
                    MemoryListContract.CurrentSetEntry._ID + " INTEGER PRIMARY KEY," +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_DATE_LAST_SEEN + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORIZE_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_REMEMBERED_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_VERSE_CONTENT + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORY_STAGE + INT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE + INT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_COUNT_VIEWED + INT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_COUNT_CORRECT + INT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_BOOK_NAME + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_CHAPTER + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_VERSE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_NUM_OF_VERSES_IN_CHAPTER + INT_TYPE + COMMA_SEP +
                    MemoryListContract.CurrentSetEntry.COLUMN_NAME_VERSION + TEXT_TYPE + " )";
    private static final String SQL_CREATE_REMEMBERED_SET_ENTRIES =
            "CREATE TABLE " + MemoryListContract.ForgottenSetEntry.TABLE_NAME + " (" +
                    MemoryListContract.ForgottenSetEntry._ID + " INTEGER PRIMARY KEY," +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_DATE_LAST_SEEN + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_MEMORIZE_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_REMEMBERED_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_VERSE_CONTENT + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_MEMORY_STAGE + INT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE + INT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_COUNT_VIEWED + INT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_COUNT_CORRECT + INT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_BOOK_NAME + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_CHAPTER + TEXT_TYPE  + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_VERSE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_NUM_OF_VERSES_IN_CHAPTER + INT_TYPE + COMMA_SEP +
                    MemoryListContract.ForgottenSetEntry.COLUMN_NAME_VERSION + TEXT_TYPE +" )";
    private static final String SQL_CREATE_MEMORIZED_SET_ENTRIES =
            "CREATE TABLE " + MemoryListContract.MemorizedSetEntry.TABLE_NAME + " (" +
                    MemoryListContract.MemorizedSetEntry._ID + " INTEGER PRIMARY KEY," +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_DATE_LAST_SEEN + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORIZE_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_REMEMBERED_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_VERSE_CONTENT + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORY_STAGE + INT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE + INT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_COUNT_VIEWED + INT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_COUNT_CORRECT + INT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_BOOK_NAME + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_CHAPTER + TEXT_TYPE  + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_VERSE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_NUM_OF_VERSES_IN_CHAPTER + INT_TYPE + COMMA_SEP +
                    MemoryListContract.MemorizedSetEntry.COLUMN_NAME_VERSION + TEXT_TYPE +" )";
    private static final String SQL_CREATE_LEARNING_SET_ENTRIES =
            "CREATE TABLE " + MemoryListContract.LearningSetEntry.TABLE_NAME + " (" +
                    MemoryListContract.LearningSetEntry._ID + " INTEGER PRIMARY KEY," +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_DATE_LAST_SEEN + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORIZE_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_REMEMBERED_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_VERSE_CONTENT + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_STAGE + INT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_MEMORY_SUB_STAGE + INT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_COUNT_VIEWED + INT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_COUNT_CORRECT + INT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_BOOK_NAME + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_CHAPTER + TEXT_TYPE  + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_VERSE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_NUM_OF_VERSES_IN_CHAPTER + INT_TYPE + COMMA_SEP +
                    MemoryListContract.LearningSetEntry.COLUMN_NAME_VERSION + TEXT_TYPE + " )";




    private static final String SQL_DELETE_CURRENT_SET_ENTRIES =
            "DROP TABLE IF EXISTS " + MemoryListContract.CurrentSetEntry.TABLE_NAME;
    private static final String SQL_DELETE_FORGOTTEN_SET_ENTRIES =
            "DROP TABLE IF EXISTS " + MemoryListContract.ForgottenSetEntry.TABLE_NAME;
    private static final String SQL_DELETE_MEMORIZED_SET_ENTRIES =
            "DROP TABLE IF EXISTS " + MemoryListContract.MemorizedSetEntry.TABLE_NAME;
    private static final String SQL_DELETE_LEARNING_SET_ENTRIES =
            "DROP TABLE IF EXISTS " + MemoryListContract.LearningSetEntry.TABLE_NAME;

    public BibleMemoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CURRENT_SET_ENTRIES);
        db.execSQL(SQL_CREATE_LEARNING_SET_ENTRIES);
        db.execSQL(SQL_CREATE_REMEMBERED_SET_ENTRIES);
        db.execSQL(SQL_CREATE_MEMORIZED_SET_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CURRENT_SET_ENTRIES);
        db.execSQL(SQL_DELETE_FORGOTTEN_SET_ENTRIES);
        db.execSQL(SQL_DELETE_MEMORIZED_SET_ENTRIES);
        db.execSQL(SQL_DELETE_LEARNING_SET_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
