package com.lextime2013.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.lextime2013.view.widget.CircleView;

public class MainActivity extends AppCompatActivity {

    private CircleView circleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleView = (CircleView) findViewById(R.id.circle_view);

        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHandler.sendEmptyMessageDelayed(0, 1000);
            circleView.calculateTime();
        }
    };

}
