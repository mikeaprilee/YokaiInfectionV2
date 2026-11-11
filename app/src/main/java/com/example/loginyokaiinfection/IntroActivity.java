package com.example.loginyokaiinfection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private boolean loginOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        FullScreenVideoView videoIntro =
                findViewById(R.id.videoIntro);

        Uri videoUri = Uri.parse(
                "android.resource://"
                        + getPackageName()
                        + "/"
                        + R.raw.intro_video
        );

        videoIntro.setVideoURI(videoUri);

        videoIntro.setOnPreparedListener(mediaPlayer -> {
            videoIntro.setVideoSize(
                    mediaPlayer.getVideoWidth(),
                    mediaPlayer.getVideoHeight()
            );

            videoIntro.start();
        });

        videoIntro.setOnCompletionListener(mediaPlayer ->
                openLogin()
        );

        videoIntro.setOnErrorListener((mediaPlayer, what, extra) -> {
            openLogin();
            return true;
        });

    }

    private void openLogin() {
        if (loginOpened) {
            return;
        }

        loginOpened = true;

        Intent intent = new Intent(
                IntroActivity.this,
                MainActivity.class
        );

        startActivity(intent);
        finish();
    }
}