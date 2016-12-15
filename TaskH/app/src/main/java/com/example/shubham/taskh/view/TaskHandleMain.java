package com.example.shubham.taskh.view;

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
import com.example.shubham.taskh.utility.FragmentCall;

import layout.TaskHandAboutUsFragment;
import layout.TaskHandGridFragment;
import layout.TaskHandListFragment;
import layout.TaskHandSearchFragment;

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
        FragmentCall.inflateFragment(new TaskHandListFragment(),getSupportFragmentManager(),
                R.id.task_hand_list_container,null,true,false);
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
        mTaskHandAddFAB.setVisibility(View.VISIBLE);
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
                FragmentCall.inflateFragment(new TaskHandListFragment(),getSupportFragmentManager()
                ,R.id.task_hand_list_container,null,true,true);
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.task_hand_list_container, new TaskHandListFragment());
//                mFragmentTransaction.commit();
                itemChecker(item);
                break;
            case R.id.about_us:
                mTaskHandAddFAB.setVisibility(View.VISIBLE);
                FragmentCall.inflateFragment(new TaskHandAboutUsFragment(),getSupportFragmentManager()
                ,R.id.task_hand_list_container,null,true,true);
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.task_hand_list_container, new TaskHandAboutUsFragment());
//                mFragmentTransaction.commit();
                itemChecker(item);
                break;
            case R.id.task_list_settings:
                Toast.makeText(this, "Add Settings Fragment", Toast.LENGTH_SHORT).show();
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
                mTaskHandAddFAB.setVisibility(View.VISIBLE);
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.task_hand_list_container, new TaskHandGridFragment());
//                mFragmentTransaction.commit();
                FragmentCall.inflateFragment(new TaskHandGridFragment(),getSupportFragmentManager()
                        ,R.id.task_hand_list_container,null,true,true);
                itemChecker(item);
                break;
            case R.id.task_hand_list:
                mTaskHandAddFAB.setVisibility(View.VISIBLE);
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.task_hand_list_container, new TaskHandListFragment());
//                mFragmentTransaction.commit();
                FragmentCall.inflateFragment(new TaskHandListFragment(),getSupportFragmentManager()
                        ,R.id.task_hand_list_container,null,true,true);
                break;
            case R.id.task_hand_search:
                mTaskHandAddFAB.setVisibility(View.GONE);
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.task_hand_list_container, new TaskHandSearchFragment());
//                mFragmentTransaction.commit();
                FragmentCall.inflateFragment(new TaskHandSearchFragment(),getSupportFragmentManager()
                        ,R.id.task_hand_list_container,null,true,true);
                break;
        }
        return true;
    }

}
