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
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Acer on 8.03.2018.
 */

public class PuzzlePieceView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener {
    private Point firstPoint;
    private ImageView targetView;
    private int puzzlePieceInitialWidth = dp2px(50);
    private int targetFrameWidth = dp2px(115);
    private MediaPlayer expand, shrink, fit, success;
    private int viewGroupWidth, viewGroupHeight;
    private int maskResourceId;


    public ImageView getTargetView() {
        return targetView;
    }

    public PuzzlePieceView(Context context, ViewGroup puzzlePieceList, ViewGroup targetViewGroup, ArrayList<PuzzlePieceView> otherInteractiveViews, ImageView backgroundImage) {
        super(context);
        setOnTouchListener(this);

        Random randomGenerator = new Random();

        FrameLayout.LayoutParams interactiveViewLayoutParams = new FrameLayout.LayoutParams(puzzlePieceInitialWidth, puzzlePieceInitialWidth);
        int topMargin = (otherInteractiveViews.size() * puzzlePieceInitialWidth) + (dp2px(10) * (otherInteractiveViews.size() + 1));
        interactiveViewLayoutParams.topMargin = topMargin;
        interactiveViewLayoutParams.leftMargin = dp2px(10);

        targetView = new ImageView(getContext());
        maskResourceId = getRandomMask();
        targetView.setImageResource(maskResourceId);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        viewGroupWidth = displayMetrics.widthPixels - dp2px(80);
        viewGroupHeight = displayMetrics.heightPixels;

        while (true) {
            targetFrameWidth = randomGenerator.nextInt(200) + puzzlePieceInitialWidth;
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
                BitmapDrawable drawable = (BitmapDrawable) backgroundImage.getDrawable();
                Bitmap originalImage = drawable.getBitmap();
                double widthAspect = (double) originalImage.getWidth() / (double) viewGroupWidth;
                double heightAspect = (double) originalImage.getHeight() / (double) viewGroupHeight;

                Bitmap originalMask = BitmapFactory.decodeResource(getResources(), maskResourceId);
                double originalMaskRatio = originalMask.getWidth() / originalMask.getHeight();
                Bitmap croppedBitmap = Bitmap.createBitmap(originalImage, (int) (targetViewParams.leftMargin * widthAspect), (int) (targetViewParams.topMargin * heightAspect), (int) (targetViewParams.width * widthAspect), (int) (targetViewParams.height * heightAspect));
                Bitmap resizedMask = Bitmap.createScaledBitmap(originalMask, croppedBitmap.getWidth(), croppedBitmap.getWidth(), false);
                Bitmap result = Bitmap.createBitmap(resizedMask.getWidth(), resizedMask.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas mCanvas = new Canvas(result);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                mCanvas.drawBitmap(croppedBitmap, 0, 0, null);
                mCanvas.drawBitmap(resizedMask, 0, 0, paint);
                paint.setXfermode(null);
                setScaleType(ScaleType.FIT_CENTER);
                setImageBitmap(result);


                targetViewGroup.addView(targetView, targetViewParams);
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
                ((ScrollView) ((FrameLayout) v.getParent()).getParent()).requestDisallowInterceptTouchEvent(true);
                expand = MediaPlayer.create(getContext(), R.raw.expand);
                expand.start();
                Float ratio = (float) targetFrameWidth / (float) puzzlePieceInitialWidth;
                v.setScaleX(ratio);
                v.setScaleY(ratio);
                break;
            case MotionEvent.ACTION_UP:
                ((ScrollView) ((FrameLayout) v.getParent()).getParent()).requestDisallowInterceptTouchEvent(false);
                v.setScaleX(1);
                v.setScaleY(1);
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
        shrink = MediaPlayer.create(getContext(), R.raw.shrink);
        shrink.start();
        animate().setDuration(200).x(firstPoint.x).y(firstPoint.y).start();
    }

    public void onDropView(PuzzlePieceView interactiveView, MotionEvent event) {

        Float x = event.getRawX() - interactiveView.getWidth() / 4;
        Float y = event.getRawY() - interactiveView.getHeight() / 4;
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int targetViewMinX = location[0];
        int targetViewMinY = location[1];
        Boolean intersects = (x > targetViewMinX && x < (targetViewMinX + targetView.getWidth()))
                && (y > targetViewMinY && y < (targetViewMinY + targetView.getHeight()));

        if (intersects) {
            fit = MediaPlayer.create(getContext(), R.raw.fit);
            fit.start();
            success = MediaPlayer.create(getContext(), R.raw.success);
            success.start();
            makeCompleted();
        } else {
            interactiveView.returnToOriginalPosition();
        }
    }

    public void makeCompleted() {
        if ( null != targetView.getParent()) {
            ((ViewGroup) targetView.getParent()).removeView(targetView);
            //((ViewGroup) this.getParent()).removeView(this);

        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int getRandomMask(){
        return this.getResources().getIdentifier("mask_" + new Random().nextInt(16), "drawable", getContext().getPackageName());
    }
}