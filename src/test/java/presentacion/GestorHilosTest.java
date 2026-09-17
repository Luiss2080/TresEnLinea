package presentacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.swing.SwingUtilities;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GestorHilos es el unico punto del proyecto que decide en que hilo corre
 * cada tarea de la UI. Estas pruebas verifican su contrato de hilos, que es
 * la base de la seguridad del EDT en toda la aplicacion: nada debe tocar un
 * componente Swing fuera del Event Dispatch Thread.
 */
class GestorHilosTest {

    @Test
    void ejecutarEnEDTCorreInmediatamenteSiYaEstaEnElEDT() throws Exception {
        AtomicBoolean ejecutado = new AtomicBoolean(false);
        AtomicBoolean corrioEnElEDT = new AtomicBoolean(false);

        SwingUtilities.invokeAndWait(() ->
            GestorHilos.ejecutarEnEDT(() -> {
                ejecutado.set(true);
                corrioEnElEDT.set(SwingUtilities.isEventDispatchThread());
            })
        );

        assertTrue(ejecutado.get());
        assertTrue(corrioEnElEDT.get(), "La tarea debe ejecutarse en el EDT");
    }

    @Test
    @Timeout(5)
    void ejecutarEnEDTEncolaLaTareaCuandoSeLlamaFueraDelEDT() throws InterruptedException {
        assertFalse(SwingUtilities.isEventDispatchThread(), "Esta prueba corre a proposito fuera del EDT");

        CountDownLatch listo = new CountDownLatch(1);
        AtomicBoolean corrioEnElEDT = new AtomicBoolean(false);

        GestorHilos.ejecutarEnEDT(() -> {
            corrioEnElEDT.set(SwingUtilities.isEventDispatchThread());
            listo.countDown();
        });

        assertTrue(listo.await(4, TimeUnit.SECONDS), "La tarea deberia ejecutarse via invokeLater");
        assertTrue(corrioEnElEDT.get(), "La tarea encolada tambien debe terminar corriendo en el EDT");
    }

    @Test
    @Timeout(5)
    void ejecutarConRetrasoEjecutaLaTareaUnaSolaVezYEnElEDT() throws InterruptedException {
        CountDownLatch listo = new CountDownLatch(1);
        AtomicBoolean corrioEnElEDT = new AtomicBoolean(false);
        AtomicInteger contador = new AtomicInteger(0);

        GestorHilos.ejecutarConRetraso(() -> {
            contador.incrementAndGet();
            corrioEnElEDT.set(SwingUtilities.isEventDispatchThread());
            listo.countDown();
        }, 50);

        assertTrue(listo.await(4, TimeUnit.SECONDS));
        Thread.sleep(200); // margen para confirmar que no se repite
        assertEquals(1, contador.get(), "El temporizador no debe repetirse (setRepeats(false))");
        assertTrue(corrioEnElEDT.get());
    }
}
