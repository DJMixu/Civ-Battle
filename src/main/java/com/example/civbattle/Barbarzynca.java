package com.example.civbattle;

import java.awt.*;
import java.util.stream.Collectors;

/**
 * Klasa reprezentująca barbarzyńcę – wrogą jednostkę niezależną, należącą do ostatniej cywilizacji (o ID 9).
 * Barbarzyńcy atakują inne jednostki, poruszają się losowo lub w stronę przeciwników.
 */
class Barbarzynca extends Jednostka {
    /** Ścieżka do obrazka barbarzyńcy. */
    private final String logoPath = "images/barbarzynca.png";

    /** Wartość ataku barbarzyńcy. */
    private int atak;

    /**
     * Konstruktor barbarzyńcy z pozycją jako współrzędne.
     *
     * @param id identyfikator jednostki
     * @param pX współrzędna X
     * @param pY współrzędna Y
     */
    public Barbarzynca(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    /**
     * Konstruktor barbarzyńcy z pozycją jako punkt.
     *
     * @param id identyfikator jednostki
     * @param pPozycja punkt określający pozycję
     */
    public Barbarzynca(int id, Point pPozycja) {
        super(id, pPozycja);
        this.pozycja = pPozycja;
        this.idCywilizacji = 9;
        this.zycie = 20;
        this.atak = 8;
    }

    /**
     * Usuwa barbarzyńcę z planszy i listy jednostek cywilizacji barbarzyńców.
     *
     * @param plansza plansza gry
     * @param civ cywilizacja barbarzyńców
     * @return kod 2 oznaczający śmierć
     */
    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikWojownikow--;
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
        return 2;
    }

    /**
     * Logika ruchu barbarzyńcy w trakcie jednej tury:
     * - Atakuje wrogie jednostki w zasięgu 1
     * - Zbliża się do widocznych wrogów w zasięgu 4
     * - Porusza się losowo, jeśli nie widzi wroga
     *
     * @param sim obiekt symulacji
     * @return 1 jeżeli jednostka wykonała akcje, 2 jeżeli zginęła
     */
    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[sim.listaCywilizacji.length - 1];
        if (this.zycie <= 0) return smierc(sim.plansza, civ);

        int punkty = 3;

        while (punkty > 0) {
            // Szukanie wrogów w zasięgu
            var wrogowie = Symulacja.obiektyWZasiegu(this.pozycja, 4, sim.plansza).stream()
                    .filter(o -> o instanceof Jednostka j && j.idCywilizacji != this.idCywilizacji)
                    .map(o -> (Jednostka) o)
                    .toList();

            // Atak, jeśli wróg obok
            boolean zaatakowano = atakuj(sim.plansza, sim);
            if (zaatakowano) {
                punkty--;
                continue;
            }

            // Ruch w kierunku wroga
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

            // Ruch losowy
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
     * Próbuje zaatakować jednostkę w sąsiednich polach.
     *
     * @param plansza plansza gry
     * @param sim obiekt symulacji
     * @return true jeśli zaatakowano wroga, false w przeciwnym razie
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
