package com.example.loginyokaiinfection;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginyokaiinfection.Characters.Character;

import java.util.List;
import java.util.Locale;

public class SelectCharacterActivity extends AppCompatActivity {

    private GridLayout gridYokai;
    private UserDatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_character);

        gridYokai = findViewById(R.id.gridYokai);
        database = new UserDatabaseHelper(this);

        List<Character> characters = database.getCharacters();
        if (characters.isEmpty()) {
            Toast.makeText(this,
                    "No se han encontrado personajes en la base de datos.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        for (Character character : characters) {
            addYokaiButton(character);
        }
    }

    private void addYokaiButton(Character yokai) {
        Button button = new Button(this);

        button.setText(
                yokai.getNombre().toUpperCase(Locale.ROOT)
                        + "\n"
                        + yokai.getTipo().toUpperCase(Locale.ROOT)
        );

        button.setAllCaps(false);
        button.setTextColor(Color.WHITE);
        button.setTextSize(13);
        button.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundResource(R.drawable.card_button_background);
        button.setPadding(dp(6), dp(8), dp(6), dp(8));
        button.setCompoundDrawablePadding(dp(8));
        button.setContentDescription(
                "Seleccionar " + yokai.getNombre() + ", " + yokai.getTipo());

        int imageRes = getResources().getIdentifier(
                yokai.getImagen(), "drawable", getPackageName());
        if (imageRes != 0) {
            Drawable portrait = getDrawable(imageRes);
            if (portrait != null) {
                portrait.setBounds(0, 0, dp(126), dp(126));
                button.setCompoundDrawables(null, portrait, null, null);
            }
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = dp(220);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(dp(5), dp(5), dp(5), dp(5));
        button.setLayoutParams(params);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(
                    SelectCharacterActivity.this,
                    CharacterActivity.class);
            intent.putExtra(CharacterActivity.EXTRA_NAME, yokai.getNombre());
            intent.putExtra(CharacterActivity.EXTRA_TYPE, yokai.getTipo());
            intent.putExtra(CharacterActivity.EXTRA_LORE, yokai.getDescripcion());
            intent.putExtra(CharacterActivity.EXTRA_IMAGE, imageRes);
            startActivity(intent);
        });

        gridYokai.addView(button);
    }

    private int dp(int value) {
        return Math.round(
                value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        if (database != null) {
            database.close();
        }
        super.onDestroy();
    }
}
