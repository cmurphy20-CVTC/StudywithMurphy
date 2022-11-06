package edu.cvtc.cmurphy20.studywithmurphy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.cvtc.cmurphy20.studywithmurphy.DatabaseContract.AssignmentInfoEntry;

import androidx.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Assignments_cmurphy20.db";
    public static final int DATABASE_VERSION = 1;

    public OpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(AssignmentInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(AssignmentInfoEntry.SQL_CREATE_INDEX1);

        DataWorker worker = new DataWorker(db);
        worker.insertAssignments();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
