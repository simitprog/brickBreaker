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

        // 1. GESTIONE PAUSA ATTIVA (Bottone centrale Play)
        if (pannelloSuCuiLavorare.isPausa() && !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
            Rectangle areaPlayCentrale = pannelloSuCuiLavorare.getAreaBottonePlay();
            if (areaPlayCentrale != null && areaPlayCentrale.contains(click)) {
                pannelloSuCuiLavorare.setPausa(false); // Riprende il gioco
                return; 
            }
        }

        // 2. GESTIONE TASTO PAUSA STANDARD (In basso a destra, solo se NON è già in pausa)
        if (pannelloSuCuiLavorare.isGiocoIniziato() && 
            !pannelloSuCuiLavorare.isGameOver() && 
            !pannelloSuCuiLavorare.isVittoria() &&
            !pannelloSuCuiLavorare.isPausa()) {
            
            Rectangle areaPausa = pannelloSuCuiLavorare.getAreaBottonePausa();
            if (areaPausa != null && areaPausa.contains(click)) {
                pannelloSuCuiLavorare.setPausa(true);
                return; 
            }
        }

        // 3. GESTIONE VOLUME (In basso a sinistra)
        int dimVol = 40;
        int margVol = 10;
        Rectangle areaVolume = new Rectangle(margVol, pannelloSuCuiLavorare.getHeight() - dimVol - margVol, dimVol, dimVol);
        
        if (areaVolume.contains(click)) {
            pannelloSuCuiLavorare.toggleMute();
            return;
        }

        // 4. GESTIONE PLAY AGAIN (Solo in Game Over)
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