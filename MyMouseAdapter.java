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
        /*if (pannelloSuCuiLavorare.isGiocoIniziato() && !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
            giocatoreLogico g = pannelloSuCuiLavorare.getGiocatoreLogico();
            if (g != null) {
                double nuovaX = e.getX() - (g.getLarghezza() / 2);
                g.getPosizione().setX(nuovaX);
                pannelloSuCuiLavorare.repaint();
            }
        }*/
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pannelloSuCuiLavorare.requestFocusInWindow();

        if (pannelloSuCuiLavorare.isGameOver()) {
            Rectangle area = pannelloSuCuiLavorare.getAreaBottone();
            if (area != null && area.contains(e.getPoint())) {
                pannelloSuCuiLavorare.setBottonePremuto(true); 
                pannelloSuCuiLavorare.repaint();
            }
        }

        int margine = 20;
        int dim = 40;
        Rectangle areaVolume = new Rectangle(margine, pannelloSuCuiLavorare.getHeight() - dim - margine, dim, dim);

        if (areaVolume.contains(e.getPoint())) {
            pannelloSuCuiLavorare.toggleMute();
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