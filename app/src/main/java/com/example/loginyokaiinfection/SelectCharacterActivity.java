package com.example.loginyokaiinfection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginyokaiinfection.Characters.YokaiCharacter;

import java.util.List;
import java.util.Locale;

public class SelectCharacterActivity extends AppCompatActivity {

    private RecyclerView recyclerYokai;
    private UserDatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_character);

        recyclerYokai = findViewById(R.id.recyclerYokai);
        database = new UserDatabaseHelper(this);

        List<YokaiCharacter> characters = database.getCharacters();

        if (characters.isEmpty()) {
            Toast.makeText(
                    this,
                    "No se han encontrado personajes en la base de datos.",
                    Toast.LENGTH_LONG).show();
            return;
        };

        recyclerYokai.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerYokai.setAdapter(new CharacterAdapter(this, characters));
    };


    @Override
    protected void onDestroy() {
        if (database != null) {
            database.close();
        };
        super.onDestroy();
    };
};
