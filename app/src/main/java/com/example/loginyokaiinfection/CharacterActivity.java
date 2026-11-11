
package com.example.loginyokaiinfection;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CharacterActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "yokai_name";
    public static final String EXTRA_TYPE = "yokai_type";
    public static final String EXTRA_LORE = "yokai_lore";
    public static final String EXTRA_IMAGE = "yokai_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        ImageView image = findViewById(R.id.imgYokaiDetail);
        TextView name = findViewById(R.id.txtYokaiName);
        TextView type = findViewById(R.id.txtYokaiType);
        TextView lore = findViewById(R.id.txtYokaiLore);
        Button back = findViewById(R.id.btnBackToSelection);
        Button play = findViewById(R.id.btnPlayCharacter);

        String characterName = getIntent().getStringExtra(EXTRA_NAME);
        String characterType = getIntent().getStringExtra(EXTRA_TYPE);
        String characterLore = getIntent().getStringExtra(EXTRA_LORE);
        int imageRes = getIntent().getIntExtra(EXTRA_IMAGE, 0);

        name.setText(characterName);
        type.setText(characterType);
        lore.setText(characterLore);


        if (imageRes != 0) {
            image.setImageResource(imageRes);
        }
        play.setOnClickListener(view ->
                showPlayConfirmation(
                        characterName,
                        imageRes
                )
        );

        back.setOnClickListener(view -> finish());
    }
    private void showPlayConfirmation(
            String characterName,
            int imageRes) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm_play);
        dialog.setCancelable(true);

        ImageView dialogImage =dialog.findViewById(R.id.imgDialogCharacter);
        TextView dialogName = dialog.findViewById(R.id.txtDialogCharacterName);
        TextView dialogMessage = dialog.findViewById(R.id.txtDialogCharacterMessage);
        Button cancel = dialog.findViewById(R.id.btnDialogCancel);
        Button play = dialog.findViewById(R.id.btnDialogPlay);

        dialogName.setText(characterName);
        dialogMessage.setText(getString(R.string.confirm_play_message,characterName));

        if (imageRes != 0) {
            dialogImage.setImageResource(imageRes);
        }

        cancel.setOnClickListener(view -> dialog.dismiss());

        play.setOnClickListener(view -> {
            Toast.makeText(
                    this,
                    "La partida estará disponible próximamente",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();
        });

        dialog.show();

        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );

            WindowManager.LayoutParams params =
                    new WindowManager.LayoutParams();

            params.copyFrom(window.getAttributes());
            params.width = (int) (
                    getResources()
                            .getDisplayMetrics()
                            .widthPixels * 0.90
            );
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;

            window.setAttributes(params);
        }
    }
}
