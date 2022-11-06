package edu.cvtc.cmurphy20.studywithmurphy;

import android.provider.BaseColumns;

public final class DatabaseContract  {

    private DatabaseContract() {}

    public static final class AssignmentInfoEntry implements BaseColumns {

        public static final String TABLE_NAME = "assignment_info";
        public static final String COLUMN_ASSIGNMENT_TITLE = "assignment_title";
        public static final String COLUMN_ASSIGNMENT_NOTES = "assignment_notes";

        public static final String INDEX1 = TABLE_NAME + "_index1";
        public static final String SQL_CREATE_INDEX1 =
                "CREATE INDEX " + INDEX1 + " ON " + TABLE_NAME +
                        "(" +COLUMN_ASSIGNMENT_TITLE + ")";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_ASSIGNMENT_TITLE + " TEXT NOT NULL, " +
                        COLUMN_ASSIGNMENT_NOTES + " TEXT)";
    }
}

