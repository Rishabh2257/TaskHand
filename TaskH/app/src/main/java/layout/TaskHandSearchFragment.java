package layout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shubham.taskh.database.TaskHandDataProvider;
import com.example.shubham.taskh.database.TaskHandDBHelper;
import com.example.shubham.taskh.R;
import com.example.shubham.taskh.task_hand_adapter.TaskHandDataAdapter;
import com.example.shubham.taskh.utility.AppContext;
import com.example.shubham.taskh.view.TaskHandDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by shubham on 13/12/16.
 */
public class TaskHandSearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TaskHandDataAdapter mDataAdapter;
    private ListView mSearchListView;
    private ArrayList<TaskHandDataProvider> mHandDataListProviderArrayList;
    private TaskHandDBHelper mTaskHandDbHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private View viewInflate;
    private Button mSearchButton;
    private EditText mSearchEditText;
    private String  mName;
    public TaskHandSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("IN Search Fragment","Yes we are in ");

        viewInflate = inflater.inflate(R.layout.fragment_task_hand_search, container, false);
        mSearchButton=(Button)viewInflate.findViewById(R.id.task_hand_search_button);
        mSearchEditText=(EditText)viewInflate.findViewById(R.id.task_hand_search_editText);

        //click Listener for Search Button
        mSearchButton.setOnClickListener(this);

        return viewInflate;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        adapterView.getItemAtPosition(position);
        //Getting Data From List and storing them in local variables for putting in
        int id = mHandDataListProviderArrayList.get(position).getmTask_Id();
        String name = mHandDataListProviderArrayList.get(position).getmTaskName();
        String detail = mHandDataListProviderArrayList.get(position).getmTaskDetail();
        String priority = mHandDataListProviderArrayList.get(position).getmTaskPriority();
        long reminderTime = mHandDataListProviderArrayList.get(position).getmTaskReminderTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm");
        Log.d("TASK DETAIL OF ID:", "" + position + " " + name + " " + detail + " " + priority + " " + simpleDateFormat.format(reminderTime));
        //Opening New Activity TaskHandDetail.java through this intent
        Intent detailIntent = new Intent(getActivity(), TaskHandDetailActivity.class);
        //putting data in Bundle
        Bundle taskBundle = new Bundle();
        taskBundle.putInt("taskId", id);
        taskBundle.putString("taskName", name);
        taskBundle.putString("taskDetail", detail);
        taskBundle.putString("taskPriority", priority);
        taskBundle.putLong("taskReminder", reminderTime);
        //putting Bundle in Intent as Extras
        detailIntent.putExtras(taskBundle);
        //Starting New Activity
        startActivity(detailIntent);
        onDestroy();
    }

    /**
     * This Method set the ListView and adapter with data
     *
     * @param list : List of values to be populated in ListView
     */
    private void setTaskHandListView(ArrayList<TaskHandDataProvider> list) {
        //check for coming list
        if (list.size() != 0) {
            Log.d("Data In Search Fragment", list.get(0).getmTaskName());
            mSearchListView = (ListView) viewInflate.findViewById(R.id.search_task_hand_listView);
            mSearchListView.setVisibility(View.VISIBLE);
            //initialising adapter
            mDataAdapter = new TaskHandDataAdapter(getActivity(), list);
            //Attaching adapter to mSearchListView
            mSearchListView.setAdapter(mDataAdapter);
            //Calling Listeners for Respective work
            //for getting Detail of particular selected Task
            mSearchListView.setOnItemClickListener(this);

        }
    }

    /***
     * Method for returning list of values taken from TASKHAND.DB of app
     *
     * @return :ArrayList with values
     */
    private ArrayList<TaskHandDataProvider> taskHandDataListProviderArrayList(String Name) {
        //taking instance of DB Helper class
        mTaskHandDbHelper = new TaskHandDBHelper(getActivity());
        //getting readable database instance
        mSqLiteDatabase = mTaskHandDbHelper.getReadableDatabase();
        //getting ArrayList of Values from helper Class Method: getTaskListData
        return mTaskHandDbHelper.getTaskHandSearchList(Name,mSqLiteDatabase);
    }

    @Override
    public void onClick(View view) {
        mName=mSearchEditText.getText().toString();
        Log.d("Name",mName);
        if (mName.length()!=0)
        {
            mHandDataListProviderArrayList = taskHandDataListProviderArrayList(mName);
            Log.d("Data", " In Search Fragment  : " + mHandDataListProviderArrayList);
            if (mHandDataListProviderArrayList.size() != 0) {
                //Calling setTaskHandListView Method and passing the coming list from DB Helper method
                setTaskHandListView(mHandDataListProviderArrayList);
            }
            else
            {
                Toast.makeText(AppContext.getContext(),"No Task Present so Please Add Task First",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(AppContext.getContext(),"Please Enter Name",Toast.LENGTH_SHORT).show();
        }
    }
}

