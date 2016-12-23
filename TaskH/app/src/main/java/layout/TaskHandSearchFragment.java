package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.shubham.taskhand.R;
import com.example.shubham.taskhand.adapter.TaskHandDataAdapter;
import com.example.shubham.taskhand.database.TaskHandDBHelper;
import com.example.shubham.taskhand.database.TaskHandDataModel;
import com.example.shubham.taskhand.utility.Logger;
import com.example.shubham.taskhand.utility.TaskHandHelper;
import com.example.shubham.taskhand.view.TaskHandDetailActivity;
import com.example.shubham.taskhand.view.TaskHandMain;

import java.util.ArrayList;

/**
 * Fragment for searching a task with name
 * <p>
 * Created by shubham on 13/12/16.
 */
public class TaskHandSearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayList<TaskHandDataModel> mTaskHandDataArrayList;
    private View viewInflate;
    private Button mSearchButton;
    private EditText mSearchEditText;

    public TaskHandSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_task_hand_search, container, false);
        mSearchButton = (Button) viewInflate.findViewById(R.id.task_hand_search_button);
        mSearchEditText = (EditText) viewInflate.findViewById(R.id.task_hand_search_editText);

        //click Listener for Search Button
        mSearchButton.setOnClickListener(this);

        return viewInflate;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent detailIntent = new Intent(getActivity(), TaskHandDetailActivity.class);
        detailIntent.putExtras(TaskHandHelper.setData(mTaskHandDataArrayList, position));
        //calling new activity which has data
        startActivity(detailIntent);
        onDestroy();

    }

    /**
     * This Method set the ListView and adapter with data
     *
     * @param list : List of values to be populated in ListView
     */
    private void setTaskHandSearchListView(ArrayList<TaskHandDataModel> list) {
        TaskHandDataAdapter adapter;
        ListView searchListView;
        //check for coming list
        if (list.size() != 0) {
            Logger.debug("Data In Search Fragment", list.get(0).getTaskName());
            searchListView = (ListView) viewInflate.findViewById(R.id.search_task_hand_listView);
            searchListView.setVisibility(View.VISIBLE);

            //initialising adapter
            adapter = new TaskHandDataAdapter(getActivity(), list);
            //Attaching adapter to listView
            searchListView.setAdapter(adapter);
            //Calling Listeners for Respective work
            //for getting Detail of particular selected Task
            searchListView.setOnItemClickListener(this);

        }
    }

    @Override
    public void onClick(View view) {
        String name = mSearchEditText.getText().toString();
        if (name.length() != 0) {

            mTaskHandDataArrayList = TaskHandDBHelper.getTaskHandSearchList(name);
            Logger.debug("Data", " In Search Fragment  : " + mTaskHandDataArrayList);
            if (mTaskHandDataArrayList.size() != 0) {
                //Calling setTaskHandSearchListView Method and passing the coming list from DB Helper method
                setTaskHandSearchListView(mTaskHandDataArrayList);
            } else {
                TaskHandHelper.toastShort("No Task Present so Please Add Task First");
            }
        } else {
            TaskHandHelper.toastShort("Please Enter Name");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TaskHandMain.floatButtonOff();
    }
}