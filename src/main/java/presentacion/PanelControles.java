package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelControles extends JPanel {
    private VentanaPrincipal ventanaPrincipal;
    private JLabel labelEstado;
    private JButton botonNuevaPartida;
    private JButton botonEstadisticas;
    private JButton botonSalir;
    
    public PanelControles(VentanaPrincipal ventana) {
        this.ventanaPrincipal = ventana;
        inicializarPanel();
        crearComponentes();
    }
    
    private void inicializarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(400, 80));
    }
    
    private void crearComponentes() {
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEstado.setBackground(new Color(45, 45, 45));
        
        labelEstado = new JLabel("Tu turno - Elige una casilla");
        labelEstado.setFont(new Font("Arial", Font.BOLD, 14));
        labelEstado.setForeground(Color.WHITE);
        labelEstado.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelEstado.add(labelEstado);
        add(panelEstado, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.setBackground(new Color(45, 45, 45));
        
        botonNuevaPartida = crearBoton("Nueva Partida", new Color(34, 139, 34));
        botonNuevaPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaPrincipal.nuevaPartida();
            }
        });
        botonNuevaPartida.setEnabled(false);
        
        botonEstadisticas = crearBoton("Estadísticas", new Color(70, 130, 180));
        botonEstadisticas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarEstadisticas();
            }
        });
        
        botonSalir = crearBoton("Salir", new Color(220, 20, 60));
        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                    ventanaPrincipal,
                    "¿Estás seguro de que quieres salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        
        panelBotones.add(botonNuevaPartida);
        panelBotones.add(botonEstadisticas);
        panelBotones.add(botonSalir);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(120, 30));
        
        Color colorOriginal = color;
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (boton.isEnabled()) {
                    boton.setBackground(color.brighter());
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorOriginal);
            }
        });
        
        return boton;
    }
    
    public void actualizarEstado(String mensaje) {
        labelEstado.setText(mensaje);
        
        if (mensaje.contains("ganado") || mensaje.contains("Felicidades")) {
            labelEstado.setForeground(new Color(144, 238, 144));
        } else if (mensaje.contains("IA ha ganado") || mensaje.contains("perdido")) {
            labelEstado.setForeground(new Color(255, 99, 71));
        } else if (mensaje.contains("Empate")) {
            labelEstado.setForeground(new Color(255, 215, 0));
        } else if (mensaje.contains("pensando")) {
            labelEstado.setForeground(new Color(255, 165, 0));
        } else {
            labelEstado.setForeground(Color.WHITE);
        }
        
        repaint();
    }
    
    public void habilitarNuevaPartida(boolean habilitar) {
        botonNuevaPartida.setEnabled(habilitar);
    }
    
    private void mostrarEstadisticas() {
        String mensaje = String.format(
            "=== ESTADÍSTICAS DEL JUEGO ===\n\n" +
            "📊 Partidas totales: %d\n" +
            "🏆 Tus victorias: %d\n" +
            "🤖 Victorias de la IA: %d\n" +
            "🤝 Empates: %d\n\n" +
            "🔥 Tu mejor racha: %d\n" +
            "⚡ Mejor racha de la IA: %d\n\n" +
            "📈 Racha actual tuya: %d\n" +
            "📉 Racha actual de la IA: %d\n\n" +
            "🕐 Primera partida: %s\n" +
            "⏰ Última partida: %s\n\n" +
            "💡 Porcentaje de victorias tuyas: %.1f%%",
            ventanaPrincipal.getEstadisticas().getPartidasTotales(),
            ventanaPrincipal.getEstadisticas().getVictoriasJugador(),
            ventanaPrincipal.getEstadisticas().getVictoriasIA(),
            ventanaPrincipal.getEstadisticas().getEmpates(),
            ventanaPrincipal.getEstadisticas().getMejorRachaJugador(),
            ventanaPrincipal.getEstadisticas().getMejorRachaIA(),
            ventanaPrincipal.getEstadisticas().getRachaActualJugador(),
            ventanaPrincipal.getEstadisticas().getRachaActualIA(),
            ventanaPrincipal.getEstadisticas().getFechaPrimeraPartida() != null ? 
                ventanaPrincipal.getEstadisticas().getFechaPrimeraPartida() : "Ninguna",
            ventanaPrincipal.getEstadisticas().getFechaUltimaPartida() != null ? 
                ventanaPrincipal.getEstadisticas().getFechaUltimaPartida() : "Ninguna",
            calcularPorcentajeVictorias()
        );
        
        JTextArea textArea = new JTextArea(mensaje);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(45, 45, 45));
        textArea.setForeground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(
            ventanaPrincipal, 
            scrollPane, 
            "Estadísticas Detalladas", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private double calcularPorcentajeVictorias() {
        int totalPartidas = ventanaPrincipal.getEstadisticas().getPartidasTotales();
        if (totalPartidas == 0) return 0.0;
        
        return (ventanaPrincipal.getEstadisticas().getVictoriasJugador() * 100.0) / totalPartidas;
    }
}
