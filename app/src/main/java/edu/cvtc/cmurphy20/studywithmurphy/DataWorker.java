package edu.cvtc.cmurphy20.studywithmurphy;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import edu.cvtc.cmurphy20.studywithmurphy.DatabaseContract.AssignmentInfoEntry;

public class DataWorker {

    private SQLiteDatabase mDb;

    public DataWorker(SQLiteDatabase db) {
        mDb = db;
    }

    private void insertCourse(String title, String notes) {
        ContentValues values = new ContentValues();
        values.put(AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE, title);
        values.put(AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES, notes);

        long newRowId = mDb.insert(AssignmentInfoEntry.TABLE_NAME, null, values);
    }

    public void insertAssignments() {
        insertCourse("Hello World Application", "Print hello world to the screen.");
        insertCourse("Debugging", "Figure out the current issue.");
        insertCourse("Odd of Even", "Use the proper function");
        insertCourse("Retirement Calculator", "What is the correct formula?");
        insertCourse("Drawables", "Follow specs closely.");
        insertCourse("Final Project Idea", "Feel free to contact me for ideas.");
        insertCourse("Clickable Images Part 1", "This will help with Part 2.");
        insertCourse("Clickable Images Part 2", "Make sure to delete comments.");
        insertCourse("Clickable Images Part 3", "Input controls.");
        insertCourse("Clickable Images Part 4", "Menus and pickers.");
        insertCourse("About Me", "Have fun with this one.");
        insertCourse("Recycler View", "This assignment may be extra credit.");
        insertCourse("ITSD Courses", "Give yourself plenty of time to do this.");
        insertCourse("Final Project", "You got this!");
    }
}



