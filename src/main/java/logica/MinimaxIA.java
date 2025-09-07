// Algoritmo Minimax para IA
package logica;

public class MinimaxIA {
    private static final char IA = Tablero.JUGADOR_O;
    private static final char HUMANO = Tablero.JUGADOR_X;
    
    public static class Movimiento {
        public int fila;
        public int columna;
        public int puntuacion;
        
        public Movimiento() {
            this.fila = -1;
            this.columna = -1;
            this.puntuacion = 0;
        }
        
        public Movimiento(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
            this.puntuacion = 0;
        }
    }
    
    public static Movimiento obtenerMejorMovimiento(Tablero tablero) {
        Movimiento mejorMovimiento = new Movimiento();
        int mejorPuntuacion = Integer.MIN_VALUE;
        
        for (int i = 0; i < tablero.getTamaño(); i++) {
            for (int j = 0; j < tablero.getTamaño(); j++) {
                if (tablero.obtenerCasilla(i, j) == Tablero.VACIO) {
                    Tablero copiaTablero = crearCopia(tablero);
                    copiaTablero.hacerMovimiento(i, j, IA);
                    
                    int puntuacion = minimax(copiaTablero, 0, false);
                    
                    if (puntuacion > mejorPuntuacion) {
                        mejorPuntuacion = puntuacion;
                        mejorMovimiento.fila = i;
                        mejorMovimiento.columna = j;
                        mejorMovimiento.puntuacion = puntuacion;
                    }
                }
            }
        }
        
        return mejorMovimiento;
    }
    
    private static int minimax(Tablero tablero, int profundidad, boolean esMaximizando) {
        char ganador = tablero.verificarGanador();
        
        if (ganador == IA) {
            return 10 - profundidad;
        } else if (ganador == HUMANO) {
            return profundidad - 10;
        } else if (tablero.estaLleno()) {
            return 0;
        }
        
        if (esMaximizando) {
            int mejorPuntuacion = Integer.MIN_VALUE;
            
            for (int i = 0; i < tablero.getTamaño(); i++) {
                for (int j = 0; j < tablero.getTamaño(); j++) {
                    if (tablero.obtenerCasilla(i, j) == Tablero.VACIO) {
                        Tablero copiaTablero = crearCopia(tablero);
                        copiaTablero.hacerMovimiento(i, j, IA);
                        
                        int puntuacion = minimax(copiaTablero, profundidad + 1, false);
                        mejorPuntuacion = Math.max(mejorPuntuacion, puntuacion);
                    }
                }
            }
            
            return mejorPuntuacion;
        } else {
            int mejorPuntuacion = Integer.MAX_VALUE;
            
            for (int i = 0; i < tablero.getTamaño(); i++) {
                for (int j = 0; j < tablero.getTamaño(); j++) {
                    if (tablero.obtenerCasilla(i, j) == Tablero.VACIO) {
                        Tablero copiaTablero = crearCopia(tablero);
                        copiaTablero.hacerMovimiento(i, j, HUMANO);
                        
                        int puntuacion = minimax(copiaTablero, profundidad + 1, true);
                        mejorPuntuacion = Math.min(mejorPuntuacion, puntuacion);
                    }
                }
            }
            
            return mejorPuntuacion;
        }
    }
    
    private static Tablero crearCopia(Tablero original) {
        Tablero copia = new Tablero();
        char[][] tableroOriginal = original.copiarTablero();
        
        for (int i = 0; i < original.getTamaño(); i++) {
            for (int j = 0; j < original.getTamaño(); j++) {
                if (tableroOriginal[i][j] != Tablero.VACIO) {
                    copia.hacerMovimiento(i, j, tableroOriginal[i][j]);
                }
            }
        }
        
        return copia;
    }
    
    public static boolean esMovimientoValido(Tablero tablero, int fila, int columna) {
        return fila >= 0 && fila < tablero.getTamaño() && 
               columna >= 0 && columna < tablero.getTamaño() && 
               tablero.obtenerCasilla(fila, columna) == Tablero.VACIO;
    }
}