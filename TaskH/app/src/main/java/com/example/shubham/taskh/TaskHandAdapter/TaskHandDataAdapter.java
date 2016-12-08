package com.example.shubham.taskh.TaskHandAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shubham.taskh.DataBase.TaskHandDataListProvider;
import com.example.shubham.taskh.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by shubham on 5/12/16.
 */
public class TaskHandDataAdapter extends BaseAdapter {

    private ArrayList<TaskHandDataListProvider> mTaskHandDataListProviderArrayList;
    private TaskHandDataListProvider mListProvider;
    private Context mContext;

    public TaskHandDataAdapter(Context context, ArrayList<TaskHandDataListProvider> arrayList) {
        this.mContext = context;
        this.mTaskHandDataListProviderArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return mTaskHandDataListProviderArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskHandDataListProviderArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.task_hand_list_row_item, parent, false);
            viewHolder = new ViewHolder();
            //set tag for view Holder
            viewHolder.mTaskHandNameTextView = (TextView) convertView.findViewById(R.id.task_hand_row_name);
            viewHolder.mTaskHandReminderTextView = (TextView) convertView.findViewById(R.id.task_hand_row_date);
            viewHolder.mTaskHandImageView=(ImageView)convertView.findViewById(R.id.task_hand_Alarm_imageView);
            viewHolder.mBackView=(View)convertView.findViewById(R.id.back_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //setting data in views
        mListProvider = mTaskHandDataListProviderArrayList.get(position);
        Log.e("data", "" + mListProvider);
        viewHolder.mTaskHandNameTextView.setText(mListProvider.getmTaskName());
        Log.e("Data", "" + mListProvider.getmTaskName());
        long datetime = mListProvider.getmTaskReminderTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm");
        Log.e("time", ""+datetime+"" + simpleDateFormat.format(datetime));
        if (datetime!=0)
        {
            viewHolder.mTaskHandReminderTextView.setText(simpleDateFormat.format(datetime));
            viewHolder.mTaskHandReminderTextView.setVisibility(View.VISIBLE);
            viewHolder.mTaskHandImageView.setVisibility(View.VISIBLE);

        }
        else
        {
            viewHolder.mTaskHandReminderTextView.setVisibility(View.INVISIBLE);
            viewHolder.mTaskHandImageView.setVisibility(View.INVISIBLE);
        }
        //colouring views according to priority
        String priority=mListProvider.getmTaskPriority();
            switch (priority)
            {
                case "Lowest":
                    viewHolder.mBackView.setBackgroundColor(mContext.getResources().getColor(R.color.holo_orange_dark));
                    break;
                case "Medium":
                    viewHolder.mBackView.setBackgroundColor(mContext.getResources().getColor(R.color.holo_red_light));
                    break;
                case "Highest":
                    viewHolder.mBackView.setBackgroundColor(mContext.getResources().getColor(R.color.holo_purple));
                    break;
            }
        return convertView;
    }

    /***
     * viewHolder class for smooth scrolling
     */
    final static class ViewHolder {
        TextView mTaskHandNameTextView;
        TextView mTaskHandReminderTextView;
        ImageView mTaskHandImageView;
        View mBackView;
        //viewHolder constructor
        public ViewHolder() {

        }
    }
}
