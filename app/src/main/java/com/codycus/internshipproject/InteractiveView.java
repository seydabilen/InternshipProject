package com.codycus.internshipproject;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by Acer on 8.03.2018.
 */

public class InteractiveView extends View implements View.OnTouchListener {

    private Button mainButton;
    private int _xdelta;
    private int _ydelta;



    public InteractiveView(Context context) {
        super(context);
        mainButton = new Button(context);
        mainButton.setBackgroundColor(getResources().getColor(R.color.button_default));
        mainButton.setText("Button1");
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(250, 250);
        mainButton.setLayoutParams(layoutParams);
        rootLayout.addView(mainButton);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int X = (int) event.getRawX();
        int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                _xdelta = X - lParams.leftMargin;
                _ydelta = Y - lParams.topMargin;
                v.setScaleX(v.getScaleX() + 0.5f);
                v.setScaleY(v.getScaleY() + 0.5f);
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(Color.rgb(28, 118, 187));
                v.setScaleX(1);
                v.setScaleY(1);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                v.setBackgroundColor(Color.GREEN);
                FrameLayout.LayoutParams layoutParams0 = (FrameLayout.LayoutParams) v.getLayoutParams();
                layoutParams0.leftMargin = X - _xdelta;
                layoutParams0.topMargin = Y - _ydelta;
                layoutParams0.rightMargin = 0;
                layoutParams0.bottomMargin = 0;
                v.setLayoutParams(layoutParams0);

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }

        rootLayout.invalidate();
        return true;
    }

}
