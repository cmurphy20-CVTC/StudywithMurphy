package edu.cvtc.cmurphy20.studywithmurphy;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class AssignmentActivityViewModel extends ViewModel {

    public static final String ORIGINAL_ASSIGNMENT_TITLE = "edu.cvtc.cmurphy20.studywithmurphy.ORIGINAL_ASSIGNMENT_TITLE";
    public static final String ORIGINAL_ASSIGNMENT_NOTES = "edu.cvtc.cmurphy20.studywithmurphy.ORIGINAL_ASSIGNMENT_NOTES";

    public String mOriginalAssignmentTitle;
    public String mOriginalAssignmentNotes;
    public boolean mIsNewlyCreated = true;

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_ASSIGNMENT_TITLE, mOriginalAssignmentTitle);
        outState.putString(ORIGINAL_ASSIGNMENT_NOTES, mOriginalAssignmentNotes);

    }

    public void restoreState(Bundle inState) {
        mOriginalAssignmentTitle = inState.getString(ORIGINAL_ASSIGNMENT_TITLE);
        mOriginalAssignmentNotes = inState.getString(ORIGINAL_ASSIGNMENT_NOTES);
    }


}
