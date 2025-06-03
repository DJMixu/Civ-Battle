package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca planszę gry, na której umieszczane są obiekty i jednostki.
 */
public class Plansza {
    /**
     * Dwuwymiarowa tablica reprezentująca pola planszy. Każde pole może zawierać jeden obiekt lub być puste (null).
     */
    public static Obiekt[][] plansza;

    /** Szerokość planszy (liczba kolumn). */
    int x;

    /** Wysokość planszy (liczba wierszy). */
    int y;

    /**
     * Tworzy nową planszę o zadanych wymiarach.
     * @param px szerokość planszy
     * @param py wysokość planszy
     */
    public Plansza(int px, int py) {
        plansza = new Obiekt[px][py];
        x = px;
        y = py;
    }

    /**
     * Zwraca obiekt znajdujący się na danym polu planszy.
     * @param x współrzędna X pola
     * @param y współrzędna Y pola
     * @return obiekt na polu lub null, jeśli pole jest puste
     */
    public Obiekt zwrocPole(int x, int y) {
        return plansza[x][y];
    }

    /**
     * Zwraca listę punktów na planszy, które są puste (nie zawierają żadnego obiektu).
     * @return lista pustych pól jako obiekty Point
     */
    public List<Point> pustePola() {
        List<Point> lista = new ArrayList<>();

        for (int x = 0; x < plansza.length; x++) {
            for (int y = 0; y < plansza[x].length; y++) {
                if (plansza[x][y] == null) {
                    lista.add(new Point(x, y));
                }
            }
        }
        return lista;
    }

    /**
     * Zwraca obiekt znajdujący się na zadanym polu planszy.
     * @param pPozycja punkt zawierający współrzędne pola
     * @return obiekt na polu lub null, jeśli pole jest puste
     */
    public Obiekt zwrocPole(Point pPozycja) {
        return plansza[pPozycja.x][pPozycja.y];
    }

    /**
     * Umieszcza obiekt na zadanym polu planszy.
     * @param pozycja współrzędne pola, na którym umieszczany jest obiekt
     * @param obiekt obiekt do umieszczenia
     */
    public void dodajObiekt(Point pozycja, Obiekt obiekt) {
        plansza[pozycja.x][pozycja.y] = obiekt;
    }

    /**
     * Umieszcza listę obiektów na planszy, zgodnie z ich współrzędnymi.
     * @param lista lista obiektów do dodania
     */
    public void dodajObiekty(List<Obiekt> lista) {
        for (Obiekt obiekt : lista) {
            plansza[obiekt.pozycja.x][obiekt.pozycja.y] = obiekt;
        }
    }

    /**
     * Umieszcza listę jednostek na planszy, zgodnie z ich współrzędnymi.
     * @param jednostki lista jednostek do dodania
     */
    public void dodajJednostki(List<Jednostka> jednostki) {
        for (Jednostka obiekt : jednostki) {
            plansza[obiekt.pozycja.x][obiekt.pozycja.y] = obiekt;
        }
    }

    /**
     * Usuwa obiekt z planszy na podanym polu.
     * @param punkt współrzędne pola, z którego należy usunąć obiekt
     */
    public void usunObiekt(Point punkt) {
        plansza[punkt.x][punkt.y] = null;
    }

    /**
     * Wypisuje planszę w formie tekstowej na standardowe wyjście.
     * Pomocne do debugowania. Symbole:
     * <ul>
     *     <li>'.' - puste pole</li>
     *     <li>'W' - wojownik</li>
     *     <li>'O' - osadnik</li>
     *     <li>'S' - surowiec</li>
     *     <li>'M' - osada</li>
     *     <li>'B' - barbarzyńca</li>
     *     <li>'?' - nieznany typ obiektu</li>
     * </ul>
     */
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
                } else if (obiekt instanceof Barbarzynca) {
                    System.out.print("B ");
                } else {
                    System.out.print("? ");
                }
            }
            System.out.println();
        }
    }
}
