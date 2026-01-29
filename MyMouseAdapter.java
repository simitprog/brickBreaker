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
        // BLOCCO INPUT: muoviamo la racchetta solo se il gioco è iniziato e NON è finito
        if (pannelloSuCuiLavorare.isGiocoIniziato() && !pannelloSuCuiLavorare.isGameOver()) {
            
            giocatoreLogico g = pannelloSuCuiLavorare.getGiocatoreLogico();
            
            if (g != null) {
                // Centriamo la racchetta sulla X del mouse
                double nuovaX = e.getX() - (g.getLarghezza() / 2);
                
                // Aggiorniamo la posizione nella logica
                g.getPosizione().setX(nuovaX);
                
                // Il repaint() viene già chiamato dal Timer nel MyPanel, 
                // ma chiamarlo qui rende il movimento ancora più fluido
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