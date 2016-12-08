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
import android.widget.GridView;
import android.widget.Toast;

import com.example.shubham.taskh.DataBase.TaskHandDBHelper;
import com.example.shubham.taskh.DataBase.TaskHandDataListProvider;
import com.example.shubham.taskh.R;
import com.example.shubham.taskh.TaskHandAdapter.TaskHandGridDataAdapter;
import com.example.shubham.taskh.Utility.AppContext;
import com.example.shubham.taskh.View.TaskHandDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskHandGridFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private GridView mGridView;
    private ArrayList<TaskHandDataListProvider> mHandDataListProviderArrayList;
    private TaskHandDBHelper mTaskHandDbHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private TaskHandGridDataAdapter mTaskHandGridDataAdapter;
    private View view;

    public TaskHandGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTaskHandDbHelper = new TaskHandDBHelper(getActivity());
        mSqLiteDatabase = mTaskHandDbHelper.getReadableDatabase();
        mHandDataListProviderArrayList = mTaskHandDbHelper.getTaskListData(mSqLiteDatabase);
        Log.e("Data", "" + mHandDataListProviderArrayList);
        if (mHandDataListProviderArrayList.size() != 0)
        {
            Log.e("Data", mHandDataListProviderArrayList.get(0).getmTaskName());
            view = inflater.inflate(R.layout.fragment_task_hand_grid, container, false);
            mGridView=(GridView)view.findViewById(R.id.task_hand_gridView);
            mTaskHandGridDataAdapter=new TaskHandGridDataAdapter(getActivity(),mHandDataListProviderArrayList);
            mGridView.setAdapter(mTaskHandGridDataAdapter);
            mGridView.setOnItemClickListener(this);
            mGridView.setOnItemLongClickListener(this);
        }
        else {
            //layout with a image of logo
            view = inflater.inflate(R.layout.task_empty_list, container, false);
        }
        // return the view for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskHandDbHelper = new TaskHandDBHelper(getActivity());
        mSqLiteDatabase = mTaskHandDbHelper.getReadableDatabase();
        mHandDataListProviderArrayList = mTaskHandDbHelper.getTaskListData(mSqLiteDatabase);
        Log.e("Data", "" + mHandDataListProviderArrayList);
        mTaskHandGridDataAdapter=new TaskHandGridDataAdapter(getActivity(),mHandDataListProviderArrayList);
        mGridView.setAdapter(mTaskHandGridDataAdapter);
        //todo when db is empty
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        adapterView.getItemAtPosition(position);
        int id=mHandDataListProviderArrayList.get(position).getmTask_Id();
        String name=mHandDataListProviderArrayList.get(position).getmTaskName();
        String detail=mHandDataListProviderArrayList.get(position).getmTaskDetail();
        String priority=mHandDataListProviderArrayList.get(position).getmTaskPriority();
        long reminderTime=mHandDataListProviderArrayList.get(position).getmTaskReminderTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm", Locale.US);
        Log.e("task :",""+position+" "+name+" "+detail+" "+priority+" "+simpleDateFormat.format(reminderTime));
        Intent detailIntent=new Intent(getActivity(), TaskHandDetailActivity.class);
        Bundle taskBundle=new Bundle();
        taskBundle.putInt("taskId",id);
        taskBundle.putString("taskName",name);
        taskBundle.putString("taskDetail",detail);
        taskBundle.putString("taskPriority",priority);
        taskBundle.putLong("taskReminder",reminderTime);
        detailIntent.putExtras(taskBundle);
        startActivity(detailIntent);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

        int id=mHandDataListProviderArrayList.get(position).getmTask_Id();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.DeleteNote)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                mTaskHandDbHelper.deleteNotes(id);
                                Toast.makeText(AppContext.getContext(), "Deleted Successfully",Toast.LENGTH_SHORT).show();

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
}
