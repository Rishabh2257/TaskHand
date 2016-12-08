package com.example.shubham.taskh.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shubham.taskh.DataBase.TaskHandDBHelper;
import com.example.shubham.taskh.R;
import com.example.shubham.taskh.Utility.AppContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Task Detail Class for storing detail of task
 * setting reminder for a particular time and date
 */
public class TaskHandDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    Calendar chosenCalendar = Calendar.getInstance();
    private Toolbar mDetailToolbar;
    private TextView txt;
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
    private int mTaskId;
    private String mPriority;
    private String mName;
    private String mDetail;
    private TaskHandDBHelper mTaskHandDBHelper;
    private SQLiteDatabase mSqLiteDatabase;
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
            getSupportActionBar().setLogo(R.drawable.pirates_colour);
        } catch (NullPointerException e) {
            e.printStackTrace();
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
            Log.e("Task Bundle", " " + getTaskBundle);
            String name = getTaskBundle.getString("taskName");
            String detail = getTaskBundle.getString("taskDetail");
            String priority = getTaskBundle.getString("taskPriority");
            long reminderDateTime = getTaskBundle.getLong("taskReminder", 0);
            mTaskId = getTaskBundle.getInt("taskId", 0);
            //setting date
            SimpleDateFormat reminderDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat reminderTimeFormat = new SimpleDateFormat("HH-mm");
            Log.e("Date", "" + reminderDateFormat.format(reminderDateTime));
            Log.e("time", "" + reminderTimeFormat.format(reminderDateTime));
            Log.e("Name", "" + name);
            Log.e("Detail", "" + detail);
            Log.e("Priority", "" + priority);
            Log.e("id", "" + mTaskId);
            datetime=reminderDateTime;
            Log.e("DATE_TIME",""+datetime);
            chosenCalendar.setTimeInMillis(datetime);
            mTaskHandTaskNameEditText.setText(name);
            mTaskHandDetailEditText.setText(detail);
            mTaskHandDateEditText.setText(reminderDateFormat.format(reminderDateTime));
            mTaskHandTimeEditText.setText(reminderTimeFormat.format(reminderDateTime));
            mTaskHandPrioritySpinner.setAdapter(staticSpinnerAdapter);
            try {
                if (!priority.equals(null)) {
                    int spinnerPosition = staticSpinnerAdapter.getPosition(priority);
                    mTaskHandPrioritySpinner.setSelection(spinnerPosition);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e("spinner exception", " " + e);
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
        txt = (TextView) findViewById(R.id.task_hand_name_textView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_hand_date_editText:
                mCalender = Calendar.getInstance();
                mYear = mCalender.get(Calendar.YEAR);
                mMonth = mCalender.get(Calendar.DAY_OF_MONTH);
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
                                datetime=chosenCalendar.getTimeInMillis();
                                Log.e("time", "" + dayOfMonth);
                                Log.e("time", "" + monthOfYear);
                                Log.e("time", "" + year);
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
                                // Display Selected time in time_EditText
                                mTaskHandTimeEditText.setText(hourOfDay + ":" + minute);
                                chosenCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                chosenCalendar.set(Calendar.MINUTE, minute);
                                datetime = chosenCalendar.getTimeInMillis();
                                //logging for seeing correct date and time are stored in db
                                Log.e("time", "" + datetime);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm", Locale.US);
                                Log.e("time", "" + simpleDateFormat.format(datetime));
                            }
                        }, mHour, mMinute, true);
                mTaskHandTimePickerDialog.show();
                break;

            case R.id.task_hand_save_button:
                //when fab is clicked
                if (mTaskHandIntent.getExtras() == null) {
                    getDetailDataFromView();
                    long addTaskReturn = mTaskHandDBHelper.addTask(mName, mDetail, mPriority, datetime, mSqLiteDatabase);
                    if (addTaskReturn > 0) {
                        Log.d("Data Base: adding", " successful");
                        Toast.makeText(AppContext.getContext(), "One task Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Data Base: adding", " unsuccessful");
                        Toast.makeText(AppContext.getContext(), "No Added", Toast.LENGTH_SHORT).show();
                    }
                    mSqLiteDatabase.close();
                }
                //when item is selected
                else {
                    getDetailDataFromView();
                    boolean updateValue = mTaskHandDBHelper.updateTask(mTaskId, mName, mDetail,
                            mPriority, datetime, mSqLiteDatabase);
                    if (updateValue) {
                        Log.d("Data Base: updating", " successful");
                        Toast.makeText(AppContext.getContext(), "One task Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Data Base: updating", " unsuccessful");
                        Toast.makeText(AppContext.getContext(), "No task updated", Toast.LENGTH_SHORT).show();
                    }
                    mSqLiteDatabase.close();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //nothing to be done here for current
        //for update portion
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        mPriority = (String) adapterView.getItemAtPosition(position);
        Log.e("spinner", "" + mPriority + " " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //todo Nothing
    }

    /**
     * for getting data from views of detail activity
     * & initialising the database helper class
     */
    private void getDetailDataFromView() {
        mName = mTaskHandTaskNameEditText.getText().toString();
        mDetail = mTaskHandDetailEditText.getText().toString();
        Log.d("Data : Name ", mName);
        Log.d("Data : Detail ", mDetail);
        Log.d("Data : Priority ", mPriority);
        Log.d("Data : date ", "" + datetime);
        mTaskHandDBHelper = new TaskHandDBHelper(this);
        mSqLiteDatabase = mTaskHandDBHelper.getWritableDatabase();
    }
}