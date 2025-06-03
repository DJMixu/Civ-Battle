package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasa odpowiedzialna za zarządzanie przebiegiem symulacji rozgrywki.
 * Zawiera logikę inicjalizacji planszy, generowania jednostek, surowców
 * oraz przeprowadzania kolejnych tur.
 */
public class Symulacja {

    /** Tablica wszystkich cywilizacji biorących udział w symulacji. */
    public Cywilizacja[] listaCywilizacji;

    /** Wymiary planszy symulacji. */
    public int iSymulacjaX, iSymulacjaY;

    /** Liczba cywilizacji w symulacji (bez barbarzyńców). */
    public int iLiczbaCywilizacji;

    /** Generator liczb losowych z ziarnem. */
    public static Random ziarno;

    /** Plansza, na której toczy się symulacja. */
    public static Plansza plansza;

    /** Licznik globalny służący do nadawania unikalnych ID obiektom. */
    public static int iSymlacjaLicznikID;

    /** Licznik aktualnej tury symulacji. */
    public int licznikTur;

    // === KONSTRUKTORY ===

    /**
     * Konstruktor uruchamiający symulację z określonymi parametrami.
     *
     * @param pX szerokość planszy
     * @param pY wysokość planszy
     * @param plCiv liczba cywilizacji
     * @param pSeed ziarno losowości (do powtarzalnych symulacji)
     */
    public Symulacja(int pX, int pY, int plCiv, String pSeed) {
        System.out.println(startSymulacji(pX, pY, plCiv, pSeed) == 0
                ? "Symulacja wystartowała"
                : "Symulacja nie wystartowała z błędem");
        licznikTur = 0;
    }

    /**
     * Konstruktor z domyślnym ziarnem.
     */
    public Symulacja(int pX, int pY, int plCiv) {
        this(pX, pY, plCiv, "defaultSeed");
    }

    /**
     * Konstruktor z domyślną liczbą cywilizacji.
     */
    public Symulacja(int pX, int pY) {
        this(pX, pY, 3, "defaultSeed");
    }

    /**
     * Konstruktor domyślny.
     */
    public Symulacja() {
        this(20, 20, 3, "defaultSeed");
    }

    // === METODY SYMULACJI ===

    /**
     * Wykonuje jedną turę symulacji:
     * - wykonuje ruchy wszystkich jednostek
     * - okresowo uzupełnia surowce
     *
     * @return zawsze 1 (można rozbudować o inne stany)
     */
    public int krokSymulacji() {
        licznikTur++;
        for (Cywilizacja civ : listaCywilizacji) {
            List<Jednostka> jednostkiKopia = new ArrayList<>(civ.jednostki);
            for (Jednostka jednostka : jednostkiKopia) {
                jednostka.ruch(this);
            }
        }
        if (licznikTur > 10 && licznikTur % 2 == 0)
            uzupelnijSurowce();
        return 1;
    }

    /**
     * Inicjuje nową symulację z określonymi parametrami.
     *
     * @param pX szerokość planszy
     * @param pY wysokość planszy
     * @param plCywilizacji liczba cywilizacji
     * @param pSeed ziarno losowości
     * @return 0 jeśli inicjalizacja się powiodła
     */
    public int startSymulacji(int pX, int pY, int plCywilizacji, String pSeed) {
        iSymlacjaLicznikID = 0;
        ziarno = new Random(pSeed.hashCode());
        iSymulacjaX = pX;
        iSymulacjaY = pY;
        iLiczbaCywilizacji = plCywilizacji;
        plansza = new Plansza(iSymulacjaX, iSymulacjaY);
        listaCywilizacji = new Cywilizacja[iLiczbaCywilizacji + 1]; // ostatnia to barbarzyńcy
        generujStart();
        plansza.wypisz();
        return 0;
    }

