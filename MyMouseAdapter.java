import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Rectangle; // Importa Rectangle per gestire il bottone

public class MyMouseAdapter extends MouseAdapter {

    private MyPanel pannelloSuCuiLavorare;

    public MyMouseAdapter(MyPanel p) {
        this.pannelloSuCuiLavorare = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Se il gioco è in corso, muovi la racchetta
        if (pannelloSuCuiLavorare.isGiocoIniziato() && !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
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
        pannelloSuCuiLavorare.requestFocusInWindow();

        // LOGICA BOTTONE GAME OVER
        if (pannelloSuCuiLavorare.isGameOver()) {
            Rectangle area = pannelloSuCuiLavorare.getAreaBottone(); // Creeremo questo metodo in MyPanel
            if (area != null && area.contains(e.getPoint())) {
                pannelloSuCuiLavorare.setBottonePremuto(true); // Per l'effetto grafico
                pannelloSuCuiLavorare.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (pannelloSuCuiLavorare.isGameOver() && pannelloSuCuiLavorare.isBottonePremuto()) {
            Rectangle area = pannelloSuCuiLavorare.getAreaBottone();
            
            // Se rilascio il mouse dentro l'area del bottone, resetto il gioco
            if (area != null && area.contains(e.getPoint())) {
                pannelloSuCuiLavorare.resetGioco();
            }
            
            pannelloSuCuiLavorare.setBottonePremuto(false);
            pannelloSuCuiLavorare.repaint();
        }
    }
}