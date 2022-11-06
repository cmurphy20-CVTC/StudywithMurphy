package edu.cvtc.cmurphy20.studywithmurphy;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.cvtc.cmurphy20.studywithmurphy.DatabaseContract.AssignmentInfoEntry;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager ourInstance = null;
    private List<AssignmentInfo> mAssignments = new ArrayList<>();

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }

    public List<AssignmentInfo> getAssignments() {
        return mAssignments;
    }

    private static void loadCoursesFromDatabase(Cursor cursor) {

        int listTitlePosition = cursor.getColumnIndex(AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE);

        int listNotesPosition = cursor.getColumnIndex(AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES);

        int idPosition = cursor.getColumnIndex(AssignmentInfoEntry._ID);

        DataManager dm = getInstance();
        dm.mAssignments.clear();

        while (cursor.moveToNext()) {
            String listTitle = cursor.getString(listTitlePosition);
            String listNotes = cursor.getString(listNotesPosition);
            int id = cursor.getInt(idPosition);

            AssignmentInfo list = new AssignmentInfo(id, listTitle, listNotes);

            dm.mAssignments.add(list);
        }

        cursor.close();
    }

    public static void loadFromDatabase(OpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] assignmentColumns = {
                AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE,
                AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES,
                AssignmentInfoEntry._ID};

        String assignmentOrderBy = AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE;

        final Cursor assignmentCursor = db.query(AssignmentInfoEntry.TABLE_NAME, assignmentColumns,
                null, null, null, null, assignmentOrderBy);
        loadCoursesFromDatabase(assignmentCursor);
    }

    public int createNewAssignment() {
        AssignmentInfo assignment = new AssignmentInfo(null, null);
        mAssignments.add(assignment);
        return mAssignments.size();
    }

    public void removeCourse(int index) {
        mAssignments.remove(index);
    }

}
