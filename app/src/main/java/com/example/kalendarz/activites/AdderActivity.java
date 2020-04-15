package com.example.kalendarz.activites;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.example.kalendarz.R;
import com.example.kalendarz.utils.DateFormatter;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.utils.RealmProvider;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import java.util.Calendar;

public class AdderActivity extends AppCompatActivity {

    private Realm realm;

    private LinearLayout mNotifyView;
    private TextView mDateView;
    private TextView mNotifyDataView;
    private TextView mTimeFrom;
    private TextView mTimeTo;
    private TextView mNotifyTime;

    private int dataPickerFor;
    private int timePickerFor;

    private Calendar startDate;
    private Calendar endDate;
    private Calendar notifyDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        notifyDate = Calendar.getInstance();

        realm = RealmProvider.getRealm();
        initViews();
    }

    public void onRadioNotifyButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        if(!checked) return;
        switch (view.getId()){
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
        realm.close();
        super.onDestroy();
    }

    public void chooseDate(View view) {
        dataPickerFor = view.getId();
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),getString(R.string.datePicker));
    }

    public void chooseTime(View view) {
        timePickerFor = view.getId();
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(),getString(R.string.timePicker));
    }

    public void proccessDataPickerResult(int year,int month, int day) {
        switch (dataPickerFor){
            case R.id.activity_add_date_textView:
                mDateView.setText(DateFormatter.formatDateDDMMYYYY(year, month, day));
                startDate.set(year, month, day);
                if(endDate.get(Calendar.HOUR_OF_DAY) < startDate.get(Calendar.HOUR_OF_DAY)) {endDate.set(year, month, day+1);} else{ endDate.set(year, month, day);}
                break;
            case  R.id.activity_add_date_notify:
                mNotifyDataView.setText(DateFormatter.formatDateDDMMYYYY(year, month, day));
                notifyDate.set(year, month, day);
        }
    }

    public void proccessTimePickerResult(int hourOfDay, int minute) {

        switch (timePickerFor){
            case R.id.date_from:
                mTimeFrom.setText(DateFormatter.formatTimeHHMM(hourOfDay, minute));
                startDate.set(Calendar.MINUTE,minute);
                startDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                break;
            case R.id.date_to:
                mTimeTo.setText(DateFormatter.formatTimeHHMM(hourOfDay,minute));
                endDate.set(Calendar.MINUTE,minute);
                endDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                break;
            case R.id.activity_add_time_notify:
                mNotifyTime.setText(DateFormatter.formatTimeHHMM(hourOfDay,minute));
                notifyDate.set(Calendar.MINUTE,minute);
                notifyDate.set(Calendar.HOUR_OF_DAY,hourOfDay);

        }
    }

    public void initViews() {
        mNotifyView = findViewById(R.id.notify_date_layout);

        mDateView = findViewById(R.id.activity_add_date_textView);
        mDateView.setText(DateFormatter.getCurrentDateDDMMYYYY());

        mNotifyDataView = findViewById(R.id.activity_add_date_notify);
        mNotifyDataView.setText(DateFormatter.getCurrentDateDDMMYYYY());

        mTimeFrom = findViewById(R.id.date_from);
        mTimeFrom.setText(getString(R.string.None));

        mTimeTo = findViewById(R.id.date_to);
        mTimeTo.setText(getString(R.string.None));

        mNotifyTime = (TextView) findViewById(R.id.activity_add_time_notify);
        mNotifyTime.setText(getString(R.string.None));
    }


    public void creatEvent(View view) {
        String content = ((EditText) findViewById(R.id.activity_add_opis_input)).getText().toString();
        boolean isNotify = ((RadioButton) findViewById(R.id.notify_radio_Yes)).isChecked();

        Event event = new Event(content, startDate.getTime(), endDate.getTime(), isNotify);
        event.save(realm);
        finish();
    }

}
