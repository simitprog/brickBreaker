import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @class MyMouseAdapter
 * @brief Gestisce il movimento della racchetta tramite mouse
 */
public class MyMouseAdapter extends MouseAdapter {

    private MyPanel pannelloSuCuiLavorare;

    public MyMouseAdapter(MyPanel p) {
        this.pannelloSuCuiLavorare = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (pannelloSuCuiLavorare.isGiocoIniziato() && !pannelloSuCuiLavorare.isGameOver()) {
            
            giocatoreLogico g = pannelloSuCuiLavorare.getGiocatoreLogico();
            
            if (g != null) {
                double nuovaX = e.getX() - (g.getLarghezza() / 2);
                
                g.getPosizione().setX(nuovaX);
                
                pannelloSuCuiLavorare.repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Cliccando sul pannello forziamo il focus (utile per la tastiera)
        pannelloSuCuiLavorare.requestFocusInWindow();
    }
}