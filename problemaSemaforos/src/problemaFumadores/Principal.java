package problemaFumadores;


import java.util.concurrent.Semaphore;


public class Principal {
	/* Declara el contador de los cigarrillos 
	Se declara VOLATILE para que no se almacene en cache sino que siempre se lea de memoria
	UTIL para trabajar con datos que son alterados por hilos 
	*/
	static volatile int cigarrillos = 0;
	// Bloquea el ingreso de los fumadores mientras no haya cigarrillos
	static public Semaphore noVacio = new Semaphore(0);
	//Permite la entrada del proveedor a la maesa
	static public Semaphore mutex = new Semaphore(1);
	// inicializado en 1 para que el primero que entre sea el proveedor 
	static public Semaphore noLleno = new Semaphore(10);
	//Semaforo contador (10) fumadores
	static public int fumadores=3;
	//Total de hilos que se ejecutaran	
	static String ingrediente[] = {"TABACO", "PAPEL","CERILLAS"};
	//ingrediente que tendrá cada hilo
	static final int maximo_cigarrillos = 10;
	// numero maximo de cigarrillos antes de que se de por completado el programa
	static volatile int cont_ftabaco= 0;
	// Contador de el fumador que tiene el tabaco (Puros Fumados)
	static volatile int cont_fpapel= 0;
	// Contador de el fumador que tiene el papel (Puros Fumados)
	static volatile int cont_fcerillas= 0;
	// Contador de el fumador que tiene los fosforos/cerrillas (Puros Fumados)
	
	static int  primero; 
	static int  segundo; 
	static int  tercero; 
	// 

	public static void main(String[] args)throws InterruptedException {
		
		System.out.println("Inicio de Programa El proceso de proveedor se inicializa");
		System.out.println("Hay un fumador que recibió el TABACO / El proceso de el fumador 1 se inicializa");
		System.out.println("Hay un fumador que recibió el PAPEL / El proceso de el fumador 2 se inicializa");
		System.out.println("Hay un fumador que recibió las CERRILLAS / El proceso de el fumador 3 se inicializa");
		
		//Hilo de fumador
		Thread[] fumador = new Thread[fumadores];
		
		
		
		Thread proveedor = new Thread(new Proveedor());
		//hilo del proveedor
		proveedor.start();
		//inicializamoe el hilo
		
		int i;
		for ( i =0; i <fumadores; i++ ) {
			fumador[i]= new Thread (new Fumador());
			// Creamos un hilo para cada fumador
			fumador[i].start();
			// Inicializamos a ese fumador
		}
		
		proveedor.join();
		// Cerrar el hilo del proveedor cuando termine
		
		System.out.println("El proveedor terminó su labor");
		for (i = 0; i < fumadores; i++) {
			fumador[i].join();
			// Cerrar cada uno de los hilos de los fumadores al terminar
		}
		System.out.println("El fumador con el tabaco fumo " + cont_ftabaco + "cigarrillos");
		System.out.println("El fumador con el papel fumo " + cont_fpapel + "cigarrillos");
		System.out.println("El fumador con el cerillas fumo " + cont_fcerillas + "cigarrillos");
		System.out.println("¡El programa ha terminado!");
	}
	
}
