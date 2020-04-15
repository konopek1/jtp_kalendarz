package com.example.kalendarz.activites;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),this,hours,min, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        AdderActivity activity = (AdderActivity) getActivity();
        activity.proccessTimePickerResult(hourOfDay,minute);
    }
}
