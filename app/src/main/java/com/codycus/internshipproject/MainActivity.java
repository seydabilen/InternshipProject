package com.codycus.internshipproject;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ViewGroup puzzlePieceList, targetLayout;
    private ArrayList<PuzzlePieceView> interactiveViews;
    private ImageView restartButton;
    private ImageView backgroundImage;
    private MediaPlayer backgroundMusic;
    private TextView levelid;
    private int levelNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        backgroundMusic = MediaPlayer.create(MainActivity.this, R.raw.background);
        backgroundMusic.start();
        backgroundMusic.setLooping(true);
        puzzlePieceList = (ViewGroup) findViewById(R.id.view_root);
        targetLayout = (ViewGroup) findViewById(R.id.target_root);
        restartButton = (ImageView) findViewById(R.id.iv_restart);
        backgroundImage = (ImageView) findViewById(R.id.iv_background);
        levelid = (TextView) findViewById(R.id.level_id);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelUp();

            }
        });

        targetLayout.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {

            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                if (targetLayout.getChildCount() == 0) {
                    levelUp();
                }
            }
        });

        levelUp();
    }

    private void levelUp() {
        levelNumber++;
        levelid.setText("Level " + levelNumber);
        puzzlePieceList.removeAllViews();
        backgroundImage.setImageResource(getRandomBackground());
        Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_out);
        animSlide.setFillAfter(true);
        backgroundImage.startAnimation(animSlide);

        interactiveViews = new ArrayList<PuzzlePieceView>();

        for (int i = 0; i < levelNumber; i++) {
            PuzzlePieceView puzzlePieceView = new PuzzlePieceView(this, puzzlePieceList, targetLayout, interactiveViews, backgroundImage);
            interactiveViews.add(puzzlePieceView);
        }

    }

    private int getRandomBackground() {
        return this.getResources().getIdentifier("bg_" + new Random().nextInt(16), "drawable", getPackageName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundMusic.release();
    }
}




