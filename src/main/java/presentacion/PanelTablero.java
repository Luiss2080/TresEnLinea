package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logica.Tablero;

public class PanelTablero extends JPanel {
    private JButton[][] botones;
    private VentanaPrincipal ventanaPrincipal;
    private static final int TAMAÑO = 3;
    
    public PanelTablero(VentanaPrincipal ventana) {
        this.ventanaPrincipal = ventana;
        this.botones = new JButton[TAMAÑO][TAMAÑO];
        
        inicializarPanel();
        crearBotones();
    }
    
    private void inicializarPanel() {
        setLayout(new GridLayout(TAMAÑO, TAMAÑO, 2, 2));
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(400, 400));
    }
    
    private void crearBotones() {
        for (int i = 0; i < TAMAÑO; i++) {
            for (int j = 0; j < TAMAÑO; j++) {
                botones[i][j] = crearBoton(i, j);
                add(botones[i][j]);
            }
        }
    }
    
    private JButton crearBoton(int fila, int columna) {
        JButton boton = new JButton();
        
        boton.setFont(new Font("Arial", Font.BOLD, 48));
        boton.setBackground(new Color(70, 130, 180));
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(120, 120));
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (boton.getText().isEmpty() && ventanaPrincipal.isJuegoActivo()) {
                    boton.setBackground(new Color(100, 149, 237));
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (boton.getText().isEmpty()) {
                    boton.setBackground(new Color(70, 130, 180));
                }
            }
        });
        
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ventanaPrincipal.isJuegoActivo() && boton.getText().isEmpty()) {
                    ventanaPrincipal.hacerMovimientoJugador(fila, columna);
                }
            }
        });
        
        return boton;
    }
    
    public void actualizarTablero() {
        Tablero tablero = ventanaPrincipal.getTablero();
        
        for (int i = 0; i < TAMAÑO; i++) {
            for (int j = 0; j < TAMAÑO; j++) {
                char casilla = tablero.obtenerCasilla(i, j);
                JButton boton = botones[i][j];
                
                if (casilla == Tablero.VACIO) {
                    boton.setText("");
                    boton.setBackground(new Color(70, 130, 180));
                } else {
                    boton.setText(String.valueOf(casilla));
                    
                    if (casilla == Tablero.JUGADOR_X) {
                        boton.setForeground(new Color(220, 20, 60));
                        boton.setBackground(new Color(255, 240, 245));
                    } else {
                        boton.setForeground(new Color(30, 144, 255));
                        boton.setBackground(new Color(240, 248, 255));
                    }
                }
                
                boton.setEnabled(casilla == Tablero.VACIO && ventanaPrincipal.isJuegoActivo());
            }
        }
        
        repaint();
    }
    
    public void resaltarCasillasGanadoras(int[][] casillasGanadoras) {
        if (casillasGanadoras != null) {
            for (int[] casilla : casillasGanadoras) {
                int fila = casilla[0];
                int columna = casilla[1];
                if (fila >= 0 && fila < TAMAÑO && columna >= 0 && columna < TAMAÑO) {
                    botones[fila][columna].setBackground(new Color(144, 238, 144));
                }
            }
        }
    }
    
    public void reiniciarEstilos() {
        for (int i = 0; i < TAMAÑO; i++) {
            for (int j = 0; j < TAMAÑO; j++) {
                botones[i][j].setBackground(new Color(70, 130, 180));
                botones[i][j].setEnabled(true);
            }
        }
    }
}
