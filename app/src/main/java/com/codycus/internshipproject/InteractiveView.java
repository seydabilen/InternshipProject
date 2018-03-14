package com.codycus.internshipproject;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by Acer on 8.03.2018.
 */

public class InteractiveView extends android.support.v7.widget.AppCompatButton implements View.OnTouchListener {

    private int _xdelta;
    private int _ydelta;
    private InteractiveViewListener interactiveViewListener;

    public interface InteractiveViewListener{
        public void onDropView(InteractiveView view, MotionEvent event);
    }

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
                _ydelta = Y - lParams.topMargin;//
                v.setScaleX(v.getScaleX() + 0.5f);
                v.setScaleY(v.getScaleY() + 0.5f);
                break;
            case MotionEvent.ACTION_UP:
                FrameLayout.LayoutParams lParams1 = (FrameLayout.LayoutParams) v.getLayoutParams();
                v.setBackgroundColor(Color.rgb(28, 118, 187));
                v.setScaleX(1);
                v.setScaleY(1);
                lParams1.leftMargin=X-_xdelta;
                lParams1.topMargin=Y-_ydelta;
                returnToOriginalPosition(lParams1.leftMargin,lParams1.topMargin,0,0);
                interactiveViewListener.onDropView(this, event);
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

    public void returnToOriginalPosition(int left, int top, int right, int bottom){
      /* Toast.makeText(getContext(),"X:"+left+"Y:"+top,Toast.LENGTH_LONG).show();
        TranslateAnimation anim = new TranslateAnimation(right, -left,bottom, -top);
        anim.setDuration(500);
       // anim.setFillAfter(true);
        this.startAnimation(anim);*/

        this.setX(this.getX() -left);
        this.setY( this.getY() -top);
        _xdelta = 0;
        _ydelta = 0;
    }

    public void setInteractiveViewListener(InteractiveViewListener interactiveViewListener) {
        this.interactiveViewListener = interactiveViewListener;
    }

}