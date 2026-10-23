package com.example.loginyokaiinfection.Characters;

import java.util.ArrayList;

/** Datos iniciales del catálogo de personajes, basados en el proyecto original. */
public class Datos {
    private final ArrayList<Character> datos;

    public ArrayList<Character> getDatos() {
        return new ArrayList<>(datos);
    }

    public void setDatos(ArrayList<Character> datos) {
        this.datos.clear();
        if (datos != null) {
            this.datos.addAll(datos);
        }
    }

    public Datos() {
        datos = new ArrayList<>();
        datos.add(new Character("Akira Kage - Kitsune",
                "Un zorro espiritual experto en engaños que puede adoptar formas humanas."));
        datos.add(new Character("Raizen - Tengu",
                "Guerrero de las montañas que protege un antiguo templo abandonado."));
        datos.add(new Character("Kuro - Oni",
                "Un poderoso demonio que perdió a su clan y busca al responsable."));
        datos.add(new Character("Mizuchi - Kappa",
                "Un espíritu del agua que apareció tras la desaparición de una aldea."));
        datos.add(new Character("Yuki - Yuki-onna",
                "Un espíritu de la nieve que vaga por las montañas buscando sus recuerdos."));
        datos.add(new Character("Hana - Jorogumo",
                "Una yokai araña que atrae a los viajeros usando una apariencia humana."));
        datos.add(new Character("Ren - Tanuki",
                "Un bromista capaz de transformarse y engañar incluso a otros yokai."));
        datos.add(new Character("Katsuro - Samurai espectral",
                "Un guerrero caído que continúa protegiendo el honor de su antiguo señor."));
        datos.add(new Character("Shiori - Onryo",
                "Un espíritu vengativo que regresó del más allá tras sufrir una traición."));
        datos.add(new Character("Genzo - Noppera-bo",
                "Una misteriosa figura sin rostro cuya verdadera identidad nadie conoce."));
    }
}
