package com.example.civbattle;

class Wojownik extends Jednostka {
    private final String logoPath = "images/wojownik.png";
    int atak;

    public Wojownik(int id, int pX, int pY) {
        super(id, pX, pY);
        this.zycie = 30;
        this.atak = 10;
    }

    void atak() {
    }

    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikWojownikow--;
        System.out.println(this.id + "wojownik usuniety");
        plansza.usunObiekt(pozycja);
        return 2;
    }

    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if(!(this.zycie > 0))
            return smierc(sim.plansza,civ);
        return 0;
    }
}
