package com.example.kalendarz.activites;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.kalendarz.R;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog.OnDateSetListener from_dateListener,to_dateListener;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        AdderActivity activity = (AdderActivity) getActivity();

        final Calendar c = Calendar.getInstance();
        int year_now = c.get(Calendar.YEAR);
        int month_now = c.get(Calendar.MONTH);
        int day_now = c.get(Calendar.DAY_OF_MONTH);

        if(!(year_now <= year && month_now <= month && day_now <= dayOfMonth)) {
            Toast.makeText(getContext(),"Wrong date!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (activity != null) {
            activity.proccessDataPickerResult(year,month,dayOfMonth);
        }
    }
}
