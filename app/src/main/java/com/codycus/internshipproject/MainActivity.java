package com.codycus.internshipproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewGroup rootLayout,targetLayout;
    private ArrayList<PuzzlePieceView> interactiveViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (ViewGroup) findViewById(R.id.view_root);
        targetLayout=(ViewGroup)findViewById(R.id.target_root);

        interactiveViews = new ArrayList<PuzzlePieceView>();
        for (int i=0; i < 3 ; i++) {
            PuzzlePieceView puzzlePieceView = new PuzzlePieceView(this, rootLayout, targetLayout, interactiveViews);
            puzzlePieceView.setText("Piece " + i);
            interactiveViews.add(puzzlePieceView);
        }

    }


}




