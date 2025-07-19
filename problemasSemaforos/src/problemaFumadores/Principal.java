package problemaFumadores;

import java.util.concurrent.Semaphore;

/**
 * Clase principal que coordina el problema de los fumadores.
 * Simula un proveedor que coloca ingredientes en una mesa y fumadores que los usan para armar cigarrillos.
 * Se declaran las variables compartidas y los semaforos que nos permiten manejar la concurrecnia de los procesos
 */
public class Principal {
    // Contador de cigarrillos que se han fumado (variable compartida por los 3 fumadores al fumar pueden aumentarlo)
    static int cigarrillosFumados = 0;
    
    // Semáforos individuales para cada fumador (0: tabaco, 1: papel, 2: cerillas)
    // Cada fumador espera en su propio semáforo a ser notificado por el proveedor para poder acceder a los recursos 
    static public Semaphore[] semaforosFumadores = new Semaphore[3];

     static public Semaphore mutex = new Semaphore(1);
    /* Semáforo para exclusión mutua entre el proveedor y los fumadores  (protege variables compartidas) 
    Propósito: Garantizar la exclusión mutua para acceder a variables compartidas.
   - Protege:* Las variables compartidas que son leídas y modificadas por múltiples hilos. En nuestro caso, estas variables son:
        - `cigarrillosFumados` - accedido por todos los fumadores (3)
        Cuando un fumador incrementa cigarrillosFumados++, ningún otro hilo puede modificar ese contador al mismo tiempo.
        - `cont_ftabaco`, `cont_fpapel`, `cont_fcerillas` 
        - `primero`, `segundo`, `tercero` (los ingredientes seleccionados en cada ronda)
        Cuando el proveedor está asignando primero = 0, segundo = 1, tercero = 2, ningún fumador puede leer esos valores simultáneamente.
  */ 
   
    static public Semaphore mesaDisponible = new Semaphore(1);
    // Semáforo que controla el acceso físico a la mesa:
    // - Proveedor debe adquirirlo para poner ingredientes
    // - Fumador lo libera después de tomar ingredientes
    /*IMPORTANTE - `mesaDisponible` no protege variables, sino que controla el flujo de trabajo: el proveedor no puede 
    poner ingredientes si la mesa no está vacía, y el fumador es el responsable de liberar la mesa 
    una vez que ha tomado los ingredientes.
    */
    
    // Límite de cigarrillos a producir antes de terminar el programa
    static final int maximo_cigarrillos = 30;
    
    // Número de fumadores (siempre 3)
    static public int fumadores = 3;
    
    // Arreglo que contiene los 3 ingredientes que luego se asignaran 
    static String ingrediente[] = {"TABACO", "PAPEL", "CERILLAS"};
    
    // Contadores individuales para cada tipo de fumador
    static int cont_ftabaco = 0;    // Fumador con tabaco (ayuda a controlor cuantas veces el hilo entro a la seccion critica)
    static int cont_fpapel = 0;     // Fumador con papel
    static int cont_fcerillas = 0;  // Fumador con cerillas

    static int primero; 
    static int segundo; 
    static int tercero; 
    // Variables para almacenar los ingredientes en cada ciclo:
    // - primero y segundo: ingredientes colocados en la mesa ESTOS ROTARAN CADA VEZ PUES SE ELIGEN AL AZAR
    //Ejemplo   (  1  ) , (  2   ) 
    //          (PAPEL,CERILLAS)
    // - tercero: ingrediente que tiene el fumador que debe actuar
    //Ejemplo      ( 0 )
    //             TABACO
    
   

    public static void main(String[] args) throws InterruptedException {
        // Inicialización de semáforos para cada fumador (inicialmente bloqueados)
        for (int j = 0; j < fumadores; j++) {
            semaforosFumadores[j] = new Semaphore(0);
        }

        // Crear y lanzar hilo del proveedor
        Thread proveedor = new Thread(new Proveedor());
        proveedor.start();
        /*
            El método start() se utiliza para iniciar la ejecución de un nuevo hilo. 
            Cuando se a start() en un objeto Thread, Java realiza dos acciones clave:
                - Crea un nuevo hilo de ejecución: Se asignan los recursos necesarios para que 
                este hilo opere de forma independiente. 
                - Llama al método run() del hilo: El código
                definido dentro del método run() (que extiende Thread o implementa Runnable) 
                comenzará a ejecutarse en este nuevo hilo.
        */
        System.out.println("Inicio del programa - Proveedor iniciado");

        // Crear y lanzar hilos de los fumadores
        Thread[] fumador = new Thread[fumadores];
        for (int i = 0; i < fumadores; i++) {
            fumador[i] = new Thread(new Fumador(i));
            fumador[i].start();
            System.out.println(" Fumador " + (i+1) + " iniciado - Tiene: " + ingrediente[i]);
        }

        // Esperar a que el proveedor termine
        proveedor.join();
        /*
        El método join() se utiliza para esperar a que un hilo termine su ejecución. 
        El hilo que llama espera indefinidamente hasta que el hilo en el que se llamó join() termine.
        Cuando se llama al hilo.join() desde otro hilo (por ejemplo, el hilo principal),
        el hilo que llama (el hilo principal en este caso) se pausa y espera hasta que 
        hilo complete su método run() y muera.
        */    
        System.out.println("Proveedor terminó su trabajo");

        // Esperar a que todos los fumadores terminen
        for (int i = 0; i < fumadores; i++) {
            fumador[i].join();
            System.out.println("Fumador " + (i+1) + " terminó");
        }

        // Mostrar resumen final
        System.out.println("\n[RESUMEN FINAL]");
        System.out.println("• Fumador con TABACO fumó: " + cont_ftabaco + " cigarrillos");
        System.out.println("• Fumador con PAPEL fumó: " + cont_fpapel + " cigarrillos");
        System.out.println("• Fumador con CERILLAS fumó: " + cont_fcerillas + " cigarrillos");
        System.out.println("¡PROGRAMA COMPLETADO!");
    }
}
