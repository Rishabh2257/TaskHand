package com.example.shubham.taskhand;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.shubham.taskhand.view.TaskHandMain;
import com.squareup.picasso.Picasso;

public class TaskHandSplash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=3000;
    private ImageView mBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskhand_splash);
        mBackground=(ImageView)findViewById(R.id.task_hand_background);
        Picasso.with(this).load(R.drawable.img_pirate_background).into(mBackground);

        //delaying the launch of EdxProjectActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(TaskHandSplash.this,TaskHandMain.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
