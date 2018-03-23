package com.codycus.internshipproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewGroup puzzlePieceList, targetLayout;
    private ArrayList<PuzzlePieceView> interactiveViews;
    private ImageView restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        puzzlePieceList = (ViewGroup) findViewById(R.id.view_root);
        targetLayout = (ViewGroup) findViewById(R.id.target_root);
        restartButton = (ImageView) findViewById(R.id.iv_restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        interactiveViews = new ArrayList<PuzzlePieceView>();
        for (int i = 0; i < 3; i++) {
            PuzzlePieceView puzzlePieceView = new PuzzlePieceView(this, puzzlePieceList, targetLayout, interactiveViews);
            puzzlePieceView.setText("Piece " + i);
            interactiveViews.add(puzzlePieceView);
        }
    }
}




