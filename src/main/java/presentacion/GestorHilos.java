package presentacion;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

public class GestorHilos {
    
    public static void ejecutarAsync(Runnable tarea) {
        CompletableFuture.runAsync(() -> {
            try {
                tarea.run();
            } catch (Exception e) {
                System.err.println("Error en tarea asíncrona: " + e.getMessage());
            }
        });
    }
    
    public static void ejecutarEnEDT(Runnable tarea) {
        if (SwingUtilities.isEventDispatchThread()) {
            tarea.run();
        } else {
            SwingUtilities.invokeLater(tarea);
        }
    }
    
    public static void ejecutarConRetraso(Runnable tarea, int delayMs) {
        Timer timer = new Timer(delayMs, e -> {
            tarea.run();
            ((Timer)e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void detenerTimers(JComponent componente) {
        // TODO: implementar registro de timers activos
    }
}
