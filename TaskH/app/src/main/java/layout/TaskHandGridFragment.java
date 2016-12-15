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
import android.widget.GridView;
import android.widget.Toast;

import com.example.shubham.taskh.database.TaskHandDBHelper;
import com.example.shubham.taskh.database.TaskHandDataProvider;
import com.example.shubham.taskh.R;
import com.example.shubham.taskh.task_hand_adapter.TaskHandGridDataAdapter;
import com.example.shubham.taskh.utility.AppContext;
import com.example.shubham.taskh.view.TaskHandDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskHandGridFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    private GridView mGridView;
    private ArrayList<TaskHandDataProvider> mHandDataListProviderArrayListGRID;
    private TaskHandDBHelper mTaskHandDbHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private TaskHandGridDataAdapter mTaskHandGridDataAdapter;
    private View view;
    private Button mByPriorityButtonInGrid;
    private Button mByCreationTimeButtonInGrid;

    public TaskHandGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_hand_grid, container, false);
        mHandDataListProviderArrayListGRID = taskHandDataListProviderArrayListGrid();
        Log.d("Data", "in TaskGRIDFragment  : " + mHandDataListProviderArrayListGRID);
        if (mHandDataListProviderArrayListGRID.size() != 0) {
            //Calling setTaskHandListView Method and passing the coming list from DB Helper method
            setTaskHandGridView(mHandDataListProviderArrayListGRID);
        } else
            Toast.makeText(AppContext.getContext(), "Add Task", Toast.LENGTH_SHORT).show();

        mByCreationTimeButtonInGrid=(Button)view.findViewById(R.id.sort_by_creation_in_grid);
        mByPriorityButtonInGrid=(Button)view.findViewById(R.id.sort_by_priority_in_grid);

        mByCreationTimeButtonInGrid.setOnClickListener(this);
        mByPriorityButtonInGrid.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandDataListProviderArrayListGRID = taskHandDataListProviderArrayListGrid();
        Log.d("Data", "in TaskGRIDFragment In On Resume : " + mHandDataListProviderArrayListGRID);
        if (mHandDataListProviderArrayListGRID.size() != 0) {
            //Calling setTaskHandListView Method and passing the coming list from
            setTaskHandGridView(mHandDataListProviderArrayListGRID);
        } else {
            Toast.makeText(AppContext.getContext(), "Add Task", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        adapterView.getItemAtPosition(position);
        int id = mHandDataListProviderArrayListGRID.get(position).getmTask_Id();
        String name = mHandDataListProviderArrayListGRID.get(position).getmTaskName();
        String detail = mHandDataListProviderArrayListGRID.get(position).getmTaskDetail();
        String priority = mHandDataListProviderArrayListGRID.get(position).getmTaskPriority();
        long reminderTime = mHandDataListProviderArrayListGRID.get(position).getmTaskReminderTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm", Locale.US);
        Log.d("task  in GRID :", "" + position + " " + name + " " + detail + " " + priority + " " + simpleDateFormat.format(reminderTime));
        Intent detailIntent = new Intent(getActivity(), TaskHandDetailActivity.class);
        Bundle taskBundle = new Bundle();
        taskBundle.putInt("taskId", id);
        taskBundle.putString("taskName", name);
        taskBundle.putString("taskDetail", detail);
        taskBundle.putString("taskPriority", priority);
        taskBundle.putLong("taskReminder", reminderTime);
        detailIntent.putExtras(taskBundle);
        startActivity(detailIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

        final int Id = mHandDataListProviderArrayListGRID.get(position).getmTask_Id();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.DeleteNote)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                mTaskHandDbHelper.deleteNotes(Id, mSqLiteDatabase);
                                Toast.makeText(AppContext.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                mHandDataListProviderArrayListGRID.remove(position);
                                mTaskHandGridDataAdapter.notifyDataSetChanged();
                                setTaskHandGridView(mHandDataListProviderArrayListGRID);
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
    private void setTaskHandGridView(ArrayList<TaskHandDataProvider> list) {
        //check for coming list
        if (list.size() != 0) {
            Log.d("Data In GRID Fragment", list.get(0).getmTaskName());
            mGridView = (GridView) view.findViewById(R.id.task_hand_gridView);
            //initialising adapter
            mTaskHandGridDataAdapter = new TaskHandGridDataAdapter(getActivity(), list);
            //Attaching adapter to mListView
            mGridView.setAdapter(mTaskHandGridDataAdapter);
            //Calling Listeners for Respective work
            //for getting Detail of particular selected Task
            mGridView.setOnItemClickListener(this);
            //open the AlertDialog For confirmation from user to delete the Task or not
            mGridView.setOnItemLongClickListener(this);
        }
    }

    /***
     * Utility Method for returning list of values taken from TASKHAND.DB of app
     *
     * @return :ArrayList with values
     */
    private ArrayList<TaskHandDataProvider> taskHandDataListProviderArrayListGrid() {
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
            case R.id.sort_by_creation_in_grid:
                if (mHandDataListProviderArrayListGRID.size()!=0)
                {
                    TaskHandDataProvider.sortArrayListWithCreationTime(mHandDataListProviderArrayListGRID);
                    mTaskHandGridDataAdapter.notifyDataSetChanged();
                    setTaskHandGridView(mHandDataListProviderArrayListGRID);
                }
                else
                {
                    Toast.makeText(AppContext.getContext(),"First Add Task",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.sort_by_priority_in_grid:
                if (mHandDataListProviderArrayListGRID.size()!=0)
                {
                    TaskHandDataProvider.sortArrayListWithPriority(mHandDataListProviderArrayListGRID);
                    mTaskHandGridDataAdapter.notifyDataSetChanged();
                    setTaskHandGridView(mHandDataListProviderArrayListGRID);
                }
                else
                {
                    Toast.makeText(AppContext.getContext(),"First Add Task",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
