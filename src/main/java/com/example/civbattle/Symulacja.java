package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Symulacja {


    public Cywilizacja[] listaCywilizacji;
    public int iSymulacjaX, iSymulacjaY, iLiczbaCywilizacji;
    public Random ziarno;
    public Plansza plansza;

    //Domyślny start symulacji
    public Symulacja(int pX, int pY, int plCiv, String pSeed) {
        System.out.println(startSymulacji(pX, pY, plCiv, pSeed) == 0
                ? "Symulacja wystartowała"
                : "Symulacja nie wystartowała z błędem");
    }

    // Konstruktor z domyślnym seed
    public Symulacja(int pX, int pY, int plCiv) {
        this(pX, pY, plCiv, "defaultSeed");
    }

    // Konstruktor z domyślną liczbą cywilizacji i seedem
    public Symulacja(int pX, int pY) {
        this(pX, pY, 3, "defaultSeed"); // np. 3 domyślne cywilizacje
    }

    // Konstruktor z domyślnymi wszystkimi parametrami
    public Symulacja() {
        this(20, 20, 3, "defaultSeed");
    }

    public int startSymulacji(int pX, int pY, int plCywilizacji, String pSeed) {
        if (plCywilizacji > 9) {
            System.out.println("Maksymlanie 9 cywilizacji");
            plCywilizacji = 9;
        }

        ziarno = new Random(pSeed.hashCode());
        iSymulacjaX = pX;
        iSymulacjaY = pY;
        iLiczbaCywilizacji = plCywilizacji;
        plansza = new Plansza(iSymulacjaX, iSymulacjaY);
        listaCywilizacji = new Cywilizacja[iLiczbaCywilizacji];
        generujStart();
        System.out.println("XXX");
        plansza.wypisz();
        return 0;
    }

    public Point generujPozycje() {
        Point pozycja1 = new Point((ziarno.nextInt((iSymulacjaX - 2)) + 1), (ziarno.nextInt((iSymulacjaY - 2)) + 1));
        //System.out.println(plansza.zwrocPole(pozycja1));
        //System.out.println(obiektyWZasiegu(pozycja1,3));
        if (plansza.zwrocPole(pozycja1) == null) {
            if (obiektyWZasiegu(pozycja1, 3).isEmpty())
                return pozycja1;
        }
        System.out.println("Pozycja zajeta dla jednostki");
        return generujPozycje();
    }

    public Point generujPozycjeSurowca() {
        Point pozycja = new Point((ziarno.nextInt(iSymulacjaX)), (ziarno.nextInt(iSymulacjaY)));
        if (plansza.zwrocPole(pozycja) == null) {
            return pozycja;
        }
        System.out.println("Pozycja surowca zajeta");
        return generujPozycjeSurowca();
    }

    private void generujStart() {
        for (int i = 0; i < iLiczbaCywilizacji; i++) {
            listaCywilizacji[i] = new Cywilizacja(i);
            Point pozycja = generujPozycje();
            System.out.println("Wygenerowane pozycje osadnik " + i);
            listaCywilizacji[i].dodajJednostkę(new Osadnik(1, pozycja));
            listaCywilizacji[i].dodajJednostkę(new Wojownik(2, (pozycja.x < iSymulacjaX / 2 ? pozycja.x + 1 : pozycja.x - 1), pozycja.y));
            listaCywilizacji[i].dodajJednostkę(new Wojownik(2, (pozycja.x), pozycja.y < iSymulacjaY / 2 ? pozycja.y + 1 : pozycja.y - 1));
            plansza.dodajJednostki(listaCywilizacji[i].jednostki);
        }
        System.out.println("Koniec generowania cywilizacji");
        int liczbaSurowcow = iSymulacjaX * iSymulacjaY / 5;
        for (int i = 0; i < liczbaSurowcow; i++) {
            Point pozycja = generujPozycjeSurowca();
            Surowiec sur = new Surowiec(i, pozycja, (ziarno.nextInt(15) + 5), ziarno.nextInt(3));
            plansza.dodajObiekt(pozycja, sur);
            System.out.print(i + ";");
        }

    }

    public List<Obiekt> obiektyWZasiegu(Point punkt, int zasieg) {
        List<Obiekt> znalezione = new ArrayList<>();

        for (int dx = -zasieg; dx <= zasieg; dx++) {
            for (int dy = -zasieg; dy <= zasieg; dy++) {
                int nx = punkt.x + dx;
                int ny = punkt.y + dy;
                if (nx >= 0 && nx < iSymulacjaX && ny >= 0 && ny < iSymulacjaY) {
                    Obiekt o = plansza.zwrocPole(nx, ny);
                    if (o != null) {
                        znalezione.add(o);
                    }
                }
            }
        }
        return znalezione;
    }


}
