package com.example.civbattle;

import java.awt.*;
import java.util.stream.Collectors;

/**
 * Klasa reprezentująca jednostkę wojownika w symulacji.
 * Wojownik porusza się po planszy, atakuje wrogie jednostki w zasięgu oraz podejmuje decyzje strategiczne.
 */
class Wojownik extends Jednostka {

    /** Ścieżka do ikony wojownika (może być użyta w GUI) */
    private final String logoPath = "images/wojownik.png";

    /** Wartość ataku wojownika */
    int atak;

    /**
     * Konstruktor wojownika oparty na współrzędnych.
     *
     * @param id  unikalny identyfikator jednostki
     * @param pX  pozycja X
     * @param pY  pozycja Y
     * @param civ ID cywilizacji, do której należy wojownik
     */
    public Wojownik(int id, int pX, int pY, int civ) {
        super(id, pX, pY);
        this.zycie = 30;
        this.atak = 5;
        this.idCywilizacji = civ;
    }

    /**
     * Konstruktor wojownika oparty na punkcie.
     *
     * @param id    unikalny identyfikator jednostki
     * @param point punkt na planszy
     * @param civ   ID cywilizacji, do której należy wojownik
     */
    public Wojownik(int id, Point point, int civ) {
        super(id, point);
        this.zycie = 30;
        this.atak = 5;
        this.idCywilizacji = civ;
    }

    /**
     * Usuwa wojownika z planszy i cywilizacji po jego śmierci.
     *
     * @param plansza plansza symulacji
     * @param civ     cywilizacja, do której należy wojownik
     * @return kod statusu 2 (śmierć)
     */
    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikWojownikow--;
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
        return 2;
    }

    /**
     * Główna logika ruchu jednostki w każdej turze.
     * - Atakuje jeśli wróg obok
     * - Zbliża się do najbliższego wroga
     * - W przeciwnym razie porusza się losowo
     *
     * @param sim instancja symulacji
     * @return kod statusu 1 (ruch wykonany)
     */
    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if (this.zycie <= 0) return smierc(sim.plansza, civ);

        int punkty = 3;

        while (punkty > 0) {
            // 1. Szukaj wrogów w zasięgu 4 pól
            var wrogowie = Symulacja.obiektyWZasiegu(this.pozycja, 4, sim.plansza).stream()
                    .filter(o -> o instanceof Jednostka j && j.idCywilizacji != this.idCywilizacji)
                    .map(o -> (Jednostka) o)
                    .toList();

            // 2. Atakuj jeśli wróg obok
            boolean zaatakowano = atakuj(sim.plansza, sim);
            if (zaatakowano) {
                punkty--;
                continue;
            }

            // 3. Ruch w kierunku najbliższego wroga
            if (!wrogowie.isEmpty()) {
                Jednostka cel = wrogowie.stream()
                        .min((a, b) -> Double.compare(a.pozycja.distance(this.pozycja), b.pozycja.distance(this.pozycja)))
                        .orElse(null);

                if (cel != null) {
                    var sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y).stream()
                            .filter(p -> sim.plansza.zwrocPole(p.x, p.y) == null)
                            .collect(Collectors.toList());

                    if (!sasiedzi.isEmpty()) {
                        Point najlepszy = sasiedzi.stream()
                                .min((a, b) -> Double.compare(a.distance(cel.pozycja), b.distance(cel.pozycja)))
                                .orElse(this.pozycja);

                        sim.plansza.usunObiekt(this.pozycja);
                        this.poprzedniaPozycja = new Point(this.pozycja);
                        this.pozycja = najlepszy;
                        sim.plansza.dodajObiekt(this.pozycja, this);
                        punkty--;
                        continue;
                    }
                }
            }

            // 4. Ruch losowy
            var sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y).stream()
                    .filter(p -> sim.plansza.zwrocPole(p.x, p.y) == null)
                    .collect(Collectors.toList());

            if (!sasiedzi.isEmpty()) {
                Point losowy = sasiedzi.get(Symulacja.ziarno.nextInt(sasiedzi.size()));
                sim.plansza.usunObiekt(this.pozycja);
                this.poprzedniaPozycja = new Point(this.pozycja);
                this.pozycja = losowy;
                sim.plansza.dodajObiekt(this.pozycja, this);
                punkty--;
            } else {
                break;
            }
        }

        return 1;
    }

    /**
     * Próbuje zaatakować wrogą jednostkę w sąsiednich polach.
     *
     * @param plansza plansza symulacji
     * @param sim     instancja symulacji
     * @return true jeśli udało się zaatakować, false w przeciwnym razie
     */
    private boolean atakuj(Plansza plansza, Symulacja sim) {
        for (Point p : getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y)) {
            Obiekt o = plansza.zwrocPole(p.x, p.y);
            if (o instanceof Jednostka j && j.idCywilizacji != this.idCywilizacji) {
                j.zycie -= this.atak;
                if (j.zycie <= 0) {
                    j.ruch(sim); // wywołuje śmierć
                }
                return true;
            }
        }
        return false;
    }
}
