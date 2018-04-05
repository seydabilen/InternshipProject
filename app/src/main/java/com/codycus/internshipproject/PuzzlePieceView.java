package com.codycus.internshipproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
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

public class PuzzlePieceView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener {
    private Point firstPoint;
    private ImageView blackMaskView;
    private int puzzlePieceInitialWidth = dp2px(50);
    private int puzzlePieceInitialHeight;
    private int targetFrameWidth;
    private int targetFrameHeight;
    private int viewGroupWidth, viewGroupHeight;
    private int maskResourceId;

    public PuzzlePieceView(Context context, ViewGroup puzzlePieceList, ViewGroup targetViewGroup, ArrayList<PuzzlePieceView> otherInteractiveViews, ImageView backgroundImage) {
        super(context);
        setOnTouchListener(this);

        Random randomGenerator = new Random();

        blackMaskView = new ImageView(getContext());
        maskResourceId = getRandomMask();
        Bitmap originalMask = BitmapFactory.decodeResource(getResources(), maskResourceId);

        puzzlePieceInitialHeight = (int) ((double) originalMask.getHeight() / (double) originalMask.getWidth() * (double) puzzlePieceInitialWidth);

        FrameLayout.LayoutParams interactiveViewLayoutParams = new FrameLayout.LayoutParams(puzzlePieceInitialWidth, puzzlePieceInitialHeight);
        int marginSum = dp2px(10);
        for (View view : otherInteractiveViews) {
            marginSum += view.getLayoutParams().height + dp2px(10);
        }
        interactiveViewLayoutParams.topMargin = marginSum;
        interactiveViewLayoutParams.leftMargin = dp2px(10);

        blackMaskView.setImageResource(maskResourceId);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        viewGroupWidth = displayMetrics.widthPixels - dp2px(80);
        viewGroupHeight = displayMetrics.heightPixels;

        while (true) {
            targetFrameWidth = randomGenerator.nextInt(dp2px(80)) + puzzlePieceInitialWidth;
            targetFrameHeight = (int) ((double) originalMask.getHeight() / (double) originalMask.getWidth() * (double) targetFrameWidth);
            FrameLayout.LayoutParams targetViewParams = new FrameLayout.LayoutParams(targetFrameWidth, targetFrameHeight);
            targetViewParams.leftMargin = randomGenerator.nextInt(Math.max(0, (viewGroupWidth - targetFrameWidth))) + 1;
            targetViewParams.topMargin = randomGenerator.nextInt(Math.max(0, (viewGroupHeight - dp2px(60) - targetFrameWidth))) + 1;
            blackMaskView.setLayoutParams(targetViewParams);

            boolean intersects = false;
            for (int viewIndex = 0; viewIndex < targetViewGroup.getChildCount(); viewIndex++) {
                View otherPieceView = targetViewGroup.getChildAt(viewIndex);
                if (otherPieceView != null) {
                    if (isViewsIntersects((FrameLayout.LayoutParams) otherPieceView.getLayoutParams(), targetViewParams)) {
                        intersects = true;
                        break;
                    }
                }
            }

            if (!intersects) {
                BitmapDrawable drawable = (BitmapDrawable) backgroundImage.getDrawable();
                Bitmap originalImage = drawable.getBitmap();
                double widthAspect = (double) originalImage.getWidth() / (double) viewGroupWidth;
                double heightAspect = (double) originalImage.getHeight() / (double) viewGroupHeight;

                Bitmap croppedBitmap = Bitmap.createBitmap(originalImage, (int) (targetViewParams.leftMargin * widthAspect), (int) (targetViewParams.topMargin * heightAspect), (int) (targetViewParams.width * widthAspect), (int) (targetViewParams.height * widthAspect));

                Bitmap resizedMask = Bitmap.createScaledBitmap(originalMask, croppedBitmap.getWidth(), croppedBitmap.getHeight(), false);

                Bitmap result = Bitmap.createBitmap(resizedMask.getWidth(), resizedMask.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas mCanvas = new Canvas(result);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                mCanvas.drawBitmap(croppedBitmap, 0, 0, null);
                mCanvas.drawBitmap(resizedMask, 0, 0, paint);
                paint.setXfermode(null);
                setScaleType(ScaleType.FIT_CENTER);
                setImageBitmap(result);


                targetViewGroup.addView(blackMaskView, targetViewParams);
                break;
            }
        }

        puzzlePieceList.addView(this, interactiveViewLayoutParams);
        firstPoint = new Point((int) interactiveViewLayoutParams.leftMargin, (int) interactiveViewLayoutParams.topMargin);
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
                MainActivity.playExpandSound();
                Float ratio = (float) targetFrameWidth / (float) puzzlePieceInitialWidth;
                animate().setDuration(300).scaleX(ratio).scaleY(ratio);
                break;
            case MotionEvent.ACTION_UP:
                animate().setDuration(500).scaleX(1).scaleY(1);
                this.onDropView(this, event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                animate().setDuration(0).x(event.getRawX() - getWidth() / 2).y(event.getRawY() - getHeight()).start();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        invalidate();
        return true;
    }

    public void returnToOriginalPosition() {
        MainActivity.playShrinkSound();
        animate().setDuration(200).x(firstPoint.x).y(firstPoint.y).start();
    }

    public void onDropView(PuzzlePieceView interactiveView, MotionEvent event) {
        MainActivity.playFitSound();

        Float x = event.getRawX() - interactiveView.getWidth() / 4;
        Float y = event.getRawY() - interactiveView.getHeight() / 4;
        int[] location = new int[2];
        blackMaskView.getLocationOnScreen(location);
        int targetViewMinX = location[0];
        int targetViewMinY = location[1];
        Boolean intersects = (x > targetViewMinX && x < (targetViewMinX + blackMaskView.getWidth()))
                && (y > targetViewMinY && y < (targetViewMinY + blackMaskView.getHeight()));

        if (intersects) {
            MainActivity.playSuccessSound();
            makeCompleted();
        } else {
            interactiveView.returnToOriginalPosition();
        }
    }

    public void makeCompleted() {
        if (blackMaskView.getParent() != null) {
            ((ViewGroup) blackMaskView.getParent()).removeView(blackMaskView);
        }

        if (this.getParent() != null) {
            ((ViewGroup) this.getParent()).removeView(this);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int getRandomMask() {
        return this.getResources().getIdentifier("mask_" + new Random().nextInt(15), "drawable", getContext().getPackageName());
    }
}