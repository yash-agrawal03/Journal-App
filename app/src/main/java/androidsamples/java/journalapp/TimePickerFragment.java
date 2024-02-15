package androidsamples.java.journalapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

  public interface TimeSelected{
    void sendTime(String input);
  }
  public TimeSelected mTimeSelected;
  @NonNull
  public static TimePickerFragment newInstance(Date time) {
    // TODO implement the method
    return null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO implement the method
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    // TODO implement the method
    final Calendar calender= Calendar.getInstance();
    int hour=calender.get(Calendar.HOUR_OF_DAY);
    int minute=calender.get(Calendar.MINUTE);
    return new TimePickerDialog(getActivity(),this,hour,minute,false);

  }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setter(hourOfDay, minute);
    }
    public void setter(int hour, int minute) {
        if(hour<10)
        {
            if(minute<10)
            {
                mTimeSelected.sendTime("0"+Integer.toString(hour)+":0"+Integer.toString(minute));
            }
            else
                mTimeSelected.sendTime("0"+Integer.toString(hour)+":"+Integer.toString(minute));
        }else
        {
            if(minute<10)
            {
                mTimeSelected.sendTime(Integer.toString(hour)+":0"+Integer.toString(minute));
            }
            else
                mTimeSelected.sendTime(Integer.toString(hour)+":"+Integer.toString(minute));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mTimeSelected = (TimeSelected) getTargetFragment();
        }catch(ClassCastException e){

        }
    }
}
