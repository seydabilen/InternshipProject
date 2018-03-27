package com.codycus.internshipproject;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewGroup puzzlePieceList, targetLayout;
    private ArrayList<PuzzlePieceView> interactiveViews;
    private ImageView restartButton, backgroundImage;
    MediaPlayer backgroundMusic;
    TextView levelid;
    //int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        backgroundMusic = MediaPlayer.create(MainActivity.this, R.raw.background);
        backgroundMusic.start();
        puzzlePieceList = (ViewGroup) findViewById(R.id.view_root);
        targetLayout = (ViewGroup) findViewById(R.id.target_root);
        restartButton = (ImageView) findViewById(R.id.iv_restart);
        backgroundImage = (ImageView) findViewById(R.id.iv_background);
        levelid=(TextView)findViewById(R.id.level_id);
        levelid.setText("Level 1");
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();


            }
        });

        interactiveViews = new ArrayList<PuzzlePieceView>();
        for (int i = 0; i < 3; i++) {
            PuzzlePieceView puzzlePieceView = new PuzzlePieceView(this, puzzlePieceList, targetLayout, interactiveViews, backgroundImage);
            interactiveViews.add(puzzlePieceView);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundMusic.release();
    }
}




