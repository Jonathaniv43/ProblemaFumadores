package problemaFumadores;

import java.util.concurrent.Semaphore;

public class Principal {
    static int cigarrillosFumados = 0;
    static public Semaphore[] semaforosFumadores = new Semaphore[3];
    static public Semaphore mutex = new Semaphore(1);
    static public Semaphore mesaDisponible = new Semaphore(1); // Controla acceso físico a la mesa
    static final int maximo_cigarrillos = 30;
    static public int fumadores = 3;
    static String ingrediente[] = {"TABACO", "PAPEL", "CERILLAS"};
    static int cont_ftabaco = 0;
    static int cont_fpapel = 0;
    static int cont_fcerillas = 0;
    static int primero;
    static int segundo;
    static int tercero;

    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < fumadores; j++) {
            semaforosFumadores[j] = new Semaphore(0);
        }

        Thread proveedor = new Thread(new Proveedor());
        proveedor.start();
        System.out.println("Inicio de Programa El proceso de proveedor se inicializa");

        Thread[] fumador = new Thread[fumadores];
        for (int i = 0; i < fumadores; i++) {
            fumador[i] = new Thread(new Fumador(i));
            fumador[i].start();
        }
        System.out.println("Hay un fumador que recibió el TABACO / El proceso de el fumador 1 se inicializa");
        System.out.println("Hay un fumador que recibió el PAPEL / El proceso de el fumador 2 se inicializa");
        System.out.println("Hay un fumador que recibió las CERRILLAS / El proceso de el fumador 3 se inicializa\n");

        proveedor.join();
        System.out.println("El proveedor terminó su labor, Se cerro el hilo");

        for (int i = 0; i < fumadores; i++) {
            fumador[i].join();
            System.out.println("El fumador ha terminado su labor, Se cerro el Hilo " + (i));
        }

        System.out.println("\nResumen final:");
        System.out.println("El fumador con el tabaco fumo " + cont_ftabaco + " cigarrillos");
        System.out.println("El fumador con el papel fumo " + cont_fpapel + " cigarrillos");
        System.out.println("El fumador con el cerillas fumo " + cont_fcerillas + " cigarrillos");
        System.out.println("¡El programa ha terminado!");
    }
}