package logica;

public class Tablero {
    private char[][] tablero;
    private static final int TAMAÑO = 3;
    public static final char VACIO = ' ';
    public static final char JUGADOR_X = 'X';
    public static final char JUGADOR_O = 'O';
    
    public Tablero() {
        reiniciar();
    }
    
    public void reiniciar() {
        tablero = new char[TAMAÑO][TAMAÑO];
        for (int i = 0; i < TAMAÑO; i++) {
            for (int j = 0; j < TAMAÑO; j++) {
                tablero[i][j] = VACIO;
            }
        }
    }
    
    public boolean hacerMovimiento(int fila, int columna, char jugador) {
        if (fila < 0 || fila >= TAMAÑO || columna < 0 || columna >= TAMAÑO) {
            return false;
        }
        if (tablero[fila][columna] != VACIO) {
            return false;
        }
        tablero[fila][columna] = jugador;
        return true;
    }
    
    public char obtenerCasilla(int fila, int columna) {
        if (fila < 0 || fila >= TAMAÑO || columna < 0 || columna >= TAMAÑO) {
            return VACIO;
        }
        return tablero[fila][columna];
    }
    
    public char verificarGanador() {
        // Verificar filas
        for (int i = 0; i < TAMAÑO; i++) {
            if (tablero[i][0] != VACIO && 
                tablero[i][0] == tablero[i][1] && 
                tablero[i][1] == tablero[i][2]) {
                return tablero[i][0];
            }
        }
        
        // Verificar columnas
        for (int j = 0; j < TAMAÑO; j++) {
            if (tablero[0][j] != VACIO && 
                tablero[0][j] == tablero[1][j] && 
                tablero[1][j] == tablero[2][j]) {
                return tablero[0][j];
            }
        }
        
        // Verificar diagonal principal
        if (tablero[0][0] != VACIO && 
            tablero[0][0] == tablero[1][1] && 
            tablero[1][1] == tablero[2][2]) {
            return tablero[0][0];
        }
        
        // Verificar diagonal secundaria
        if (tablero[0][2] != VACIO && 
            tablero[0][2] == tablero[1][1] && 
            tablero[1][1] == tablero[2][0]) {
            return tablero[0][2];
        }
        
        return VACIO;
    }
    
    public boolean estaLleno() {
        for (int i = 0; i < TAMAÑO; i++) {
            for (int j = 0; j < TAMAÑO; j++) {
                if (tablero[i][j] == VACIO) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean juegoTerminado() {
        return verificarGanador() != VACIO || estaLleno();
    }
    
    public char[][] copiarTablero() {
        char[][] copia = new char[TAMAÑO][TAMAÑO];
        for (int i = 0; i < TAMAÑO; i++) {
            for (int j = 0; j < TAMAÑO; j++) {
                copia[i][j] = tablero[i][j];
            }
        }
        return copia;
    }
    
    public int getTamaño() {
        return TAMAÑO;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAMAÑO; i++) {
            for (int j = 0; j < TAMAÑO; j++) {
                sb.append(tablero[i][j]);
                if (j < TAMAÑO - 1) sb.append("|");
            }
            sb.append("\n");
            if (i < TAMAÑO - 1) sb.append("-----\n");
        }
        return sb.toString();
    }
}