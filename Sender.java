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

public class Sender implements java.io.Serializable {

    DatagramSocket socket = new DatagramSocket();
    Scanner sn = new Scanner(System.in);
    String G = "El ganador es: ";
    List<int[]> cood = new ArrayList<>();
    List<int[]> coodrec;
    tablero T1 = new tablero();
    tablero T2 = new tablero();
    Barco[][] t1 = T1.tablero;
    Barco[][] t2;
    int[][] ta1 = T1.tableroMostrar;
    int[][] ta2;
    int R = 1, r = 0, Rr = 1;

    public Sender() throws Exception, IOException {

        try {
            while (!T1.finjug() || r < 8) {
                byte[] data = new byte[1500]; // MTU = 1500
                interfazMenu();
                System.out.println();
                System.out.println("TABLERO DEL JUGADOR");
                T1.mostrarDEF();
                System.out.println("TABLERO DEL ENEMIGO");
                T2.mostrarTablero();
                turno();
                ByteArrayOutputStream byteStreamOut = new ByteArrayOutputStream();
                ObjectOutputStream objectOut = new ObjectOutputStream(byteStreamOut);
                objectOut.writeObject(cood);
                objectOut.writeObject(t1);
                objectOut.writeObject(ta1);
                data = byteStreamOut.toByteArray();
                DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("127.0.0.1"), 2020);
                //mostrarL(cood);

                this.socket.send(packet);

                if (R <= 16) {
                    System.out.println("\n\nESPERANDO EL TURNO DEL ENEMIGO");
                } else {
                    System.out.println("JUEGO TERMINADO");
                    System.out.println(G);
                }

                r++;

                packet = new DatagramPacket(data, data.length);

                this.socket.receive(packet);

                data = packet.getData();
                ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
                ObjectInputStream object = new ObjectInputStream(byteStream);
                coodrec = (List<int[]>) object.readObject();
                t2 = (Barco[][]) object.readObject();
                ta2 = (int[][]) object.readObject();
                System.out.println();
                T2.tablero = t2;
                T2.tableroMostrar = ta2;
                recibirDisparos(T1, coodrec);
                recibirDisparos(T2, cood);
                System.out.println("TABLERO DEL JUGADOR");
                T1.mostrarDEF();
                System.out.println("TABLERO DEL ENEMIGO");
                T2.mostrarTablero();

                /*
                 * mostrarL(coodrec);
                 * System.out.println();
                 */
            }

        } catch (Exception e) {
            e.printStackTrace();
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
                    cood.add(enteros);
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
            new Sender();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
