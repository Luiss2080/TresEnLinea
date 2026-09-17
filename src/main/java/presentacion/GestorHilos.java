package presentacion;

import javax.swing.*;

/**
 * Punto unico de la aplicacion para decidir en que hilo corre una tarea de
 * la interfaz. Todo el codigo de {@code presentacion} debe pasar por aqui
 * en vez de crear {@link Thread}, usar un {@link java.util.concurrent.Executor}
 * propio, o llamar a {@link SwingUtilities} directamente - así el contrato
 * de hilos queda concentrado en una sola clase, fácil de auditar.
 *
 * <p><b>Contrato:</b> ningun metodo de esta clase entrega el control a un
 * hilo en segundo plano que luego toque un componente Swing directamente.
 * {@link #ejecutarEnEDT(Runnable)} y {@link #ejecutarConRetraso(Runnable, int)}
 * siempre terminan ejecutando la tarea en el Event Dispatch Thread (EDT), que
 * es el unico hilo desde el que Swing permite leer o modificar sus
 * componentes de forma segura.</p>
 *
 * <p>Anteriormente esta clase tambien exponia un {@code ejecutarAsync(Runnable)}
 * que despachaba la tarea al {@code ForkJoinPool.commonPool()} (un hilo que
 * NO es el EDT) sin ninguna forma de volver al EDT despues. No tenia
 * llamadores en el codigo, pero era una trampa lista para producir una
 * violacion real del EDT en cuanto alguien lo usara para actualizar la UI
 * al terminar. Se elimino en vez de dejarlo como deuda tecnica latente; el
 * calculo de la IA (Minimax sobre un tablero 3x3, unos pocos milisegundos
 * en el peor caso real del juego) es lo bastante rapido para ejecutarse
 * directamente en el EDT via {@link #ejecutarConRetraso(Runnable, int)}, sin
 * necesitar un hilo aparte.</p>
 */
public class GestorHilos {

    /**
     * Ejecuta {@code tarea} en el EDT. Si quien llama ya esta en el EDT, la
     * corre inline (sin encolar); si no, la encola con
     * {@link SwingUtilities#invokeLater(Runnable)}.
     */
    public static void ejecutarEnEDT(Runnable tarea) {
        if (SwingUtilities.isEventDispatchThread()) {
            tarea.run();
        } else {
            SwingUtilities.invokeLater(tarea);
        }
    }

    /**
     * Ejecuta {@code tarea} una sola vez, tras {@code delayMs} milisegundos,
     * en el EDT. Se implementa con un {@link Timer} de Swing (no con un
     * {@link Thread} ni un hilo del pool comun), por lo que la tarea corre
     * de forma segura sin necesidad de marshaling adicional.
     */
    public static void ejecutarConRetraso(Runnable tarea, int delayMs) {
        Timer timer = new Timer(delayMs, e -> {
            tarea.run();
            ((Timer) e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
