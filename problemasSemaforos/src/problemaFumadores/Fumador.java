package problemaFumadores;

/**
 * Hilo de un fumador específico
 * Cada fumador tiene un ingrediente permanente:
 * - id=0: TABACO
 * - id=1: PAPEL
 * - id=2: CERILLAS
 */
public class Fumador implements Runnable {
    private final int id;  // Identificador del fumador (0-2)

   /* Constructor Fumador
    
    
    Inicializar el fumador con un identificador único (0, 1 o 2) que representa el ingrediente que posee.
    Un constructor es un método especial que se utiliza para inicializar un objeto de una clase cuando este
     es creado. Su función principal es asignar valores iniciales a los atributos del objeto y realizar cualquier 
     otra configuración necesaria antes de que el objeto esté listo para ser utilizado. 
     se usa Cada vez que se crea una instancia de `Fumador` (una vez por cada fumador en el método `main`).
     El constructor se ejecuta , cuando se crea el objeto, antes de que se inicie el hilo (antes de llamar a `start()`).
     new Thread(new Fumador(0)).start(); // Fumador con id=0 (TABACO)
     new Thread(new Fumador(1)).start(); // Fumador con id=1 (PAPEL)
     new Thread(new Fumador(2)).start(); // Fumador con id=2 (CERILLAS)
   */ 
    public Fumador(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // PASO A: Esperar notificación del proveedor
                // (Bloqueante - espera hasta que su semáforo específico sea liberado)
                Principal.semaforosFumadores[id].acquire();
                
                // PASO B: Entrar en sección crítica
                Principal.mutex.acquire();
                
                // Verificar si se alcanzó el límite de producción
                if (Principal.cigarrillosFumados >= Principal.maximo_cigarrillos) {
                    Principal.mutex.release();
                    break;  // Terminar ciclo
                }
                
                // PASO C: Tomar ingredientes y fabricar cigarrillo
                System.out.println("\n[FUMADOR " + Principal.ingrediente[id] + "]"
                        + "\n-> Toma de la mesa: " + Principal.ingrediente[Principal.primero] + 
                        " y " + Principal.ingrediente[Principal.segundo] +
                        "\n-> Combina con su " + Principal.ingrediente[id] + 
                        "\n-> Fabrica y fuma cigarrillo #" + (Principal.cigarrillosFumados + 1));
                
                // Actualizar contadores
                Principal.cigarrillosFumados++;
                switch (id) {
                    case 0: 
                        Principal.cont_ftabaco++; 
                        break;
                    case 1: 
                        Principal.cont_fpapel++;
                        break;
                    case 2: 
                        Principal.cont_fcerillas++;
                        break;
                }
                
                // Mostrar progreso general
                System.out.println("  [PROGRESO] Total fumados: " + Principal.cigarrillosFumados +
                                 " | Tabaco: " + Principal.cont_ftabaco +
                                 " | Papel: " + Principal.cont_fpapel +
                                 " | Cerillas: " + Principal.cont_fcerillas);
                
                // PASO D: Liberar la mesa para nuevo uso
                Principal.mesaDisponible.release();
                
                // PASO E: Salir de sección crítica
                Principal.mutex.release();
                
                // Simular tiempo de fumar 
                Thread.sleep(200);
            }
        } catch (InterruptedException ex) {
            System.err.println("[ERROR-Fumador " + Principal.ingrediente[id] + "] " + ex.getMessage());
        }
        System.out.println("[FUMADOR " + Principal.ingrediente[id] + "] Terminó");
    }
}
