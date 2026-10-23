
package com.example.loginyokaiinfection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

        name.setText(getIntent().getStringExtra(EXTRA_NAME));
        type.setText(getIntent().getStringExtra(EXTRA_TYPE));
        lore.setText(getIntent().getStringExtra(EXTRA_LORE));

        int imageRes = getIntent().getIntExtra(EXTRA_IMAGE, 0);

        if (imageRes != 0) {
            image.setImageResource(imageRes);
        }

        back.setOnClickListener(view -> finish());
    }
}
