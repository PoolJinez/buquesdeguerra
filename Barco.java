/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.uees.proyectodistribuidos1;

import java.awt.Point;

/**
 *
 * @author PC-HOME
 */
public class Barco  implements java.io.Serializable  {

    private int longi;
    private Point[] coordenadas;
    private boolean[] daños;

    public Barco(int longi) {
        this.longi = longi;
        this.coordenadas = new Point[longi];
        this.daños = new boolean[longi];
    }

    public int getLongi() {
        return longi;
    }

    public void setLongi(int longi) {
        this.longi = longi;
    }

    public Point[] getCoodenadas() {
        return coordenadas;
    }

    public void setCoodenadas(Point[] coodenadas) {
        this.coordenadas = coodenadas;
    }

    public void DAÑOS(int x, int y) {
        for (int i = 0; i < this.coordenadas.length; i++) {
            if(this.coordenadas[i].x==x && this.coordenadas[i].y==y){
                this.daños[i]=true;
            }
            
        }
    }

    public boolean hundido() {
        for (int i = 0; i < this.daños.length; i++) {
            if (!this.daños[i]) {
                return false;
            }
        }
        return true;
    }

}
