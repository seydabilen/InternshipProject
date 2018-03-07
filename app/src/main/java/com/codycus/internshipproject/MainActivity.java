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

    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (ViewGroup) findViewById(R.id.view_root);


        for (int i = 0; i < 4; i++) {

            Button b = new Button(this);
            b.setBackgroundColor(getResources().getColor(R.color.button_default));
            b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            b.setId(i);
            b.setOnTouchListener(new ChoiceTouchListener());
            switch (b.getId()) {
                case 0:
                    b.setText("Button0");
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(250, 250);
                    b.setLayoutParams(layoutParams);
                    break;
                case 1:
                    b.setText("Button1");
                    FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(250, 250);
                    b.setLayoutParams(layoutParams1);
                    layoutParams1.topMargin = 300;

                    break;
                case 2:
                    b.setText("Button2");
                    FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(250, 250);
                    b.setLayoutParams(layoutParams2);
                    layoutParams2.topMargin = 600;

                    break;
                case 3:
                    b.setText("Button3");
                    FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(250, 250);
                    b.setLayoutParams(layoutParams3);
                    layoutParams3.topMargin = 900;
                    break;

            }

            rootLayout.addView(b);
        }
    }

            private final class ChoiceTouchListener implements View.OnTouchListener {
                private int _xdelta;
                private int _ydelta;


                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    {

                         int X = (int) event.getRawX();
                         int Y = (int) event.getRawY();
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                                _xdelta = X - lParams.leftMargin;
                                _ydelta = Y - lParams.topMargin;
                                view.setScaleX(view.getScaleX() + 0.5f);
                                view.setScaleY(view.getScaleY() + 0.5f);
                                break;
                            case MotionEvent.ACTION_UP:
                                view.setBackgroundColor(Color.rgb(28, 118, 187));
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




/*
        public class preparation extends View {
            public preparation(Context context) {
                super(context);
            }

            //buton init
            //Buton a listener eklenecek
            //tsrgetView init edilecek
            //button ve targetview için frame hesaplanacak

        }

        //touch handle event içinde button frame center ile target frame center match ediyor ise, bunu superview den kaldır
        //yada görünmez yap
        */



}






