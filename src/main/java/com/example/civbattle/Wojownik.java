package com.example.civbattle;

import java.awt.*;
import java.util.stream.Collectors;

/**
 * Klasa reprezentująca jednostkę wojownika w symulacji.
 * <p>
 * Wojownik porusza się po planszy, atakuje wrogów w sąsiedztwie oraz dąży do najbliższych przeciwników.
 * Jeśli nie ma celu, porusza się losowo. W przypadku śmierci zostaje usunięty z gry.
 */
public class Wojownik extends Jednostka {

    /** Ścieżka do ikony reprezentującej wojownika. */
    private final String logoPath = "images/wojownik.png";

    /** Wartość ataku wojownika. */
    int atak;

    /**
     * Tworzy nowego wojownika na podstawie współrzędnych X i Y.
     *
     * @param id  unikalny identyfikator jednostki
     * @param pX  współrzędna X
     * @param pY  współrzędna Y
     * @param civ identyfikator cywilizacji, do której należy wojownik
     */
    public Wojownik(int id, int pX, int pY, int civ) {
        super(id, pX, pY);
        this.zycie = 30;
        this.atak = 5;
        this.idCywilizacji = civ;
    }

    /**
     * Tworzy nowego wojownika na podstawie punktu.
     *
     * @param id    unikalny identyfikator jednostki
     * @param point pozycja wojownika na planszy
     * @param civ   identyfikator cywilizacji, do której należy wojownik
     */
    public Wojownik(int id, Point point, int civ) {
        super(id, point);
        this.zycie = 30;
        this.atak = 5;
        this.idCywilizacji = civ;
    }

    /**
     * Obsługuje śmierć wojownika — usuwa go z planszy i z listy jednostek cywilizacji.
     *
     * @param plansza plansza, z której należy usunąć jednostkę
     * @param civ     cywilizacja, z której należy usunąć jednostkę
     * @return kod 2 informujący o śmierci jednostki
     */
    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikWojownikow--;
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
        return 2;
    }

    /**
     * Logika ruchu wojownika podczas tury symulacji:
     * <ul>
     *     <li>Jeśli wróg znajduje się w sąsiedztwie — atakuje.</li>
     *     <li>W przeciwnym razie zbliża się do najbliższego przeciwnika w zasięgu.</li>
     *     <li>Jeśli nie ma celu — porusza się losowo.</li>
     * </ul>
     *
     * @param sim instancja symulacji zawierająca planszę i wszystkie cywilizacje
     * @return kod 1 oznaczający wykonanie ruchu, lub kod 2, jeśli jednostka zginęła
     */
    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if (this.zycie <= 0) return smierc(sim.plansza, civ);

        int punkty = 3;  // liczba możliwych ruchów

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

            // 3. Porusz się w stronę najbliższego wroga
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

            // 4. Ruch losowy, jeśli nie ma celu
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
     * Próbuje zaatakować wroga na sąsiednim polu.
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
                    j.ruch(sim); // wywołuje logikę śmierci jednostki
                }
                return true;
            }
        }
        return false;
    }
}
