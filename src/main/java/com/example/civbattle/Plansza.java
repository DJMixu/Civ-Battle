package com.example.civbattle;

import java.awt.*;
import java.util.List;

public class Plansza {
    private Obiekt[][] plansza;

    public Plansza(int px, int py) {
        plansza = new Obiekt[px][py];
    }

    public Obiekt zwrocPole(int x, int y) {
        return plansza[x][y];
    }

    public Obiekt zwrocPole(Point pPozycja) {
        return plansza[pPozycja.x][pPozycja.y];
    }

    public void dodajObiekt(Point pozycja, Obiekt obiekt) {
        plansza[pozycja.x][pozycja.y] = obiekt;
        System.out.println("dodano" + plansza[pozycja.x][pozycja.y].toString());
    }

    public void dodajObiekty(List<Obiekt> lista) {
        for (Obiekt obiekt : lista) {
            plansza[obiekt.pozycja.x][obiekt.pozycja.y] = obiekt;
        }

    }

    public void dodajJednostki(List<Jednostka> jednostki) {
        for (Jednostka obiekt : jednostki) {
            plansza[obiekt.pozycja.x][obiekt.pozycja.y] = obiekt;
        }
    }

    public void wypisz() {
        int szer = plansza.length;
        int wys = plansza[0].length;
        for (int y = 0; y < wys; y++) {
            for (int x = 0; x < szer; x++) {
                Obiekt obiekt = plansza[x][y];
                if (obiekt == null) {
                    System.out.print(". ");
                } else if (obiekt instanceof Wojownik) {
                    System.out.print("W ");
                } else if (obiekt instanceof Osadnik) {
                    System.out.print("O ");
                } else if (obiekt instanceof Surowiec) {
                    System.out.print("S ");
                } else if (obiekt instanceof Osada) {
                    System.out.print("M ");
                } else {
                    System.out.print("? "); // nieznany typ
                }
            }
            System.out.println(); // nowa linia po każdym wierszu
        }
    }
}
