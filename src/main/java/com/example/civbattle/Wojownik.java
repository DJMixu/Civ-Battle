package com.example.civbattle;

import java.awt.*;
import java.util.stream.Collectors;

class Wojownik extends Jednostka {
    private final String logoPath = "images/wojownik.png";
    int atak;

    public Wojownik(int id, int pX, int pY , int civ) {
        super(id, pX, pY);
        this.zycie = 30;
        this.atak = 10;
        this.idCywilizacji = civ;
    }
    public Wojownik(int id, Point point , int civ) {
        super(id, point);
        this.zycie = 30;
        this.atak = 10;
        this.idCywilizacji = civ;
    }


    void atak() {
    }

    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikWojownikow--;
        System.out.println(this.id + "wojownik usuniety");
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
        return 2;
    }

    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if (this.zycie <= 0) return smierc(sim.plansza, civ);

        int punkty = 3;

        while (punkty > 0) {
            // 1. Szukaj wrogich jednostek w zasięgu 4
            var wrogowie = Symulacja.obiektyWZasiegu(this.pozycja, 4, sim.plansza).stream()
                    .filter(o -> o instanceof Jednostka j && j.idCywilizacji != this.idCywilizacji)
                    .map(o -> (Jednostka) o)
                    .toList();

            // 2. Jeśli wróg obok — atakuj
            boolean zaatakowano = atakuj(sim.plansza, sim);
            if (zaatakowano) {
                punkty--;
                continue;
            }

            // 3. Jeśli widzisz wroga — rusz się w jego stronę
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

            // 4. Brak wroga lub możliwości ruchu w jego stronę — ruch losowy
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

    private boolean atakuj(Plansza plansza, Symulacja sim) {
        for (Point p : getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y)) {
            Obiekt o = plansza.zwrocPole(p.x, p.y);
            if (o instanceof Jednostka j && j.idCywilizacji != this.idCywilizacji) {
                j.zycie -= this.atak;
                System.out.println("Wojownik " + id + " zaatakował jednostkę " + j.id + " z cywilizacji " + j.idCywilizacji);
                if (j.zycie <= 0) {
                    j.ruch(sim); // wywołuje śmierć
                }
                return true;
            }
        }
        return false;
    }


}
