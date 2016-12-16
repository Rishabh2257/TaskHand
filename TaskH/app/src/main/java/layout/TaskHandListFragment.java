package layout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.shubham.taskh.R;
import com.example.shubham.taskh.adapter.TaskHandDataAdapter;
import com.example.shubham.taskh.alarm_new.AlarmHelper;
import com.example.shubham.taskh.database.TaskHandDBHelper;
import com.example.shubham.taskh.database.TaskHandDataModel;
import com.example.shubham.taskh.utility.AppContext;
import com.example.shubham.taskh.utility.Logger;
import com.example.shubham.taskh.utility.TaskHandHelper;
import com.example.shubham.taskh.view.TaskHandDetailActivity;
import com.example.shubham.taskh.view.TaskHandleMain;

import java.util.ArrayList;

/**
 * ListFragment for showing task in ListView
 */
public class TaskHandListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {
    private TaskHandDataAdapter mDataAdapter;
    private ListView mListView;
    private ArrayList<TaskHandDataModel> mTaskHandDataArrayList;
    private View viewInflate;
    private Button mByPriorityButton;
    private Button mByCreationTimeButton;

    public TaskHandListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_task_hand_list, container, false);
        mTaskHandDataArrayList = TaskHandHelper.getTaskHandArrayList();
        Logger.debug("Data", "in TaskListFragment  : " + mTaskHandDataArrayList);
        if (mTaskHandDataArrayList.size() != 0) {
            //Calling setTaskHandListView Method and passing the coming list from DB Helper method
            setTaskHandListView(mTaskHandDataArrayList);
        } else
            TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.first_add_task));

        TaskHandleMain.floatButtonOn();

        mByCreationTimeButton = (Button) viewInflate.findViewById(R.id.sort_by_creation);
        mByPriorityButton = (Button) viewInflate.findViewById(R.id.sort_by_priority);

        mByCreationTimeButton.setOnClickListener(this);
        mByPriorityButton.setOnClickListener(this);
        return viewInflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskHandDataArrayList = TaskHandHelper.getTaskHandArrayList();
        if (mTaskHandDataArrayList.size() != 0) {
            //Calling setTaskHandListView Method and passing the coming list from
            setTaskHandListView(mTaskHandDataArrayList);
        } else {
            TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.first_add_task));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent detailIntent = new Intent(getActivity(), TaskHandDetailActivity.class);
        detailIntent.putExtras(TaskHandHelper.setData(mTaskHandDataArrayList, position));
        //calling new activity which has data
        startActivity(detailIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {

        final int Id = mTaskHandDataArrayList.get(position).getTask_Id();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.DeleteNote)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                int noRowDeleted = TaskHandDBHelper.deleteNotes(Id);

                                if (noRowDeleted == 1) {
                                    TaskHandHelper.toastShort(AppContext.getContext().getResources().
                                            getString(R.string.delete_success));//Toast
                                    mTaskHandDataArrayList.remove(position);//removing element from ArrayList
                                    mDataAdapter.notifyDataSetChanged();//refreshing Adapter
                                    setTaskHandListView(mTaskHandDataArrayList);//resetting the data
                                    TaskHandDetailActivity.createFakeURI(Id);//creating
                                    AlarmHelper.cancelAlarm(TaskHandDetailActivity.createFakeURI(Id));
                                } else
                                    TaskHandHelper.toastShort(AppContext.getContext().getResources().
                                            getString(R.string.delete_unsuccess));//Toast

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
     * @param inList : List of values to be populated in ListView
     */
    private void setTaskHandListView(ArrayList<TaskHandDataModel> inList) {
        //check for coming inList
        if (inList.size() != 0) {
            mListView = (ListView) viewInflate.findViewById(R.id.task_hand_list_view);

            //initialising adapter
            mDataAdapter = new TaskHandDataAdapter(getActivity(), inList);

            //Attaching adapter to mListView
            mListView.setAdapter(mDataAdapter);

            //Calling Listeners for Respective work
            //for getting Detail of particular selected Task
            mListView.setOnItemClickListener(this);

            //open the AlertDialog For confirmation from user to delete the Task or not
            mListView.setOnItemLongClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_by_creation:
                if (mTaskHandDataArrayList.size() != 0) {
                    TaskHandDataModel.sortArrayListWithCreationTime(mTaskHandDataArrayList);
                    mDataAdapter.notifyDataSetChanged();
                    setTaskHandListView(mTaskHandDataArrayList);
                } else {
                    TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.first_add_task));
                }

                break;
            case R.id.sort_by_priority:
                if (mTaskHandDataArrayList.size() != 0) {
                    TaskHandDataModel.sortArrayListWithPriority(mTaskHandDataArrayList);
                    mDataAdapter.notifyDataSetChanged();
                    setTaskHandListView(mTaskHandDataArrayList);
                } else {
                    TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.first_add_task));
                }

                break;
        }
    }
}