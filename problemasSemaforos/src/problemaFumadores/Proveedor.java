package problemaFumadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Proveedor implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 0; i < Principal.maximo_cigarrillos; i++) {
                // Esperar a que la mesa esté disponible
                Principal.mesaDisponible.acquire();
                
                // Entrar a sección crítica
                Principal.mutex.acquire();
                
                // Generar ingredientes aleatorios
                Random random = new Random();
                List<Integer> numero = new ArrayList<>();
                for (int j = 0; j < Principal.ingrediente.length; j++) {
                    numero.add(j);
                }
                
                int indexPrimero = random.nextInt(numero.size());
                Principal.primero = numero.get(indexPrimero);
                numero.remove(indexPrimero);
                
                int indexSegundo = random.nextInt(numero.size());
                Principal.segundo = numero.get(indexSegundo);
                numero.remove(indexSegundo);
                
                Principal.tercero = numero.get(0);
                
                System.out.println("El proveedor PONE " + Principal.ingrediente[Principal.primero] +
                        " y " + Principal.ingrediente[Principal.segundo] + " encima de la mesa y AVISA al fumador " +
                        " que tiene " + Principal.ingrediente[Principal.tercero]);
                
                // Desbloquear solo al fumador correspondiente
                Principal.semaforosFumadores[Principal.tercero].release();
                
                Principal.mutex.release();
                Thread.sleep(500); // Simular tiempo de preparación
            }
        } catch (InterruptedException ex) {
            System.err.println("Error en el hilo Proveedor: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Liberar a todos los fumadores al finalizar
            for (int i = 0; i < Principal.semaforosFumadores.length; i++) {
                Principal.semaforosFumadores[i].release();
            }
            System.out.println("Proveedor: Liberados todos los fumadores al terminar.");
        }
    }
}