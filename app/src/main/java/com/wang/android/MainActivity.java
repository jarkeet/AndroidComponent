package com.wang.android;

import android.os.Bundle;
import android.view.View;

import com.wang.library.view.RippleView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RippleView mRippleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mRippleView = findViewById(R.id.ripple_view);
        mRippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mRippleView.s
            }
        });
    }
}
