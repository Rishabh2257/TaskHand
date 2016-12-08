package com.example.shubham.taskh.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shubham.taskh.R;
import com.example.shubham.taskh.Utility.AppContext;

import layout.TaskHandGridFragment;
import layout.TaskHandListFragment;

/***
 * Main Activity of TaskHand Project
 */

public class TaskHandleMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mTaskDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FragmentTransaction mFragmentTransaction;
    private NavigationView mNavigationView;
    private FloatingActionButton mTaskHandAddFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_handle_layout);
        //setting ToolBar
        mToolbar = (Toolbar) findViewById(R.id.task_list_toolBar);
        setSupportActionBar(mToolbar);
        mTaskDrawerLayout = (DrawerLayout) findViewById(R.id.task_hand_drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mTaskDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        //setting the listener
        mTaskDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        //Fragment inflation
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.task_hand_list_container, new TaskHandListFragment());
        mFragmentTransaction.commit();
        try {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setLogo(R.drawable.pirates_colour);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //navigation handling
        mNavigationView = (NavigationView) findViewById(R.id.task_handle_navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        //adding listener for mTaskHandAddFAB
        mTaskHandAddFAB = (FloatingActionButton) findViewById(R.id.task_hand_floating_action_button);
        mTaskHandAddFAB.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.task_list_home:
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.task_hand_list_container, new TaskHandListFragment());
                mFragmentTransaction.commit();
                itemChecker(item);
//                item.setChecked(true);
//                mTaskDrawerLayout.closeDrawers();
                break;
            case R.id.about_us:
                Toast.makeText(this, "Add About Us Fragment", Toast.LENGTH_SHORT).show();
                itemChecker(item);
                break;
            case R.id.task_list_settings:
                Toast.makeText(this, "Add Settings Fragment", Toast.LENGTH_SHORT).show();
                itemChecker(item);
                break;
            case R.id.calender:
                Toast.makeText(this, "Add Calender Fragment", Toast.LENGTH_SHORT).show();
                itemChecker(item);
                break;
        }
        return true;
    }

    /**
     * for closing the drawer
     *
     * @param item : id of selected menu item
     */
    private void itemChecker(MenuItem item) {
        item.setChecked(true);
        mTaskDrawerLayout.closeDrawers();
    }

    @Override
    public void onClick(View view) {
        launchIntent(TaskHandDetailActivity.class);
    }

    /**
     * method for launching activity
     *
     * @param iActivityClass: instance of passed class name
     */
    private void launchIntent(Class iActivityClass) {
        //opening the passed class
        Intent intent = new Intent(this, iActivityClass);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.task_hand_grid:
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.task_hand_list_container, new TaskHandGridFragment());
                mFragmentTransaction.commit();
                itemChecker(item);
                break;
            case R.id.task_hand_list:
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.task_hand_list_container, new TaskHandListFragment());
                mFragmentTransaction.commit();
                break;
            case R.id.task_hand_search:
                Toast.makeText(AppContext.getContext(),"to implement search",Toast.LENGTH_SHORT).show();
               // TODO: 7/12/16 add search
                break;
        }
        return true;
    }
}
