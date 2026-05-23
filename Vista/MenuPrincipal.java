package Vista;

import java.awt.*;
import javax.swing.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {

        // Estilo 
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        setTitle("Sistema Hirata - Menú");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Fondo
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 20));
        panel.setBackground(Color.decode("#FFFFE0")); 
        panel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));
        setContentPane(panel);

        // Flota
        JButton btnFlota = new JButton("<html><center><br>GESTIÓN DE FLOTA</center></html>");
        btnFlota.setBackground(Color.WHITE);
        btnFlota.addActionListener(e -> {
            new SubMenuFlota().setVisible(true);
            this.dispose();
        });

        // Oficina
        JButton btnOficina = new JButton("<html><center><br>GESTIÓN DE OFICINA</center></html>");
        btnOficina.setBackground(Color.WHITE);
        btnOficina.addActionListener(e -> {
            new SubMenuOficina().setVisible(true);
            this.dispose();
        });

        add(btnFlota);
        add(btnOficina);
    }
}
