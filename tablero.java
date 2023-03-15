package ec.edu.uees.proyectodistribuidos1;

import java.util.Arrays;
import java.awt.Point;
import java.util.Date;

public class tablero implements java.io.Serializable {

    Barco[][] tablero; // null es agua
    int[][] tableroMostrar;
    private Date tiempo_ini;

    private final int barcoG = 0;
    private final int barcoP = 1;

    private int[] numBarcos;

    private final int Ar = 0;
    private final int De = 1;
    private final int Ab = 2;
    private final int Iz = 3;

    private final int No_exp = 0;
    private final int Barco = 1;
    private final int Agua = 2;
    private final int D = 3;

    int puntaje = 0;

    public tablero() {
        crearTablero();
        this.numBarcos = new int[5];
        for (int i = 0; i < this.numBarcos.length; i++) {
            numBarcos[0] = 0;
            numBarcos[1] = 0;
            numBarcos[2] = 0;
            numBarcos[3] = 1;
            numBarcos[4] = 1;
        }
        generarBarco();
        this.tiempo_ini = new Date();

    }

    public static int[] removeElement(int[] arr) {
        int barquitos = arr.length;
        return Arrays.stream(arr, 1, barquitos)
                .toArray();

    }

    public int ranNUM(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void crearTablero() {
        int col = 7;
        int fil = 6;
        this.tablero = new Barco[col][fil];
        this.tableroMostrar = new int[col][fil];

    }

    private void generarBarco() {
        Barco b = null;
        int x, y, dir, dX = 0, dY = 0, pfx, pfy, longi;
        boolean posscorrecta;
        for (int i = 0; i < this.numBarcos.length; i++) {
            if (this.numBarcos[i] == 0) {
                b = new barcoG();
            } else {
                b = new barcoP();
            }

            longi = b.getLongi();

            do {

                posscorrecta = true;
                x = ranNUM(1, 7);
                y = ranNUM(1, 6);
                dir = ranNUM(0, 3);

                switch (dir) {
                    case Ar:
                        dX = 0;
                        dY = -1;
                        break;
                    case De:
                        dX = 1;
                        dY = 0;
                        break;
                    case Ab:
                        dX = 0;
                        dY = 1;
                        break;
                    case Iz:
                        dX = -1;
                        dY = 0;
                        break;
                }
                pfx = (longi * dX) + x;
                pfy = (longi * dY) + y;

                // System.out.print("Final X: " + pfx + " ");
                // System.out.print("Final Y: " + pfy);
                if (pfx > 0 && pfx < this.tablero.length && pfy > 0 && pfy < (this.tablero.length - 1)) {
                    // k es x
                    // m es y
                    for (int k = x, m = y; (k != pfx || m != pfy) && posscorrecta; k += dX, m += dY) {

                        if (this.tablero[k][m] != null) {
                            posscorrecta = false;
                        }

                    }
                } else {
                    posscorrecta = false;
                }

            } while (!posscorrecta);

            Point[] coord = new Point[b.getLongi()];
            for (int k = x, m = y, ind = 0; (k != pfx || m != pfy) && posscorrecta; k += dX, m += dY, ind++) {
                this.tablero[k][m] = b;
                coord[ind] = new Point(k, m);

            }
            b.setCoodenadas(coord);

            int uIx, uIy, uFx, uFy = 0;
            uIx = x;
            uIy = y;
            uFx = pfx;
            uFy = pfy;
            // System.out.println("El barco se genero entre {" + (uIx + 1) + "," + (uIy + 1)
            // + "} y " + "{" + uFx + "," + uFy + "}");
        }

    }

    public void mostrarnumbarco() {
        System.out.println("");
        System.out.println("---------------------------------------------");
        System.out.println("Quedan: " + this.numBarcos.length + "-Barcos");
        System.out.println("---------------------------------------------");
    }

    public void mostrarTablero() {

        // cabecera
        System.out.print("   ");
        for (char letra = 'A'; letra < ('A' + this.tablero.length - 1); letra++) {
            System.out.print(" " + letra + " ");
        }
        System.out.println("");
        // contenido
        char letra = 'A';
        for (int i = 0; i < this.tableroMostrar.length; i++) {
            System.out.print(" " + letra + " ");
            for (int j = 0; j < this.tableroMostrar[0].length; j++) {
                switch (this.tableroMostrar[i][j]) {
                    case No_exp:
                        System.out.print(" - ");
                        break;
                    case Barco:
                        System.out.print(" X ");
                        break;
                    case Agua:
                        System.out.print(" O ");
                        break;
                }
            }
            System.out.println("");
            letra++;
        }
        this.mostrartiempo();
        this.mostrarnumbarco();
    }

    public void mostrarDEF() {

        // cabecera
        System.out.print("   ");
        for (char letra = 'A'; letra < ('A' + this.tablero.length - 1); letra++) {
            System.out.print(" " + letra + " ");
        }
        System.out.println("");
        // contenido
        char letra = 'A';
        String G = " S ";
        for (int i = 0; i < this.tableroMostrar.length; i++) {
            System.out.print(" " + letra + " ");
            for (int j = 0; j < this.tableroMostrar[0].length; j++) {
                if (this.tablero[i][j] != null) {
                    System.out.print(G);
                } else if (this.tableroMostrar[i][j] == Barco) {
                    G = " X ";
                    System.out.print(G);
                } else if (this.tableroMostrar[i][j] == No_exp) {
                    System.out.print(" - ");
                } else if (this.tableroMostrar[i][j] == Agua) {
                    System.out.print(" O ");
                }

            }
            System.out.println("");
            letra++;
        }

        this.mostrartiempo();
        this.mostrarnumbarco();
    }

    public void comprobarCass(int x, int y) {

        try {
            if (x >= 0 && x < this.tablero.length && y >= 0 && y < this.tableroMostrar.length) {
                if (this.tablero[x][y] != null) {
                    this.tableroMostrar[x][y] = this.Barco;
                    this.tablero[x][y].DAÑOS(x, y);
                    if (this.tablero[x][y].hundido()) {
                        puntaje = puntaje + 15;
                        System.out.println("     ///////////////////////");
                        System.out.println("     ///////////////////////");
                        System.out.println(":D   //// BARCO HUNDIDO ////   :D");
                        System.out.println("     ///////////////////////");
                        System.out.println("     ///////////////////////");
                        System.out.println();
                        System.out.println("===========================");
                        System.out.println("EL PUNTAJE ES: \n       ***" + puntaje + "***");
                        System.out.println("===========================");

                        for (int H = 0; H < this.numBarcos.length; H++) {
                            if (this.numBarcos[H] == 0 || this.numBarcos[H] == 1) {
                                this.numBarcos = removeElement(this.numBarcos);
                                break;
                            }
                        }
                    } else {
                        puntaje = this.puntaje + 10;
                        System.out.println("            ////////////");
                        System.out.println("            ////////////");
                        System.out.println("        :)  /// HIT! ///   :)");
                        System.out.println("            ////////////");
                        System.out.println("            ////////////");
                        System.out.println();
                        System.out.println("===========================");
                        System.out.println("EL PUNTAJE ES: \n       ***" + puntaje + "***");
                        System.out.println("===========================");
                    }

                } else {
                    this.tableroMostrar[x][y] = this.Agua;
                    puntaje = puntaje - 10;
                    System.out.println("         ///////////////");
                    System.out.println("         ///////////////");
                    System.out.println("    :(   //// MISS! ////   :(");
                    System.out.println("         ///////////////");
                    System.out.println("         ///////////////");
                    System.out.println();
                    System.out.println("===========================");
                    System.out.println("EL PUNTAJE ES: \n       ***" + puntaje + "***");
                    System.out.println("===========================");
                }
            } else {
                System.out.println("TE SALISTE DEL LIMITE");
            }

        } catch (Exception e) {
            System.out.println();
        }

    }

    public void comprobar(int x, int y, int[][] i) {
        try {
            if (x >= 0 && x < 6 && y >= 0 && y < 7) {
                if (this.tablero[x][y] != null) {
                    i[x][y] = this.Barco;
                } else {
                    i[x][y] = this.Agua;
                }
            } else {
                System.out.println("SALIO DEL LIMITE");
            }

        } catch (Exception e) {
            System.out.println();
        }
    }

    public void comprobarCassJUG(int x, int y) {
        try {
            if (x >= 0 && x < this.tablero.length && y >= 0 && y < this.tableroMostrar.length) {
                if (this.tablero[x][y] != null) {
                    this.tableroMostrar[x][y] = this.Barco;
                    this.tablero[x][y].DAÑOS(x, y);
                    if (this.tablero[x][y].hundido()) {
                        for (int H = 0; H < this.numBarcos.length; H++) {
                            if (this.numBarcos[H] == 0 || this.numBarcos[H] == 1) {
                                this.numBarcos = removeElement(this.numBarcos);
                                break;
                            }
                        }
                    }
                } else {
                    this.tableroMostrar[x][y] = this.Agua;
                }
            } else {
                System.out.println("SALIO DEL LIMITE");
            }

        } catch (Exception e) {
            System.out.println();
        }
    }

    public void mostrartiempo() {

        Date despues = new Date();
        long seg = (despues.getTime() - this.tiempo_ini.getTime()) / 1000;
        int min = (int) (seg / 60);
        int segres = (int) (seg % 60);
        System.out.println("");
        System.out.println("Tiempo de Juego: ");
        if (min <= 0) {
            System.out.print("0" + min);
        } else {
            System.out.print(min);
        }
        System.out.print(":");
        if (segres <= 0) {
            System.out.print("0" + segres);
        } else {
            System.out.print(segres);
        }
        System.out.println("");

    }

    public boolean finjug() {
        for (int i = 0; i < this.numBarcos.length; i++) {
            if (this.numBarcos[i] > 0) {
                return false;
            }

        }
        return true;
    }
}
