
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

//NOTE: il codice e' stato parzialmente ordinato e commentato da Gemini,
//      e per alcune funzionalita' (come l'audio) e' stata usata IA

public class SwingPaintDemo1 {
    
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
    
    private static void createAndShowGUI() {
        
        JFrame f = new JFrame("BrickBreaker67");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1000,700);
        f.setResizable(false);
        MyPanel p = new MyPanel();
        f.add(p);
        f.pack();
        f.setVisible(true);
    }
}