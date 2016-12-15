package layout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shubham.taskh.R;
import com.example.shubham.taskh.alarm_new.AlarmHelper;
import com.example.shubham.taskh.database.TaskHandDBHelper;
import com.example.shubham.taskh.database.TaskHandDataProvider;
import com.example.shubham.taskh.task_hand_adapter.TaskHandDataAdapter;
import com.example.shubham.taskh.utility.AppContext;
import com.example.shubham.taskh.view.TaskHandDetailActivity;
import com.example.shubham.taskh.view.TaskHandleMain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskHandListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {
    private TaskHandDataAdapter mDataAdapter;
    private ListView mListView;
    private ArrayList<TaskHandDataProvider> mHandDataListProviderArrayList;
    private TaskHandDBHelper mTaskHandDbHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private View viewInflate;
    private Button mByPriorityButton;
    private Button mByCreationTimeButton;
    private AlarmHelper mHelper;

    public TaskHandListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_task_hand_list, container, false);
        mHandDataListProviderArrayList = taskHandDataListProviderArrayList();
        Log.d("Data", "in TaskListFragment  : " + mHandDataListProviderArrayList);
        if (mHandDataListProviderArrayList.size() != 0) {
            //Calling setTaskHandListView Method and passing the coming list from DB Helper method
            setTaskHandListView(mHandDataListProviderArrayList);
        } else
            Toast.makeText(AppContext.getContext(), "Add Task", Toast.LENGTH_SHORT).show();


        TaskHandleMain.floatButtonOn();

        mByCreationTimeButton=(Button)viewInflate.findViewById(R.id.sort_by_creation);
        mByPriorityButton=(Button)viewInflate.findViewById(R.id.sort_by_priority);

        mByCreationTimeButton.setOnClickListener(this);
        mByPriorityButton.setOnClickListener(this);
        return viewInflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandDataListProviderArrayList = taskHandDataListProviderArrayList();
        Log.d("Data", "in TaskListFragment In On Resume : " + mHandDataListProviderArrayList);
        if (mHandDataListProviderArrayList.size() != 0) {
            //Calling setTaskHandListView Method and passing the coming list from
            setTaskHandListView(mHandDataListProviderArrayList);
        } else {
            Toast.makeText(AppContext.getContext(), "Add Task", Toast.LENGTH_SHORT).show();
        }
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
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {

        final int Id = mHandDataListProviderArrayList.get(position).getmTask_Id();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.DeleteNote)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                mTaskHandDbHelper.deleteNotes(Id, mSqLiteDatabase);
                                Toast.makeText(AppContext.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                mHandDataListProviderArrayList.remove(position);
                                mDataAdapter.notifyDataSetChanged();
                                setTaskHandListView(mHandDataListProviderArrayList);
                               TaskHandDetailActivity.createFakeURI(Id);
                                mHelper.cancelAlarm(TaskHandDetailActivity.createFakeURI(Id));
                            }
                        })
                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Are you sure");
        alertDialog.show();
        return true;
    }

    /**
     * This Method set the ListView and adapter with data
     *
     * @param list : List of values to be populated in ListView
     */
    private void setTaskHandListView(ArrayList<TaskHandDataProvider> list) {
        //check for coming list
        if (list.size() != 0) {
            Log.d("Data In List Fragment", list.get(0).getmTaskName());
            mListView = (ListView) viewInflate.findViewById(R.id.task_hand_list_view);
            //initialising adapter
            mDataAdapter = new TaskHandDataAdapter(getActivity(), list);
            //Attaching adapter to mListView
            mListView.setAdapter(mDataAdapter);
            //Calling Listeners for Respective work
            //for getting Detail of particular selected Task
            mListView.setOnItemClickListener(this);
            //open the AlertDialog For confirmation from user to delete the Task or not
            mListView.setOnItemLongClickListener(this);
        }
    }

    /***
     * Method for returning list of values taken from TASKHAND.DB of app
     *
     * @return :ArrayList with values
     */
    private ArrayList<TaskHandDataProvider> taskHandDataListProviderArrayList() {
        //taking instance of DB Helper class
        mTaskHandDbHelper = new TaskHandDBHelper(getActivity());
        //getting readable database instance
        mSqLiteDatabase = mTaskHandDbHelper.getReadableDatabase();
        //getting ArrayList of Values from helper Class Method: getTaskListData
        return mTaskHandDbHelper.getTaskListData(mSqLiteDatabase);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sort_by_creation:
                if (mHandDataListProviderArrayList.size()!=0)
                {
                    TaskHandDataProvider.sortArrayListWithCreationTime(mHandDataListProviderArrayList);
                    mDataAdapter.notifyDataSetChanged();
                    setTaskHandListView(mHandDataListProviderArrayList);
                }
                else
                {
                    Toast.makeText(AppContext.getContext(),"First Add Task",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.sort_by_priority:
                if (mHandDataListProviderArrayList.size()!=0)
                {
                    TaskHandDataProvider.sortArrayListWithPriority(mHandDataListProviderArrayList);
                    mDataAdapter.notifyDataSetChanged();
                    setTaskHandListView(mHandDataListProviderArrayList);
                }
                else
                {
                    Toast.makeText(AppContext.getContext(),"First Add Task",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}