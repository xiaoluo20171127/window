package com.coocaa.floatingwindow;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WindowManager.LayoutParams mLayoutParams;
    WindowManager mWindowManager;
    Button mButton;
    private WindowManager.LayoutParams mLayoutParams2;
    private View mContentView;
    private ImageView mImageView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Settings.canDrawOverlays(MainActivity.this))
        {
            Toast.makeText(MainActivity.this,"已开启Toucher",Toast.LENGTH_SHORT).show();
            initWindow();
        }else {
            //若没有权限，提示获取.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            Toast.makeText(MainActivity.this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
            startActivityForResult(intent,1);
        }
        mButton = findViewById(R.id.button);
        final TranslateAnimation animation = new TranslateAnimation(0,0,-200,0);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView1 = new ImageView(MainActivity.this);
                imageView1.setImageResource(R.drawable.ic_launcher_background);
                mWindowManager.addView(imageView1, mLayoutParams2);
                mWindowManager.addView(mContentView,mLayoutParams);
                mImageView.setAnimation(animation);
                imageView1.startAnimation(animation);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("activityResult",requestCode+"");
        if(requestCode == 1){
              initWindow();
        }
    }

    public void initWindow(){
        Log.e("initWindow","initWindow");
        mContentView = LayoutInflater.from(this).inflate(R.layout.window_layout,null);
        mImageView = mContentView.findViewById(R.id.imageButton);
        mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,0,0, PixelFormat.TRANSPARENT);
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mLayoutParams.width = 200;
        mLayoutParams.height = 200;
        mLayoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
        {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int dx = moveX-startX;
                        int dy = moveY-startY;
                        startX = moveX;
                        startY = moveY;
                        mLayoutParams.x = mLayoutParams.x+dx;
                        mLayoutParams.y = mLayoutParams.y+dy;
                        mWindowManager.updateViewLayout(mContentView,mLayoutParams);
                        break;
                }
                return true;
            }
        });
        mLayoutParams2 = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,0,0, PixelFormat.TRANSPARENT);
        mLayoutParams2.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mLayoutParams2.x = 0;
        mLayoutParams2.y = 0;
        mLayoutParams2.width = 200;
        mLayoutParams2.height = 1000;
        mLayoutParams2.gravity = Gravity.RIGHT|Gravity.TOP;
        mLayoutParams2.windowAnimations = R.anim.y_0_to_y_1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
        {
            mLayoutParams2.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            mLayoutParams2.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

    }
}
