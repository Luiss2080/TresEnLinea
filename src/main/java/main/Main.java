package main;

import javax.swing.*;
import java.awt.*;
import presentacion.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo establecer Nimbus: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                mostrarSplashScreen();
                
                Timer timer = new Timer(2000, e -> {
                    new VentanaPrincipal().setVisible(true);
                    System.out.println("Tres en Raya con IA Minimax iniciado correctamente");
                    System.out.println("Jugador: X | IA: O");
                    System.out.println("La IA es invencible - Intenta conseguir un empate!");
                });
                timer.setRepeats(false);
                timer.start();
                
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(
                    null,
                    "Error al iniciar la aplicación:\n" + e.getMessage(),
                    "Error de Inicio",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
    
    private static void mostrarSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(400, 300);
        splash.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titulo = new JLabel("TRES EN RAYA", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(new Color(100, 149, 237));
        
        JLabel subtitulo = new JLabel("Con IA Minimax", JLabel.CENTER);
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitulo.setForeground(Color.WHITE);
        
        JLabel loading = new JLabel("Cargando...", JLabel.CENTER);
        loading.setFont(new Font("Arial", Font.PLAIN, 14));
        loading.setForeground(Color.LIGHT_GRAY);
        
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(subtitulo, BorderLayout.CENTER);
        panel.add(loading, BorderLayout.SOUTH);
        
        splash.add(panel);
        splash.setVisible(true);
        
        // Efecto de parpadeo para el texto de carga
        Timer blinkTimer = new Timer(500, e -> {
            loading.setVisible(!loading.isVisible());
        });
        blinkTimer.start();
        
        // Detener el parpadeo y cerrar splash después de 2 segundos
        Timer stopTimer = new Timer(2000, e -> {
            blinkTimer.stop();
            splash.dispose();
        });
        stopTimer.setRepeats(false);
        stopTimer.start();
    }
}
