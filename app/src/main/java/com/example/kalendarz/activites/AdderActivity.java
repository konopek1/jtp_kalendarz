package com.example.kalendarz.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.example.kalendarz.DAO.EventDAO;
import com.example.kalendarz.R;
import com.example.kalendarz.common.Event;
import com.example.kalendarz.fragments.DatePickerFragment;
import com.example.kalendarz.fragments.TimePickerFragment;
import com.example.kalendarz.notifcations.Notify;
import com.example.kalendarz.utils.DateFormatter;
import com.example.kalendarz.utils.RealmProvider;
import com.example.kalendarz.utils.Validator;
import io.realm.Realm;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import java.util.Calendar;
import java.util.Date;

/**
 * Acitivity responsible for adding new Event
 */
public class AdderActivity extends AppCompatActivity {

    private EventDAO eventDAO;
    private Realm realm;
    private Bundle extras;

    private LinearLayout mNotifyView;
    private TextView mDateView;
    private TextView mNotifyDataView;
    private TextView mTimeFrom;
    private TextView mTimeTo;
    private TextView mNotifyTime;
    private RadioButton mNotifyRadioButton;

    private int dataPickerFor;
    private int timePickerFor;

    private boolean isToDo = true;
    private Calendar startDate;
    private Calendar endDate;
    private Calendar notifyDate;
    private EditText mContentInput;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setUp();
        initViews();
    }

    private void setUp() {
        Realm.init(getApplicationContext());
        long date = extras.getLong(MainActivity.DATE_EXTRAS);
        realm = RealmProvider.getRealm();
        setContentView(R.layout.activity_adder);
        startDate = Calendar.getInstance();
        startDate.setTimeInMillis(date);
        startDate.set(Calendar.MINUTE,1);
        endDate = Calendar.getInstance();
        endDate.setTimeInMillis(date);
        endDate.set(Calendar.MINUTE,1);
        notifyDate = Calendar.getInstance();
        notifyDate.setTimeInMillis(date);
        endDate.set(Calendar.MINUTE,1);
        eventDAO = EventDAO.getInstance();
    }

    /**
     * On radio notify button clicked.
     *
     * @param view the view
     */
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
        realm.close();
    }

    /**
     * Choose date.
     * Starts DatePickerFragment
     * @param view the view
     */
    public void chooseDate(View view) {
        dataPickerFor = view.getId();
        DialogFragment newFragment = new DatePickerFragment(extras.getLong(MainActivity.DATE_EXTRAS));
        newFragment.show(getSupportFragmentManager(), getString(R.string.datePicker));
    }

    /**
     * Choose time.
     * Start TimePickerFragment
     * @param view the view
     */
    public void chooseTime(View view) {
        timePickerFor = view.getId();
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), getString(R.string.timePicker));
    }

    /**
     * Proccess data picker result.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    public void proccessDataPickerResult(int year, int month, int day) {
        switch (dataPickerFor) {
            case R.id.activity_add_date_textView:
                proccesDataPickerForDate(year, month, day);
                break;
            case R.id.activity_add_date_notify:
                proccessDataPickerForNotifyDate(year, month, day);
        }
    }

    private void proccessDataPickerForNotifyDate(int year, int month, int day) {
        mNotifyDataView.setText(DateFormatter.formatDateDDMMYYYY(year, month, day));
        notifyDate.set(year, month, day);
    }

    private void proccesDataPickerForDate(int year, int month, int day) {
        mDateView.setText(DateFormatter.formatDateDDMMYYYY(year, month, day));
        startDate.set(year, month, day);
        endDate.set(year, month, day);
    }


    /**
     * Proccess time picker result.
     *
     * @param hourOfDay the hour of day
     * @param minute    the minute
     */
    public void proccessTimePickerResult(int hourOfDay, int minute) {
        switch (timePickerFor) {
            case R.id.date_from:
                mTimeFrom.setText(DateFormatter.formatTimeHHMM(hourOfDay, minute));
                startDate.set(Calendar.MINUTE, minute);
                startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                isToDo = false;
                break;
            case R.id.date_to:
                mTimeTo.setText(DateFormatter.formatTimeHHMM(hourOfDay, minute));
                endDate.set(Calendar.MINUTE, minute);
                endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                isToDo = false;
                break;
            case R.id.activity_add_time_notify:
                mNotifyTime.setText(DateFormatter.formatTimeHHMM(hourOfDay, minute));
                notifyDate.set(Calendar.MINUTE, minute);
                notifyDate.set(Calendar.HOUR_OF_DAY, hourOfDay);

        }
    }

    /**
     * Initi views used by this Activity
     */
    public void initViews() {

        mNotifyView = findViewById(R.id.notify_date_layout);
        mNotifyRadioButton = findViewById(R.id.notify_radio_Yes);

        mContentInput = findViewById(R.id.activity_add_opis_input);

        mDateView = findViewById(R.id.activity_add_date_textView);
        initDateGenericView(mDateView);

        mNotifyDataView = findViewById(R.id.activity_add_date_notify);
        initDateGenericView(mNotifyDataView);

        mTimeFrom = findViewById(R.id.date_from);

        mTimeTo = findViewById(R.id.date_to);

        mNotifyTime = findViewById(R.id.activity_add_time_notify);
    }

    /**
     * Init date generic view.
     *
     * @param view the view
     */
    public void initDateGenericView(TextView view) {
        long timestampFromIntent = extras.getLong(MainActivity.DATE_EXTRAS);
        view.setText(DateFormatter.formatDateDDMMYYYY(timestampFromIntent));
    }


    /**
     * On create button click.
     *
     * @param view the view
     */
    public void onCreateButtonClick(View view) {
        if (!validate()) return;
        createEventFromForm();
        finish();
    }

    private boolean validate() {
        boolean valid = true;

        valid = Validator.validateTextView("Can't be empty", textView -> textView.getText().toString().trim().length() != 0, mContentInput);

        return valid;
    }

    /**
     * @throws RealmPrimaryKeyConstraintException
     * Tworzy event z danych zapisanych w tej aktywności oraz zapisuje do bazy danych
     */
    private void createEventFromForm() throws RealmPrimaryKeyConstraintException {
        String content = mContentInput.getText().toString();
        boolean isNotify = mNotifyRadioButton.isChecked();
        Event event = null;

        if (!isNotify) {
            event = new Event(content, startDate.getTime(), endDate.getTime(), false, isToDo);
        } else {
            Notify notify = new Notify(getApplicationContext(), notifyDate.getTimeInMillis(), "Event notification", content);
            event = new Event(content, startDate.getTime(), endDate.getTime(), isToDo, notify);
        }

        try {
            eventDAO.save(realm, event);
        } catch (RealmException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}
