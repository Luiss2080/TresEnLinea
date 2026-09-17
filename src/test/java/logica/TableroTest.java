package logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableroTest {

    private Tablero tablero;

    @BeforeEach
    void setUp() {
        tablero = new Tablero();
    }

    @Test
    void tableroNuevoEstaCompletamenteVacio() {
        for (int i = 0; i < tablero.getTamaño(); i++) {
            for (int j = 0; j < tablero.getTamaño(); j++) {
                assertEquals(Tablero.VACIO, tablero.obtenerCasilla(i, j));
            }
        }
        assertFalse(tablero.estaLleno());
        assertEquals(Tablero.VACIO, tablero.verificarGanador());
        assertFalse(tablero.juegoTerminado());
    }

    @Test
    void hacerMovimientoEnCasillaVaciaTieneExito() {
        assertTrue(tablero.hacerMovimiento(0, 0, Tablero.JUGADOR_X));
        assertEquals(Tablero.JUGADOR_X, tablero.obtenerCasilla(0, 0));
    }

    @Test
    void hacerMovimientoEnCasillaOcupadaFalla() {
        assertTrue(tablero.hacerMovimiento(1, 1, Tablero.JUGADOR_X));
        assertFalse(tablero.hacerMovimiento(1, 1, Tablero.JUGADOR_O));
        // La casilla conserva la ficha del primer jugador, no se sobrescribe
        assertEquals(Tablero.JUGADOR_X, tablero.obtenerCasilla(1, 1));
    }

    @Test
    void hacerMovimientoFueraDeRangoFallaYNoLanzaExcepcion() {
        assertFalse(tablero.hacerMovimiento(-1, 0, Tablero.JUGADOR_X));
        assertFalse(tablero.hacerMovimiento(0, -1, Tablero.JUGADOR_X));
        assertFalse(tablero.hacerMovimiento(3, 0, Tablero.JUGADOR_X));
        assertFalse(tablero.hacerMovimiento(0, 3, Tablero.JUGADOR_X));
    }

    @Test
    void obtenerCasillaFueraDeRangoDevuelveVacioEnVezDeLanzarExcepcion() {
        assertEquals(Tablero.VACIO, tablero.obtenerCasilla(-1, -1));
        assertEquals(Tablero.VACIO, tablero.obtenerCasilla(5, 5));
    }

    @Test
    void detectaGanadorEnCadaFila() {
        for (int fila = 0; fila < 3; fila++) {
            Tablero t = new Tablero();
            t.hacerMovimiento(fila, 0, Tablero.JUGADOR_X);
            t.hacerMovimiento(fila, 1, Tablero.JUGADOR_X);
            t.hacerMovimiento(fila, 2, Tablero.JUGADOR_X);
            assertEquals(Tablero.JUGADOR_X, t.verificarGanador(), "Fila " + fila + " deberia dar la victoria");
        }
    }

    @Test
    void detectaGanadorEnCadaColumna() {
        for (int col = 0; col < 3; col++) {
            Tablero t = new Tablero();
            t.hacerMovimiento(0, col, Tablero.JUGADOR_O);
            t.hacerMovimiento(1, col, Tablero.JUGADOR_O);
            t.hacerMovimiento(2, col, Tablero.JUGADOR_O);
            assertEquals(Tablero.JUGADOR_O, t.verificarGanador(), "Columna " + col + " deberia dar la victoria");
        }
    }

    @Test
    void detectaGanadorEnDiagonalPrincipal() {
        tablero.hacerMovimiento(0, 0, Tablero.JUGADOR_X);
        tablero.hacerMovimiento(1, 1, Tablero.JUGADOR_X);
        tablero.hacerMovimiento(2, 2, Tablero.JUGADOR_X);
        assertEquals(Tablero.JUGADOR_X, tablero.verificarGanador());
    }

    @Test
    void detectaGanadorEnDiagonalSecundaria() {
        tablero.hacerMovimiento(0, 2, Tablero.JUGADOR_O);
        tablero.hacerMovimiento(1, 1, Tablero.JUGADOR_O);
        tablero.hacerMovimiento(2, 0, Tablero.JUGADOR_O);
        assertEquals(Tablero.JUGADOR_O, tablero.verificarGanador());
    }

    @Test
    void tableroLlenoSinGanadorEsEmpateYNoCuelgaNiLanzaExcepcion() {
        // X | O | X
        // X | O | O
        // O | X | X
        char[][] jugadas = {
            {'X', 'O', 'X'},
            {'X', 'O', 'O'},
            {'O', 'X', 'X'}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero.hacerMovimiento(i, j, jugadas[i][j]);
            }
        }

        assertTrue(tablero.estaLleno(), "El tablero debe detectarse como lleno");
        assertEquals(Tablero.VACIO, tablero.verificarGanador(), "No debe haber ganador en un empate");
        assertTrue(tablero.juegoTerminado(), "El juego debe darse por terminado (empate)");
    }

    @Test
    void reiniciarDejaElTableroVacioOtraVez() {
        tablero.hacerMovimiento(0, 0, Tablero.JUGADOR_X);
        tablero.hacerMovimiento(1, 1, Tablero.JUGADOR_O);
        tablero.reiniciar();

        for (int i = 0; i < tablero.getTamaño(); i++) {
            for (int j = 0; j < tablero.getTamaño(); j++) {
                assertEquals(Tablero.VACIO, tablero.obtenerCasilla(i, j));
            }
        }
    }

    @Test
    void copiarTableroDevuelveUnaCopiaIndependiente() {
        tablero.hacerMovimiento(0, 0, Tablero.JUGADOR_X);
        char[][] copia = tablero.copiarTablero();

        // Mutar el arreglo copiado no debe afectar el tablero original
        copia[0][0] = Tablero.JUGADOR_O;
        assertEquals(Tablero.JUGADOR_X, tablero.obtenerCasilla(0, 0));

        char[][] copiaFresca = tablero.copiarTablero();
        assertArrayEquals(new char[]{Tablero.JUGADOR_X, Tablero.VACIO, Tablero.VACIO}, copiaFresca[0]);
    }
}
