package problemaFumadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/*Se implementa la interfaz Runnable para separar la lógica de la tarea
 del control del hilo en sí. 
 define un solo método: run().  
 Se puede usar esa instancia como parametro a un objeto Thread para ejecutarla en paralelo.
*/

public class Proveedor implements Runnable {
	
	@Override
	// Sobreescribiremos el metodo run para controlar lo que se ejecutará
	
	public void run() {
		while (Principal.cigarrillos < Principal.maximo_cigarrillos ) {
			
			try {
				Random random = new Random();
				List<Integer> numero = new ArrayList<>();
				for (int j = 0 ; j < Principal.ingrediente.length; j++) {
					numero.add(j);
				}
				// Nos permite elegir que tipo de ingrediente se coloca en la mesa
				int tamanio = numero.size();
				
				
				// Entrada a la SECCIÓN CRÍTICA
				Principal.mutex.acquire();
				// Al estar el semaforo mutext en 1 el proveedro entra y bloquea el mutex
				Principal.primero = random.nextInt(tamanio);
				
				/* Selecciona aleatoriamente el tipo de fumador o el ingrediente que se le dará
				al primero
				*/
				numero.remove(Principal.primero);
				tamanio --;
				/* UNA VEZ seleccionado el primer elemento (1 de los 2 que iran en la mesa)
					Se elimina de las opciones para ser elegido
				*/
				int elemento2;
				elemento2 = random.nextInt(tamanio);
				/*
				 * Se selecciona el elemento (2 /2 para ser puesto en la mesa) 
				 */
				
				Principal.segundo= numero.get(elemento2);
				numero.remove(elemento2);
				// lo elimina del arreglo numero 
				// Selecciona el tipo de fumador 3 de el ingrediente que queda
				Principal.tercero = numero.get(0);
				/*
				 * Se selecciona o se obtinene el ultimo elemento que será el que tenga el FUMADOR correpondiente
				 */
				System.out.println("El proveedor PONE " + Principal.ingrediente[Principal.primero] +
				" y " + Principal.ingrediente[Principal.segundo] + " encima de la mesa y AVISA al fumador "+
				" que tiene " + Principal.ingrediente[Principal.tercero]);
				
				Principal.noVacio.release();
				//Desbloquea el semaforo para que pueda entrar el fumador diciendo que la mesa tiene 
				//ingredientes AVISA AL FUMADOR CORRESPONDIENTE para que TOME los materiales
				Thread.sleep(100);
				//Espera
				
			}catch (InterruptedException ex) {
				    System.err.println("Error en el hilo Proveedor: " + ex.getMessage());
				    ex.printStackTrace(); // Esto imprime toda la traza del error
			}
		
			
		}
		Principal.noLleno.release(3);
		/* Una vez finalizado o llegado a la cantidad de cigarrillos definida el proveedor ya no debe 
		poner mas elementos por ello ya no hay ingrediente ya no deja a los proximos 3 que entren 
		*/
	}
}
