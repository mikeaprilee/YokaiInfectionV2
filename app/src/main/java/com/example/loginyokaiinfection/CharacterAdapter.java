package com.example.loginyokaiinfection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginyokaiinfection.Characters.YokaiCharacter;

import java.util.List;
import java.util.Locale;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private final Context context;
    private final List <YokaiCharacter> characters;

    public CharacterAdapter (Context context, List <YokaiCharacter> characters){
        this.context = context;
        this.characters = characters;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent,false);
        return new CharacterViewHolder(view);
    }
    @Override
    public void onBindViewHolder(
            @NonNull CharacterViewHolder holder, int position) {

        YokaiCharacter character = characters.get(position);

        holder.name.setText(character.getNombre().toUpperCase(Locale.ROOT));
        holder.type.setText(character.getTipo().toUpperCase(Locale.ROOT)        );

        int imageRes = context.getResources().getIdentifier(
                character.getImagen(),
                "drawable",
                context.getPackageName()
        );

        if (imageRes != 0) {
            holder.image.setImageResource(imageRes);
        } else {
            holder.image.setImageDrawable(null);
        }

        holder.itemView.setContentDescription(
                "Seleccionar "
                        + character.getNombre()
                        + ", "
                        + character.getTipo()
        );

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CharacterActivity.class);
            intent.putExtra(CharacterActivity.EXTRA_NAME,character.getNombre());
            intent.putExtra(CharacterActivity.EXTRA_TYPE,character.getTipo());
            intent.putExtra(CharacterActivity.EXTRA_LORE,character.getDescripcion());
            intent.putExtra(CharacterActivity.EXTRA_IMAGE,imageRes);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder{

        private final ImageView image;
        private final TextView name;
        private final TextView type;

        CharacterViewHolder(@NonNull View itemView){
            super(itemView);

            image = itemView.findViewById(R.id.imgCharacter);
            name = itemView.findViewById(R.id.txtCharacterName);
            type = itemView.findViewById(R.id.txtCharacterType);
        }
    }
}
