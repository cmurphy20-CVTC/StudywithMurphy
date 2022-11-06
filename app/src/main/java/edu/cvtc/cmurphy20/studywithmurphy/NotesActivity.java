package edu.cvtc.cmurphy20.studywithmurphy;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import edu.cvtc.cmurphy20.studywithmurphy.DatabaseContract.AssignmentInfoEntry;


public class NotesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ASSIGNMENTS = 0;
    private boolean mIsCreated = false;


    private OpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mAssignmentsLayoutManager;
    private AssignmentRecyclerAdapter mAssignmentRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        mDbOpenHelper = new OpenHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesActivity.this, AssignmentActivity.class));
            }
        });

        initializeDisplayContent();
    }

    private void initializeDisplayContent() {

        DataManager.loadFromDatabase(mDbOpenHelper);

        mRecyclerItems = (RecyclerView)  findViewById(R.id.list_items);
        mAssignmentsLayoutManager = new LinearLayoutManager(this);

        mAssignmentRecyclerAdapter = new AssignmentRecyclerAdapter(this, null);

        displayAssignment();
    }

    private void displayAssignment() {
        mRecyclerItems.setLayoutManager(mAssignmentsLayoutManager);
        mRecyclerItems.setAdapter(mAssignmentRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).initLoader(LOADER_ASSIGNMENTS, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_home) {
            Intent intent = new Intent(NotesActivity.this, HomeActivity.class);
            startActivity(intent);
            return true;
        }

        if( id == R.id.action_email) {
            Intent intent = new Intent(NotesActivity.this, EmailActivity.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader = null;

        if (id == LOADER_ASSIGNMENTS) {
            loader = new CursorLoader(this) {
                @Override
                public Cursor loadInBackground() {
                    mIsCreated = true;

                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                    String[] assignmentColumns = {
                            AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE,
                            AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES,
                            AssignmentInfoEntry._ID};

                    String assignmentByOrder = AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE;

                    return db.query(AssignmentInfoEntry.TABLE_NAME, assignmentColumns,
                            null, null, null, null, assignmentByOrder);
                }
            };
        }

        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_ASSIGNMENTS && mIsCreated) {
            mAssignmentRecyclerAdapter.changeCursor(data);
            mIsCreated = false;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_ASSIGNMENTS) {
            mAssignmentRecyclerAdapter.changeCursor(null);
        }
    }
}


