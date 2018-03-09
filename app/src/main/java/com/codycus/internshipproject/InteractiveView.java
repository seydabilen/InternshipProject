package com.codycus.internshipproject;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


/**
 * Created by Acer on 8.03.2018.
 */

public class InteractiveView extends android.support.v7.widget.AppCompatButton implements View.OnTouchListener {

    private Button mainButton;
    private int _xdelta;
    private int _ydelta;
    private float startPosX;
    private float startPosY;
    private float destinationPos;


    public InteractiveView(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.button_default));
        setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int X = (int) event.getRawX();//ilk pozisyon
        int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                _xdelta = X - lParams.leftMargin;//start noktası
                _ydelta = Y - lParams.topMargin;
                v.setScaleX(v.getScaleX() + 0.5f);
                v.setScaleY(v.getScaleY() + 0.5f);
                break;
            case MotionEvent.ACTION_UP:
                FrameLayout.LayoutParams lParams1 = (FrameLayout.LayoutParams) v.getLayoutParams();
                v.setBackgroundColor(Color.rgb(28, 118, 187));
                v.setScaleX(1);
                v.setScaleY(1);
                returnToOriginalPosition(v);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                v.setBackgroundColor(Color.GREEN);
                FrameLayout.LayoutParams layoutParams0 = (FrameLayout.LayoutParams) v.getLayoutParams();
                layoutParams0.leftMargin = X - _xdelta; //last
                layoutParams0.topMargin = Y - _ydelta;
                layoutParams0.rightMargin = 0;
                layoutParams0.bottomMargin = 0;
                v.setLayoutParams(layoutParams0);

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }

        invalidate();
        return true;
    }
    public void returnToOriginalPosition(View view){

        int[] loc = new int[2];
        int[] loc2 = new int[2];
        view.getLocationOnScreen(loc);
        view.getLocationInWindow(loc2);

        final int x = loc[0];
        final int y = loc[1];
        final int a = loc[0];
        final int b = loc[1];

            Toast.makeText(getContext(),"X:"+x+"Y:"+y,Toast.LENGTH_LONG).show();
            TranslateAnimation anim = new TranslateAnimation(x,0,y,0);
            anim.setDuration(3000);
            anim.setFillAfter(true);
            view.startAnimation(anim);
            /*Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_out);
            anim.setFillAfter(true);
            view.startAnimation(anim);*/


    }

}