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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ViewGroup puzzlePieceList, targetLayout;
    private ArrayList<PuzzlePieceView> interactiveViews = new ArrayList<>();
    private ImageView restartButton;
    private ImageView backgroundImage;
    private static MediaPlayer backgroundMusic, expandSound, shrink, fit, success, applause;
    private TextView levelid;
    private int levelNumber = 0;
    private AdView bottomAdView;
    private InterstitialAd mInterstitialAd;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-4272100289211422~9540102157");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        expandSound = MediaPlayer.create(this, R.raw.expand);
        shrink = MediaPlayer.create(this, R.raw.shrink);
        fit = MediaPlayer.create(this, R.raw.fit);
        success = MediaPlayer.create(this, R.raw.success);
        applause = MediaPlayer.create(this, R.raw.applause);

        puzzlePieceList = findViewById(R.id.fl_puzzle_piece_list);
        targetLayout = findViewById(R.id.target_root);
        restartButton = findViewById(R.id.iv_restart);
        backgroundImage = findViewById(R.id.iv_background);
        levelid = findViewById(R.id.level_id);
        bottomAdView = findViewById(R.id.bottomAdView);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelNumber--;
                targetLayout.setOnHierarchyChangeListener(null);
                targetLayout.removeAllViews();
                levelUp();
            }
        });

        levelUp();
        bottomAdView.loadAd(new AdRequest.Builder().build());
        bottomAdView.setVisibility(View.GONE);
    }

    private void levelUp() {
        levelNumber++;
        levelid.setText("Level " + levelNumber);
        puzzlePieceList.removeAllViews();
        interactiveViews = new ArrayList<>();
        backgroundImage.setImageResource(getRandomBackground());
        Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_out);
        animSlide.setFillAfter(true);
        backgroundImage.startAnimation(animSlide);

        int pieceCount = levelNumber;
        if (levelNumber > 5) {
            pieceCount = 5;
        }
        for (int i = 0; i < pieceCount; i++) {
            PuzzlePieceView puzzlePieceView = new PuzzlePieceView(this, puzzlePieceList, targetLayout, interactiveViews, backgroundImage);
            interactiveViews.add(puzzlePieceView);
        }

        targetLayout.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {

            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                if (targetLayout.getChildCount() == 0) {
                    playApplauseSound();
                    levelUp();
                }
            }
        });

        if (levelNumber % 3 == 0) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        if (levelNumber > 2) {
            bottomAdView.setVisibility(View.VISIBLE);
        }
    }

    private int getRandomBackground() {
        return this.getResources().getIdentifier("bg_" + new Random().nextInt(16), "drawable", getPackageName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            backgroundMusic.seekTo(length);
            backgroundMusic.start();
        } catch (Exception e) {
            backgroundMusic = MediaPlayer.create(MainActivity.this, R.raw.background);
            backgroundMusic.start();
            backgroundMusic.setLooping(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundMusic.pause();
        length = backgroundMusic.getCurrentPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundMusic.release();
    }

    public static void playExpandSound() {
        if (!expandSound.isPlaying()) {
            expandSound.start();
        }
    }

    public static void playShrinkSound() {
        if (!shrink.isPlaying()) {
            shrink.start();
        }
    }

    public static void playFitSound() {
        if (!fit.isPlaying()) {
            fit.start();
        }
    }

    public static void playSuccessSound() {
        if (!success.isPlaying()) {
            success.start();
        }
    }

    public static void playApplauseSound() {
        if (!applause.isPlaying()) {
            applause.start();
        }
    }

    // bottom_unit_id : ca-app-pub-4272100289211422/4750809905
    // fullscreen_id : ca-app-pub-4272100289211422/4435949289
}




