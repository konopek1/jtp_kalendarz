package com.example.kalendarz.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.example.kalendarz.DAO.EventDAO;
import com.example.kalendarz.R;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.utils.DateFormatter;
import com.example.kalendarz.utils.RealmProvider;
import com.example.kalendarz.utils.Validator;
import io.realm.Realm;

import java.util.Calendar;
import java.util.Date;

public class AdderActivity extends AppCompatActivity {

    private EventDAO eventDAO;
    private Realm realm;

    private LinearLayout mNotifyView;
    private TextView mDateView;
    private TextView mNotifyDataView;
    private TextView mTimeFrom;
    private TextView mTimeTo;
    private TextView mNotifyTime;
    private RadioButton mNotifyRadioButton;

    private int dataPickerFor;
    private int timePickerFor;

    private Calendar startDate;
    private Calendar endDate;
    private Calendar notifyDate;
    private EditText mContentInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmProvider.getRealm();
        setContentView(R.layout.activity_adder);
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        notifyDate = Calendar.getInstance();

        eventDAO = EventDAO.getInstance();
        initViews();
    }

    public void onRadioNotifyButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) return;
        switch (view.getId()) {
            case R.id.notify_radio_Yes:
                mNotifyView.setVisibility(View.VISIBLE);
                break;
            case R.id.notify_radio_No:
                mNotifyView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void chooseDate(View view) {
        dataPickerFor = view.getId();
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), getString(R.string.datePicker));
    }

    public void chooseTime(View view) {
        timePickerFor = view.getId();
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), getString(R.string.timePicker));
    }

    public void proccessDataPickerResult(int year, int month, int day) {
        switch (dataPickerFor) {
            case R.id.activity_add_date_textView:
                mDateView.setText(DateFormatter.formatDateDDMMYYYY(year, month, day));
                startDate.set(year, month, day);
                if (endDate.get(Calendar.HOUR_OF_DAY) < startDate.get(Calendar.HOUR_OF_DAY)) {
                    //TODO uproscic sprawdzic tez czy godzina sie zmienia najelpiej wykstraktowac wszystko do mniejszych funkcji
                    endDate.set(year, month, day + 1);
                } else {
                    endDate.set(year, month, day);
                }
                break;
            case R.id.activity_add_date_notify:
                mNotifyDataView.setText(DateFormatter.formatDateDDMMYYYY(year, month, day));
                notifyDate.set(year, month, day);
        }
    }

    public void proccessTimePickerResult(int hourOfDay, int minute) {
        switch (timePickerFor) {
            case R.id.date_from:
                mTimeFrom.setText(DateFormatter.formatTimeHHMM(hourOfDay, minute));
                startDate.set(Calendar.MINUTE, minute);
                startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                break;
            case R.id.date_to:
                mTimeTo.setText(DateFormatter.formatTimeHHMM(hourOfDay, minute));
                endDate.set(Calendar.MINUTE, minute);
                endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                break;
            case R.id.activity_add_time_notify:
                mNotifyTime.setText(DateFormatter.formatTimeHHMM(hourOfDay, minute));
                notifyDate.set(Calendar.MINUTE, minute);
                notifyDate.set(Calendar.HOUR_OF_DAY, hourOfDay);

        }
    }

    public void initViews() {
        mNotifyView = findViewById(R.id.notify_date_layout);
        mNotifyRadioButton = findViewById(R.id.notify_radio_Yes);

        mContentInput = findViewById(R.id.activity_add_opis_input);

        mDateView = findViewById(R.id.activity_add_date_textView);
        mDateView.setText(DateFormatter.getCurrentDateDDMMYYYY());

        mNotifyDataView = findViewById(R.id.activity_add_date_notify);
        mNotifyDataView.setText(DateFormatter.getCurrentDateDDMMYYYY());

        mTimeFrom = findViewById(R.id.date_from);

        mTimeTo = findViewById(R.id.date_to);

        mNotifyTime = findViewById(R.id.activity_add_time_notify);
    }


    public void onCreateButtonClick(View view) {
        if(!validate()) return;
        createEventFromForm();
        finish();
    }
    
    private boolean validate() {
        boolean valid = true;

        valid = Validator.validateTextView("Can't be empty", textView -> textView.getText().toString().trim().length() != 0, mContentInput);

        return valid;
    }
    
    private void createEventFromForm() {
        String content = mContentInput.getText().toString();
        boolean isNotify = mNotifyRadioButton.isChecked();
        Date startTime = null;
        Date endTime = null;

        if(startDate.isSet(Calendar.HOUR_OF_DAY)) startTime = startDate.getTime();
        if(endDate.isSet(Calendar.HOUR_OF_DAY)) endTime = endDate.getTime();

        Event event = new Event(content, startTime, endTime , isNotify);
        eventDAO.save(realm,event);
    }


}
