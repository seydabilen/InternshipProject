package com.codycus.internshipproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Random;
/**
 * Created by Acer on 8.03.2018.
 */

public class PuzzlePieceView extends android.support.v7.widget.AppCompatButton implements View.OnTouchListener {
    private Point firstPoint;
    private ImageView targetView;
    private ViewGroup viewGroup;
    private ArrayList<PuzzlePieceView> otherInteractiveViews;
    private Integer puzzlePieceInitialWidth = 250;
    private Integer targetFrameWidth = 350;

    public ImageView getTargetView(){
        return targetView;
    }

    public PuzzlePieceView(Context context, ViewGroup viewGroup, ViewGroup targetViewGroup, ArrayList<PuzzlePieceView> otherInteractiveViews) {
        super(context);
        this.viewGroup = viewGroup;
        this.otherInteractiveViews = otherInteractiveViews;
        Random randomGenerator = new Random();

        setBackgroundColor(getResources().getColor(R.color.button_default));
        setOnTouchListener(this);

        FrameLayout.LayoutParams interactiveViewLayoutParams = new FrameLayout.LayoutParams(puzzlePieceInitialWidth, puzzlePieceInitialWidth);

        Double leftMargin = otherInteractiveViews.size() * puzzlePieceInitialWidth * 1.25;
        interactiveViewLayoutParams.leftMargin = leftMargin.intValue();

        viewGroup.addView(this, interactiveViewLayoutParams);

        targetView = new ImageView(getContext());
        targetView.setImageResource(R.drawable.puzzlepiecemissing0);

        Integer width =  800;
        Integer height = 500;

        targetFrameWidth = randomGenerator.nextInt(100) + puzzlePieceInitialWidth;
        FrameLayout.LayoutParams targetViewParams = new FrameLayout.LayoutParams(targetFrameWidth,targetFrameWidth);

        targetViewParams.leftMargin=  randomGenerator.nextInt(Math.max(0, (width - targetFrameWidth))) + 1;
        targetViewParams.topMargin= randomGenerator.nextInt(Math.max(0, (height - targetFrameWidth))) + 1;


        //TODO: For döngüsü ile diğer InteractiveView targetleri ile kesişip kesişmediğini kontrol edeceğiz.

        targetViewGroup.addView(targetView, targetViewParams);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int X = (int) event.getRawX();
        int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Float ratio = puzzlePieceInitialWidth.floatValue() /targetFrameWidth.floatValue();
                v.setScaleX(  v.getScaleX()+ratio);
                v.setScaleY( v.getScaleY()+ratio);
                firstPoint = new Point((int) getX(), (int) getY());
                //Point holds two integer coordinates.0,0
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(Color.rgb(255, 214, 28));
                v.setScaleX(1);
                v.setScaleY(1);
                this.onDropView(this, event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                v.setBackgroundColor(Color.GREEN);
                animate().setDuration(0).x(event.getRawX() - getWidth() / 2).y(event.getRawY() - getHeight()).start();
                //if you want to move the view from its center,
                // you only have to substract the view's half height and width to the movement.
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

    public void onDropView(PuzzlePieceView interactiveView, MotionEvent event) {

        Float x = event.getRawX();
        Float y = event.getRawY();
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int targetViewMinX = location[0];
        int targetViewMinY = location[1];
        Boolean intersects = ( x > targetViewMinX && x < (targetViewMinX + targetView.getWidth()))
                && (y > targetViewMinY && y < (targetViewMinY + targetView.getHeight()));

        if (intersects) {
            interactiveView.setVisibility(View.GONE);
            targetView.setVisibility(View.GONE);
        } else {
            interactiveView.returnToOriginalPosition();
        }
    }
}