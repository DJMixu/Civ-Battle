package com.example.civbattle;


import java.awt.*;
import java.util.List;
class Obiekt {
    int id;
    public Point pozycja;

    public Obiekt(int id, int pX , int pY) {
        this.id = id;
        pozycja = new Point(pX,pY);
    }

    public Obiekt(int id , Point pP){
        this.id = id;
        this.pozycja = pP;
    }
}












