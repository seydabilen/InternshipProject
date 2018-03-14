package com.codycus.internshipproject;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Acer on 14.03.2018.
 */

public class PuzzleEngine implements InteractiveView.InteractiveViewListener {

    private View targetView;
    private ViewGroup canvasView;

    @Override
    public void onDropView(InteractiveView interactiveView, MotionEvent event) {


        // bizim butun dunyamiz burasi olmali
        // hangi view ne kadar hareket etti
        // ne oldu ne bitti
        // simdi ne yapilmasi gerekiyor burasi karar verecek

        // elimizde her sey mevcut, target da belli, canvas(tuval yani en alttaki rootLayout) da elimizin altinda


        //if(interactiveView targetView'in uzerinde ise){
        //    interactiveView'i gizle
        //}


        // kaydirma vesaire turu isleri de buradan yonetebiliriz
        //interactiveView.returnToOriginalPosition(0,0,0,0);

    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }

    public void setCanvasView(ViewGroup canvasView) {
        this.canvasView = canvasView;
    }
}
