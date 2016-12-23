package com.example.shubham.taskhand.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.shubham.taskhand.R;
import com.example.shubham.taskhand.alarm.AlarmDetails;
import com.example.shubham.taskhand.alarm.AlarmHelper;
import com.example.shubham.taskhand.constants.StringConstants;
import com.example.shubham.taskhand.database.TaskHandDBHelper;
import com.example.shubham.taskhand.utility.AppContext;
import com.example.shubham.taskhand.utility.Logger;
import com.example.shubham.taskhand.utility.TaskHandHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Task Detail Class for storing detail of task
 * setting reminder for a particular ic_time and date
 * with notification and ringtone
 */
public class TaskHandDetailActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = TaskHandDetailActivity.class.getSimpleName();
    private Calendar chosenCalendar = Calendar.getInstance();
    private Toolbar mDetailToolbar;
    private EditText mTaskHandDateEditText;
    private EditText mTaskHandTimeEditText;
    private EditText mTaskHandTaskNameEditText;
    private EditText mTaskHandDetailEditText;
    private Button mTaskHandSaveButton;
    private Calendar mCalender;
    private Spinner mTaskHandPrioritySpinner;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond = 0;
    private int mTaskId;
    private String mPriority;
    private String mName;
    private String mDetail;
    private long datetime;

    private Intent mTaskHandIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_hand_detail);
        //setting toolbar
        mDetailToolbar = (Toolbar) findViewById(R.id.task_list_toolBar);
        setSupportActionBar(mDetailToolbar);
        try {
            getSupportActionBar().setTitle("Detail");
            getSupportActionBar().setLogo(R.drawable.ic_pirates_colour);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Logger.error(TAG, "Error in Title setting", e);
        }
        //initialising the views
        initViews();

        //setting adapter for spinner
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticSpinnerAdapter = ArrayAdapter
                .createFromResource(this, R.array.priority,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mTaskHandPrioritySpinner.setAdapter(staticSpinnerAdapter);

        //setting data from bundle if data is already present
        mTaskHandIntent = getIntent();
        if (mTaskHandIntent.getExtras() != null) {
            Bundle getTaskBundle = mTaskHandIntent.getExtras();
            Logger.debug("Task Bundle", " " + getTaskBundle);
            String name = getTaskBundle.getString(StringConstants.TASK_NAME);
            String detail = getTaskBundle.getString(StringConstants.TASK_DETAIL);
            String priority = getTaskBundle.getString(StringConstants.TASK_PRIORITY);
            long reminderDateTime = getTaskBundle.getLong(StringConstants.TASK_REMINDER_TIME, 0);
            mTaskId = getTaskBundle.getInt(StringConstants.TASK_ID, 0);

            //setting date
            SimpleDateFormat reminderDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat reminderTimeFormat = new SimpleDateFormat("HH-mm");

            datetime = reminderDateTime;
            chosenCalendar.setTimeInMillis(datetime);
            mTaskHandTaskNameEditText.setText(name);
            mTaskHandDetailEditText.setText(detail);
            mTaskHandDateEditText.setText(reminderDateFormat.format(reminderDateTime));
            mTaskHandTimeEditText.setText(reminderTimeFormat.format(reminderDateTime));
            mTaskHandPrioritySpinner.setAdapter(staticSpinnerAdapter);
            try {
                if (!TextUtils.isEmpty(priority)) {
                    int spinnerPosition = staticSpinnerAdapter.getPosition(priority);
                    mTaskHandPrioritySpinner.setSelection(spinnerPosition);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Logger.error(TAG, "spinner exception", e);
            }
        }

        //calling listeners for respective views
        mTaskHandDateEditText.setOnClickListener(this);
        mTaskHandTimeEditText.setOnClickListener(this);
        mTaskHandPrioritySpinner.setOnItemSelectedListener(this);
        mTaskHandSaveButton.setOnClickListener(this);
    }

    /**
     * for initialising views
     */
    private void initViews() {
        mTaskHandDateEditText = (EditText) findViewById(R.id.task_hand_date_editText);
        mTaskHandTimeEditText = (EditText) findViewById(R.id.task_hand_time_editText);
        mTaskHandDetailEditText = (EditText) findViewById(R.id.task_hand_detail_editText);
        mTaskHandTaskNameEditText = (EditText) findViewById(R.id.task_hand_name_editText);
        mTaskHandSaveButton = (Button) findViewById(R.id.task_hand_save_button);
        mTaskHandPrioritySpinner = (Spinner) findViewById(R.id.task_hand_priority_spinner);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_hand_date_editText:
                mCalender = Calendar.getInstance();
                mYear = mCalender.get(Calendar.YEAR);
                mMonth = mCalender.get(Calendar.MONTH);
                mDay = mCalender.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog mTaskHandDatePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Display Selected date in date_editText
                                mTaskHandDateEditText.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                chosenCalendar.set(year, monthOfYear, dayOfMonth);
                                datetime = chosenCalendar.getTimeInMillis();
                            }
                        }, mYear, mMonth, mDay);
                mTaskHandDatePickerDialog.show();
                break;
            case R.id.task_hand_time_editText:
                mCalender = Calendar.getInstance();
                mHour = mCalender.get(Calendar.HOUR_OF_DAY);
                mMinute = mCalender.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog mTaskHandTimePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Display Selected ic_time in time_EditText
                                mTaskHandTimeEditText.setText(hourOfDay + ":" + minute);
                                chosenCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                chosenCalendar.set(Calendar.MINUTE, minute);
                                chosenCalendar.set(Calendar.SECOND, mSecond);
                                datetime = chosenCalendar.getTimeInMillis();
                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.US);
                                Logger.debug("ic_time", "" + simpleDateFormat.format(datetime));
                            }
                        }, mHour, mMinute, true);
                mTaskHandTimePickerDialog.show();
                break;

            case R.id.task_hand_save_button:
                //when new task has to be saved
                mName = mTaskHandTaskNameEditText.getText().toString();
                mDetail = mTaskHandDetailEditText.getText().toString();
                if (mTaskHandIntent.getExtras() == null) {
                    long addTaskReturn =
                            TaskHandDBHelper.addTask(mName, mDetail, mPriority, datetime);
                    if (addTaskReturn > 0) {
                        if (datetime != 0) {
                             AlarmHelper.setOneTimeExactAlarm(TaskHandHelper.createFakeURI((int) addTaskReturn),
                                    setAlarmDetailValues());

                        }
                        TaskHandHelper.toastShort(AppContext.getContext()
                                .getResources().getString(R.string.one_task_added));
                        finish();
                    } else {
                        Logger.debug("Data Base: adding", " unsuccessful");
                        TaskHandHelper.toastShort("Please Enter Unique Name of Task");
                    }

                }
                //when Task has to be updated
                else {
                    boolean updateValue = TaskHandDBHelper.updateTask(mTaskId, mName, mDetail,
                            mPriority, datetime);
                    if (updateValue) {
                        AlarmHelper.cancelAlarm(TaskHandHelper.createFakeURI(mTaskId));
                        AlarmHelper.setOneTimeExactAlarm(TaskHandHelper.createFakeURI(mTaskId),
                                setAlarmDetailValues());
                        TaskHandHelper.toastShort(AppContext.getContext()
                                .getResources().getString(R.string.task_updated));
                    } else {
                        Logger.debug("Data Base: updating", " unsuccessful");
                        TaskHandHelper.toastShort("No task updated");
                    }
                    finish();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //nothing to be done here
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        mPriority = (String) adapterView.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //nothing to be done here
    }

    /**
     * Method for setting the Alarm Details which has to be passed to Alarm Helper
     * class for setting Alarm and Notification
     *
     * @return : AlarmDetail Class object which has details of task
     */
    private AlarmDetails setAlarmDetailValues() {
        AlarmDetails outDetails = new AlarmDetails();
        outDetails.setAlarmNotificationTitle(mName);
        outDetails.setAlarmNotificationMsg(mDetail);
        outDetails.setTriggerTime(datetime);

        return outDetails;
    }
}