    /**
     * Generuje losową, wolną pozycję na planszy z zachowaniem odległości od innych obiektów.
     *
     * @return pozycja w formacie Point
     */
    public Point generujPozycje() {
        Point pozycja1 = new Point((ziarno.nextInt(iSymulacjaX - 2) + 1), (ziarno.nextInt(iSymulacjaY - 2) + 1));
        if (plansza.zwrocPole(pozycja1) == null && obiektyWZasiegu(pozycja1, 3, plansza).isEmpty()) {
            return pozycja1;
        }
        return generujPozycje();
    }

    /**
     * Uzupełnia planszę o nowe surowce, jeśli liczba pustych pól jest odpowiednia.
     */
    public void uzupelnijSurowce() {
        List<Point> pola = plansza.pustePola();
        if (pola.size() > 10) {
            int x = pola.size() / 10;
            for (int i = 0; i < x; i++) {
                int z = ziarno.nextInt(pola.size());
                Point pozycja = pola.get(z);
                Surowiec sur = new Surowiec(iSymlacjaLicznikID++, pozycja, ziarno.nextInt(15) + 5, ziarno.nextInt(3));
                plansza.dodajObiekt(pozycja, sur);
                pola.remove(z);
            }
        }
    }

    /**
     * Generuje losową pozycję na planszy dla surowca.
     *
     * @return pozycja w formacie Point
     */
    public Point generujPozycjeSurowca() {
        Point pozycja = new Point(ziarno.nextInt(iSymulacjaX), ziarno.nextInt(iSymulacjaY));
        if (plansza.zwrocPole(pozycja) == null) {
            return pozycja;
        }
        return generujPozycjeSurowca();
    }

    /**
     * Generuje startową konfigurację symulacji:
     * - tworzy cywilizacje
     * - umieszcza jednostki
     * - generuje barbarzyńców
     * - rozmieszcza początkowe surowce
     */
    private void generujStart() {
        for (int i = 0; i < iLiczbaCywilizacji; i++) {
            listaCywilizacji[i] = new Cywilizacja(i);
            Point pozycja = generujPozycje();
            listaCywilizacji[i].dodajJednostkę(new Osadnik(iSymlacjaLicznikID++, pozycja, i));
            listaCywilizacji[i].licznikOsadnikow++;
            listaCywilizacji[i].dodajJednostkę(new Wojownik(iSymlacjaLicznikID++, (pozycja.x < iSymulacjaX / 2 ? pozycja.x + 1 : pozycja.x - 1), pozycja.y, i));
            listaCywilizacji[i].licznikWojownikow++;
            listaCywilizacji[i].dodajJednostkę(new Wojownik(iSymlacjaLicznikID++, pozycja.x, (pozycja.y < iSymulacjaY / 2 ? pozycja.y + 1 : pozycja.y - 1), i));
            listaCywilizacji[i].licznikWojownikow++;
            plansza.dodajJednostki(listaCywilizacji[i].jednostki);
        }

        // Dodanie barbarzyńców
        listaCywilizacji[iLiczbaCywilizacji] = new Cywilizacja(9);
        listaCywilizacji[iLiczbaCywilizacji].licznikWojownikow = iLiczbaCywilizacji;
        for (int i = 0; i < iLiczbaCywilizacji; i++) {
            Point pozycja = generujPozycje();
            listaCywilizacji[iLiczbaCywilizacji].dodajJednostkę(new Barbarzynca(i, pozycja));
            plansza.dodajJednostki(listaCywilizacji[iLiczbaCywilizacji].jednostki);
        }

        // Dodanie początkowych surowców
        int liczbaSurowcow = iSymulacjaX * iSymulacjaY / 5;
        for (int i = 0; i < liczbaSurowcow; i++) {
            Point pozycja = generujPozycjeSurowca();
            Surowiec sur = new Surowiec(iSymlacjaLicznikID++, pozycja, ziarno.nextInt(15) + 5, ziarno.nextInt(3));
            plansza.dodajObiekt(pozycja, sur);
        }
    }

    /**
     * Zwraca listę obiektów w zadanym zasięgu od podanego punktu.
     *
     * @param punkt punkt odniesienia
     * @param zasieg maksymalna odległość
     * @param plansza plansza do przeszukania
     * @return lista znalezionych obiektów w zasięgu
     */
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
