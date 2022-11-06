package edu.cvtc.cmurphy20.studywithmurphy;

import android.os.Parcel;
import android.os.Parcelable;

public class AssignmentInfo implements Parcelable {

    private String mTitle;
    private String mNotes;
    private int mId;

    public AssignmentInfo(String title, String notes) {
        mTitle = title;
        mNotes = notes;
    }

    public AssignmentInfo(int id, String title, String notes) {
        mId = id;
        mTitle = title;
        mNotes = notes;
    }

    public int getId() { return mId;}

    public String getTitle() { return mTitle;}

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    private String getCompareKey() {
        return mTitle + "|" + mNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentInfo that = (AssignmentInfo) o;
        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    @Override
    public String toString() {
        return getCompareKey();
    }

    protected AssignmentInfo(Parcel parcel) {
        mTitle = parcel.readString();
        mNotes = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mTitle);
        parcel.writeString(mNotes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AssignmentInfo> CREATOR = new Creator<AssignmentInfo>() {
        @Override
        public AssignmentInfo createFromParcel(Parcel parcel) {
            return new AssignmentInfo(parcel);
        }

        @Override
        public AssignmentInfo[] newArray(int size) {
            return new AssignmentInfo[size];
        }
    };
}


