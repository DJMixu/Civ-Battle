package com.example.civbattle;

import java.awt.*;

public class Surowiec extends Obiekt {
    int[] wartosciSurowca = {0, 0, 0};
    public int pTyp; // typ surowca: 0=stone, 1=wood, 2=gold

    public Surowiec(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    public Surowiec(int id, Point pP, int pJakosc, int pTyp) {
        super(id, pP);
        this.pTyp = pTyp;
        generujWartosci(pJakosc, pTyp);
    }

    public void generujWartosci(int pJakosc, int pTyp) {
        wartosciSurowca[0] = pJakosc * 10;
        wartosciSurowca[1] = pJakosc * 10;
        wartosciSurowca[2] = pJakosc * 10;
        wartosciSurowca[pTyp] = wartosciSurowca[pTyp] * 3;
    }
}
