package it.uniba.dib.sysag.pepper4seriousgames.First_game;

import it.uniba.dib.sysag.pepper4seriousgames.R;

public class DatabaseAnimals {

    // Lista domande = nomi degli animali che appaiono
    public static String[] questions = new String []{
            "cane",
            "cavallo",
            "delfino",
            "gabbiano",
            "gallina",
            "granchio",
            "maiale",
            "mucca",
            "foca",
            "papera",
            "pecora",
            "pesce",
    };

    // Lista immagini degli animali che appaiono
    public static int[] images = new int[] {
            R.drawable.cane,
            R.drawable.cavallo,
            R.drawable.delfino,
            R.drawable.gabbiano,
            R.drawable.gallina,
            R.drawable.granchio,
            R.drawable.maiale,
            R.drawable.mucca,
            R.drawable.foca,
            R.drawable.papera,
            R.drawable.pecora,
            R.drawable.pesce,
    };

    // Lista risposte all'ambiente a cui fa parte ogni animale
    public static String[] answers = new String []{
            "terra", "terra", "mare", "mare", "terra", "mare", "terra", "terra", "mare", "terra", "terra", "mare",
    };

}

