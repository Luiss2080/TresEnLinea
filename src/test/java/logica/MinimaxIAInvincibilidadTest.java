package logica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verificacion EXHAUSTIVA de la afirmacion central del proyecto: "la IA es invencible".
 *
 * <p>En vez de jugar unas pocas partidas de muestra, estas pruebas recorren el arbol de
 * juego COMPLETO del tres en raya (todas las secuencias legales de movimientos del
 * oponente, en cada punto de decision), y verifican que la IA (Minimax, ficha 'O')
 * jamas llega a un estado terminal de derrota, sin importar como juegue el humano.</p>
 *
 * <p>Se prueban las dos configuraciones posibles:</p>
 * <ul>
 *   <li>La IA mueve SEGUNDO (el flujo real del juego: {@code VentanaPrincipal} siempre
 *       deja que el humano mueva primero).</li>
 *   <li>La IA mueve PRIMERO (configuracion hipotetica, no usada por la UI actual, pero
 *       que {@link MinimaxIA#obtenerMejorMovimiento(Tablero)} debe resolver igual de
 *       bien porque no asume nada sobre quien empezo la partida).</li>
 * </ul>
 */
class MinimaxIAInvincibilidadTest {

    private long estadosTerminales;
    private long victoriasIA;
    private long empates;
    private long derrotasIA;

    @Test
    void iaNuncaPierdeCuandoElHumanoMuevePrimero_juegoRealDeLaApp() {
        estadosTerminales = 0;
        victoriasIA = 0;
        empates = 0;
        derrotasIA = 0;

        explorarHumanoPrimero(new Tablero(), true);

        // Numeros conocidos del arbol completo del tres en raya cuando el segundo
        // jugador (aqui la IA) juega de forma optima: 569 partidas terminales posibles,
        // de las cuales la IA gana 386 y empata 183. Cero derrotas.
        assertEquals(569, estadosTerminales, "Deberian explorarse las 569 partidas terminales posibles");
        assertEquals(0, derrotasIA, "La IA NUNCA debe perder jugando en segundo lugar");
        assertEquals(386, victoriasIA, "La IA deberia ganar en 386 de las 569 lineas de juego");
        assertEquals(183, empates, "El resto de las partidas (183) deben terminar en empate");
        assertTrue(victoriasIA + empates == estadosTerminales, "Toda partida debe ser victoria de la IA o empate");
    }

    @Test
    void iaNuncaPierdeCuandoLaIaMuevePrimero_configuracionHipotetica() {
        estadosTerminales = 0;
        victoriasIA = 0;
        empates = 0;
        derrotasIA = 0;

        explorarIaPrimero(new Tablero());

        assertEquals(73, estadosTerminales, "Deberian explorarse las 73 partidas terminales posibles");
        assertEquals(0, derrotasIA, "La IA NUNCA debe perder jugando en primer lugar");
        assertEquals(71, victoriasIA);
        assertEquals(2, empates);
    }

    @Test
    void mejorResultadoDelHumanoEsElEmpate_noExisteLineaGanadoraParaElHumano() {
        // Corolario directo de la invencibilidad: en ninguna de las 569 lineas de juego
        // reales (humano primero) el humano puede forzar una victoria.
        estadosTerminales = 0;
        victoriasIA = 0;
        empates = 0;
        derrotasIA = 0;

        explorarHumanoPrimero(new Tablero(), true);

        assertEquals(0, derrotasIA, "No debe existir ninguna secuencia de movimientos humanos que gane");
    }

    // Humano (X) juega de forma adversarial en cada ramificacion; la IA (O) responde
    // de forma deterministica usando el algoritmo bajo prueba.
    private void explorarHumanoPrimero(Tablero tablero, boolean turnoHumano) {
        char ganador = tablero.verificarGanador();
        if (ganador != Tablero.VACIO || tablero.estaLleno()) {
            registrarResultado(ganador);
            return;
        }

        if (turnoHumano) {
            for (int i = 0; i < tablero.getTamaño(); i++) {
                for (int j = 0; j < tablero.getTamaño(); j++) {
                    if (tablero.obtenerCasilla(i, j) == Tablero.VACIO) {
                        Tablero copia = copiar(tablero);
                        copia.hacerMovimiento(i, j, Tablero.JUGADOR_X);
                        explorarHumanoPrimero(copia, false);
                    }
                }
            }
        } else {
            MinimaxIA.Movimiento movimiento = MinimaxIA.obtenerMejorMovimiento(tablero);
            Tablero copia = copiar(tablero);
            copia.hacerMovimiento(movimiento.fila, movimiento.columna, Tablero.JUGADOR_O);
            explorarHumanoPrimero(copia, true);
        }
    }

    // La IA (O) mueve primero de forma deterministica; el humano (X) juega de forma
    // adversarial (todas las respuestas posibles) en cada ramificacion.
    private void explorarIaPrimero(Tablero tablero) {
        char ganador = tablero.verificarGanador();
        if (ganador != Tablero.VACIO || tablero.estaLleno()) {
            registrarResultado(ganador);
            return;
        }

        int equisContadas = 0;
        int oesContadas = 0;
        for (int i = 0; i < tablero.getTamaño(); i++) {
            for (int j = 0; j < tablero.getTamaño(); j++) {
                char casilla = tablero.obtenerCasilla(i, j);
                if (casilla == Tablero.JUGADOR_X) equisContadas++;
                else if (casilla == Tablero.JUGADOR_O) oesContadas++;
            }
        }

        boolean turnoIA = oesContadas <= equisContadas; // la IA abrio la partida
        if (turnoIA) {
            MinimaxIA.Movimiento movimiento = MinimaxIA.obtenerMejorMovimiento(tablero);
            Tablero copia = copiar(tablero);
            copia.hacerMovimiento(movimiento.fila, movimiento.columna, Tablero.JUGADOR_O);
            explorarIaPrimero(copia);
        } else {
            for (int i = 0; i < tablero.getTamaño(); i++) {
                for (int j = 0; j < tablero.getTamaño(); j++) {
                    if (tablero.obtenerCasilla(i, j) == Tablero.VACIO) {
                        Tablero copia = copiar(tablero);
                        copia.hacerMovimiento(i, j, Tablero.JUGADOR_X);
                        explorarIaPrimero(copia);
                    }
                }
            }
        }
    }

    private void registrarResultado(char ganador) {
        estadosTerminales++;
        if (ganador == Tablero.JUGADOR_O) {
            victoriasIA++;
        } else if (ganador == Tablero.JUGADOR_X) {
            derrotasIA++;
        } else {
            empates++;
        }
    }

    private Tablero copiar(Tablero original) {
        Tablero copia = new Tablero();
        char[][] datos = original.copiarTablero();
        for (int i = 0; i < original.getTamaño(); i++) {
            for (int j = 0; j < original.getTamaño(); j++) {
                if (datos[i][j] != Tablero.VACIO) {
                    copia.hacerMovimiento(i, j, datos[i][j]);
                }
            }
        }
        return copia;
    }
}
