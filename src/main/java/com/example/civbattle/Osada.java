package com.example.civbattle;

import java.awt.*;
import java.util.List;

/**
 * Klasa reprezentująca obiekt osady w grze.
 * Osada jest jednostką, która może produkować inne jednostki (osadników lub wojowników),
 * regeneruje swoje życie oraz znika z planszy po śmierci.
 */
public class Osada extends Jednostka {
    private final String logoPath = "images/osada.png";

    /**
     * Konstruktor osady.
     *
     * @param id       unikalny identyfikator jednostki
     * @param pozycja  pozycja osady na planszy
     * @param civ      identyfikator cywilizacji, do której należy osada
     */
    public Osada(int id, Point pozycja, int civ) {
        super(id, pozycja, civ);
        this.zycie = 100;
    }

    /**
     * Usuwa osadę z planszy i listy jednostek cywilizacji.
     *
     * @param plansza plansza symulacji
     * @param civ     cywilizacja, do której należy osada
     */
    void smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsad--;
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
    }

    /**
     * Produkuje nową jednostkę (osadnika lub wojownika) w sąsiedztwie osady,
     * jeśli dostępne jest puste pole i cywilizacja ma wystarczającą ilość surowców.
     *
     * @param typ  typ produkowanej jednostki: 1 - Osadnik, inny - Wojownik
     * @param sim  odniesienie do głównej symulacji
     * @return 3 - jeśli jednostka została wyprodukowana lub brak miejsca
     */
    public int produkuj(int typ, Symulacja sim) {
        List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.iSymulacjaX, sim.iSymulacjaY);
        Point pustePole = sasiedzi.stream()
                .filter(p -> sim.plansza.zwrocPole(p) == null)
                .findFirst()
                .orElse(null);

        if (pustePole == null) return 3; // brak miejsca

        Jednostka nowa;
        if (typ == 1) { // Osadnik
            nowa = new Osadnik(Symulacja.iSymlacjaLicznikID++, pustePole, this.idCywilizacji);
            sim.listaCywilizacji[this.idCywilizacji].licznikOsadnikow++;
        } else { // Wojownik
            nowa = new Wojownik(Symulacja.iSymlacjaLicznikID++, pustePole, this.idCywilizacji);
            sim.listaCywilizacji[this.idCywilizacji].licznikWojownikow++;
        }

        sim.plansza.dodajObiekt(pustePole, nowa);
        sim.listaCywilizacji[this.idCywilizacji].jednostki.add(nowa);
        for (int i = 0; i < 3; i++)
            sim.listaCywilizacji[this.idCywilizacji].surowce[i] -= 400;

        return 3;
    }

    /**
     * Metoda wykonywana w każdej turze przez osadę.
     * Odpowiada za:
     * - regenerację życia (jeśli jest mniejsze niż 50)
     * - sprawdzenie śmierci (życie ≤ 0)
     * - produkcję nowych jednostek, jeśli są dostępne surowce
     *
     * @param sim odniesienie do głównej symulacji
     * @return kod statusu: 2 - osada umiera, 0 - normalna tura
     */
    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];

        if (this.zycie < 50) {
            this.zycie = Math.min(50, this.zycie + 1);
        }

        if (this.zycie <= 0) {
            smierc(sim.plansza, civ);
            return 2;
        }

        boolean maSurowce = civ.surowce[0] >= 800 && civ.surowce[1] >= 800 && civ.surowce[2] >= 800;
        if (maSurowce && civ.licznikOsadnikow < civ.licznikOsad) {
            if (civ.licznikOsadnikow < civ.licznikWojownikow) {
                produkuj(1, sim); // Osadnik
            } else {
                produkuj(2, sim); // Wojownik
            }
        }

        return 0;
    }
}
