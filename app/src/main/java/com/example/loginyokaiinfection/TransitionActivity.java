package com.example.loginyokaiinfection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TransitionActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "transition_username";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean navigated = false;

    private final Runnable openSelection = new Runnable() {
        @Override
        public void run() {
            openCharacterSelection();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        TextView welcome = findViewById(R.id.txtTransitionWelcome);
        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        if (username != null && !username.trim().isEmpty()) {
            welcome.setText("BIENVENIDO, " + username.toUpperCase());
        }

        // La transición es breve y también se puede omitir tocando la pantalla.
        findViewById(R.id.transitionRoot).setOnClickListener(v -> openCharacterSelection());
        handler.postDelayed(openSelection, 1000);
    }

    private void openCharacterSelection() {
        if (navigated || isFinishing()) {
            return;
        }
        navigated = true;
        handler.removeCallbacks(openSelection);
        startActivity(new Intent(this, SelectCharacterActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(openSelection);
        super.onDestroy();
    }
}
