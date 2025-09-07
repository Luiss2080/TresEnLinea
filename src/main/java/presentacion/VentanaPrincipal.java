package presentacion;

import javax.swing.*;
import java.awt.*;
import logica.EstadisticasManager;
import logica.Tablero;
import logica.MinimaxIA;

public class VentanaPrincipal extends JFrame {
    private PanelTablero panelTablero;
    private JPanel panelBotones;
    private JPanel panelEstadisticas;
    private Tablero tablero;
    private EstadisticasManager estadisticas;
    private boolean juegoActivo;
    
    private JPanel labelPartidas;
    private JPanel labelTu;
    private JPanel labelIA;
    private JPanel labelEmpates;
    
    public VentanaPrincipal() {
        this.tablero = new Tablero();
        this.estadisticas = new EstadisticasManager();
        this.juegoActivo = true;
        
        inicializarComponentes();
        configurarVentana();
        nuevaPartida();
        actualizarEstadisticas();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        panelTablero = new PanelTablero(this);
        add(panelTablero, BorderLayout.CENTER);
        
        panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.NORTH);
        
        panelEstadisticas = crearPanelEstadisticas();
        add(panelEstadisticas, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(60, 60, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton btnNuevaPartida = crearBoton("Nueva Partida", new Color(46, 204, 113), Color.WHITE);
        btnNuevaPartida.addActionListener(e -> nuevaPartida());
        
        JButton btnAyuda = crearBoton("Ayuda", new Color(52, 152, 219), Color.WHITE);
        btnAyuda.addActionListener(e -> mostrarAyuda());
        
        JButton btnResetStats = crearBoton("Reset Stats", new Color(230, 126, 34), Color.WHITE);
        btnResetStats.addActionListener(e -> resetearEstadisticas());
        
        JButton btnSalir = crearBoton("Salir", new Color(231, 76, 60), Color.WHITE);
        btnSalir.addActionListener(e -> cerrarAplicacion());
        
        panel.add(btnNuevaPartida);
        panel.add(btnAyuda);
        panel.add(btnResetStats);
        panel.add(btnSalir);
        
        return panel;
    }
    
    private JButton crearBoton(String texto, Color color, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(colorTexto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setPreferredSize(new Dimension(110, 35));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        Color colorOriginal = color;
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorOriginal);
            }
        });
        
        return boton;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Estadísticas de Combate",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(100, 149, 237)
            ),
            BorderFactory.createEmptyBorder(10, 20, 15, 20)
        ));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(new Color(240, 240, 240));
        
        labelPartidas = crearLabelEstadistica("Partidas:", "0", Color.BLACK);
        labelTu = crearLabelEstadistica("Tú:", "0", new Color(34, 139, 34));
        labelIA = crearLabelEstadistica("IA:", "0", new Color(220, 20, 60));
        labelEmpates = crearLabelEstadistica("Empates:", "0", new Color(255, 140, 0));
        
        statsPanel.add(labelPartidas);
        statsPanel.add(labelTu);
        statsPanel.add(labelIA);
        statsPanel.add(labelEmpates);
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearLabelEstadistica(String texto, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel labelTexto = new JLabel(texto, SwingConstants.CENTER);
        labelTexto.setFont(new Font("Arial", Font.BOLD, 12));
        labelTexto.setForeground(Color.DARK_GRAY);
        
        JLabel labelValor = new JLabel(valor, SwingConstants.CENTER);
        labelValor.setFont(new Font("Arial", Font.BOLD, 18));
        labelValor.setForeground(color);
        
        panel.add(labelTexto, BorderLayout.NORTH);
        panel.add(labelValor, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configurarVentana() {
        setTitle("Tres en Raya - IA Minimax");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                cerrarAplicacion();
            }
        });
        
        setResizable(false);
        setSize(600, 700);
        setLocationRelativeTo(null);
    }
    
    private void cerrarAplicacion() {
        juegoActivo = false;
        
        Timer closeTimer = new Timer(200, e -> {
            System.exit(0);
        });
        closeTimer.setRepeats(false);
        closeTimer.start();
    }
    
    public void nuevaPartida() {
        juegoActivo = false;
        
        Timer resetTimer = new Timer(100, e -> {
            tablero.reiniciar();
            juegoActivo = true;
            panelTablero.actualizarTablero();
            ((Timer)e.getSource()).stop();
        });
        resetTimer.setRepeats(false);
        resetTimer.start();
    }
    
    public void hacerMovimientoJugador(int fila, int columna) {
        if (!juegoActivo) return;
        
        if (tablero.hacerMovimiento(fila, columna, Tablero.JUGADOR_X)) {
            panelTablero.actualizarTablero();
            
            if (verificarFinJuego()) return;
            
            GestorHilos.ejecutarConRetraso(() -> {
                if (juegoActivo) {
                    hacerMovimientoIA();
                }
            }, 500);
        }
    }
    
    private void hacerMovimientoIA() {
        if (!juegoActivo) return;
        
        try {
            MinimaxIA.Movimiento movimiento = MinimaxIA.obtenerMejorMovimiento(tablero);
            
            if (movimiento != null && movimiento.fila != -1 && movimiento.columna != -1 && juegoActivo) {
                tablero.hacerMovimiento(movimiento.fila, movimiento.columna, Tablero.JUGADOR_O);
                panelTablero.actualizarTablero();
                verificarFinJuego();
            }
        } catch (Exception e) {
            System.err.println("Error en movimiento de IA: " + e.getMessage());
        }
    }
    
    private boolean verificarFinJuego() {
        char ganador = tablero.verificarGanador();
        
        if (ganador != Tablero.VACIO) {
            juegoActivo = false;
            
            GestorHilos.ejecutarEnEDT(() -> {
                if (ganador == Tablero.JUGADOR_X) {
                    estadisticas.registrarVictoriaJugador();
                    EfectosVisuales.mostrarVictoria(this);
                } else if (ganador == Tablero.JUGADOR_O) {
                    estadisticas.registrarVictoriaIA();
                    EfectosVisuales.mostrarDerrota(this);
                }
                actualizarEstadisticas();
            });
            
            return true;
        } else if (tablero.estaLleno()) {
            juegoActivo = false;
            
            GestorHilos.ejecutarEnEDT(() -> {
                estadisticas.registrarEmpate();
                EfectosVisuales.mostrarEmpate(this);
                actualizarEstadisticas();
            });
            
            return true;
        }
        
        return false;
    }
    
    private void mostrarAyuda() {
        String mensaje = "🎯 Tres en Raya con IA Minimax\n\n" +
                        "📋 Reglas:\n" +
                        "• Tú juegas con X, la IA con O\n" +
                        "• Consigue 3 en línea (horizontal, vertical o diagonal)\n" +
                        "• La IA usa el algoritmo Minimax - es invencible\n" +
                        "• Tu mejor resultado posible es un empate\n\n" +
                        "🎮 Controles:\n" +
                        "• Haz clic en una casilla vacía para mover\n" +
                        "• Nueva Partida: Reinicia el juego\n" +
                        "• Reset Stats: Borra todas las estadísticas\n\n" +
                        "¡Buena suerte! 🎮";
        
        JOptionPane.showMessageDialog(this, mensaje, "Ayuda", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resetearEstadisticas() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que quieres resetear todas las estadísticas?",
            "Confirmar Reset",
            JOptionPane.YES_NO_OPTION
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            estadisticas.resetearEstadisticas();
            actualizarEstadisticas();
            JOptionPane.showMessageDialog(this, "Estadísticas reseteadas", "Reset Completado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void actualizarEstadisticas() {
        ((JLabel) labelPartidas.getComponent(1)).setText(String.valueOf(estadisticas.getPartidasTotales()));
        ((JLabel) labelTu.getComponent(1)).setText(String.valueOf(estadisticas.getVictoriasJugador()));
        ((JLabel) labelIA.getComponent(1)).setText(String.valueOf(estadisticas.getVictoriasIA()));
        ((JLabel) labelEmpates.getComponent(1)).setText(String.valueOf(estadisticas.getEmpates()));
    }
    
    public Tablero getTablero() { return tablero; }
    public boolean isJuegoActivo() { return juegoActivo; }
    public EstadisticasManager getEstadisticas() { return estadisticas; }
}
