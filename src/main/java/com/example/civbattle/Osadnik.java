package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Klasa reprezentująca jednostkę Osadnik – odpowiedzialną za zakładanie osad i zbieranie surowców
class Osadnik extends Jednostka {
    /** Ścieżka do ikony  */
    private final String logoPath = "images/osadnik.png";

    // Konstruktor ustawiający ID, pozycję i przynależność cywilizacyjną osadnika
    public Osadnik(int id, Point pozycja, int civ) {
        super(id, pozycja);
        this.zycie = 20;
        this.idCywilizacji = civ;
    }

    // Główna metoda wykonująca ruch osadnika w turze
    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];

        // Jeśli osadnik nie żyje – usuń go z planszy
        if (!(this.zycie > 0))
            return smierc(sim.plansza, civ);

        // Zakładanie osady, jeśli spełnione warunki ekonomiczne i balans jednostek
        if (civ.surowce[0] >= 2000 && civ.surowce[1] >= 2000 && civ.surowce[2] >= 2000 && civ.licznikOsad <= civ.licznikWojownikow) {
            return zakladajOsadę(sim.plansza, civ);
        } else {
            dzialaj(sim.plansza, sim);
            return 0;
        }
    }

    // Zbieranie surowca przez osadnika i dodanie jego wartości do zasobów cywilizacji
    void zbierajSurowiec(Surowiec surowiec, Symulacja sim, int idCywilizacji) {
        sim.listaCywilizacji[idCywilizacji].surowce[0] += surowiec.wartosciSurowca[0];
        sim.listaCywilizacji[idCywilizacji].surowce[1] += surowiec.wartosciSurowca[1];
        sim.listaCywilizacji[idCywilizacji].surowce[2] += surowiec.wartosciSurowca[2];
        sim.plansza.usunObiekt(surowiec.pozycja);
    }

    // Zakładanie osady: zużycie surowców, usunięcie osadnika i dodanie obiektu osady
    int zakladajOsadę(Plansza plansza, Cywilizacja civ) {
        smierc(plansza, civ);
        civ.surowce[0] -= 1000;
        civ.surowce[1] -= 1000;
        civ.surowce[2] -= 1000;
        Osada os = new Osada(Symulacja.iSymlacjaLicznikID++, pozycja, civ.idCywilizacji);
        plansza.dodajObiekt(pozycja, os);
        civ.jednostki.add(os);
        civ.licznikOsad++;

        return 2;
    }

    // Usunięcie osadnika z planszy i z listy jednostek cywilizacji
    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsadnikow--;
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
        return 2;
    }

    // Próba ucieczki osadnika od zagrożenia
    public boolean uciekaj(Plansza plansza, Symulacja sim, List<Jednostka> zagrozenie) {
        // Oblicz środek zagrożenia
        double srodekX = 0, srodekY = 0;
        for (Jednostka j : zagrozenie) {
            srodekX += j.pozycja.x;
            srodekY += j.pozycja.y;
        }
        srodekX /= zagrozenie.size();
        srodekY /= zagrozenie.size();

        List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y);
        List<Point> kandydaci = new ArrayList<>();

        // Szukamy pól możliwych do ucieczki
        for (Point p : sasiedzi) {
            if (p.equals(this.poprzedniaPozycja)) continue;

            Obiekt obiekt = plansza.zwrocPole(p.x, p.y);
            if (obiekt == null) {
                kandydaci.add(p);
            } else if (obiekt instanceof Surowiec surowiec) {
                zbierajSurowiec(surowiec, sim, this.idCywilizacji);
                kandydaci.add(p);
            }
        }

        if (kandydaci.isEmpty()) return false;

        // Wybierz pole najdalej od zagrożenia
        Point najlepsze = null;
        double maxDystans = -1;

        for (Point p : kandydaci) {
            double dystans = p.distance(srodekX, srodekY);
            if (dystans > maxDystans) {
                maxDystans = dystans;
                najlepsze = p;
            }
        }

        if (najlepsze == null) return false;

        // Wykonaj ruch ucieczkowy
        this.poprzedniaPozycja = this.pozycja;
        this.pozycja = najlepsze;
        plansza.dodajObiekt(this.pozycja, this);
        plansza.usunObiekt(this.poprzedniaPozycja);
        return true;
    }

    // Główna metoda wykonująca logikę ruchu osadnika
    public void dzialaj(Plansza plansza, Symulacja sim) {
        int punkty = 3;

        while (punkty > 0) {
            List<Obiekt> znalezione = Symulacja.obiektyWZasiegu(this.pozycja, 4, plansza);
            List<Surowiec> surowiecList = new ArrayList<>();
            List<Jednostka> zagrozenie = znalezione.stream()
                    .filter(o -> o instanceof Barbarzynca)
                    .map(o -> (Jednostka) o)
                    .collect(Collectors.toList());

            // Wyszukiwanie wrogich jednostek
            for (Obiekt o : znalezione) {
                if (o instanceof Wojownik woj && woj.idCywilizacji != this.idCywilizacji) {
                    zagrozenie.add(woj);
                } else if (o instanceof Surowiec sur)
                    surowiecList.add(sur);
            }

            // Jeśli istnieje zagrożenie – próbuj uciec
            if (!zagrozenie.isEmpty()) {
                boolean udaloSie = uciekaj(plansza, sim, zagrozenie);
                if (udaloSie) {
                    punkty--;
                    continue;
                }
            }

            // Jeśli są surowce – spróbuj zebrać lub zbliżyć się do najbliższego
            if (!surowiecList.isEmpty()) {
                List<Point> sasiedziDoZbioru = getSasiedzi(this.pozycja, Symulacja.plansza.x, Symulacja.plansza.y);
                for (Point p : sasiedziDoZbioru) {
                    Obiekt o = plansza.zwrocPole(p.x, p.y);
                    if (o instanceof Surowiec surowiec) {
                        zbierajSurowiec(surowiec, sim, this.idCywilizacji);
                        Symulacja.plansza.usunObiekt(surowiec.pozycja);
                        punkty--;
                        break;
                    }
                }

                // Ruch w stronę najbliższego surowca
                Surowiec najblizszy = Collections.min(surowiecList, Comparator.comparingDouble(s -> s.pozycja.distance(this.pozycja)));

                List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y).stream()
                        .filter(p -> plansza.zwrocPole(p.x, p.y) == null)
                        .filter(p -> !p.equals(this.poprzedniaPozycja))
                        .collect(Collectors.toList());

                if (!sasiedzi.isEmpty()) {
                    Point najlepszy = Collections.min(sasiedzi, Comparator.comparingDouble(p -> p.distance(najblizszy.pozycja)));
                    this.poprzedniaPozycja = new Point(this.pozycja);
                    this.pozycja = najlepszy;
                    plansza.dodajObiekt(this.pozycja, this);
                    plansza.usunObiekt(this.poprzedniaPozycja);
                    punkty--;
                    continue;
                }
            }

            // Jeśli brak celu – ruch losowy
            List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y).stream()
                    .filter(p -> plansza.zwrocPole(p.x, p.y) == null)
                    .collect(Collectors.toList());

            if (sasiedzi.isEmpty()) break;

            Point losowy = sasiedzi.get(Symulacja.ziarno.nextInt(sasiedzi.size()));
            this.poprzedniaPozycja = new Point(this.pozycja);
            this.pozycja = losowy;
            plansza.dodajObiekt(this.pozycja, this);
            plansza.usunObiekt(this.poprzedniaPozycja);
            punkty--;
        }
    }
}
