package edu.cvtc.cmurphy20.studywithmurphy;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import edu.cvtc.cmurphy20.studywithmurphy.DatabaseContract.AssignmentInfoEntry;

public class AssignmentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ASSIGNMENT_ID = "edu.cvtc.cmurphy20.studywithmurphy.ASSIGNMENT_ID";
    public static final String ORIGINAL_ASSIGNMENT_TITLE = "edu.cvtc.cmurphy20.studywithmurphy.ORIGINAL_ASSIGNMENT_TITLE";
    public static final String ORIGINAL_ASSIGNMENT_NOTES = "edu.cvtc.cmurphy20.studywithmurphy.ORIGINAL_ASSIGNMENT_NOTES";
    public static final int LOADER_ASSIGNMENTS = 0;

    public static final int ID_NOT_SET = -1;

    private AssignmentInfo mAssignment = new AssignmentInfo(0, "", "");

    private boolean mIsNewAssignment;
    private boolean mIsCancelling;
    private int mAssignmentId;
    private int mAssignmentTitlePosition;
    private int mAssignmentNotesPosition;
    private String mOriginalAssignmentTitle;
    private String mOriginalAssignmentNotes;

    private EditText mTextAssignmentTitle;
    private EditText mTextAssignmentNotes;
    private OpenHelper mDbOpenHelper;
    private Cursor mAssignmentCursor;

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        mDbOpenHelper = new OpenHelper(this);

        readDisplayStateValues();

        if (savedInstanceState == null) {
            saveOriginalAssignmentValues();
        } else {
            restoreOriginalAssignmentValues(savedInstanceState);
        }

        mTextAssignmentTitle = findViewById(R.id.text_assignment_title);

        mTextAssignmentNotes = findViewById(R.id.text_assignment_notes);

        if (!mIsNewAssignment) {
            LoaderManager.getInstance(this).initLoader(LOADER_ASSIGNMENTS, null, this);
        }

    }

    private void loadAssignmentData() {
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        String selection = AssignmentInfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mAssignmentId)};

        String[] assignmentColumns = {
                AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE,
                AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES
        };

        mAssignmentCursor = db.query(AssignmentInfoEntry.TABLE_NAME, assignmentColumns,
                selection, selectionArgs, null, null, null);
        mAssignmentTitlePosition = mAssignmentCursor.getColumnIndex(AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE);
        mAssignmentNotesPosition = mAssignmentCursor.getColumnIndex(AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES);

        mAssignmentCursor.moveToNext();

        displayAssignment();
    }

    private void displayAssignment() {

        String assignmentTitle = mAssignmentCursor.getString(mAssignmentTitlePosition);
        String assignmentNotes = mAssignmentCursor.getString(mAssignmentNotesPosition);

        mTextAssignmentTitle.setText(assignmentTitle);
        mTextAssignmentNotes.setText(assignmentNotes);
    }


    private void restoreOriginalAssignmentValues(Bundle savedInstanceState) {
        mOriginalAssignmentTitle = savedInstanceState.getString(ORIGINAL_ASSIGNMENT_TITLE);
        mOriginalAssignmentNotes = savedInstanceState.getString(ORIGINAL_ASSIGNMENT_NOTES);
    }

    private void saveOriginalAssignmentValues() {
        if (!mIsNewAssignment) {
            mOriginalAssignmentTitle = mAssignment.getTitle();
            mOriginalAssignmentNotes = mAssignment.getNotes();
        }
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();

        mAssignmentId = intent.getIntExtra(ASSIGNMENT_ID, ID_NOT_SET);

        mIsNewAssignment = mAssignmentId == ID_NOT_SET;
        if (mIsNewAssignment) {
            createNewAssignment();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader (int id, @Nullable Bundle args) {
        CursorLoader loader = null;

        if (id == LOADER_ASSIGNMENTS) {
            loader = createLoaderAssignments();
        }
        return loader;
    }

    private CursorLoader createLoaderAssignments() {
        return new CursorLoader(this) {

            @Override
            public Cursor loadInBackground() {
                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                String selection = AssignmentInfoEntry._ID + "= ?";
                String[] selectionArgs = {Integer.toString(mAssignmentId)};

                String[] assignmentColumns = {
                        AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE,
                        AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES
                };

                return db.query(AssignmentInfoEntry.TABLE_NAME, assignmentColumns, selection, selectionArgs, null, null, null);

            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_ASSIGNMENTS) {
            loadFinishedAssignments(data);
        }
    }

    private void loadFinishedAssignments(Cursor data) {
        mAssignmentCursor = data;

        mAssignmentTitlePosition = mAssignmentCursor.getColumnIndex(AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE);
        mAssignmentNotesPosition = mAssignmentCursor.getColumnIndex(AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES);

        mAssignmentCursor.moveToNext();

        displayAssignment();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_ASSIGNMENTS) {
            if (mAssignmentCursor != null) {
                mAssignmentCursor.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveAssignmentToDatabase(String assignmentTitle, String assignmentNotes) {
        final String selection = AssignmentInfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mAssignmentId)};

        final ContentValues values = new ContentValues();
        values.put(AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE, assignmentTitle);
        values.put(AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES, assignmentNotes);

        AsyncTaskLoader<String> task = new AsyncTaskLoader < String > (this) {
            @Nullable
            @Override
            public String loadInBackground () {

                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                db.update(AssignmentInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                return null;
            }
        } ;

        task.loadInBackground();
    }

    private void storePreviousAssignmentValues() {
        mAssignment.setTitle(mOriginalAssignmentTitle);
        mAssignment.setNotes(mOriginalAssignmentNotes);
    }

    private void saveAssignment() {
        String assignmentTitle = mTextAssignmentTitle.getText().toString();
        String assignmentNotes = mTextAssignmentNotes.getText().toString();

        saveAssignmentToDatabase(assignmentTitle, assignmentNotes);
    }

    private void createNewAssignment() {
        ContentValues values = new ContentValues();

        values.put(AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE, "");
        values.put(AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES, "");

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        mAssignmentId = (int)db.insert(AssignmentInfoEntry.TABLE_NAME, null, values);

    }

    private void deleteAssignmentFromDatabase() {
        final String selection = AssignmentInfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mAssignmentId)};

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {

                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                db.delete(AssignmentInfoEntry.TABLE_NAME, selection, selectionArgs);
                return null;
            }
        };

        task.loadInBackground();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mIsCancelling) {

            if(mIsNewAssignment) {
                deleteAssignmentFromDatabase();
            } else {

                storePreviousAssignmentValues();
            }
        } else {
            saveAssignment();
        }
    }


}