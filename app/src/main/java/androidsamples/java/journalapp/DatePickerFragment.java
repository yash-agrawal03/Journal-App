package androidsamples.java.journalapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
  private static final String TAG = "DatePickerFragment";
  public interface DateSelected{
    void sendDate(String input);
  }
  public DateSelected mdateSelected;
  @NonNull
  public static DatePickerFragment newInstance(Date date) {
    // TODO implement the method
    DatePickerFragment  datePickerFragment=new DatePickerFragment();
    Bundle args=new Bundle();
    args.putSerializable(TAG,date);
    datePickerFragment.setArguments(args);
    return datePickerFragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // TODO implement the method
    final Calendar calender= Calendar.getInstance();
    int year=calender.get(Calendar.YEAR);
    int month=calender.get(Calendar.MONTH);
    int day=calender.get(Calendar.DAY_OF_MONTH);

    return new DatePickerDialog(getActivity(),this,year,month,day);

  }
  public void onDateSet(DatePicker view, int yy, int mm, int dd) {
    setter(yy, mm+1, dd);
  }

  public void setter(int year, int month, int day) {
    String final_date="";
    Calendar c = Calendar.getInstance();
    c.set(year,month-1,day);
    int ans=c.get(Calendar.DAY_OF_WEEK);
    String[] weekDays= {"SUN","MON","TUE","WED","THU","FRI","SAT"};
    String[] months= {"JAN","FEB","MAR","APR","MAY","JUN","JULY","AUG","SEP","OCT","NOV","DEC"};
    mdateSelected.sendDate(weekDays[ans-1]+", "+months[month-1]+" "+day+", "+year);
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    try{
      mdateSelected = (DateSelected) getTargetFragment();
    }catch(ClassCastException e){

    }
  }


}
