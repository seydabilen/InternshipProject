package com.codycus.internshipproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private Button btn,btn1;
    private ViewGroup rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (ViewGroup) findViewById(R.id.view_root);
        btn1 = (Button) rootLayout.findViewById(R.id.button1);
        btn = (Button)rootLayout.findViewById(R.id.button);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(250, 250);
        layoutParams.topMargin=300;
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(250, 250);
        btn1.setLayoutParams(layoutParams);
        btn.setLayoutParams(layoutParams1);
        btn1.setOnTouchListener(new ChoiceTouchListener());
        btn.setOnTouchListener(new ChoiceTouchListener());

    }

    private final class ChoiceTouchListener implements View.OnTouchListener {
        private int _xdelta;
        private int _ydelta;


        @Override
        public boolean onTouch(View view, MotionEvent event) {

            {

                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                        _xdelta = X - lParams.leftMargin;
                        _ydelta = Y - lParams.topMargin;
                        view.setScaleX(view.getScaleX() + 0.5f);
                        view.setScaleY(view.getScaleY() + 0.5f);
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(Color.rgb(28,118,187));
                        view.setScaleX(1);
                        view.setScaleY(1);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.setBackgroundColor(Color.GREEN);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = X - _xdelta;
                        layoutParams.topMargin = Y - _ydelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }

                rootLayout.invalidate();
                return true;
            }
        }
    }




}





