package com.codycus.internshipproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Acer on 8.03.2018.
 */

public class InteractiveView extends android.support.v7.widget.AppCompatButton implements View.OnTouchListener {

    private InteractiveViewListener interactiveViewListener;
    private Point firstPoint;

    public interface InteractiveViewListener {
         void onDropView(InteractiveView view, MotionEvent event);
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
                v.setScaleX(v.getScaleX() + 0.5f);
                v.setScaleY(v.getScaleY() + 0.5f);

                firstPoint = new Point((int) getX(), (int) getY());
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(Color.rgb(28, 118, 187));

                v.setScaleX(1);
                v.setScaleY(1);

                interactiveViewListener.onDropView(this, event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                v.setBackgroundColor(Color.GREEN);

                animate().setDuration(0).x(event.getRawX() - getWidth() / 2).y(event.getRawY() - getHeight()).start();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        invalidate();
        return true;
    }

    public void returnToOriginalPosition() {
        animate().setDuration(200).x(firstPoint.x).y(firstPoint.y).start();
    }

    public void setInteractiveViewListener(InteractiveViewListener interactiveViewListener) {
        this.interactiveViewListener = interactiveViewListener;
    }

}