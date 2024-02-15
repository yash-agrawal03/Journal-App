package androidsamples.java.journalapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.UUID;

public class EntryDetailsViewModel extends androidx.lifecycle.ViewModel{
    private final JournalRepository mRepository;
    private final MutableLiveData<UUID> entryIdLiveData = new MutableLiveData<>();

    public EntryDetailsViewModel()
    {
        mRepository= JournalRepository.getInstance();
    }
    void saveEntry(JournalEntry entry)
    {
        mRepository.update(entry);
    }
    void loadEntry(UUID entryId)
    {
        entryIdLiveData.setValue(entryId);
    }
    void insertEntry(JournalEntry entry)
    {
        mRepository.insert(entry);
    }

    void deleteEntry(JournalEntry entry)
    {
        mRepository.delete(entry);
    }

    LiveData<JournalEntry> getEntryLiveData()
    {
        return Transformations.switchMap(entryIdLiveData, mRepository::getEntry);
    }

    private MutableLiveData<Boolean> isDateSelected;
    private MutableLiveData<String> date;
    private MutableLiveData<String> startTime;
    private MutableLiveData<String> endTime;

    public MutableLiveData<String> getDate() {
        if (date == null) {
            date = new MutableLiveData<String>();
        }
        return date;
    }

    public MutableLiveData<Boolean> getIsDateSelected() {
        if (isDateSelected == null) {
            isDateSelected = new MutableLiveData<Boolean>();
            isDateSelected.setValue(false);
        }
        return isDateSelected;
    }

    public MutableLiveData<String> getStartTime() {
        if (startTime == null) {
            startTime = new MutableLiveData<String>();
        }
        return startTime;
    }

    public MutableLiveData<String> getEndTime() {
        if (endTime == null) {
            endTime = new MutableLiveData<String>();
        }
        return endTime;
    }

}
