package problemaFumadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hilo del proveedor que coloca ingredientes en la mesa
 * Cada vez que actúa, selecciona dos ingredientes aleatorios
 * y notifica al fumador que tiene el tercer ingrediente
 */
public class Proveedor implements Runnable {
    @Override
    public void run() {
        try {
            // Producir exactamente el máximo de cigarrillos
            for (int i = 0; i < Principal.maximo_cigarrillos; i++) {
                // PASO 1: Esperar a que la mesa esté disponible
                // (Bloqueante - se detiene aquí si un fumador está usando la mesa)
                Principal.mesaDisponible.acquire();
                
                // PASO 2: Entrar en sección crítica (protege variables compartidas)
                Principal.mutex.acquire();
                
                // Crear lista de ingredientes disponibles [0, 1, 2]
                /* Tenemos un arreglo de Tipo Lista
                son estructuras de datos de tamaño dinámico que te permiten almacenar una secuencia ordenada de elementos.
                Tamaño dinámico: Las listas pueden crecer o encogerse automáticamente según necesites añadir o eliminar elementos.
                */
                Random random = new Random();
                List<Integer> ingredientesDisponibles = new ArrayList<>();
                for (int j = 0; j < Principal.ingrediente.length; j++) {
                    ingredientesDisponibles.add(j);
                    // Añadimos 3 indices estos 
                }
                
                // Seleccionar primer ingrediente aleatorio de (3) que estan en el arreglo de indices que hemos creado
                int idx1 = random.nextInt(ingredientesDisponibles.size());
                Principal.primero = ingredientesDisponibles.get(idx1);
                ingredientesDisponibles.remove(idx1);
                
                // Seleccionar segundo ingrediente aleatorio
                int idx2 = random.nextInt(ingredientesDisponibles.size());
                Principal.segundo = ingredientesDisponibles.get(idx2);
                ingredientesDisponibles.remove(idx2);
                
                // El ingrediente restante determina qué fumador debe actuar 
                Principal.tercero = ingredientesDisponibles.get(0);
                
                // Mostrar acción del proveedor
                System.out.println("\n[PROVEEDOR] Coloca en la mesa: " + 
                        Principal.ingrediente[Principal.primero] + " y " + 
                        Principal.ingrediente[Principal.segundo] + 
                        "\n• Avisa al fumador con " + Principal.ingrediente[Principal.tercero]);
                
                // PASO 3: Notificar al fumador correspondiente
                // (Libera su semáforo individual para que pueda actuar)
                Principal.semaforosFumadores[Principal.tercero].release();
                
                // PASO 4: Salir de sección crítica
                Principal.mutex.release();
                
                // Simular tiempo de preparación (no crítico)
                Thread.sleep(300);
            }
        } catch (InterruptedException ex) {
            System.err.println("[ERROR-Proveedor] " + ex.getMessage());
        } finally {
            // Al terminar, liberar a todos los fumadores
            for (int i = 0; i < Principal.semaforosFumadores.length; i++) {
                Principal.semaforosFumadores[i].release();
            }
            System.out.println("\n[PROVEEDOR] Terminó de producir - Notificando a todos los fumadores");
        }
    }
}
