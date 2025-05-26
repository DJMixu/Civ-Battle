package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Symulacja {


    public Cywilizacja[] listaCywilizacji;
    public int iSymulacjaX, iSymulacjaY, iLiczbaCywilizacji;
    public static Random ziarno;
    public static Plansza plansza;
    public static int iSymlacjaLicznikID;

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

    public int krokSymulacji() {
        for (Cywilizacja civ : listaCywilizacji) {
            List<Jednostka> jednostkiKopia = new ArrayList<>(civ.jednostki);
            civ.wypiszDane();
            for (Jednostka jednostka : jednostkiKopia) {
                jednostka.ruch(this);
            }
        }
        uzupelnijSurowce();
        plansza.wypisz();
        System.out.println("KROK");
        return 1;
    }

    public int startSymulacji(int pX, int pY, int plCywilizacji, String pSeed) {
        iSymlacjaLicznikID = 0;
        if (plCywilizacji > 8) {
            System.out.println("Maksymlanie 9 cywilizacji");
            plCywilizacji = 8;
        }

        ziarno = new Random(pSeed.hashCode());
        iSymulacjaX = pX;
        iSymulacjaY = pY;
        iLiczbaCywilizacji = plCywilizacji;
        plansza = new Plansza(iSymulacjaX, iSymulacjaY);
        listaCywilizacji = new Cywilizacja[(iLiczbaCywilizacji + 1)];
        generujStart();
        //System.out.println("XXX");
        plansza.wypisz();
        return 0;
    }

    public Point generujPozycje() {
        Point pozycja1 = new Point((ziarno.nextInt((iSymulacjaX - 2)) + 1), (ziarno.nextInt((iSymulacjaY - 2)) + 1));
        //System.out.println(plansza.zwrocPole(pozycja1));
        //System.out.println(obiektyWZasiegu(pozycja1,3));
        if (plansza.zwrocPole(pozycja1) == null) {
            if (obiektyWZasiegu(pozycja1, 3, plansza).isEmpty())
                return pozycja1;
        }
        //System.out.println("Pozycja zajeta dla jednostki");
        return generujPozycje();
    }

    public void uzupelnijSurowce() {
        List<Point> pola = plansza.pustePola();
        if (pola.size() > 10) {
            int x = pola.size() / 10;
            for(int i = 0 ; i < x ; i++){
                int z = Symulacja.ziarno.nextInt(pola.size());
                Surowiec sur = new Surowiec(iSymlacjaLicznikID++, pola.get(z), (ziarno.nextInt(15) + 5), ziarno.nextInt(3));
                plansza.dodajObiekt(pola.get(z), sur);
                pola.remove(z);
            }

        }
    }

    public Point generujPozycjeSurowca() {
        Point pozycja = new Point((ziarno.nextInt(iSymulacjaX)), (ziarno.nextInt(iSymulacjaY)));
        if (plansza.zwrocPole(pozycja) == null) {
            return pozycja;
        }
        //System.out.println("Pozycja surowca zajeta");
        return generujPozycjeSurowca();
    }

    private void generujStart() {
        for (int i = 0; i < iLiczbaCywilizacji; i++) {
            listaCywilizacji[i] = new Cywilizacja(i);
            Point pozycja = generujPozycje();
            //System.out.println("Wygenerowane pozycje osadnik " + i);
            listaCywilizacji[i].dodajJednostkę(new Osadnik(iSymlacjaLicznikID++, pozycja, i));
            listaCywilizacji[i].licznikOsadnikow++;
            listaCywilizacji[i].dodajJednostkę(new Wojownik(iSymlacjaLicznikID++, (pozycja.x < iSymulacjaX / 2 ? pozycja.x + 1 : pozycja.x - 1), pozycja.y, i));
            listaCywilizacji[i].licznikWojownikow++;
            listaCywilizacji[i].dodajJednostkę(new Wojownik(iSymlacjaLicznikID++, (pozycja.x), pozycja.y < iSymulacjaY / 2 ? pozycja.y + 1 : pozycja.y - 1, i));
            listaCywilizacji[i].licznikWojownikow++;
            plansza.dodajJednostki(listaCywilizacji[i].jednostki);
        }
        listaCywilizacji[iLiczbaCywilizacji] = new Cywilizacja(9);
        listaCywilizacji[iLiczbaCywilizacji].licznikWojownikow = iLiczbaCywilizacji;
        for (int i = 0; i < iLiczbaCywilizacji; i++) {
            Point pozycja = generujPozycje();
            //System.out.println("Wygenerowane pozycje barbazynca " + i);
            listaCywilizacji[iLiczbaCywilizacji].dodajJednostkę(new Barbarzynca(i, pozycja));
            plansza.dodajJednostki(listaCywilizacji[iLiczbaCywilizacji].jednostki);
        }
        //System.out.println("Koniec generowania cywilizacji");
        int liczbaSurowcow = iSymulacjaX * iSymulacjaY / 5;
        for (int i = 0; i < liczbaSurowcow; i++) {
            Point pozycja = generujPozycjeSurowca();
            Surowiec sur = new Surowiec(iSymlacjaLicznikID++, pozycja, (ziarno.nextInt(15) + 5), ziarno.nextInt(3));
            plansza.dodajObiekt(pozycja, sur);
            // System.out.print(i + ";");
        }


    }

    public static List<Obiekt> obiektyWZasiegu(Point punkt, int zasieg, Plansza plansza) {
        List<Obiekt> znalezione = new ArrayList<>();
        for (int dx = -zasieg; dx <= zasieg; dx++) {
            for (int dy = -zasieg; dy <= zasieg; dy++) {
                int nx = punkt.x + dx;
                int ny = punkt.y + dy;
                if (nx >= 0 && nx < plansza.x && ny >= 0 && ny < plansza.y) {
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
