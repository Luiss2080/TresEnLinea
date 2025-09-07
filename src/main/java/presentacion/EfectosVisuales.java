package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EfectosVisuales {
    
    public static void mostrarVictoria(JFrame padre) {
        mostrarAnimacion(padre, "¡VICTORIA!", new Color(144, 238, 144), "🎉");
    }
    
    public static void mostrarDerrota(JFrame padre) {
        mostrarAnimacion(padre, "IA GANA", new Color(255, 99, 71), "🤖");
    }
    
    public static void mostrarEmpate(JFrame padre) {
        mostrarAnimacion(padre, "EMPATE", new Color(255, 215, 0), "🤝");
    }
    
    private static void mostrarAnimacion(JFrame padre, String mensaje, Color color, String emoji) {
        JWindow dialogo = new JWindow(padre);
        dialogo.setSize(300, 150);
        dialogo.setLocationRelativeTo(padre);
        dialogo.setAlwaysOnTop(true);
        
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, color.darker(),
                    getWidth(), getHeight(), color.brighter()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2d.setColor(color.darker().darker());
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
            }
        };
        
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel labelEmoji = new JLabel(emoji, SwingConstants.CENTER);
        labelEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JLabel labelMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        labelMensaje.setFont(new Font("Arial", Font.BOLD, 24));
        labelMensaje.setForeground(Color.WHITE);
        
        JPanel contenido = new JPanel(new BorderLayout(5, 5));
        contenido.setOpaque(false);
        contenido.add(labelEmoji, BorderLayout.NORTH);
        contenido.add(labelMensaje, BorderLayout.CENTER);
        
        panel.add(contenido, BorderLayout.CENTER);
        dialogo.add(panel);
        
        Timer timer = new Timer(2000, e -> {
            dialogo.dispose();
        });
        timer.setRepeats(false);
        
        Timer pulseTimer = new Timer(200, new ActionListener() {
            private boolean grande = false;
            private int contador = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contador < 10) {
                    Font font = labelMensaje.getFont();
                    if (grande) {
                        labelMensaje.setFont(font.deriveFont(24f));
                        labelEmoji.setFont(labelEmoji.getFont().deriveFont(40f));
                    } else {
                        labelMensaje.setFont(font.deriveFont(26f));
                        labelEmoji.setFont(labelEmoji.getFont().deriveFont(44f));
                    }
                    grande = !grande;
                    contador++;
                } else {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        
        dialogo.setVisible(true);
        pulseTimer.start();
        timer.start();
    }
    
    public static void mostrarCarga(JFrame padre, String mensaje, int duracionMs) {
        // Usar JWindow en lugar de JDialog modal
        JWindow dialogo = new JWindow(padre);
        dialogo.setSize(250, 100);
        dialogo.setLocationRelativeTo(padre);
        dialogo.setAlwaysOnTop(true);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setBackground(new Color(60, 60, 60));
        progressBar.setForeground(new Color(100, 149, 237));
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
        
        Timer timer = new Timer(duracionMs, e -> {
            dialogo.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void animarBoton(JButton boton) {
        Color colorOriginal = boton.getBackground();
        Font fontOriginal = boton.getFont();
        
        Timer timer = new Timer(100, new ActionListener() {
            private int contador = 0;
            private boolean expandido = false;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contador < 6) {
                    if (!expandido) {
                        boton.setBackground(colorOriginal.brighter());
                        boton.setFont(fontOriginal.deriveFont(fontOriginal.getSize() + 2f));
                        expandido = true;
                    } else {
                        boton.setBackground(colorOriginal);
                        boton.setFont(fontOriginal);
                        expandido = false;
                    }
                    contador++;
                } else {
                    boton.setBackground(colorOriginal);
                    boton.setFont(fontOriginal);
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        
        timer.start();
    }
    
    public static void resaltarGanador(JFrame padre, String mensaje) {
        JWindow ventanaFlotante = new JWindow(padre);
        ventanaFlotante.setSize(300, 80);
        ventanaFlotante.setLocationRelativeTo(padre);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(144, 238, 144, 200));
        panel.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 3));
        
        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(34, 139, 34));
        
        panel.add(label);
        ventanaFlotante.add(panel);
        ventanaFlotante.setVisible(true);
        
        // Movimiento de la ventana flotante
        Timer moveTimer = new Timer(50, new ActionListener() {
            private int y = padre.getY() + padre.getHeight() / 2;
            private int direccion = -2;
            private int contador = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contador < 100) {
                    y += direccion;
                    if (y <= padre.getY() + 50 || y >= padre.getY() + padre.getHeight() - 150) {
                        direccion *= -1;
                    }
                    ventanaFlotante.setLocation(
                        padre.getX() + (padre.getWidth() - ventanaFlotante.getWidth()) / 2, 
                        y
                    );
                    contador++;
                } else {
                    ventanaFlotante.dispose();
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        
        moveTimer.start();
    }
}
