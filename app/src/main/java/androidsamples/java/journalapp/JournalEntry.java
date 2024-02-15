package androidsamples.java.journalapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "journal_table")
public class JournalEntry {
    @PrimaryKey
    @ColumnInfo(name="id")
    @NonNull
    private UUID Uid;

    @ColumnInfo(name="title")
    private String mTitle;

    @ColumnInfo(name="date")
    private String mDate;

    @ColumnInfo(name="start_time")
    private String mStartTime;

    @ColumnInfo(name="end_time")
    private String mEndTime;

    public JournalEntry(@NonNull String title, String date, String startTime, String endTime) {
        this.Uid = UUID.randomUUID();
        this.mTitle = title;
        this.mDate = date;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
    }

    @NonNull
    public UUID getUid() {
        return Uid;
    }

    public void setUid(@NonNull UUID uid) {
        Uid = uid;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        this.mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        this.mEndTime = endTime;
    }
}
