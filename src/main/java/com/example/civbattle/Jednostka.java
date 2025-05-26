package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract class Jednostka extends Obiekt {
    int zycie;
    int idCywilizacji;
    public Point poprzedniaPozycja;

    public Jednostka(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    public Jednostka(int id, Point pP, int pidCywilizacji) {
        super(id, pP);
        this.idCywilizacji = pidCywilizacji;
    }

    public Jednostka(int id, Point pP) {
        super(id, pP);

    }

    public java.util.List<Point> getSasiedzi(Point p, int szerokosc, int wysokosc) {
        List<Point> wynik = new ArrayList<>();
        int[] dx = {-1, 0, 1};
        int[] dy = {-1, 0, 1};

        for (int x : dx) {
            for (int y : dy) {
                if (x == 0 && y == 0) continue;

                int nx = p.x + x;
                int ny = p.y + y;

                if (nx >= 0 && nx < szerokosc && ny >= 0 && ny < wysokosc) {
                    wynik.add(new Point(nx, ny));
                }
            }
        }

        return wynik;
    }


    public abstract int ruch(Symulacja sim);
}