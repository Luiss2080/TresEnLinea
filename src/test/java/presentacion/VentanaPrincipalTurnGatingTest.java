package presentacion;

import logica.Tablero;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Regresion para un bug real encontrado en la auditoria: entre el momento en
 * que el jugador mueve y el momento en que la IA responde, la aplicacion
 * espera 500ms (GestorHilos.ejecutarConRetraso en hacerMovimientoJugador).
 * Antes de esta correccion, {@code juegoActivo} por si solo NO bloqueaba al
 * jugador durante esa ventana: un segundo clic en otra casilla vacia
 * colocaba una segunda 'X' en el tablero antes de que la IA hubiera movido.
 *
 * <p>Verificado directamente contra el codigo sin corregir antes de escribir
 * este test: con dos llamadas seguidas a hacerMovimientoJugador (simulando
 * un doble clic rapido) el tablero terminaba con dos 'X' y cero 'O'. Con la
 * bandera esperandoIA (ver VentanaPrincipal.puedeJugarHumano()), el segundo
 * clic se rechaza y el tablero queda con exactamente una 'X'.</p>
 *
 * <p>Requiere un entorno grafico real (JFrame no se puede instanciar en modo
 * headless); si no hay uno disponible el test se salta en vez de fallar.</p>
 */
class VentanaPrincipalTurnGatingTest {

    @Test
    @Timeout(10)
    void segundoClicDuranteElTurnoDeLaIaEsRechazado() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requiere un entorno grafico (o Xvfb) para crear un JFrame real");

        VentanaPrincipal[] ventana = new VentanaPrincipal[1];
        SwingUtilities.invokeAndWait(() -> ventana[0] = new VentanaPrincipal());

        // nuevaPartida() activa juegoActivo mediante su propio Timer de 100ms;
        // dejarlo terminar antes de simular los clics del jugador.
        Thread.sleep(300);

        SwingUtilities.invokeAndWait(() -> {
            ventana[0].hacerMovimientoJugador(0, 0);
            ventana[0].hacerMovimientoJugador(1, 1); // clic rapido, antes de que la IA responda
        });

        Tablero tablero = ventana[0].getTablero();
        int equis = 0;
        int oes = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char casilla = tablero.obtenerCasilla(i, j);
                if (casilla == Tablero.JUGADOR_X) equis++;
                else if (casilla == Tablero.JUGADOR_O) oes++;
            }
        }

        assertEquals(1, equis, "El jugador no debe poder colocar una segunda X mientras se espera a la IA");
    }
}
