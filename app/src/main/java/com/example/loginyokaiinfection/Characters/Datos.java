package com.example.loginyokaiinfection.Characters;

import java.util.ArrayList;

/** Datos iniciales del catálogo de personajes, basados en el proyecto original. */
public class Datos {
    private final ArrayList<YokaiCharacter> datos;

    public ArrayList<YokaiCharacter> getDatos() {
        return new ArrayList<>(datos);
    }

    public void setDatos(ArrayList<YokaiCharacter> datos) {
        this.datos.clear();
        if (datos != null) {
            this.datos.addAll(datos);
        }
    }

    public Datos() {
        datos = new ArrayList<>();
        datos.add(new YokaiCharacter("Akira Kage - Kitsune",
                "Un zorro espiritual experto en engaños que puede adoptar formas humanas."));
        datos.add(new YokaiCharacter("Raizen - Tengu",
                "Guerrero de las montañas que protege un antiguo templo abandonado."));
        datos.add(new YokaiCharacter("Kuro - Oni",
                "Un poderoso demonio que perdió a su clan y busca al responsable."));
        datos.add(new YokaiCharacter("Mizuchi - Kappa",
                "Un espíritu del agua que apareció tras la desaparición de una aldea."));
        datos.add(new YokaiCharacter("Yuki - Yuki-onna",
                "Un espíritu de la nieve que vaga por las montañas buscando sus recuerdos."));
        datos.add(new YokaiCharacter("Hana - Jorogumo",
                "Una yokai araña que atrae a los viajeros usando una apariencia humana."));
        datos.add(new YokaiCharacter("Ren - Tanuki",
                "Un bromista capaz de transformarse y engañar incluso a otros yokai."));
        datos.add(new YokaiCharacter("Katsuro - Samurai espectral",
                "Un guerrero caído que continúa protegiendo el honor de su antiguo señor."));
        datos.add(new YokaiCharacter("Shiori - Onryo",
                "Un espíritu vengativo que regresó del más allá tras sufrir una traición."));
        datos.add(new YokaiCharacter("Genzo - Noppera-bo",
                "Una misteriosa figura sin rostro cuya verdadera identidad nadie conoce."));
    }
}
