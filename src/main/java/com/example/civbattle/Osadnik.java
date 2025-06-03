package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa reprezentująca jednostkę Osadnik.
 * Osadnik odpowiada za zbieranie surowców i zakładanie osad.
 */
class Osadnik extends Jednostka {
    /** Ścieżka do ikony osadnika. */
    private final String logoPath = "images/osadnik.png";

    /**
     * Konstruktor osadnika.
     *
     * @param id  identyfikator jednostki
     * @param pozycja  pozycja startowa na planszy
     * @param civ  identyfikator cywilizacji
     */
    public Osadnik(int id, Point pozycja, int civ) {
        super(id, pozycja);
        this.zycie = 20;
        this.idCywilizacji = civ;
    }

    /**
     * Główna metoda decydująca o ruchu osadnika w każdej turze.
     *
     * @param sim obiekt symulacji
     * @return kod akcji: 2 – śmierć, 1 – ruch, 0 – bez ruchu
     */
    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];

        if (!(this.zycie > 0))
            return smierc(sim.plansza, civ);

        if (civ.surowce[0] >= 2000 && civ.surowce[1] >= 2000 && civ.surowce[2] >= 2000 &&
                civ.licznikOsad <= civ.licznikWojownikow) {
            return zakladajOsadę(sim.plansza, civ);
        } else {
            dzialaj(sim.plansza, sim);
            return 0;
        }
    }

    /**
     * Dodaje wartość zebranego surowca do zasobów cywilizacji.
     *
     * @param surowiec  obiekt surowca
     * @param sim  obiekt symulacji
     * @param idCywilizacji  identyfikator cywilizacji osadnika
     */
    void zbierajSurowiec(Surowiec surowiec, Symulacja sim, int idCywilizacji) {
        sim.listaCywilizacji[idCywilizacji].surowce[0] += surowiec.wartosciSurowca[0];
        sim.listaCywilizacji[idCywilizacji].surowce[1] += surowiec.wartosciSurowca[1];
        sim.listaCywilizacji[idCywilizacji].surowce[2] += surowiec.wartosciSurowca[2];
        sim.plansza.usunObiekt(surowiec.pozycja);
    }

    /**
     * Zakłada osadę, jeśli spełnione są warunki.
     * Usuwa osadnika, dodaje obiekt osady i zmniejsza surowce.
     *
     * @param plansza  obiekt planszy
     * @param civ  cywilizacja osadnika
     * @return kod akcji: 2 – utworzono osadę
     */
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

    /**
     * Usuwa osadnika z planszy i z listy jednostek cywilizacji.
     *
     * @param plansza  obiekt planszy
     * @param civ  cywilizacja osadnika
     * @return kod akcji: 2 – śmierć jednostki
     */
    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsadnikow--;
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
        return 2;
    }

    /**
     * Próbuje wykonać ruch oddalający osadnika od wrogich jednostek.
     *
     * @param plansza  plansza gry
     * @param sim  symulacja
     * @param zagrozenie  lista wrogich jednostek
     * @return true jeśli ruch się powiódł, false jeśli nie znaleziono bezpiecznego pola
     */
    public boolean uciekaj(Plansza plansza, Symulacja sim, List<Jednostka> zagrozenie) {
        // ...
    }

    /**
     * Metoda odpowiedzialna za działanie osadnika:
     * zbieranie surowców, ucieczka przed zagrożeniem, ruch losowy.
     *
     * @param plansza  plansza gry
     * @param sim  obiekt symulacji
     */
    public void dzialaj(Plansza plansza, Symulacja sim) {
        // ...
    }
}
