package com.codycus.internshipproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
    private int puzzlePieceInitialWidth = dp2px(80);
    private int targetFrameWidth = dp2px(115);

    public ImageView getTargetView() {
        return targetView;
    }

    public PuzzlePieceView(Context context, ViewGroup puzzlePieceList, ViewGroup targetViewGroup, ArrayList<PuzzlePieceView> otherInteractiveViews) {
        super(context);
        Random randomGenerator = new Random();

        setBackgroundColor(getResources().getColor(R.color.button_default));
        setOnTouchListener(this);

        FrameLayout.LayoutParams interactiveViewLayoutParams = new FrameLayout.LayoutParams(puzzlePieceInitialWidth, puzzlePieceInitialWidth);

        int topMargin = (otherInteractiveViews.size() * puzzlePieceInitialWidth) + (dp2px(10) * (otherInteractiveViews.size() + 1));
        interactiveViewLayoutParams.topMargin = topMargin;
        interactiveViewLayoutParams.leftMargin = dp2px(10);

        puzzlePieceList.addView(this, interactiveViewLayoutParams);

        targetView = new ImageView(getContext());
        targetView.setImageResource(R.drawable.puzzlepiecemissing0);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int viewGroupWidth = displayMetrics.widthPixels - dp2px(100);
        int viewGroupHeight = displayMetrics.heightPixels;

        while (true) {
            targetFrameWidth = randomGenerator.nextInt(100) + puzzlePieceInitialWidth;
            FrameLayout.LayoutParams targetViewParams = new FrameLayout.LayoutParams(targetFrameWidth, targetFrameWidth);
            targetViewParams.leftMargin = randomGenerator.nextInt(Math.max(0, (viewGroupWidth - targetFrameWidth))) + 1;
            targetViewParams.topMargin = randomGenerator.nextInt(Math.max(0, (viewGroupHeight - targetFrameWidth))) + 1;
            targetView.setLayoutParams(targetViewParams);

            boolean intersects = false;
            for (int viewIndex = 0; viewIndex < targetViewGroup.getChildCount(); viewIndex++) {
                View oldTargetView = targetViewGroup.getChildAt(viewIndex);
                if (isViewsIntersects((FrameLayout.LayoutParams) oldTargetView.getLayoutParams(), targetViewParams)) {
                    intersects = true;
                    break;
                }
            }

            if (!intersects) {
                targetViewGroup.addView(targetView, targetViewParams);
                break;
            }
        }
    }

    private boolean isViewsIntersects(FrameLayout.LayoutParams a, FrameLayout.LayoutParams b) {
        return a.leftMargin < b.leftMargin + b.width
                && a.leftMargin + a.width > b.leftMargin
                && a.topMargin < b.topMargin + b.height
                && a.topMargin + a.height > b.topMargin;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int X = (int) event.getRawX();
        int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Float ratio = (float)puzzlePieceInitialWidth / (float)targetFrameWidth;
                v.setScaleX(v.getScaleX() + ratio);
                v.setScaleY(v.getScaleY() + ratio);
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
        Boolean intersects = (x > targetViewMinX && x < (targetViewMinX + targetView.getWidth()))
                && (y > targetViewMinY && y < (targetViewMinY + targetView.getHeight()));

        if (intersects) {
            interactiveView.setVisibility(View.GONE);
            targetView.setVisibility(View.GONE);
        } else {
            interactiveView.returnToOriginalPosition();
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}