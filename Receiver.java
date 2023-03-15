package ec.edu.uees.proyectodistribuidos1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Receiver implements java.io.Serializable {

    DatagramSocket socket = new DatagramSocket(2020);
    Scanner sn = new Scanner(System.in);
    String G = "El ganador es: ";
    List<int[]> cood;
    List<int[]> coodenv = new ArrayList<>();
    tablero T1 = new tablero();
    tablero T2 = new tablero();
    Barco[][] t2 = T2.tablero;
    Barco[][] t1;
    int[][] ta1;
    int[][] ta2 = T2.tableroMostrar;
    int R = 2, r = 0, Rr = 1;

    public Receiver() throws Exception, IOException {
        System.out.println("Receiver is running.");
        while (true) {
            try {
                while (!T2.finjug() || r < 8) {

                    byte[] data = new byte[1500]; // MTU = 1500
                    DatagramPacket packet = new DatagramPacket(data, data.length);

                    System.out.println("\n\nESPERANDO EL TURNO DEL ENEMIGO");
                    this.socket.receive(packet);
                    data = packet.getData();
                    ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
                    ObjectInputStream object = new ObjectInputStream(byteStream);
                    cood = (List<int[]>) object.readObject();
                    t1 = (Barco[][]) object.readObject();
                    ta1 = (int[][]) object.readObject();
                    System.out.println();
                    T1.tablero = t1;
                    T1.tableroMostrar = ta1;
                    recibirDisparos(T2, cood);
                    recibirDisparos(T1, coodenv);
                    /*
                     * mostrarL(cood);
                     * System.out.println();
                     */
                    interfazMenu();
                    System.out.println();
                    System.out.println("TABLERO DEL JUGADOR");
                    T2.mostrarDEF();
                    System.out.println("TABLERO DEL ENEMIGO");
                    T1.mostrarTablero();
                    turno();
                    InetAddress senders_address = packet.getAddress();
                    int senders_port = packet.getPort();
                    ByteArrayOutputStream byteStreamOut = new ByteArrayOutputStream();
                    ObjectOutputStream objectOut = new ObjectOutputStream(byteStreamOut);
                    objectOut.writeObject(coodenv);
                    objectOut.writeObject(t2);
                    data = byteStreamOut.toByteArray();
                    packet = new DatagramPacket(data, data.length, senders_address, senders_port);

                    //mostrarL(coodenv);
                    System.out.println("TABLERO DEL JUGADOR");
                    T2.mostrarDEF();
                    System.out.println("TABLERO DEL ENEMIGO");
                    T1.mostrarTablero();
                    this.socket.send(packet);

                    r++;
                }

                System.out.println("JUEGO TERMINADO");
                System.out.println(G);
                break;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void mostrarL(List<int[]> L) {
        for (int i = 0; i < L.size(); i++) {
            System.out.println("ENVIADO");
            System.out.println("Coordenada en X: " + L.get(i)[0]);
            System.out.println("Coordenada en Y: " + L.get(i)[1]);
        }
    }

    public void interfazMenu() {
        System.out.println("*******************************************************");
        System.out.println("Tablero de Columnas: " + 7 + " - Filas: " + 6);
        System.out.println("*******************************************************");
        System.out.println();
        System.out.println("!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!");
        System.out.println(
                "!-!-!-!-!-!-!-!-!-!-!-!-!-!     RONDA NUMERO:   " + (Rr) + "   !-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!");
        System.out.println("!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!-!");
        System.out.println();
        System.out.println("                                ██████████████");
        System.out.println("            /////////////////    TURNO NUMERO:" + (R) + "   /////////////////");
        System.out.println("                                ██████████████");
        System.out.println();
        System.out.println("TURNO DEL JUGADOR 1");
        R += 2;
        Rr++;
    }

    public void turno() throws Exception {
        while (true) {
            try {
                int x = -1, y = -1, cnt = 0;
                System.out.println();
                int n = 5;
                while (cnt < 5) {
                    int enteros[] = new int[2];
                    System.out.println("__________________________");
                    System.out.println("TIENES " + n + " DISPAROS.");
                    System.out.println("__________________________");
                    System.out.println("Introducir Columna y Fila");
                    x = sn.nextInt();
                    y = sn.nextInt();
                    enteros[0] = x;
                    enteros[1] = y;
                    coodenv.add(enteros);
                    n--;
                    cnt++;
                }

                break;
            } catch (Exception e) {
                System.out.println("TE SALISTE DEL LIMITE");
            }
        }
    }

    public tablero recibirDisparos(tablero b, List<int[]> env)
            throws Exception, IOException, ClassNotFoundException {
        for (int i = 0; i < env.size(); i++) {
            b.comprobarCassJUG((env.get(i)[0] - 1), (env.get(i)[1] - 1));
        }
        return b;
    }

    public static void main(String[] args) {
        try {
            new Receiver();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
