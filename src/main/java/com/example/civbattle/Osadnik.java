package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Osadnik extends Jednostka {
    private final String logoPath = "images/osadnik.png";

    public Osadnik(int id, Point pozycja) {
        super(id, pozycja);
        this.zycie = 20;
    }

    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if(!(this.zycie > 0))
            return smierc(sim.plansza,civ);
        if (civ.surowce[0] >= 2000 && civ.surowce[1] >= 2000 && civ.surowce[2] >= 2000 && civ.licznikOsad < civ.licznikWojownikow) {
            return zakladajOsadę(sim.plansza, civ);
        } else {
            dzialaj(sim.plansza, sim);
            return 0;
        }
    }

    void zbierajSurowiec(Surowiec surowiec , Symulacja sim, int idCywilizacji) {
        sim.listaCywilizacji[idCywilizacji].surowce[0] += surowiec.wartosciSurowca[0];
        sim.listaCywilizacji[idCywilizacji].surowce[1] += surowiec.wartosciSurowca[1];
        sim.listaCywilizacji[idCywilizacji].surowce[2] += surowiec.wartosciSurowca[2];
        System.out.println(sim.listaCywilizacji[idCywilizacji].surowce[0] + "         drewno");
        System.out.println(sim.listaCywilizacji[idCywilizacji].surowce[1] + "         metal");
        System.out.println(sim.listaCywilizacji[idCywilizacji].surowce[2] + "         woada");
        System.out.println(sim.listaCywilizacji[idCywilizacji].licznikOsad);
        System.out.println(sim.listaCywilizacji[idCywilizacji].licznikWojownikow);

        sim.plansza.usunObiekt(surowiec.pozycja);
    }

    int zakladajOsadę(Plansza plansza, Cywilizacja civ) {
        smierc(plansza, civ);
        civ.surowce[0] -=1000;
        civ.surowce[1] -=1000;
        civ.surowce[2] -=1000;
        plansza.dodajObiekt(pozycja, new Osada(Symulacja.iSymlacjaLicznikID++, pozycja, civ.idCywilizacji));
        return 2;
    }

    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsadnikow--;
        System.out.println(this.id + "osadnik usuniety");
        plansza.usunObiekt(pozycja);
        return 2;
    }


   public List<Point> getSasiedzi(Point p, int szerokosc, int wysokosc) {
       List<Point> wynik = new ArrayList<>();
       int[] dx = {-1, 0, 1};
       int[] dy = {-1, 0, 1};

       for (int x : dx) {
           for (int y : dy) {
               if (x == 0 && y == 0) continue;

               int nx = p.x + x;
               int ny = p.y + y;

               if (nx >= 0 && nx < szerokosc && ny >= 0 && ny < wysokosc) {
                   wynik.add(new Point(nx, ny));
               }
           }
       }

       return wynik;
   }
    public boolean uciekaj(Plansza plansza, Symulacja sim, List<Jednostka> zagrozenie) {
       double srodekX = 0, srodekY = 0;
       for (Jednostka j : zagrozenie) {
           srodekX += j.pozycja.x;
           srodekY += j.pozycja.y;
       }
       srodekX /= zagrozenie.size();
       srodekY /= zagrozenie.size();

       List<Point> sasiedzi = getSasiedzi(this.pozycja , sim.plansza.x , sim.plansza.y);
       List<Point> kandydaci = new ArrayList<>();

       for (Point p : sasiedzi) {
           if (p.equals(this.poprzedniaPozycja)) continue;

           Obiekt obiekt = plansza.zwrocPole(p.x, p.y);
           if (obiekt == null) {
               kandydaci.add(p);
           } else if (obiekt instanceof Surowiec surowiec) {
               zbierajSurowiec(surowiec, sim  , this.idCywilizacji);
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
                }else if(o instanceof Surowiec sur)
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
            // === PRÓBA ZEBRANIA SUROWCA Z SĄSIEDZTWA ===
            List<Point> sasiedziDoZbioru = getSasiedzi(this.pozycja, Symulacja.plansza.x, Symulacja.plansza.y);
            for (Point p : sasiedziDoZbioru) {
                Obiekt o = plansza.zwrocPole(p.x, p.y);
                if (o instanceof Surowiec surowiec) {
                    zbierajSurowiec(surowiec, sim , this.idCywilizacji);
                    Symulacja.plansza.usunObiekt(surowiec.pozycja);
                    punkty--;
                    break;
                }
            }


            if (!surowiecList.isEmpty()) {
                // Znajdź najbliższy surowiec
                Surowiec najblizszy = Collections.min(surowiecList, Comparator.comparingDouble(s -> s.pozycja.distance(this.pozycja)));

                // Szukamy najlepszego sąsiedniego pola, które przybliży nas do surowca
                List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.plansza.x, sim.plansza.y).stream()
                        .filter(p -> plansza.zwrocPole(p.x, p.y) == null) // tylko wolne
                        .filter(p -> !p.equals(this.poprzedniaPozycja))    // nie cofaj się
                        .collect(Collectors.toList());

                if (!sasiedzi.isEmpty()) {
                    Point najlepszy = Collections.min(sasiedzi, Comparator.comparingDouble(p -> p.distance(najblizszy.pozycja)));
                    this.poprzedniaPozycja = this.pozycja;
                    this.pozycja = najlepszy;
                    plansza.dodajObiekt(this.pozycja, this);
                    plansza.usunObiekt(this.poprzedniaPozycja);
                    punkty--;
                    continue; // zakończ pętlę tej tury i przejdź do kolejnej
                }
            }

            // === RUCH LOSOWY ===
            List<Point> sasiedzi = getSasiedzi(this.pozycja , sim.plansza.x , sim.plansza.y).stream()
                    .filter(p -> plansza.zwrocPole(p.x, p.y) == null)
                    .collect(Collectors.toList());

            if (sasiedzi.isEmpty()) break;

            Point losowy = sasiedzi.get(Symulacja.ziarno.nextInt(sasiedzi.size()));
            this.poprzedniaPozycja = this.pozycja;
            this.pozycja = losowy;
            plansza.dodajObiekt(this.pozycja, this);
            plansza.usunObiekt(this.poprzedniaPozycja);
            punkty--;
        }
    }
    /* @Override
    public int ruch(Symulacja sim) {
        int punkty = 3;
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if(!(this.zycie > 0))
            return smierc(sim.plansza,civ);
        if (civ.surowce[0] >= 200 && civ.surowce[1] >= 200 && civ.surowce[2] >= 200 && civ.licznikOsad < civ.licznikWojownikow) {
            return zakładajOsadę(sim.plansza, civ);
        }
        while(punkty>0){
            List<Obiekt> znalezione = Symulacja.obiektyWZasiegu(pozycja, 4, sim.plansza);
            List<Jednostka> zagrozenie = znalezione.stream().filter(obiekt -> obiekt instanceof Barbarzynca).map(obiekt -> (Jednostka)obiekt).toList();
            List<Jednostka> wojownik = znalezione.stream().filter(obiekt -> obiekt instanceof Wojownik).map(obiekt -> (Jednostka)obiekt).toList();
            for (Jednostka obiekt : wojownik) {
                if(obiekt.idCywilizacji != this.idCywilizacji)
                    zagrozenie.add(obiekt);
            }
            double x = 0 , y = 0;
            for (Jednostka jednostka : zagrozenie) {
                x+=jednostka.pozycja.x;
                y+=jednostka.pozycja.y;
            }
            x = x/zagrozenie.size() - this.pozycja.x;
            y = y/zagrozenie.size() - this.pozycja.y;
            if (x == 0 && y > 0) {
                // priorytet: ruch na południe (y-1)
                if (sim.plansza.zwrocPole(this.pozycja.x, this.pozycja.y - 1) == null) {
                    this.pozycja = new Point(this.pozycja.x, this.pozycja.y - 1);
                } else {
                    // rzut losowy w lewo lub w prawo
                    if (Symulacja.ziarno.nextInt(2) == 1) {
                        if (sim.plansza.zwrocPole(this.pozycja.x + 1, this.pozycja.y) == null) {
                            this.pozycja = new Point(this.pozycja.x + 1, this.pozycja.y);
                        } else if (sim.plansza.zwrocPole(this.pozycja.x - 1, this.pozycja.y) == null) {
                            this.pozycja = new Point(this.pozycja.x - 1, this.pozycja.y);
                        }
                    } else {
                        if (sim.plansza.zwrocPole(this.pozycja.x - 1, this.pozycja.y) == null) {
                            this.pozycja = new Point(this.pozycja.x - 1, this.pozycja.y);
                        } else if (sim.plansza.zwrocPole(this.pozycja.x + 1, this.pozycja.y) == null) {
                            this.pozycja = new Point(this.pozycja.x + 1, this.pozycja.y);
                        }
                    }
                }
                sim.plansza.dodajObiekt(this.pozycja,this);
                punkty--;

            }



        }

        return 0;
    }*/



}
