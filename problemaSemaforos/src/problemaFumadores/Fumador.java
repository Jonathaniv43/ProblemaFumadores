package problemaFumadores;

public class Fumador implements Runnable {
	
	@Override 
	
	public void run() {
		while (true) {
			try {
				Principal.noLleno.acquire();
				/* Entran los fumadores hasta llegar al ultimo (10)
				 Es el que controla cuantas veces se ejecutará o la parte de los fumadores
				
				*/
				if (Principal.cigarrillos < Principal.maximo_cigarrillos) {
					// Mientras no se llegue al maximo definido el proceso seguirá
					
					// ENTRADA A LA SECCIÓN CRÍTICA
					Principal.noVacio.acquire();
					/* Una vez el proveedor entra llena la mesa y libera el bloqeo para que el fumador pueda entrar
					ENTRA Y BLOQUEA EL RECURSO 
					*/
					System.out.println("El fumador que tiene "+ Principal.ingrediente[Principal.tercero] + " TOMA los materiales"
							+ " restantes " +
					Principal.ingrediente[Principal.segundo] + " y " + Principal.ingrediente[Principal.primero]+
					" y FABRICA su cigarro.");
					System.out.println("Fuma el Cigarro...");
					/* En el proveedor definimos que aleatoriamente se le definiria un material al fumador 3 y le
					asignaria los que le faltan para que pueda formar su cigarro 
					en este apartado TOMA los Materiales y Forma su cigarro
					*/
					
					Principal.cigarrillos++;
					// Cuenta los cigarrillos que se han CREADO 
					
					System.out.println("\nSe han fumado " + Principal.cigarrillos + " en total") ;
					
					// Verificaremos quien fue el 3r Fumador 
					/*
					 * Definimos un  Arreglo de  definimos de 3 elementos dentro de
					 * la posición (0) Se encruntra TABACO
					 * en la posicion (1) se encuenta PAPEL
					 * en la Posicion (2) se encuenta CERILLOS
					 * se elige un random un las posiciones para ser primero segundo y tercero (Ingredientes elegidps) y el tercero
					 * simula en que ya tenia el fumador. Asi es como se escoge
					 *  auque se pueda pensar que primero se refiere a la posicion en realidad se refiere al primer random que se 
					 *  escogio, y asi con los demas
					 *   posiciones pero el la posicion sigue ligada al valor que se le definió
					 *
					 */
					if (Principal.tercero == 0) Principal.cont_ftabaco ++;
					else if (Principal.tercero == 1) Principal.cont_fpapel ++;
					else Principal.cont_fcerillas ++;
					
					Thread.sleep(100);
					// FIN DE LA SECCION CRITICA - LA MESA VUELVE A ESTAR DISPONIBLE PARA SER LLENADA
					Principal.mutex.release();
					// SE Libera el Acceso a la MESA con el semaforo mutex rara que el PROVEEDOR pueda volver a entrar
					
			
				/* Si ya se consimieron el numero de cigarrillos ya no le da entrada a mas fumadores
				PROCESO TERMINA
				*/
			}else break;
				
				
		}catch (InterruptedException ex) {
			 System.err.println("Error en el hilo FUMADOR: " + ex.getMessage());
			    ex.printStackTrace(); } // Esto imprime toda la traza del error
			   	
		}
	}
	
}