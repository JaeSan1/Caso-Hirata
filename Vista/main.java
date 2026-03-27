package Vista; 

public class main { 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Vista().setVisible(true); 
        });
    }
}