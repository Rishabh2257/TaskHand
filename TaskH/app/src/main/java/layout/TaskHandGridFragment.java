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
import android.widget.GridView;

import com.example.shubham.taskh.R;
import com.example.shubham.taskh.adapter.TaskHandGridDataAdapter;
import com.example.shubham.taskh.database.TaskHandDBHelper;
import com.example.shubham.taskh.database.TaskHandDataModel;
import com.example.shubham.taskh.utility.AppContext;
import com.example.shubham.taskh.utility.Logger;
import com.example.shubham.taskh.utility.TaskHandHelper;
import com.example.shubham.taskh.view.TaskHandDetailActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskHandGridFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    private GridView mGridView;
    private ArrayList<TaskHandDataModel> mTaskHandGridArrayList;
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
        mTaskHandGridArrayList = TaskHandHelper.getTaskHandArrayList();
        Logger.debug("Data", "in TaskGRIDFragment  : " + mTaskHandGridArrayList);
        if (mTaskHandGridArrayList.size() != 0) {
            //Calling setTaskHandListView Method and passing the coming list from DB Helper method
            setTaskHandGridView(mTaskHandGridArrayList);
        } else
        TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.first_add_task));

        mByCreationTimeButtonInGrid = (Button) view.findViewById(R.id.sort_by_creation_in_grid);
        mByPriorityButtonInGrid = (Button) view.findViewById(R.id.sort_by_priority_in_grid);

        mByCreationTimeButtonInGrid.setOnClickListener(this);
        mByPriorityButtonInGrid.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskHandGridArrayList = TaskHandHelper.getTaskHandArrayList();
        if (mTaskHandGridArrayList.size() != 0) {
            //Calling setTaskHandListView Method and passing the coming list from
            setTaskHandGridView(mTaskHandGridArrayList);
        } else {
            TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.first_add_task));
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Intent detailIntent = new Intent(getActivity(), TaskHandDetailActivity.class);
        detailIntent.putExtras(TaskHandHelper.setData(mTaskHandGridArrayList, position));
        //calling new activity which has data
        startActivity(detailIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

        final int Id = mTaskHandGridArrayList.get(position).getTask_Id();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.DeleteNote)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                TaskHandDBHelper.deleteNotes(Id);
                                TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.delete_success));
                                mTaskHandGridArrayList.remove(position);
                                mTaskHandGridDataAdapter.notifyDataSetChanged();
                                setTaskHandGridView(mTaskHandGridArrayList);
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
    private void setTaskHandGridView(ArrayList<TaskHandDataModel> list) {
        //check for coming list
        if (list.size() != 0) {
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sort_by_creation_in_grid:
                if (mTaskHandGridArrayList.size() != 0) {
                    TaskHandDataModel.sortArrayListWithCreationTime(mTaskHandGridArrayList);
                    mTaskHandGridDataAdapter.notifyDataSetChanged();
                    setTaskHandGridView(mTaskHandGridArrayList);
                } else {
                    TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.first_add_task));
                }

                break;
            case R.id.sort_by_priority_in_grid:
                if (mTaskHandGridArrayList.size() != 0) {
                    TaskHandDataModel.sortArrayListWithPriority(mTaskHandGridArrayList);
                    mTaskHandGridDataAdapter.notifyDataSetChanged();
                    setTaskHandGridView(mTaskHandGridArrayList);
                } else {
                    TaskHandHelper.toastShort(AppContext.getContext().getResources().getString(R.string.first_add_task));
                }

                break;
        }
    }
}
