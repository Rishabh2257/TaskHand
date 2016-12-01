package com.example.shubham.taskh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import layout.TaskHandListFragment;

/***
 * Main Activity of TaskHand Project
 */

public class TaskHandleMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mTaskDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FragmentTransaction mFragmentTransaction;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_handle_layout);
        //setting ToolBar
        mToolbar=(Toolbar)findViewById(R.id.task_list_toolBar);
        setSupportActionBar(mToolbar);
        mTaskDrawerLayout=(DrawerLayout)findViewById(R.id.task_hand_drawer_layout);
        mActionBarDrawerToggle=new ActionBarDrawerToggle(this,mTaskDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);
        //setting the listener
        mTaskDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        //Fragment inflation
        mFragmentTransaction=getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.task_hand_list_container,new TaskHandListFragment());
        mFragmentTransaction.commit();
        try
        {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setLogo(R.drawable.pirates_colour);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        //navigation handling
        mNavigationView=(NavigationView)findViewById(R.id.task_handle_navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.task_list_home:
                mFragmentTransaction=getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.task_hand_list_container,new TaskHandListFragment());
                mFragmentTransaction.commit();
                itemChecker(item);
//                item.setChecked(true);
//                mTaskDrawerLayout.closeDrawers();
                break;
            case R.id.about_us:
                Toast.makeText(this,"Add About Us Fragment",Toast.LENGTH_SHORT).show();
                itemChecker(item);
                break;
            case R.id.task_list_settings:
                Toast.makeText(this,"Add Settings Fragment",Toast.LENGTH_SHORT).show();
                itemChecker(item);
                break;
            case R.id.calender:
                Toast.makeText(this,"Add Calender Fragment",Toast.LENGTH_SHORT).show();
                itemChecker(item);
                break;
        }
      return true;
    }
    private void itemChecker(MenuItem item)
    {
        item.setChecked(true);
        mTaskDrawerLayout.closeDrawers();
    }
}
