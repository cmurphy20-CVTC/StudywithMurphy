package edu.cvtc.cmurphy20.studywithmurphy;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.cvtc.cmurphy20.studywithmurphy.DatabaseContract.AssignmentInfoEntry;


public class AssignmentRecyclerAdapter extends RecyclerView.Adapter<AssignmentRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;
    private int mAssignmentTitlePosition;
    private int mAssignmentNotesPosition;
    private int mIdPosition;

    public AssignmentRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(context);

        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if (mCursor != null) {
            mAssignmentTitlePosition = mCursor.getColumnIndex(AssignmentInfoEntry.COLUMN_ASSIGNMENT_TITLE);
            mAssignmentNotesPosition = mCursor.getColumnIndex(AssignmentInfoEntry.COLUMN_ASSIGNMENT_NOTES);
            mIdPosition = mCursor.getColumnIndex(AssignmentInfoEntry._ID);
        }
    }

    public void changeCursor(Cursor cursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = cursor;

        populateColumnPositions();

        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        mCursor.moveToPosition(position);

        String assignmentTitle = mCursor.getString(mAssignmentTitlePosition);
        String assignmentNotes = mCursor.getString(mAssignmentNotesPosition);
        int id = mCursor.getInt(mIdPosition);

        holder.mAssignmentTitle.setText(assignmentTitle);
        holder.mAssignmentNotes.setText(assignmentNotes);
        holder.mId = id;
    }

    @Override
    public int getItemCount() {

        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAssignmentTitle;
        public final TextView mAssignmentNotes;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAssignmentTitle = (TextView)itemView.findViewById(R.id.assignment_title);
            mAssignmentNotes= (TextView)itemView.findViewById(R.id.assignment_notes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AssignmentActivity.class);
                    intent.putExtra(AssignmentActivity.ASSIGNMENT_ID, mId);
                    mContext.startActivity(intent);

                }


            });

        }
    }
}
