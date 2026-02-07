import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import java.awt.Point;

public class MyMouseAdapter extends MouseAdapter {

    private MyPanel pannelloSuCuiLavorare;

    public MyMouseAdapter(MyPanel p) {
        this.pannelloSuCuiLavorare = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pannelloSuCuiLavorare.requestFocusInWindow();
        Point click = e.getPoint();

        // 1. GESTIONE PAUSA (Bottone in basso a destra)
        if (pannelloSuCuiLavorare.isGiocoIniziato() && 
            !pannelloSuCuiLavorare.isGameOver() && 
            !pannelloSuCuiLavorare.isVittoria()) {
            
            Rectangle areaPausa = pannelloSuCuiLavorare.getAreaBottonePausa();
            if (areaPausa.contains(click)) {
                pannelloSuCuiLavorare.setPausa(!pannelloSuCuiLavorare.isPausa());
                return; // Esco così non triggero altri click sovrapposti
            }
        }

        // 2. GESTIONE VOLUME (In basso a sinistra)
        // Definiamo l'area dell'icona volume (stesse coordinate del paintComponent)
        int dimVol = 40;
        int margVol = 10;
        Rectangle areaVolume = new Rectangle(margVol, pannelloSuCuiLavorare.getHeight() - dimVol - margVol, dimVol, dimVol);
        
        if (areaVolume.contains(click)) {
            pannelloSuCuiLavorare.toggleMute();
            return;
        }

        // 3. GESTIONE PLAY AGAIN (Solo in Game Over)
        if (pannelloSuCuiLavorare.isGameOver()) {
            Rectangle areaReset = pannelloSuCuiLavorare.getAreaBottone();
            if (areaReset != null && areaReset.contains(click)) {
                pannelloSuCuiLavorare.setBottonePremuto(true); 
                pannelloSuCuiLavorare.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (pannelloSuCuiLavorare.isGameOver() && pannelloSuCuiLavorare.isBottonePremuto()) {
            Rectangle area = pannelloSuCuiLavorare.getAreaBottone();
            if (area != null && area.contains(e.getPoint())) {
                pannelloSuCuiLavorare.resetGioco();
            }
            pannelloSuCuiLavorare.setBottonePremuto(false);
            pannelloSuCuiLavorare.repaint();
        }
    }
}