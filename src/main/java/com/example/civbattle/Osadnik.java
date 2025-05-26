package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Osadnik extends Jednostka {
    private final String logoPath = "images/osadnik.png";

    public Osadnik(int id, Point pozycja, int civ) {
        super(id, pozycja);
        this.zycie = 20;
        this.idCywilizacji = civ;
    }

    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if (!(this.zycie > 0))
            return smierc(sim.plansza, civ);
        if (civ.surowce[0] >= 2000 && civ.surowce[1] >= 2000 && civ.surowce[2] >= 2000 && civ.licznikOsad <= civ.licznikWojownikow) {
            return zakladajOsadę(sim.plansza, civ);
        } else {
            dzialaj(sim.plansza, sim);
            return 0;
        }
    }

    void zbierajSurowiec(Surowiec surowiec, Symulacja sim, int idCywilizacji) {
        sim.listaCywilizacji[idCywilizacji].surowce[0] += surowiec.wartosciSurowca[0];
        sim.listaCywilizacji[idCywilizacji].surowce[1] += surowiec.wartosciSurowca[1];
        sim.listaCywilizacji[idCywilizacji].surowce[2] += surowiec.wartosciSurowca[2];
        //System.out.println(sim.listaCywilizacji[idCywilizacji].surowce[0] + "         drewno");
        //System.out.println(sim.listaCywilizacji[idCywilizacji].surowce[1] + "         metal");
        //System.out.println(sim.listaCywilizacji[idCywilizacji].surowce[2] + "         woada");
        //System.out.println(sim.listaCywilizacji[idCywilizacji].licznikOsad);
        //System.out.println(sim.listaCywilizacji[idCywilizacji].licznikWojownikow);

        sim.plansza.usunObiekt(surowiec.pozycja);
    }

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

    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsadnikow--;
        //System.out.println(this.id + "osadnik usuniety");
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
        return 2;
    }


    public boolean uciekaj(Plansza plansza, Symulacja sim, List<Jednostka> zagrozenie) {
        double srodekX = 0, srodekY = 0;
        for (Jednostka j : zagrozenie) {
            srodekX += j.pozycja.x;
            srodekY += j.pozycja.y;
        }
        srodekX /= zagrozenie.size();
        srodekY /= zagrozenie.size();

        List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y);
        List<Point> kandydaci = new ArrayList<>();

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

        this.poprzedniaPozycja = this.pozycja;
        this.pozycja = najlepsze;
        plansza.dodajObiekt(this.pozycja, this);
        plansza.usunObiekt(this.poprzedniaPozycja);
        return true;
    }

    public void dzialaj(Plansza plansza, Symulacja sim) {
        int punkty = 3;

        while (punkty > 0) {
            List<Obiekt> znalezione = Symulacja.obiektyWZasiegu(this.pozycja, 4, plansza);
            List<Surowiec> surowiecList = new ArrayList<>();
            List<Jednostka> zagrozenie = znalezione.stream()
                    .filter(o -> o instanceof Barbarzynca)
                    .map(o -> (Jednostka) o)
                    .collect(Collectors.toList());

            for (Obiekt o : znalezione) {
                if (o instanceof Wojownik woj && woj.idCywilizacji != this.idCywilizacji) {
                    zagrozenie.add(woj);
                } else if (o instanceof Surowiec sur)
                    surowiecList.add(sur);

            }

            // === UCIECZKA ===
            if (!zagrozenie.isEmpty()) {
                boolean udaloSie = uciekaj(plansza, sim, zagrozenie);
                if (udaloSie) {
                    punkty--;
                    continue;
                }
                // jeśli nie udało się uciec — kontynuuj dalej
            }


            if (!surowiecList.isEmpty()) {
                // === PRÓBA ZEBRANIA SUROWCA Z SĄSIEDZTWA ===
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

                //System.out.println("SUROWIEC NIE PUSTY");
                // Znajdź najbliższy surowiec
                Surowiec najblizszy = Collections.min(surowiecList, Comparator.comparingDouble(s -> s.pozycja.distance(this.pozycja)));

                // Szukamy najlepszego sąsiedniego pola, które przybliży nas do surowca
                List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y).stream()
                        .filter(p -> plansza.zwrocPole(p.x, p.y) == null) // tylko wolne
                        .filter(p -> !p.equals(this.poprzedniaPozycja))    // nie cofaj się
                        .collect(Collectors.toList());

                if (!sasiedzi.isEmpty()) {
                    //System.out.println("SASIEDZI !is EMprt");
                    Point najlepszy = Collections.min(sasiedzi, Comparator.comparingDouble(p -> p.distance(najblizszy.pozycja)));
                    this.poprzedniaPozycja = new Point(this.pozycja);
                    ;
                    this.pozycja = najlepszy;
                    plansza.dodajObiekt(this.pozycja, this);
                    plansza.usunObiekt(this.poprzedniaPozycja);
                    punkty--;
                    continue; // zakończ pętlę tej tury i przejdź do kolejnej
                }
            }

            // === RUCH LOSOWY ===
            List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y).stream()
                    .filter(p -> plansza.zwrocPole(p.x, p.y) == null)
                    .collect(Collectors.toList());

            if (sasiedzi.isEmpty()) break;

            Point losowy = sasiedzi.get(Symulacja.ziarno.nextInt(sasiedzi.size()));
            this.poprzedniaPozycja = new Point(this.pozycja);
            ;
            this.pozycja = losowy;
            plansza.dodajObiekt(this.pozycja, this);
            plansza.usunObiekt(this.poprzedniaPozycja);
            punkty--;
        }
    }
}
