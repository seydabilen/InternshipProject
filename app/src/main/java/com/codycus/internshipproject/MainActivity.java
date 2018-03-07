package com.codycus.internshipproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewGroup rootLayout;
    FrameLayout target0;
    TextView targetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (ViewGroup) findViewById(R.id.view_root);
        targetText= (TextView) findViewById(R.id.targetText);


        for (int i = 0; i < 4; i++) {
            final Button mainButton = new Button(this);
            mainButton.setBackgroundColor(getResources().getColor(R.color.button_default));
            mainButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mainButton.setId(i);
            mainButton.setOnTouchListener(new ChoiceTouchListener());
            mainButton.setText("Button" + i);
            final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(250, 250);
            mainButton.setLayoutParams(layoutParams);
            layoutParams.leftMargin = 280 * i;
            rootLayout.addView(mainButton);

        }


    }

    private class ChoiceDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
                default:
                    break;
            }
            return true;
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




