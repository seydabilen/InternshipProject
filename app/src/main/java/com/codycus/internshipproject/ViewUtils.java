package com.codycus.internshipproject;

import android.graphics.Point;
import android.view.View;

/**
 * Created by andacozcan on 3/14/18.
 */

public class ViewUtils {

    public static boolean isViewCoversAnother(View targetView, View movingView) {
        Point centerOfMoving = new Point((int) targetView.getX() + targetView.getWidth() / 2, (int) targetView.getY() + targetView.getHeight() / 2);
        return ispointInView(centerOfMoving, movingView);
    }

    private static boolean ispointInView(Point p, View movingView) {
        return p.x >= movingView.getX()
                && p.y >= movingView.getY()
                && p.x <= movingView.getX() + movingView.getWidth()
                && p.y <= movingView.getY() + movingView.getHeight();
    }
}
