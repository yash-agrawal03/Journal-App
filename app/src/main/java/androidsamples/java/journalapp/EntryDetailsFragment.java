package androidsamples.java.journalapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryDetailsFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryDetailsFragment extends Fragment implements DatePickerFragment.DateSelected, TimePickerFragment.TimeSelected {

  UUID entryId;
  private JournalEntry mEntry;
  EntryDetailsViewModel mEntryDetailsViewModel;
  EditText mEditTitle;
  Button bDate,bStart,bEnd,bSave;
  boolean check=true,timer;


  String date, startTime, endTime;
  @Override
  public void sendDate(String date)
  {
    mEntryDetailsViewModel.getDate().setValue(date);
    mEntryDetailsViewModel.getIsDateSelected().setValue(true);
  }
  @Override
  public void sendTime(String time)
  {
    if(!timer)
    {
      mEntryDetailsViewModel.getStartTime().setValue(time);
    }
    else
    {
      mEntryDetailsViewModel.getEndTime().setValue(time);
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    mEntryDetailsViewModel = new ViewModelProvider(this).get(EntryDetailsViewModel.class);
    if(getArguments()!=null)
    {
      check=false;
      entryId=(UUID) getArguments().getSerializable(MainActivity.KEY_ENTRY_ID);
      Log.d(TAG, "Loading entry: " + entryId);
        mEntryDetailsViewModel.loadEntry(entryId);
    }
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_entry_details,container,false);
    mEditTitle=view.findViewById(R.id.edit_title);
    bDate=view.findViewById(R.id.btn_entry_date);
    bStart=view.findViewById(R.id.btn_start_time);
    bEnd=view.findViewById(R.id.btn_end_time);
    bSave=view.findViewById(R.id.btn_save);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if(!check)
    {
        mEntryDetailsViewModel.getEntryLiveData().observe(getViewLifecycleOwner(), entry -> {
            this.mEntry = entry;
            updateUI();
        });
    }
    bDate.setOnClickListener(v -> {
      DialogFragment  dialogFragment=new DatePickerFragment();
      dialogFragment.setTargetFragment(EntryDetailsFragment.this,1);
        dialogFragment.show(getFragmentManager(),"datePicker");
    });

    bStart.setOnClickListener(v -> {
      timer=false;
      DialogFragment  dialogFragment=new TimePickerFragment();
      dialogFragment.setTargetFragment(EntryDetailsFragment.this,1);
      dialogFragment.show(getFragmentManager(),"timePicker");
    });
    bEnd.setOnClickListener(v -> {
      timer=true;
      DialogFragment  dialogFragment=new TimePickerFragment();
      dialogFragment.setTargetFragment(EntryDetailsFragment.this,1);
      dialogFragment.show(getFragmentManager(),"timePicker");
    });

    bSave.setOnClickListener(v -> {
      if(mEditTitle.getText().toString().equals("") || !mEntryDetailsViewModel.getIsDateSelected().getValue()|| bDate.getText().toString().equals("DATE") || bStart.getText().toString().equals("Start Time") || bEnd.getText().toString().equals("End Time"))
        Toast.makeText(getActivity(), "All Details must be filled for saving entry.", Toast.LENGTH_SHORT).show();
      else if(bStart.getText().toString().compareTo(bEnd.getText().toString())>0)
        Toast.makeText(getActivity(), "End Time must be greater than Start Time", Toast.LENGTH_SHORT).show();
      else {
        if(check) {
          mEntry = new JournalEntry(mEditTitle.getText().toString(), bDate.getText().toString(),
                  bStart.getText().toString(), bEnd.getText().toString());
          mEntryDetailsViewModel.insertEntry(mEntry);
        }
        else{
          mEntry.setTitle(mEditTitle.getText().toString());
          mEntry.setDate(bDate.getText().toString());
          mEntry.setStartTime(bStart.getText().toString());
          mEntry.setEndTime(bEnd.getText().toString());
          mEntryDetailsViewModel.saveEntry(mEntry);
        }
        getActivity().onBackPressed();
      }
    });

    // Observe LiveData in ViewModel
    mEntryDetailsViewModel.getDate().observe(getViewLifecycleOwner(), date -> {
      bDate.setText(date);
    });

    mEntryDetailsViewModel.getStartTime().observe(getViewLifecycleOwner(), startTime -> {
      bStart.setText(startTime);
    });

    mEntryDetailsViewModel.getEndTime().observe(getViewLifecycleOwner(), endTime -> {
      bEnd.setText(endTime);
    });
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.d_menu, menu);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.share:  {
        if(check){
          Toast.makeText(getActivity(), "Please save the entry first", Toast.LENGTH_SHORT).show();
        }
        else {
          Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
          sharingIntent.setType("text/plain");
          String shareBody = "Look what I have been up to: " + mEntry.getTitle() + " on " + mEntry.getDate() + " from "+mEntry.getStartTime() + " to " + mEntry.getEndTime();
          sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
          startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        return true;
      }
      case R.id.delete: {
        if(check){
          Toast.makeText(getActivity(), "Please save the entry first", Toast.LENGTH_SHORT).show();
        }
        else {
          new AlertDialog.Builder(getContext())
                  .setTitle("Delete entry")
                  .setMessage("Are you sure you want to delete this entry?")
                  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      mEntryDetailsViewModel.deleteEntry(mEntry);
                      Toast.makeText(getActivity(), "Entry Deleted successfully", Toast.LENGTH_SHORT).show();
                      getActivity().onBackPressed();
                    }
                  })
                  .setNegativeButton(android.R.string.no, null)
                  .setIcon(android.R.drawable.ic_menu_delete)
                  .show();
        }
        return true;
      }
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  private void updateUI() {
    try {
      mEditTitle.setText(mEntry.getTitle());
      bDate.setText(mEntry.getDate());
      bStart.setText(mEntry.getStartTime());
      bEnd.setText(mEntry.getEndTime());
    }
    catch (NullPointerException ignored) {}
  }

}
