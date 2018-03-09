package com.codycus.internshipproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewGroup rootLayout;
    FrameLayout target0;
    TextView targetText;
    InteractiveView myInteractiveView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (ViewGroup) findViewById(R.id.view_root);
        FrameLayout.LayoutParams inreactiveViewLayoutParams = new FrameLayout.LayoutParams(400, 400);
        FrameLayout.LayoutParams inreactiveViewLayoutParams2 = new FrameLayout.LayoutParams(400, 400);

        InteractiveView firstInteractiveView = new InteractiveView(this);
        firstInteractiveView.setText("Button1");
        rootLayout.addView(firstInteractiveView, inreactiveViewLayoutParams);

       /* InteractiveView secondInteractiveView = new InteractiveView(this);
        secondInteractiveView.setText("Button2");
        rootLayout.addView(secondInteractiveView, inreactiveViewLayoutParams2);*/

    }


}




