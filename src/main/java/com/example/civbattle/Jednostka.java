package com.example.civbattle;

import java.awt.*;

abstract class Jednostka extends Obiekt {
    int zycie;
    int idCywilizacji;
    public Point poprzedniaPozycja;

    public Jednostka(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    public Jednostka(int id, Point pP , int pidCywilizacji) {
        super(id, pP);
        this.idCywilizacji = pidCywilizacji;
    }
    public Jednostka(int id, Point pP) {
        super(id, pP);

    }


    public abstract int ruch(Symulacja sim);
}