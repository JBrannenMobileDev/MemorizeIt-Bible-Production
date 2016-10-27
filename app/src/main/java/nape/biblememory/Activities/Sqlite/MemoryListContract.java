package nape.biblememory.Activities.Sqlite;

import android.provider.BaseColumns;

public final class MemoryListContract {
    private MemoryListContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class CurrentSetEntry implements BaseColumns {
        public static final String TABLE_NAME = "currentset";
        public static final String PRIMARY_KEY_ID = "_id";
        public static final String COLUMN_NAME_ENTRY_ID = "scriptureid";
        public static final String COLUMN_NAME_VERSE_CONTENT = "versecontent";
        public static final String COLUMN_NAME_MEMORY_STAGE = "memorystage";
        public static final String COLUMN_NAME_MEMORY_SUB_STAGE = "memorysubstage";
        public static final String COLUMN_NAME_COUNT_CORRECT = "correctcount";
        public static final String COLUMN_NAME_COUNT_VIEWED = "viewedcount";
        public static final String COLUMN_NAME_START_DATE = "startdate";
        public static final String COLUMN_NAME_MEMORIZE_DATE = "memorizedate";
        public static final String COLUMN_NAME_REMEMBERED_DATE = "remembereddate";
        public static final String COLUMN_NAME_DATE_LAST_SEEN = "datelastseen";
    }

    public static abstract class LearningSetEntry implements BaseColumns {
        public static final String TABLE_NAME = "learningset";
        public static final String PRIMARY_KEY_ID = "_id";
        public static final String COLUMN_NAME_ENTRY_ID = "scriptureid";
        public static final String COLUMN_NAME_VERSE_CONTENT = "versecontent";
        public static final String COLUMN_NAME_MEMORY_STAGE = "memorystage";
        public static final String COLUMN_NAME_MEMORY_SUB_STAGE = "memorysubstage";
        public static final String COLUMN_NAME_COUNT_CORRECT = "correctcount";
        public static final String COLUMN_NAME_COUNT_VIEWED = "viewedcount";
        public static final String COLUMN_NAME_START_DATE = "startdate";
        public static final String COLUMN_NAME_MEMORIZE_DATE = "memorizedate";
        public static final String COLUMN_NAME_REMEMBERED_DATE = "remembereddate";
        public static final String COLUMN_NAME_DATE_LAST_SEEN = "datelastseen";
    }

    public static abstract class RememberedSetEntry implements BaseColumns {
        public static final String TABLE_NAME = "rememberedset";
        public static final String PRIMARY_KEY_ID = "_id";
        public static final String COLUMN_NAME_ENTRY_ID = "scriptureid";
        public static final String COLUMN_NAME_VERSE_CONTENT = "versecontent";
        public static final String COLUMN_NAME_MEMORY_STAGE = "memorystage";
        public static final String COLUMN_NAME_MEMORY_SUB_STAGE = "memorysubstage";
        public static final String COLUMN_NAME_COUNT_CORRECT = "correctcount";
        public static final String COLUMN_NAME_COUNT_VIEWED = "viewedcount";
        public static final String COLUMN_NAME_START_DATE = "startdate";
        public static final String COLUMN_NAME_MEMORIZE_DATE = "memorizedate";
        public static final String COLUMN_NAME_REMEMBERED_DATE = "remembereddate";
        public static final String COLUMN_NAME_DATE_LAST_SEEN = "datelastseen";
    }

    public static abstract class MemorizedSetEntry implements BaseColumns {
        public static final String TABLE_NAME = "memorizedset";
        public static final String PRIMARY_KEY_ID = "_id";
        public static final String COLUMN_NAME_ENTRY_ID = "scriptureid";
        public static final String COLUMN_NAME_VERSE_CONTENT = "versecontent";
        public static final String COLUMN_NAME_MEMORY_STAGE = "memorystage";
        public static final String COLUMN_NAME_MEMORY_SUB_STAGE = "memorysubstage";
        public static final String COLUMN_NAME_COUNT_CORRECT = "correctcount";
        public static final String COLUMN_NAME_COUNT_VIEWED = "viewedcount";
        public static final String COLUMN_NAME_START_DATE = "startdate";
        public static final String COLUMN_NAME_MEMORIZE_DATE = "memorizedate";
        public static final String COLUMN_NAME_REMEMBERED_DATE = "remembereddate";
        public static final String COLUMN_NAME_DATE_LAST_SEEN = "datelastseen";
    }
}
