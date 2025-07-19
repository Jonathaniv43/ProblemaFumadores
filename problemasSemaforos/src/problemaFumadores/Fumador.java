package problemaFumadores;

public class Fumador implements Runnable {
    private final int id;

    public Fumador(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Esperar notificación del proveedor
                Principal.semaforosFumadores[id].acquire();
                
                // Entrar a sección crítica
                Principal.mutex.acquire();
                
                // Verificar si debe terminar
                if (Principal.cigarrillosFumados >= Principal.maximo_cigarrillos) {
                    Principal.mutex.release();
                    break;
                }
                
                System.out.println("El fumador que tiene "+ Principal.ingrediente[Principal.tercero] + " TOMA los materiales" +
                        " restantes " + Principal.ingrediente[Principal.segundo] + " y " + Principal.ingrediente[Principal.primero] +
                        " y FABRICA su cigarro.");
                System.out.println("Fuma el Cigarro...");
                
                // Actualizar contadores
                Principal.cigarrillosFumados++;
                switch (id) {
                    case 0: 
                        Principal.cont_ftabaco++;
                        break;
                    case 1: 
                        Principal.cont_fpapel++;
                        break; // Break crucial agregado
                    case 2: 
                        Principal.cont_fcerillas++;
                        break;
                }
                
                System.out.println("\nSe han fumado " + Principal.cigarrillosFumados + " en total");
                
                // Liberar la mesa para el próximo
                Principal.mesaDisponible.release();
                
                Principal.mutex.release();
                Thread.sleep(100); // Simular tiempo de fumar
            }
        } catch (InterruptedException ex) {
            System.err.println("Error en el hilo FUMADOR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}